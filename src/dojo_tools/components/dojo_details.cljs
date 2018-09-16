(ns dojo-tools.components.dojo-details
  (:require [re-frame.core :as rf]
            [dojo-tools.components.bootstrap :refer [grid row col]]))


(defn dojo-details []
  (let [dojo (rf/subscribe [:dojo-by-route])]
    (fn []
      [:h1
       (:title @dojo)])))
