(ns dojo-tools.components.admin-sidebar
  (:require [dojo-tools.router :refer [url-for]]
            [dojo-tools.components.bulma.core :as b]))

(defn admin-sidebar []
  (defn tabs [{:keys [active]}]
    [b/tabs {:tabs       [{:id      :admin-dojos
                           :url     (url-for :admin-dojos)
                           :content "Admin"}
                          {:id      :about
                           :url     (url-for :admin-dojo-form)
                           :content "New dojo"}]
             :active-tab active
             :mods       [:is-toggle :is-toggle-rounded :is-centered]}]))
