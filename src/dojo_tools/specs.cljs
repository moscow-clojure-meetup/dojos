(ns dojo-tools.specs
  (:require [moment]
            [reagent.interop :refer [$]]
            [schema.core :as s :include-macros true]
            [schema.coerce :as coerce]
            [schema.utils :as s-utils]))

(def Id s/Str)

(def IdsList [Id])

(def Timestamp s/Num)

(defn non-empty [x]
  (not (empty? x)))

(def NonEmptyStr (s/constrained s/Str non-empty))

(defn id-keys-matcher [schema]
  (when (= Id schema)
    (coerce/safe
     (fn [x]
       (if (keyword? x)
         (name x)
         (str x))))))

(defn ids-list-keys-matcher [schema]
  (when (= IdsList schema)
    (coerce/safe
     (fn [x]
       (if (map? x)
         (vals x)
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
                                       ids-list-keys-matcher
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
   :title                    NonEmptyStr
   :description              NonEmptyStr
   :state                    s/Keyword
   (s/optional-key :cover)   s/Str
   (s/optional-key :place)   s/Str
   (s/optional-key :gallery) [s/Str]
   :start-time               Timestamp})

(def dojos-spec
  {Id dojo-spec})

(def coerce-dojo
  (partial coerce-data dojo-spec))

(def check-dojo
  (partial s/check dojo-spec))

(def coerce-dojos
  (partial coerce-data dojos-spec))

;; Members
(def member-spec
  {:id      Id
   :name    NonEmptyStr
   :dojo-id Id})

(def members-spec
  {Id member-spec})

(def check-member
  (partial s/check member-spec))

(def coerce-members
  (partial coerce-data members-spec))

;; Groups
(def members-group
  {:id      Id
   :number  s/Int
   :dojo-id Id
   :members IdsList})

(def members-groups
  {Id members-group})

(def coerce-members-groups
  (partial coerce-data members-groups))

;; Misc
(def upcommin-dojos-spec
  [Id])

(def past-dojos-spec
  [Id])

;; DB
(def db-spec
  {:dojos          (s/maybe dojos-spec)
   :members        (s/maybe members-spec)
   :members-groups (s/maybe members-groups)
   :upcommin-dojos upcommin-dojos-spec
   :past-dojos     past-dojos-spec
   s/Any           s/Any})
