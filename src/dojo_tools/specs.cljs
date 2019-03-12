(ns dojo-tools.specs
  (:require [cljs.spec.alpha :as s]
            [spec-tools.core :as st]))

(s/def ::id
  string?)

(s/def ::dojo-id
  string?)

(s/def ::title
  string?)

(s/def ::name
  string?)

(s/def ::description
  string?)

(s/def ::state
  keyword?)

(s/def ::timestamp
  (st/spec
    {:spec          inst?
     :description   "milliseconds to js Date"
     :decode/string #(js/Date. %2)
     :encode/string #(js/Number %2)}))

(s/def ::start-time
  ::timestamp)

(s/def ::cover
  string?)

(s/def ::place
  string?)

(s/def ::photo-url
  string?)

(s/def ::gallery
  (s/coll-of ::photo-url))

(s/def ::number
  number?)

(s/def ::group-members
  (s/coll-of ::id))

(s/def ::upcommin-dojos
  (s/coll-of ::dojo-id))

(s/def ::past-dojos
  (s/coll-of ::dojo-id))

;; Dojos
(s/def ::dojo
  (s/keys :req-un [::id ::title ::description ::state ::start-time]
          :opt-un [::cover ::place ::gallery]))

(s/def ::dojos
  (s/map-of ::id ::dojo))

;; Members
(s/def ::member
  (s/keys :req-un [::id ::name ::dojo-id]))

(s/def ::members
  (s/map-of ::id ::member))

;; Groups
(s/def :group/members
  (s/coll-of ::id))

(s/def ::members-group
  (s/keys :req-un [::id ::number ::dojo-id :group/members]))

(s/def ::members-groups
  (s/map-of ::id ::members-group))

;; DB
(s/def ::db
  (s/keys :opt-un [::dojos ::members ::members-groups
                   ::upcommin-dojos ::past-dojos]))

;; Check
(defn log-error [message data error]
  (js/console.group message)
  (js/console.log "data -> " (clj->js data))
  (js/console.log "errors -> " (clj->js error))
  (js/console.groupEnd))

(defn check [spec db]
  (when-not (s/valid? spec db)
    (let [mismatch (s/explain-data spec db)]
      (log-error (str "Validate error | Spec " spec)
                 db
                 mismatch))))

(def check-member
  (partial check ::member))

(def check-db
  (partial check ::db))

;; Coerce
(defn coerce-dojo [dojo]
  (st/coerce ::dojo dojo st/string-transformer))

(defn coerce-dojos [dojos]
  (st/coerce ::dojos dojos st/string-transformer))

(defn coerce-member [member]
  (st/coerce ::member member st/string-transformer))

(defn coerce-members [members]
  (st/coerce ::members members st/string-transformer))

(defn coerce-members-group [group]
  (st/coerce ::members-group group st/string-transformer))

(defn coerce-members-groups [groups]
  (st/coerce ::members-groups groups st/string-transformer))
