(ns dojo-tools.components.admin-dojo-form
  (:require [moment]
            [react-datepicker :as dp]
            [re-frame.core :as rf]
            [reagent.core :refer [adapt-react-class]]
            [dojo-tools.specs :refer [coerce-dojo]]
            [dojo-tools.components.form :refer [form]]
            [dojo-tools.components.bulma.core :as b]))

(def datepicker (adapt-react-class dp/default))

(defn admin-dojo-form-render [{:keys [on-submit errors title description start-time]}]
  [:<>
   [b/title {:class "admin-header"}
    "New DOJO"]

   [:form {:on-submit on-submit
           :class     "admin-dojo-form"}
    [:form-group {:validation-state (when (:title errors) "error")}
     [:label
      "dojo title"]

     [:input {:type        "text"
              :on-change   (:action title)
              :placeholder "Name new dojo"}]
]

    [:form-group {:validation-state (when (:description errors) "error")}
     [:label
      "dojo description"]

     [:textarea {:rows            8
                      :class           "root-font"
                      :component-class "textarea"
                      :on-change       (:action description)
                      :placeholder     "What is gonna happen"}]]

    [:form-group
     [:label
      "dojo start date"]

     [:input {:date-format     "DD MMM YYYY"}]
     ]

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
           ;; check-dojo
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
