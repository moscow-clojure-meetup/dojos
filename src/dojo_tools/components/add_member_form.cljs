(ns dojo-tools.components.add-member-form
  (:require [re-frame.core :as rf]
            ; [dojo-tools.specs :refer [check-member]]
            [dojo-tools.components.form :refer [form]]
            [dojo-tools.components.bulma.core :as b]))

(defn add-member-form-render [{:keys [name name-error on-submit]}]
  [:form {:on-submit on-submit}
   [:h3 {:class "stylish-font"}
    "Join to DOJO members"]

   [b/control
    [b/input {:type        "text"
              :on-change   (:action name)
              :placeholder "Write your name and press Enter"}]]])

(defn save-member [form-state]
  (rf/dispatch [:save-member form-state]))

(def form-errors
  {:name "Name should not be empty"})

(defn validate-member-form [member-form-state]
  (some->> member-form-state
           ; check-member
           keys
           (select-keys form-errors)))

(defn add-member-form [{:keys [dojo-id]}]
  [form {:id        :add-member-form
         :validate  validate-member-form
         :on-submit save-member
         :fields    {:id      {:default-value (str (random-uuid))}
                     :dojo-id {:default-value dojo-id}
                     :name    nil}}
   (fn [fields errors on-submit]
     [add-member-form-render
      {:name       (:name fields)
       :name-error (:name errors)
       :on-submit  on-submit}])])
