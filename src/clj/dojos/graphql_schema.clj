(ns dojos.graphql-schema
  (:require [clojure.edn :as edn]
            [integrant.core :as ig]
            [com.walmartlabs.lacinia.util :refer [attach-resolvers]]
            [com.walmartlabs.lacinia.schema :as schema]
            [dojos.dojos :refer [get-dojos]]))


(defn get-prop [& ks]
  (fn [_ _ v]
    (get-in v ks)))


(defmethod ig/init-key :dojos/graphql-schema [_ {:keys [schema-file]}]
  (-> schema-file
      slurp
      edn/read-string
      (attach-resolvers ;; TODO scan project for resolvers automatically
       {:get-dojos get-dojos
        :get       get-prop})
      schema/compile))
