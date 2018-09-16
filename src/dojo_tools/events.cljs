(ns dojo-tools.events
  (:require [re-frame.core :as rf]
            [dojo-tools.db :refer [default check-db-spec]]
            [dojo-tools.fb]
            [dojo-tools.specs :refer [coerce-dojos coerce-dojo]]
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
       :process coerce-dojos}
      {:path    [:upcommin-dojos]
       :process #(vals %)}]}))


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


(rf/reg-event-db
  :create-form
  [rf/trim-v]
  (fn [db [form]]
    (let [form-id    (:id form)
          ready-form (utils/prepare-form (:fields form))]
      (assoc-in db [:app-forms form-id] ready-form))))


(rf/reg-event-db
  :update-form-field
  [rf/trim-v]
  (fn [db [form-id field-id value]]
    (-> db
        (assoc-in [:app-forms form-id field-id] value)
        (update-in [:app-forms form-id :errors] dissoc field-id))))


(rf/reg-event-db
  :set-form-errors
  [rf/trim-v]
  (fn [db [form-id errors]]
    (assoc-in db [:app-forms form-id :errors] errors)))


(rf/reg-event-fx
  :save-new-dojo
  [rf/trim-v]
  (fn [_ [new-dojo]]
    (let [dojo (coerce-dojo new-dojo)
          id   (:id dojo)]
      {:firebase/save     [{:path  [:dojos id]
                            :value dojo}
                           {:path  [:upcommin-dojos id]
                            :value id}]
       :navigate-to-route [:admin-dojos]})))
