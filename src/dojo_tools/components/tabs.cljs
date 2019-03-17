(ns dojo-tools.components.tabs
  (:require [dojo-tools.components.bulma.core :as b]
            [dojo-tools.router :refer [url-for]]))

(defn tabs [{:keys [active]}]
  [b/tabs {:tabs       [{:id      :dojos
                         :url     (url-for :dojos)
                         :content "ДОДЖО"}
                        {:id      :about
                         :url     (url-for :about)
                         :content "ЧТО ЭТО?"}
                        {:id      :puzzles
                         :url     (url-for :puzzles)
                         :content "ЗАДАЧИ"}]
           :active-tab active
           :mods       [:is-toggle :is-toggle-rounded :is-centered]}])
