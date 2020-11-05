(ns dojos.app.routing
  (:require [reitit.frontend :as rt.front]
            [reitit.frontend.easy :as rt.easy]
            [dojos.app.state :as app.state]))


(def routes
  ["/"
   ["" {:name :home}]
   ["dojos"
    ["" {:name :dojos-list}]
    ["/:dojo-id" {:name :dojo-details}]]])


(def router
  (rt.front/router routes {:conflicts nil}))


(defn- on-navigate
  "Function to be called when route changes"
  [new-match _]
  (let [{:keys [data path-params query-params]} new-match
        route {:name    (:name data)
               :params  path-params
               :query   query-params
               :options (dissoc data :name)}]
    (app.state/current-route route)))


(defn init!
  "Registers event listeners on HTML5 history"
  []
  (rt.easy/start!
   router
   on-navigate
   {:use-fragment false}))
