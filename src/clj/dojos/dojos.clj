(ns dojos.dojos
  (:require [datahike.api :as datahike]))


(defn get-dojos [{:keys [db]} & _]
  (datahike/q
   '[:find [(pull ?dojo [*]) ...]
     :where
     [?dojo :dojo/id _]]
   db))
