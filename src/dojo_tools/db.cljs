(ns dojo-tools.db
  (:require [schema.core :as s :include-macros true]
            [re-frame.core :as rf]))

;; Dojos
(def dojo-spec
  {:id          s/Str
   :title       s/Str
   :description s/Str
   :cover       s/Str
   :place       s/Str
   :gallery     [s/Str]
   :start-time  s/Int})

(def dojos-spec
  {s/Str dojo-spec})

;; Members
(def member
  {:id      s/Str
   :name    s/Str
   :dojo-id s/Str})

(def members
  {s/Str member})

;; Groups
(def members-group
  {:id      s/Str
   :number  s/Int
   :dojo-id s/Str
   :members [s/Str]})

(def members-groups
  {s/Str members-group})

;; Misc
(def upcommin-dojos-spec
  [s/Str])

(def past-dojos-spec
  [s/Str])

;; DB
(def db-spec
  {:dojos          (s/maybe dojos-spec)
   :members        (s/maybe members)
   :members-groups (s/maybe members-groups)
   :upcommin-dojos upcommin-dojos-spec
   :past-dojos     past-dojos-spec
   s/Any           s/Any})


(defn check [spec db]
  (let [mismatch (s/check spec db)]
    (when mismatch
      (do
        (js/console.group "DB doesn't match spec")
        (js/console.log "data -> " (clj->js db))
        (js/console.log "errors -> " (clj->js mismatch))
        (js/console.groupEnd)))))

(def check-db-spec
  (rf/after (partial check db-spec)))


(def default
  {:dojos          nil
   :members        nil
   :members-groups nil

   :upcommin-dojos ["2018-09-05"]
   :past-dojos     ["2018-09-06" "2018-09-07"]})
