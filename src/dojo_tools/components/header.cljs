(ns dojo-tools.components.header
  (:require [react-fela]
            [dojo-tools.components.fela :refer-macros [fela]]
            [dojo-tools.router :refer [url-for]]
            [dojo-tools.components.bulma.core :as b]))

(defn header []
  (fela [cn {:text-align  :center
             :font-family "Monoton, cursive"}]
    [:header {:class cn}
     [b/title {:as   :h1
               :mods [:is-1]}
      (fela [cn {:font-weight                100
                 "@media (max-width: 768px)" {:display        "flex"
                                              :flex-direction "column"}}]
        [b/button {:href  (url-for :dojos)
                   :class cn
                   :mods  [:is-pseudo]}
           [:span "CLOJURE"]

           (fela [cn {"@media (min-width: 769px)" {:margin-left "20px"}}]
             [:span {:class cn}
              "DOJO"])])]]))
