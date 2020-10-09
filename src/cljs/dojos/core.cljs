(ns dojos.core
  (:require [reagent.core :as reagent]
            [reagent.dom :as dom]
            ["@apollo/client" :as apollo]))


(def development?
  ^boolean goog.DEBUG)


(def client (apollo/ApolloClient. #js {:uri   "http://localhost:3001/api/gql"
                                       :cache (apollo/InMemoryCache.)}))


(def dojos-query
  (apollo/gql "{ dojos { id name } }"))


(defn dojos []
  (let [data (apollo/useQuery dojos-query)]
    (reagent/as-element
     [:div
      (println data)])))


(defn app-root []
  [:> apollo/ApolloProvider {:client client}
   [:h2 "All Dojos"]
   [:> dojos]])


(defn mount []
  (dom/render [app-root] js/app))


(defn on-update []
  (mount))


(defn ^:export main []
  (when development?
    (enable-console-print!)
    (println "init app in dev mode"))

  (mount))
