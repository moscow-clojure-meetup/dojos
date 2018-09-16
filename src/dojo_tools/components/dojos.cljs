(ns dojo-tools.components.dojos
  (:require [re-frame.core :as rf]
            [dojo-tools.router :refer [url-for]]
            [dojo-tools.components.bootstrap :refer [grid row col]]))


(defn dojos-list [{:keys [dojos title class]}]
  [:section {:class ["dojos-list" class]}
   [:h3 {:class "dojos-list__title"}
    title]

   [:ul {:class "dojos-list__events"}
    (if (> (count dojos) 0)
      (for [dojo dojos]
        ^{:key (:id dojo)}
        [:a {:href  (url-for :dojo-details {:dojo-id (:id dojo)})
             :class ["link" "dojos-list__event-link"]}
         [:li {:class "dojos-list__event"}
          [:h2 {:class "dojos-list__event-title"}
           (:title dojo)]

          [:div {:class "dojos-list__event-time"}
           (:start-time dojo)]]])

      [:li {:class "dojos-list__event"}
       [:h2 {:class "dojos-list__event-title"}
        "No dojos so far"]])]])


(defn past-dojos-list []
  (let [past-dojos (rf/subscribe [:past-dojos])]
    (fn []
      [dojos-list {:dojos @past-dojos
                   :title "Past Dojos"}])))


(defn upcomming-dojos-list []
  (let [upcomming-dojos (rf/subscribe [:upcomming-dojos])]
    (fn []
      [dojos-list {:dojos @upcomming-dojos
                   :title "Upcomming Dojos"}])))

(defn dojos []
  [row
   [col {:xs        12
         :md        10
         :md-offset 1}
    [upcomming-dojos-list]]

   [col {:xs        12
         :md        10
         :md-offset 1}
    [past-dojos-list]]])
