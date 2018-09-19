(ns dojo-tools.components.admin-dojo-form
  (:require [moment]
            [react-datepicker :as dp]
            [re-frame.core :as rf]
            [dojo-tools.router :refer [url-for]]
            [dojo-tools.specs :refer [coerce-dojo check-dojo]]
            [dojo-tools.components.form :refer [form]]
            [dojo-tools.components.bootstrap :as b]))


(defn admin-dojo-form-render [{:keys [on-submit errors title description start-time]}]
  [:<>
   [b/page-header {:class "admin-header"}
    "New DOJO"]

   [:form {:on-submit on-submit}
    [b/form-group {:validation-state (when (:title errors) "error")}
     [b/control-label
      "dojo title"]

     [b/form-control {:type        "text"
                      :on-change   (:action title)
                      :placeholder "Name new dojo"}]

     [b/feedback]]

    [b/form-group {:validation-state (when (:description errors) "error")}
     [b/control-label
      "dojo description"]

     [b/form-control {:rows            8
                      :component-class "textarea"
                      :on-change       (:action description)
                      :placeholder     "What is gonna happen"}]

     [b/feedback]]

    [b/form-group
     [b/control-label
      "dojo start date"]

     [b/form-control {:component-class dp/default
                      :date-format     "DD MMM YYYY"
                      :selected        (:value start-time)
                      :on-change       (:action start-time)}]

     [b/feedback]]

    [:div
     [b/button {:type     "submit"
                :bs-style "success"
                :bs-size  "large"}
      "Save"]]]])


(defn save-new-dojo [dojo-form-state]
  (rf/dispatch [:save-new-dojo dojo-form-state]))


(def form-errors
  {:title       "Title should not be empty"
   :description "Description should not be empty"})

(defn validate-dojo-form [dojo-form-state]
  (some->> dojo-form-state
           coerce-dojo
           check-dojo
           keys
           (select-keys form-errors)))

(defn admin-dojo-form []
  [form {:id        :dojo-form
         :on-submit save-new-dojo
         :validate  validate-dojo-form
         :fields    {:id          {:default-value (str (random-uuid))}
                     :start-time  {:default-value (moment)
                                   :process       identity}
                     :title       {:default-value ""}
                     :description {:default-value ""}
                     :state       {:default-value :pending}}}
   (fn [fields errors on-submit]
     [admin-dojo-form-render
      {:on-submit   on-submit
       :errors      errors
       :title       (:title fields)
       :description (:description fields)
       :start-time  (:start-time fields)}])])
