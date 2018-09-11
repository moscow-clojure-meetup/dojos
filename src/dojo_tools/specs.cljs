(ns dojo-tools.specs
  (:require [schema.core :as s :include-macros true]
            [schema.coerce :as coerce]
            [schema.utils :as s-utils]))


(def Id s/Str)

(defn id-keys-matcher [schema]
  (when (= Id schema)
    (coerce/safe
      (fn [x]
        (if (keyword? x)
          (name x)
          x)))))


(defn log-error [message data error]
  (js/console.group message)
  (js/console.log "data -> " (clj->js data))
  (js/console.log "errors -> " (clj->js error))
  (js/console.groupEnd))


(defn coerce-data [spec data]
  (let [matcher (coerce/first-matcher [id-keys-matcher coerce/json-coercion-matcher])
        coercer (coerce/coercer spec matcher)
        result  (coercer data)]
    (if (s-utils/error? result)
      (do
        (log-error "Data doesn't match spec" data (s-utils/error-val result))
        data)

      result)))


(defn check [spec db]
  (let [mismatch (s/check spec db)]
    (when mismatch
      (log-error "Data doesn't match spec" db mismatch))))


;; Dojos
(def dojo-spec
  {:id          Id
   :title       s/Str
   ;:description s/Str
   ;:cover       s/Str
   ;:place       s/Str
   ;:gallery     [s/Str]
   :start-time  s/Int})

(def dojos-spec
  {s/Str dojo-spec})

(def coerce-dojos
  (partial coerce-data dojos-spec))


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
