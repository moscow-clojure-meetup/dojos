(ns dojo-tools.components.admin-dojos
  (:require [re-frame.core :as rf]
            [dojo-tools.components.bulma.core :as b]
            [dojo-tools.router :refer [url-for]]))

(defn admin-dojos-render [{:keys [dojos]}]
  [:<>
   [b/title {:class "admin-header"}
    "Create or edit DOJOS"]

   [:div {:class "admin-dojos__controls"}
    [b/button {:class    "admin-dojos__new"
               :bs-style "success"
               :href     (url-for :admin-dojo-form)}
     "+"

     [:span {:class "admin-dojos__new-text"}
      "New DOJO"]]]

   [b/section {:class "admin-dojos__list"}
    (for [[id dojo] dojos
          :let [title (:title dojo)]]
      ^{:key id}
      [b/box {:class "admin-dojos__dojo"}
       [:div {:class "admin-dojos__dojo-title"}
        [:b title]
        [b/media-left
         [b/button {:href     (url-for :admin-run-dojo {:dojo-id id})
                    :bs-style "success"
                    :bs-size  "xs"}
          "Start"]]]])]])

(defn admin-dojos []
  (let [dojos (rf/subscribe [:dojos])]
    (fn []
      [admin-dojos-render
       {:dojos @dojos}])))
