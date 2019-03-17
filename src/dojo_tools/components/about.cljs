(ns dojo-tools.components.about
  (:require [react-fela]
            [dojo-tools.components.tabs :refer [tabs]]
            [dojo-tools.components.fela :refer-macros [fela]]
            [dojo-tools.components.bulma.core :as b]))

(defn about []
  [:<>
   [b/container
    (fela [cn {:margin "40px 0 20px"}]
      [:div {:class cn}
       [tabs {:active :about}]
       [:h2 "About"]])]])
