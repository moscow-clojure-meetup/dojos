(ns dojo-tools.subs
  (:require [moment]
            [re-frame.core :as rf]
            [dojo-tools.utils :as utils]))


;; Subscriptions
(defn dojos [db]
  (:dojos db))

(rf/reg-sub
 :dojos
 dojos)

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
