(ns dojos.core
  (:require [reagent.core :as reagent]
            [reagent.dom :as dom]
            ["@apollo/client" :as apollo]
            ["@material-ui/core" :as mui]
            ["react" :as react]
            [cljs-bean.core :as b]))


(def development?
  ^boolean goog.DEBUG)


(def cache
  (apollo/InMemoryCache.))


(def client
  (apollo/ApolloClient.
    (b/->js
       {:uri   "http://localhost:3001/api/gql"
        :cache cache
        :queryDeduplication true
        :defaultOptions
        {:watchQuery
          {:fetchPolicy "cache-and-network"
           :nextFetchPolicy "cache-first"}}})))


(def dojos-query
  ;; It seems that query builders are sensless
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


(defn dojos []
  (let [{:keys [loading error data]} (use-query dojos-query)]
    (reagent/as-element
      (cond 
        loading "loading"
        ;; Seems that b/bean recursive is not recursive enough
        error (str "Error happened: " (-> error b/bean :message))
        :else [:> mui/List (map dojo-list-item (:dojos data))]))))


(defn main-page []
  [:> mui/Box {:margin "20vh auto" :maxWidth 480}
    [:> mui/Paper
      [:> mui/Box {:padding 2}
        [:> mui/Typography {:variant "h4"} "All Dojos"]]  
      [:> dojos]]])


(defn app-root []
  (let [prefers-dark-mode (mui/useMediaQuery "(prefers-color-scheme: dark)" #js {:noSsr true})
        theme (react/useMemo
                #(mui/createMuiTheme (b/->js {:palette {:type (if prefers-dark-mode "dark" "light")}}))
                #js [prefers-dark-mode])]
    (reagent/as-element
      [:> apollo/ApolloProvider {:client client}
        [:> mui/ThemeProvider {:theme theme}
          [:> mui/CssBaseline
            [main-page]]]])))


(defn mount []
  (dom/render [:> app-root] js/app))


(defn on-update []
  (mount))


(defn ^:export main []
  (when development?
    (enable-console-print!)
    (println "init app in dev mode"))

  (mount))
