(ns dojo-tools.components.app
  (:require [re-frame.core :as rf]
            [clojure.string :refer [starts-with?]]
            [dojo-tools.router :refer [url-for]]
            [dojo-tools.components.bootstrap :refer [grid row col]]
            [dojo-tools.components.footer :refer [footer]]
            [dojo-tools.components.dojos :refer [dojos]]
            [dojo-tools.components.admin-dojo-form :refer [admin-dojo-form]]
            [dojo-tools.components.dojo-details :refer [dojo-details]]
            [dojo-tools.components.admin-dojos :refer [admin-dojos]]
            [dojo-tools.components.admin-sidebar :refer [admin-sidebar]]
            [dojo-tools.components.admin-run-dojo :refer [admin-run-dojo]]))


(defn public-page-content [{:keys [route-name]}]
  (let [content-component (case route-name
                            :dojo-details dojo-details
                            dojos)]
    [content-component]))


;; Public base layout
(defn public-page [{:keys [route-name]}]
  [:<>
   [grid {:class "app"}
    [row
     [col {:xs 12}
      [:a {:href  (url-for :dojos)
           :class "link app__title-link"}
       [:h1 {:class "app__title"}
        [:span "MCLJSUG"]
        [:span "DOJO"]]]]]

    [public-page-content {:route-name route-name}]]

   [footer]])


(defn admin-page-content [{:keys [route-name]}]
  (let [content-component (case route-name
                            :admin-dojo-form admin-dojo-form
                            :admin-dojos admin-dojos
                            :admin-run-dojo admin-run-dojo
                            nil)]
    [content-component]))


;; Admin base layout
(defn admin-page [{:keys [route-name]}]
  [:<>
   [grid {:class "app"
          :fluid true}
    [row
     [col {:sm 3
           :md 2
           :class "app__admin-sidebar"}
      [admin-sidebar]]

     [col {:sm        9
           :sm-offset 3
           :md        10
           :md-offset 2
           :class "app__admin-main"}
      [admin-page-content {:route-name route-name}]]]]])


(defn app []
  (let [route-name (rf/subscribe [:current-route-name])]
    (fn []
      (if (starts-with? (name @route-name) "admin")
        [admin-page {:route-name @route-name}]
        [public-page {:route-name @route-name}]))))
