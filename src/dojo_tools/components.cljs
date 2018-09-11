(ns dojo-tools.components
  (:require [re-frame.core :as rf]
            [dojo-tools.router :refer [url-for]]
            [dojo-tools.bootstrap :refer [grid row col]]))


;; Components
(defn dojos-list [{:keys [dojos title class]}]
  [:section {:class ["dojos-list" class]}
   [:h3 {:class "dojos-list__title"}
    title]

   [:ul {:class "dojos-list__events"}
    (for [dojo dojos]
      ^{:key (:id dojo)}
      [:a {:href  (url-for :dojo-details {:dojo-id (:id dojo)})
           :class ["link" "dojos-list__event-link"]}
       [:li {:class "dojos-list__event"}
        [:h2 {:class "dojos-list__event-title"}
         (:title dojo)]

        [:div {:class "dojos-list__event-time"}
         (:start-time dojo)]]])]])


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


(defn footer []
  [:footer {:class "footer"}
   [:a {:class  "link footer__link"
        :href   "https://www.meetup.com/Moscow-Clojure-Script-Meetup/"
        :target "_blank"}
    "Meetup"]

   [:a {:class  "link footer__link"
        :href   "https://github.com/moscow-clojure-meetup"
        :target "_blank"}
    "Github"]

   [:a {:class  "link footer__link"
        :href   "https://www.facebook.com/groups/clojuremoscow/"
        :target "_blank"}
    "Facebook"]])


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


(defn dojo-details []
  (let [dojo (rf/subscribe [:dojo-by-route])]
    (fn []
      [:h1
       (:title @dojo)])))


(defn app-content []
  (let [route-name (rf/subscribe [:current-route-name])]
    (fn []
      (let [content-component (case @route-name
                                :dojo-details dojo-details
                                dojos)]
        [content-component]))))


(defn app []
  [:<>
   [grid {:class "app"}
    [row
     [col {:xs 12}
      [:a {:href  (url-for :dojos)
           :class "link app__title-link"}
       [:h1 {:class "app__title"}
        [:span "MCLJSUG"]
        [:span "DOJO"]]]]]

    [app-content]]

   [footer]])
