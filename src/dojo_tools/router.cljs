(ns dojo-tools.router
  (:require [cljs.spec.alpha :as s]
            [bide.core :as r]
            [re-frame.core :refer [dispatch-sync reg-fx]]))

(s/def ::name
  keyword?)

(s/def ::params
  map?)

(s/def ::query
  map?)

(s/def ::route
  (s/keys :req-un [::name ::params ::query]))


(defonce router
  (r/router
   [["/about" :about]
    ["/puzzles" :puzzles]
    ["/dojos" :dojos]
    ["/dojos/:dojo-id" :dojo-details]
    ["/admin" :admin-dojos]
    ["/admin/new" :admin-dojo-form]
    ["/admin/run/:dojo-id" :admin-run-dojo]]))


(defn url-for
  ([name]
   (url-for name nil nil))

  ([name params]
   (url-for name params nil))

  ([name params query]
   (str "#" (r/resolve router name params query))))

(s/fdef url-for
  :args (s/alt :name (s/cat :name ::name)
               :params (s/cat :name ::name
                              :params ::params)
               :query (s/cat :name ::name
                             :params ::params
                             :query ::query))
  :ret string?)


(defn route-for [path]
  (r/match router path))

(s/fdef route-for
  :args (s/cat :path string?)
  :ret ::route)


(defn on-navigate [name params query]
  (dispatch-sync [:set-route name params query]))

(s/fdef on-navigate
  :args (s/cat :name ::name
               :params ::params
               :query ::query))


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

(s/fdef navigate-to!
  :args (s/cat :name ::name
               :params ::params
               :query ::query))


(reg-fx
 :navigate-to-route
 (fn [[name params query]]
   (navigate-to! name params query)))
