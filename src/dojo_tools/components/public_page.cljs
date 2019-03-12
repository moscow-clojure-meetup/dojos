(ns dojo-tools.components.public-page
  (:require [dojo-tools.components.fela :refer [*cn*] :refer-macros [fela]]
            [dojo-tools.router :refer [url-for]]
            [react-fela :refer [FelaComponent]]
            [dojo-tools.components.bulma.core :as b]))

(defn footer [] [:div])
(defn dojo-details [] [:div])

(defn header []
  [:> FelaComponent {:as    :header
                     :style {:text-align  :center
                             :font-family "Monoton, cursive"}}
   [b/title {:as   :h1
             :mods [:is-1]}
    (fela {:font-weight                100
           "@media (max-width: 768px)" {:display        "flex"
                                        :flex-direction "column"}}
      [b/button {:href  (url-for :dojos)
                 :class *cn*
                 :mods  [:is-pseudo]}
         [:span "CLOJURE"]

         (fela {"@media (min-width: 769px)" {:margin-left "20px"}}
           [:span {:class *cn*}
            "DOJO"])])]])

(defn tabs-nav []
  [b/tabs {:tabs       [{:id      :dojos
                         :content "ДОДЖО"}
                        {:id      :about
                         :content "ЧТО ЭТО?"}
                        {:id      :puzzles
                         :content "ЗАДАЧИ"}]
           :active-tab :dojos
           :mods       [:is-toggle :is-toggle-rounded :is-centered]}])

(defn dojos []
  [:<>
   [b/container
    [:> FelaComponent {:style {:margin "40px 0 20px"}}
     [tabs-nav]]]])

(defn page-content [{:keys [route-name]}]
  (let [content-component (case route-name
                            :dojo-details dojo-details
                            dojos)]
    [content-component]))

;; Public base layout
(defn public-page [{:keys [route-name]}]
  (fela {:border-top-width "4px"
         :border-top-style "solid"
         :border-image "linear-gradient(to left, #5881d8, #63b132) 100% 1"}
   [:<>
    [b/section {:class *cn*}
     [header]
     [page-content {:route-name route-name}]]
    [footer]]))
