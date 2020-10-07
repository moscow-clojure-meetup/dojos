(ns dojos.core
  (:require [reagent.core :as reagent]
            [reagent.dom :as dom]
            [re-graph.core :as re-graph]))


(def development?
  ^boolean goog.DEBUG)


(defonce state
  (reagent/atom {:dojos nil}))


(defn app-root []
  (let [dojos (:dojos @state)]
    [:div
     [:h2 "All Dojos"]
     (when (some? dojos)
       [:ul
        (doall
         (for [dojo dojos]
           ^{:key (:id dojo)}
           [:li
            [:b [:em (:name dojo)]]]))])]))


(defn set-dojos [{:keys [data errors]}]
  (swap! state assoc :dojos (:dojos data)))


(defn init-graph-api []
  (re-graph/init
   {:http {:url "http://localhost:3001/api/gql"}
    :ws   {:url nil}}))


(defn fetch-dojos []
  (re-graph/query :dojos-request "{ dojos { id, name } }" nil set-dojos))


(defn mount []
  (dom/render [app-root] js/app))


(defn on-update []
  (mount))


(defn ^:export main []
  (when development?
    (enable-console-print!)
    (println "init app in dev mode"))

  (mount)
  (init-graph-api)
  (fetch-dojos))
