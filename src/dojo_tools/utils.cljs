(ns dojo-tools.utils
  (:require [moment]
            [reagent.interop :refer-macros [$]]))

(defn format-dojo [dojo]
  (update dojo :start-time #(-> (moment %)
                                ($ format "DD MMM YYYY  H:mm"))))

(defn stringify-keys [data]
  (reduce-kv
    (fn [res k v] (assoc res (name k) v))
    {}
    data))
