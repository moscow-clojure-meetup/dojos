(ns dojos.dojos-feed.core
  (:require ["react" :as react]
            [applied-science.js-interop :as jsi]
            [dojos.app.graphql :as gql]
            [reitit.frontend.easy :refer [href]]

            ["@material-ui/core/Link" :default Link]
            ["@material-ui/core/List" :default List]
            ["@material-ui/core/ListItem" :default ListItem]
            ["@material-ui/core/ListItemText" :default ListItemText]
            ["@material-ui/lab/Skeleton" :default Skeleton]))


(defn dojo-list-item [{:keys [id name]}]
  [:> ListItem {:key       id
                :component Link
                :href      (href :dojo-details {:dojo-id id})}
   [:> ListItemText
    {:primary name}]])


(defn loading-list-item [key]
  [:> ListItem {:key key}
   [:> ListItemText
    {:primary (react/createElement Skeleton #js {:width "20ex"})}]])


(def static-loading-list
  [:> List
   (map loading-list-item (range 3))])


(def dojos-query
  (gql/make-query "{ dojos { id name } }"))


(defn main []
  (let [{:keys [loading error data]} (gql/use-query dojos-query)]
    (cond
      loading
      static-loading-list

      (some? error)
      (str "Error happened: " (jsi/get error :message))

      :else
      [:> List (map dojo-list-item (:dojos data))])))
