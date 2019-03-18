(ns dojo-tools.components.footer
  (:require [react-fela]
            [dojo-tools.components.fela :refer-macros [fela]]
            [dojo-tools.components.bulma.core :as b]))

(defn footer-link [{:keys [href text]}]
  (fela [cn {:margin-top ".5rem"}]
    [:p {:class cn}
     (fela [cn {:color    "#5bb7db"
                "&:hover" {:color "#fff"}}]
       [:a {:class  cn
            :href   href
            :target "_blank"}
        text])]))

(defn footer []
  (fela [cn {:background "#363636"
             :margin-top "4rem"}]
    [:footer {:class ["footer" cn]}
     [b/container
      [b/columns
       [b/column {:mods [:is-3]}
        [footer-link {:href "https://www.meetup.com/Moscow-Clojure-Script-Meetup/"
                      :text "Meetup"}]
        [footer-link {:href "https://github.com/moscow-clojure-meetup"
                      :text "Github"}]
        [footer-link {:href "https://www.facebook.com/groups/clojuremoscow/"
                      :text "Facebook"}]]]]]))
