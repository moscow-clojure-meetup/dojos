(ns dojos.home.core
  (:require [reitit.frontend.easy :refer [href]]))


(defn main []
  [:div
   [:h2 "Welcome"]

   [:a {:href (href :dojos-list)}
    "Dojos feed"]])
