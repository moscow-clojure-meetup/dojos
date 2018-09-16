(ns dojo-tools.components.admin-sidebar
  (:require [dojo-tools.router :refer [url-for]]
            [dojo-tools.components.bootstrap :refer [nav nav-item]]))

(defn admin-sidebar []
  [nav {:class "admin-sidebar"}
   [nav-item {:class "admin-sidebar__link"
              :href (url-for :dojos)}
    "Home"]

   [nav-item {:class "admin-sidebar__link"
              :href (url-for :admin-dojos)}
    "Dojos"]])
