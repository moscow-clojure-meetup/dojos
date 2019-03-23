(ns dojo-tools.events
  (:require [re-frame.core :as rf]
            [dojo-tools.db :refer [default]]
            [dojo-tools.effects.fb]
            [dojo-tools.interceptors.core :refer [check-db]]
            [dojo-tools.specs :as specs]
            [dojo-tools.utils :as utils]))

;; Events
(rf/reg-event-fx
 :initialize
 [rf/trim-v]
 (fn [db [config]]
   {:db            default
    :firebase/init (:firebase config)
    :dispatch      [:set-fb-subscriptions]}))

(rf/reg-event-fx
 :set-fb-subscriptions
 (fn []
   {:firebase/subscribe
    [{:path    [:dojos]
      :process specs/coerce-dojos}
     {:path    [:upcoming-dojos]
      :process #(vals %)}
     {:path    [:past-dojos]
      :process #(vals %)}
     {:path    [:members]
      :process specs/coerce-members}
     {:path    [:members-groups]
      :process specs/coerce-members-groups}]}))

(rf/reg-event-db
 :set-route
 [rf/trim-v]
 (fn [db [name params query]]
   (assoc db :current-route {:name   name
                             :params params
                             :query  query})))

(rf/reg-event-db
 :assoc-to-path
 [rf/trim-v check-db]
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
   (let [dojo (specs/coerce-dojo new-dojo)
         id   (:id dojo)]
     {:firebase/save     [{:path  [:dojos id]
                           :value dojo}
                          {:path  [:upcomming-dojos id]
                           :value id}]
      :navigate-to-route [:admin-dojos]})))

(defmulti save-dojo-state (fn [_ [_ state]] state))

(defmethod save-dojo-state :groups-created
  [{:keys [db]} [id state partition]]
  (let [members        (->> db :members vals (filter #(= (:dojo-id %) id)))
        groups         (utils/split-member-to-groups members partition)
        make-group     #(utils/create-members-group id (inc %1) %2)
        members-groups (->> groups
                            (map-indexed make-group)
                            utils/reduce-by-id)]
    {:dispatch      [:save-members-groups members-groups]
     :firebase/save [{:path  [:dojos id :state]
                      :value state}]}))

(defmethod save-dojo-state :default
  [_ [id state]]
  {:firebase/save [{:path  [:dojos id :state]
                    :value state}]})

(rf/reg-event-fx
 :save-dojo-state
 [rf/trim-v]
 save-dojo-state)

(rf/reg-event-fx
 :save-member
 [rf/trim-v]
 (fn [_ [{:keys [id] :as new-member}]]
   {:firebase/save [{:path  [:members id]
                     :value new-member}]}))

(rf/reg-event-fx
 :save-members-groups
 [rf/trim-v]
 (fn [_ [members-groups]]
   (let [membes (specs/coerce-members-groups members-groups)]
     {:firebase/save-map {:path  [:members-groups]
                          :value membes}})))
