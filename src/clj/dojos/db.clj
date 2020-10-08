(ns dojos.db
  (:require [integrant.core :as ig]
            [datahike.api :as datahike]))


(def cfg            ;; TODO move this to the config
  {:store              {:backend :mem
                        :id      "dojos-store"}
   :name               "dojos"
   :schema-flexibility :write
   :keep-history?      true})


(def schema
  [{:db/ident       :dojo/name
    :db/valueType   :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident       :dojo/id
    :db/unique      :db.unique/identity
    :db/valueType   :db.type/long
    :db/cardinality :db.cardinality/one}])


(def demo-data
  [{:dojo/name "First Dojo", :dojo/id 20}
   {:dojo/name "Second Dojo", :dojo/id 30}
   {:dojo/name "Third Dojo", :dojo/id 40}])


(defmethod ig/init-key :dojos/db [_ _]
  (datahike/create-database cfg)

  (let [conn (datahike/connect cfg)]
    (datahike/transact conn schema)
    (datahike/transact conn demo-data) ;; TODO only for initial setup
    conn))


(defmethod ig/halt-key! :dojos/db [_ _]
  (datahike/delete-database cfg))
