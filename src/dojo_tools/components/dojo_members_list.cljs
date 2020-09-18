(ns dojo-tools.components.dojo-members-list
  (:require [re-frame.core :as rf]))

(defn dojo-members-list-render [{:keys [members]}]
  [:ul
   (for [member members]
     ^{:key (:id member)}
     [:li
      [:span
       (:name member)]])])

(defn dojo-members-list [{:keys [dojo-id]}]
  (let [dojo-members (rf/subscribe [:dojo-members dojo-id])]
    (fn []
      [dojo-members-list-render
       {:members @dojo-members}])))
