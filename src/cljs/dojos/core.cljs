(ns dojos.core
  (:require ["react" :as react]
            [uix.core.alpha :as uix]
            [uix.dom.alpha :as uix.dom]
            ["@apollo/client" :as apollo]
            ["@material-ui/core" :as mui]
            ["@material-ui/lab" :refer [Skeleton]]
            [cljs-bean.core :as bean]
            [dojos.mui :refer-macros [defstyled]]))


(def development?
  ^boolean goog.DEBUG)


(def client
  (apollo/ApolloClient.
   (bean/->js
    {:uri                "http://localhost:3001/api/gql"
     :cache              (apollo/InMemoryCache.)
     :queryDeduplication true
     :defaultOptions     {:watchQuery {:fetchPolicy     "cache-and-network"
                                       :nextFetchPolicy "cache-first"}}})))


(def dojos-query
  (apollo/gql "{ dojos { id name } }"))


(defn use-query [& args]
  (->
   (apply apollo/useQuery (map bean/->js args))
   (bean/bean :recursive true)))


(defn dojo-list-item [{:keys [id name]}]
  [:> mui/ListItem {:key id :button true}
   [:> mui/ListItemText
    {:primary name}]])


(defn loading-list-item [key]
  [:> mui/ListItem {:key key}
   [:> mui/ListItemText
    {:primary (react/createElement Skeleton #js {:width "20ex"})}]])


(def static-loading-list
  [:> mui/List
   (map loading-list-item (range 3))])


(defn dojos []
  ;; FIXME variables are only for the test
  (let [{:keys [loading error data]} (use-query dojos-query {:variables {:id 1}})]
    (cond
      loading static-loading-list
      ;; Seems that b/bean recursive is not recursive enough
      error (str "Error happened: " (-> error bean/bean :message))
      :else [:> mui/List (map dojo-list-item (:dojos data))])))


(defstyled MyHeader
  mui/Typography
  {:padding 18
   :color   "green"})


(defn main-page []
  [:> mui/Box {:margin "20vh auto" :maxWidth 480}
   [:> mui/Paper
    [:> MyHeader {:variant "h4"}
     "All Dojos"]

    [dojos]]])


(defn app-root []
  (let [prefers-dark-mode (mui/useMediaQuery "(prefers-color-scheme: dark)" #js {:noSsr true})
        theme             (uix/memo
                           #(mui/createMuiTheme (bean/->js {:palette {:type (if prefers-dark-mode "dark" "light")}}))
                           [prefers-dark-mode])]
    [:> apollo/ApolloProvider {:client client}
     [:> mui/ThemeProvider {:theme theme}
      [:> mui/CssBaseline]
      [main-page]]]))


(defn mount []
  (uix.dom/render [app-root] js/app))


(defn on-update []
  (mount))


(defn ^:export main []
  (when development?
    (enable-console-print!)
    (println "init app in dev mode"))

  (mount))
