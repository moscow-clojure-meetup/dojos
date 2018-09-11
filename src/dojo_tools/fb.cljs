(ns dojo-tools.fb
  (:require ["firebase/app" :refer [database] :as firebase]
            ["firebase/database"]
            ["firebase/auth"]
            [reagent.interop :refer-macros [$]]
            [re-frame.core :refer [reg-fx dispatch]]
            [clojure.string :as str]))


(defn fb-init [config]
  (firebase/initializeApp
    (-> {:apiKey      "AIzaSyD-vrcl7TLF-uTTqBDk-ECJxfrHyxnhcHY"
         :authDomain  "mcljsug-dojos.firebaseapp.com"
         :databaseURL "https://mcljsug-dojos.firebaseio.com"
         :projectId   "mcljsug-dojos"}
        (merge config)
        (clj->js))))

(reg-fx :firebase/init fb-init)


(defn db-ref
  [path-vector]
  (let [path (->> path-vector (map name) (str/join "/"))]
    ($ (database) ref path)))


(defn fb-save
  [path-vector value]
  ($ (db-ref path-vector) set (clj->js value)))

(reg-fx
  :firebase/save
  (fn [[path value]]
    (fb-save path value)))


(defn db-subscribe
  [path-vector db-path process]
  ($ (db-ref path-vector) on "value"
     (fn [snapshot]
       (let [value (js->clj (.val snapshot) :keywordize-keys true)]
         (dispatch [:assoc-to-path db-path (if process (process value) value)])))))

(reg-fx
  :firebase/subscribe
  (fn [subscriptions]
    (dorun
      (->> subscriptions
           (map (fn [{:keys [path db-path process]}]
                  (db-subscribe path
                                (if db-path db-path path)
                                process)))))))
