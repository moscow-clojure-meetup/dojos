(ns dojo-tools.effects.fb
  (:require ["firebase/app" :refer [database] :as firebase]
            ["firebase/database"]
            [reagent.interop :refer-macros [$]]
            [re-frame.core :refer [reg-fx dispatch]]
            [dojo-tools.utils :as utils]))

(defn fb-init [config]
  (firebase/initializeApp (clj->js config)))

(defn db-ref [path-vector]
  (let [path (utils/vec->path path-vector)]
    ($ (database) ref path)))

(defn fb-save [path-vector value]
  ($ (db-ref path-vector) set (clj->js value)))

(defn fb-save-map [path value]
  (let [update-map (reduce-kv (fn [acc k v]
                                (assoc acc
                                       (-> path (conj k) utils/vec->path)
                                       v))
                              {}
                              value)
        db-ref ($ (database) ref)]
    ($ db-ref update (clj->js update-map))))

(defn db-subscribe [path-vector db-path process]
  ($ (db-ref path-vector) on "value"
     (fn [snapshot]
       (let [value (js->clj (.val snapshot) :keywordize-keys true)]
         (when value
           (dispatch [:assoc-to-path db-path (if process (process value) value)]))))))

;; Effects
(reg-fx
 :firebase/init
 fb-init)

(reg-fx
 :firebase/save
 (fn [new-data]
   (dorun
    (map (fn [{:keys [path value]}]
           (fb-save path value))
         new-data))))

(reg-fx
 :firebase/save-map
 (fn [{:keys [path value]}]
   (fb-save-map path value)))

(reg-fx
 :firebase/subscribe
 (fn [subscriptions]
   (dorun
    (map (fn [{:keys [path db-path process]}]
           (db-subscribe path
                         (if db-path db-path path)
                         process))
         subscriptions))))
