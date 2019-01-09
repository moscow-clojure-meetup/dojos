(ns dojo-tools.components.form
  (:require [reagent.core :refer [create-class props children]]
            [reagent.interop :refer-macros [$]]
            [re-frame.core :refer [dispatch dispatch-sync subscribe]]))

(defn event->value [event]
  ($ event :target.value))

(defn create-fields [raw-fields {:keys [id fields]}]
  (reduce-kv
   (fn [fields field-id field]
     (assoc
      fields
      field-id
      {:value  (get raw-fields field-id)
       :action (fn [& args]
                 (let [process (or (:process field)
                                   event->value)
                       value   (apply process args)]
                   (dispatch [:update-form-field id field-id value])))}))
   {}
   fields))

(defn reduce-to-subs [m k v]
  (assoc m k (subscribe [:to-path v])))

(defn form [{:keys [id on-submit clean-up validate include] :as form-props}]
  (let [form-state   (subscribe [:get-form id])
        include-subs (when include (reduce-kv reduce-to-subs {} include))
        form-submit  (fn [state event]
                       ($ event preventDefault)

                       (if validate
                         (let [errors (validate state)]
                           (if errors
                             (dispatch [:set-form-errors id errors])
                             (on-submit state)))

                         (on-submit state)))]
    (create-class
     {:display-name
      "form"

      :component-will-mount
      (fn []
        (dispatch-sync [:create-form form-props]))

      :component-will-unmount
      (fn []
        (when clean-up
          (clean-up)))

      :render
      (fn [this]
        (let [ui-fn      (first (children this))
              form-props (props this)
              state      @form-state
              includes   (when include-subs
                           (into
                            {}
                            (map #(vector (first %) (deref (second %)))
                                 include-subs)))]
          (ui-fn
             ;; fields + included values from state
           (merge (create-fields state form-props) includes)
             ;; form errors
           (:errors state)
             ;; form submit function or nil
           (when on-submit (partial form-submit (dissoc state :errors))))))})))
