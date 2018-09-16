(ns dojo-tools.utils
  (:require [moment]
            [reagent.interop :refer-macros [$]]))

(defn format-dojo [dojo]
  (update dojo :start-time #(-> (moment %)
                                ($ format "DD MMM YYYY  H:mm"))))


(defn prepare-form
  "Prepare form fields to use in components"
  [fields]
  (-> (reduce-kv
        (fn [fields id field]
          (assoc fields id (:default-value field)))
        {}
        fields)
      (assoc :errors {})))
