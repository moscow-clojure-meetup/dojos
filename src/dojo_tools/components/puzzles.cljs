(ns dojo-tools.components.puzzles
  (:require [react-fela]
            [dojo-tools.components.tabs :refer [tabs]]
            [dojo-tools.components.fela :refer-macros [fela]]
            [dojo-tools.components.bulma.core :as b]))

(defn puzzles []
  [:<>
   [b/container
    (fela [cn {:margin "20px 0 20px"}]
      [:div {:class cn}
       [tabs {:active :puzzles}]
       [:h2 "Puzzles"]])]])
