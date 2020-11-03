(ns dojos.dojos
  (:require [datahike.api :as datahike]))


(defn get-dojos [{:keys [db]} & _]
  (datahike/q
   '[:find [(pull ?dojo [*]) ...]
     :where
     [?dojo :dojo/id _]]
   db))


(defn get-dojo-by-id [{:keys [db]} {:keys [id]} & _]
  (datahike/q
   '[:find (pull ?dojo [*]) .
     :in $ ?dojo-id
     :where
     [?dojo :dojo/id ?dojo-id]]
   db id))
