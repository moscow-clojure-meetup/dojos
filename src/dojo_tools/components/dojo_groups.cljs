(ns dojo-tools.components.dojo-groups
  (:require [re-frame.core :as rf]
            [dojo-tools.components.dojo-members-list :refer [dojo-members-list-render]]))

(defn dojo-group-render [{:keys [group-num members]}]
  [:div
   [:h3
    (str "Group №" group-num)]

   [dojo-members-list-render {:members members}]])

(defn dojo-group [{:keys [members-group]}]
  (let [members (rf/subscribe [:members-by-ids (:members members-group)])]
    (fn []
      [dojo-group-render
       {:group-num (:number members-group)
        :members   @members}])))

(defn dojo-groups-render [{:keys [members-groups]}]
  [:div {:class "dojo-groups stylish-font"}
   (for [group members-groups]
     ^{:key (:id group)}
     [dojo-group {:members-group group}])])

(defn dojo-groups [{:keys [dojo-id]}]
  (let [members-groups (rf/subscribe [:dojo-members-groups dojo-id])]
    (fn []
      [dojo-groups-render
       {:members-groups @members-groups}])))
