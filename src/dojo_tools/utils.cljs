(ns dojo-tools.utils
  (:require [moment]
            [clojure.string :as str]
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


(defn split-member-to-groups
  ([members partition]
   (split-member-to-groups members partition 0))

  ([members partition shuffle-count]
   (if (= shuffle-count 4)
     (partition-all partition members)
     (recur (shuffle members)
            partition
            (inc shuffle-count)))))


(defn create-members-group [dojo-id idx members-list]
  {:id      (random-uuid)
   :number  idx
   :dojo-id dojo-id
   :members (vec (map :id members-list))})


(defn reduce-by-id [coll]
  (reduce
    (fn [acc {:keys [id] :as item}] (assoc acc id item))
    {}
    coll))


(defn get-str [x]
  (cond
    (keyword? x) (name x)
    :else (str x)))


(defn vec->path [path-vector]
  (->> path-vector (map get-str) (str/join "/")))
