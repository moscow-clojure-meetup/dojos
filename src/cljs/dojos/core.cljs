(ns dojos.core
  (:require ["react" :as react]
            [uix.core.alpha :as uix]
            [uix.dom.alpha :as uix.dom]
            ["@apollo/client" :as apollo]
            ["@material-ui/core" :as mui]
            ["@material-ui/core/styles" :as mui.styles]
            ["@material-ui/lab" :refer [Skeleton]]
            [cljs-bean.core :as b]))


(def development?
  ^boolean goog.DEBUG)


(def cache
  (apollo/InMemoryCache.))


(def client
  (apollo/ApolloClient.
   (b/->js
    {:uri                "http://localhost:3001/api/gql"
     :cache              cache
     :queryDeduplication true
     :defaultOptions     {:watchQuery {:fetchPolicy     "cache-and-network"
                                       :nextFetchPolicy "cache-first"}}})))


(def dojos-query
  ;; It seems that query builders are senseless
  ;; because they don't generate GraphQL Document
  (apollo/gql "{ dojos { id name } }"))


(defn use-query [& args]
  (->
   (apply apollo/useQuery args)
   (b/bean :recursive true)))


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
  (let [{:keys [loading error data]} (use-query dojos-query)]
    (cond
      loading static-loading-list
      ;; Seems that b/bean recursive is not recursive enough
      error (str "Error happened: " (-> error b/bean :message))
      :else [:> mui/List (map dojo-list-item (:dojos data))])))


;; Might be sensible to create a helper macro
(def my-header
  ((mui/styled mui/Typography) #js {:padding 16}))


(defn main-page []
  [:> mui/Box {:margin "20vh auto" :maxWidth 480}
   [:> mui/Paper
    [:> my-header {:variant "h4"
                   :css {:color "red"}}
     "All Dojos"]
    [dojos]]])


(defn app-root []
  (let [prefers-dark-mode (mui/useMediaQuery "(prefers-color-scheme: dark)" #js {:noSsr true})
        theme             (react/useMemo
                           #(mui/createMuiTheme (b/->js {:palette {:type (if prefers-dark-mode "dark" "light")}}))
                           #js [prefers-dark-mode])]
    [:> apollo/ApolloProvider {:client client}
     [:> mui/ThemeProvider {:theme theme}
      [:> mui/CssBaseline]
      [:div {:css {:border "1px solid blue"}}
       [main-page]]]]))


(defn mount []
  (uix.dom/render [app-root] js/app))


(defn on-update []
  (mount))


(defn material-styles-hook-init []
  (uix/add-transform-fn
   (fn [attrs]
     (if-not (contains? attrs :css)
       attrs
       (let [classes    (:class attrs)
             css        (b/->js {:css (:css attrs)})
             root-class (.-css ((mui.styles/makeStyles css)))]
         (js/console.log root-class)
         (-> attrs
             (dissoc :css)
             (assoc :class (str classes " " root-class))))))))


(defn ^:export main []
  (when development?
    (enable-console-print!)
    (println "init app in dev mode"))

  (material-styles-hook-init)
  (mount))
