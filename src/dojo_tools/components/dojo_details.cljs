(ns dojo-tools.components.dojo-details
  (:require [re-frame.core :as rf]
            [dojo-tools.components.bootstrap :refer [grid row col]]
            [dojo-tools.components.add-member-form :refer [add-member-form]]
            [dojo-tools.components.dojo-members-list :refer [dojo-members-list]]
            [dojo-tools.components.dojo-groups :refer [dojo-groups]]
            [markdown.core :refer [md->html]]))


(defn dojo-members-groups [{:keys [dojo-id]}]
  [:div
   [:h2 "DOJO groups"]

   [dojo-groups {:dojo-id dojo-id}]])


(defn dojo-collect-members [{:keys [dojo-id]}]
  [:<>
   [add-member-form {:dojo-id dojo-id}]
   [dojo-members-list {:dojo-id dojo-id}]])


(defn dojo-preview [{:keys [dojo]}]
  [:article {:class "dojo-details"}
   (when (:cover dojo)
     [:img {:class "dojo-details__cover" :style {:background-image (str "url(" (:cover dojo) ")")}}])
   [:h1 {:class "dojo-details__header"}
    (:title dojo)]
   [:div {:class "dojo-details__description"
          :dangerouslySetInnerHTML
          {:__html (md->html (:description dojo))}}]])

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

