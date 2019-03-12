(ns dojo-tools.components.admin-page)
  ; (:require [dojo-tools.router :refer [url-for]]))

(defn grid [] [:div])
(defn row [] [:div])
(defn col [] [:div])
(defn admin-sidebar [] [:div])
(defn admin-dojo-form [] [:div])
(defn admin-dojos [] [:div])
(defn admin-run-dojo [] [:div])

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
