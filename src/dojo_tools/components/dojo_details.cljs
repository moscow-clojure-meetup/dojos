(ns dojo-tools.components.dojo-details
  (:require [re-frame.core :as rf]
            [dojo-tools.components.bootstrap :refer [grid row col]]
            [dojo-tools.components.add-member-form :refer [add-member-form]]
            [dojo-tools.components.dojo-members-list :refer [dojo-members-list]]))


(defn dojo-members-groups [{:keys [dojo]}]
  [:div "members groups"])


(defn dojo-collect-members [{:keys [dojo-id]}]
  [:<>
   [add-member-form {:dojo-id dojo-id}]
   [dojo-members-list {:dojo-id dojo-id}]])


(defn dojo-preview [{:keys [dojo]}]
  [:h1
   (:title dojo)])


(defn dojo-details-render [{:keys [dojo]}]
  (let [dojo-state (:state dojo)
        dojo-id    (:id dojo)]
    (case dojo-state
      :pending [dojo-preview {:dojo dojo}]

      :started [dojo-collect-members {:dojo-id dojo-id}]

      :groups-created [dojo-members-groups {:dojo-id dojo-id}]

      nil)))


(defn dojo-details []
  (let [dojo (rf/subscribe [:dojo-by-route])]
    (fn []
      [dojo-details-render
       {:dojo @dojo}])))

