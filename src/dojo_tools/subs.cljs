(ns dojo-tools.subs
  (:require [moment]
            [re-frame.core :as rf]
            [dojo-tools.utils :as utils]))

;; Subscriptions
(rf/reg-sub
 :dojos
 (fn [db]
   (:dojos db)))

(defn past-dojos-list [db]
  (:past-dojos db))

(rf/reg-sub
 :past-dojos-list
 past-dojos-list)

(defn upcomming-dojos-list [db]
  (:upcommin-dojos db))

(rf/reg-sub
 :upcomming-dojos-list
 upcomming-dojos-list)

(defn prepare-dojos [[dojos dojos-list]]
  (let [raw-dojos (-> dojos
                      (select-keys dojos-list)
                      (vals))]
    (mapv utils/format-dojo raw-dojos)))

(rf/reg-sub
 :past-dojos
 :<- [:dojos]
 :<- [:past-dojos-list]
 prepare-dojos)

(rf/reg-sub
 :upcomming-dojos
 :<- [:dojos]
 :<- [:upcomming-dojos-list]
 prepare-dojos)

(rf/reg-sub
 :current-route-name
 (fn [db]
   (get-in db [:current-route :name])))

(rf/reg-sub
 :current-route-params
 (fn [db]
   (get-in db [:current-route :params])))

(rf/reg-sub
 :dojo-by-route
 :<- [:dojos]
 :<- [:current-route-params]
 (fn [[dojos {:keys [dojo-id]}]]
   (get dojos dojo-id)))

(rf/reg-sub
 :to-path
 (fn [db [_ path]]
   (get-in db path)))

(rf/reg-sub
 :get-form
 (fn [db [_ form-id]]
   (get-in db [:app-forms form-id])))

(rf/reg-sub
 :members
 (fn [db]
   (:members db)))

(rf/reg-sub
 :dojo-members
 :<- [:members]
 (fn [members [_ dojo-id]]
   (->> members
        vals
        (filter #(= (:dojo-id %) dojo-id)))))

(rf/reg-sub
 :members-by-ids
 :<- [:members]
 (fn [members [_ ids]]
   (vals (select-keys members ids))))

(rf/reg-sub
 :members-groups
 (fn [db]
   (:members-groups db)))

(rf/reg-sub
 :dojo-members-groups
 :<- [:members-groups]
 (fn [members-groups [_ dojo-id]]
   (->> members-groups
        vals
        (filter #(= (:dojo-id %) dojo-id)))))
