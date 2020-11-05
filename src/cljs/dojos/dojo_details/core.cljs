(ns dojos.dojo-details.core
  (:require [dojos.app.graphql :as gql]))


(def dojo-details-query
  (gql/make-query
   "query dojo($id: Int) {
       dojo(id: $id) { id, name }
   }"))


(defn main [{:keys [dojo-id]}]
  (let [id    (js/parseInt dojo-id)
        {:keys [data]} (gql/use-query dojo-details-query {:variables {:id id}})
        title (get-in data [:dojo :name])]
    [:h3 title]))
