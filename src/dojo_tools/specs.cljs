(ns dojo-tools.specs
  (:require [moment]
            [reagent.interop :refer [$]]
            [schema.core :as s :include-macros true]
            [schema.coerce :as coerce]
            [schema.utils :as s-utils]))


(def Id s/Str)

(def Timestamp s/Num)

(defn non-empty [x]
  (not (empty? x)))


(defn id-keys-matcher [schema]
  (when (= Id schema)
    (coerce/safe
      (fn [x]
        (if (keyword? x)
          (name x)
          x)))))


(defn timestamp-matcher [schema]
  (when (= Timestamp schema)
    (coerce/safe
      (fn [x]
        (if ($ moment isMoment x)
          (js/Number x)
          x)))))


(defn log-error [message data error]
  (js/console.group message)
  (js/console.log "data -> " (clj->js data))
  (js/console.log "errors -> " (clj->js error))
  (js/console.groupEnd))


(defn coerce-data [spec data]
  (let [matcher (coerce/first-matcher [id-keys-matcher
                                       timestamp-matcher
                                       coerce/json-coercion-matcher])
        coercer (coerce/coercer spec matcher)
        result  (coercer data)]
    (if (s-utils/error? result)
      (do
        (log-error "Coerce | Data doesn't match spec" data (s-utils/error-val result))
        data)

      result)))


(defn check [spec db]
  (let [mismatch (s/check spec db)]
    (when mismatch
      (log-error "Validate | Data doesn't match spec" db mismatch))))


;; Dojos
(def dojo-spec
  {:id                       Id
   :title                    (s/constrained s/Str non-empty)
   :description              (s/constrained s/Str non-empty)
   (s/optional-key :cover)   s/Str
   (s/optional-key :place)   s/Str
   (s/optional-key :gallery) [s/Str]
   :start-time               Timestamp})

(def coerce-dojo
  (partial coerce-data dojo-spec))

(def check-dojo
  (partial s/check dojo-spec))

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
