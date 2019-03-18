(ns dojo-tools.components.public-page
  (:require [react-fela]
            [dojo-tools.components.dojos :refer [dojos]]
            [dojo-tools.components.dojo-details :refer [dojo-details]]
            [dojo-tools.components.about :refer [about]]
            [dojo-tools.components.puzzles :refer [puzzles]]
            [dojo-tools.components.header :refer [header]]
            [dojo-tools.components.footer :refer [footer]]
            [dojo-tools.components.fela :refer-macros [fela]]
            [dojo-tools.components.bulma.core :as b]))

(defn page-content [{:keys [route-name]}]
  (let [content-component (case route-name
                            :dojos        dojos
                            :dojo-details dojo-details
                            :about        about
                            :puzzles      puzzles
                            [:h2 "Not found"])]
    [b/container
     [content-component]]))

;; Public base layout
(defn public-page [{:keys [route-name]}]
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
       [page-content {:route-name route-name}]])
    [footer]]))
