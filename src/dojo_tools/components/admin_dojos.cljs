(ns dojo-tools.components.admin-dojos
  (:require [re-frame.core :as rf]
            [dojo-tools.router :refer [url-for]]
            [dojo-tools.components.bootstrap :as b]))

(defn admin-dojos-render [{:keys [dojos]}]
  [:<>
   [b/page-header {:class "admin-header"}
    "Create or edit DOJOS"]

   [:div {:class "admin-dojos__controls"}
    [b/button {:class    "admin-dojos__new"
               :bs-style "success"
               :href     (url-for :admin-dojo-form)}
     [b/glyph-icon {:glyph "plus"}]

     [:span {:class "admin-dojos__new-text"}
      "New DOJO"]]]

   [b/list-group {:class "admin-dojos__list"}
    (for [[id dojo] dojos
          :let [title (:title dojo)]]
      ^{:key id}
      [b/list-group-item {:class "admin-dojos__dojo"}
       [:div {:class "admin-dojos__dojo-title"}
        [:b title]]

       [:div
        [b/button {:href     (url-for :admin-run-dojo {:dojo-id id})
                   :bs-style "success"
                   :bs-size  "xs"}
         [b/glyph-icon {:glyph "play"}]]]])]])

(defn admin-dojos []
  (let [dojos (rf/subscribe [:dojos])]
    (fn []
      [admin-dojos-render
       {:dojos @dojos}])))
