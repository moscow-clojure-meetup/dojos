(ns dojo-tools.components.admin-page
  (:require [react-fela]
            [dojo-tools.components.fela :refer-macros [fela]]
            [dojo-tools.components.header :refer [header]]
            [dojo-tools.components.footer :refer [footer]]
            [dojo-tools.components.admin-sidebar :refer [admin-sidebar]]
            [dojo-tools.components.bulma.core :as b]
            [dojo-tools.components.admin-dojos :refer [admin-dojos]]
            [dojo-tools.components.admin-dojo-form :refer [admin-dojo-form]]
            ))
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
  (fela [cn {:display          "flex"
             :flex-direction   "column"
             :min-height       "100vh"
             :border-top-width "4px"
             :border-top-style "solid"
             :border-image     "linear-gradient(to left, #5881d8, #63b132) 100% 1"}]
        [:div {:class cn}
         (fela [cn {:flex 1}]
               [b/section {:class cn}
                [header]
                [admin-sidebar]
                [admin-page-content {:route-name route-name}]
                ])
         [footer]]))
