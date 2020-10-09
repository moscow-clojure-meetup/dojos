(ns dojos.core
  (:require [reagent.core :as reagent]
            [reagent.dom :as dom]
            ["@apollo/client" :as apollo]
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
  [:li {:key id}
    [:b [:em name]]])

(defn dojos []
  (let [{:keys [loading error data]} (use-query dojos-query)]
    (reagent/as-element
      [:div
        (cond 
          loading "loading"
          ;; Seems that b/bean recursive is not recursive enough
          error (str "Error happened: " (-> error b/bean :message))
          :else [:ul (map dojo-list-item (:dojos data))])])))


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
