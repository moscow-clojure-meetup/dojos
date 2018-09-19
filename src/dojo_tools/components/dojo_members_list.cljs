(ns dojo-tools.components.dojo-members-list
  (:require [re-frame.core :as rf]
            [dojo-tools.components.bootstrap :as b]))


(defn dojo-members-list-render [{:keys [members]}]
  [b/list-group {:class ""}
   (for [member members]
     ^{:key (:id member)}
     [b/list-group-item
      [:span
       (:name member)]])])


(defn dojo-members-list [{:keys [dojo-id]}]
  (let [dojo-members (rf/subscribe [:dojo-members dojo-id])]
    (fn []
      [dojo-members-list-render
       {:members @dojo-members}])))
