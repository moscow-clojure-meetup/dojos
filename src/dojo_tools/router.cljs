(ns dojo-tools.router
  (:require [bide.core :as r]
            [re-frame.core :refer [dispatch-sync reg-fx]]))


(defonce router
  (r/router
    [["/" :dojos]
     ["/dojo/:dojo-id" :dojo-details]
     ["/admin" :admin-dojos]
     ["/admin/new" :admin-dojo-form]]))


(defn url-for
  ([name]
   (url-for name nil nil))

  ([name params]
   (url-for name params nil))

  ([name params query]
   (str "#" (r/resolve router name params query))))


(defn route-for [path]
  (r/match router path))


(defn on-navigate [name params query]
  (dispatch-sync [:set-route name params query]))


(defn start-router! []
  (r/start!
    router
    {:default     :dojos
     :on-navigate on-navigate}))


(defn navigate-to! [name params query]
  ;; Should be triggered in the next tick
  (js/setTimeout
    #(r/navigate! router name params query)
    0))


(reg-fx
  :navigate-to-route
  (fn [[name params query]]
    (navigate-to! name params query)))
