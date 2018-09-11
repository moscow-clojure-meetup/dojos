(ns dojo-tools.events
  (:require [re-frame.core :as rf]
            [dojo-tools.db :refer [default check-db-spec] :as db]
            [dojo-tools.fb]
            [dojo-tools.utils :as utils]))


;; Events
(rf/reg-event-fx
  :initialize
  [check-db-spec]
  (fn []
    {:db            default
     :firebase/init nil
     :dispatch      [:set-fb-subscriptions]}))


(rf/reg-event-fx
  :set-fb-subscriptions
  (fn []
    {:firebase/subscribe
     [{:path    [:dojos]
       :process utils/stringify-keys}]}))


(rf/reg-event-db
  :set-route
  [rf/trim-v
   check-db-spec]
  (fn [db [name params query]]
    (assoc db :current-route {:name   name
                              :params params
                              :query  query})))


(rf/reg-event-db
  :assoc-to-path
  [rf/trim-v
   check-db-spec]
  (fn [db [path value]]
    (assoc-in db path value)))
