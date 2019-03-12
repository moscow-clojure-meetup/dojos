(ns dojo-tools.components.app
  (:require [re-frame.core :as rf]
            [clojure.string :refer [starts-with?]]
            [fela :refer [createRenderer]]
            [react-fela :refer [Provider]]
            [dojo-tools.components.public-page :refer [public-page]]
            [dojo-tools.components.admin-page :refer [admin-page]]))

(defonce renderer
  (createRenderer))

(defn app []
  (let [route-name @(rf/subscribe [:current-route-name])]
    [:> Provider {:renderer renderer}
     (if (starts-with? (name route-name) "admin")
        [admin-page {:route-name route-name}]
        [public-page {:route-name route-name}])]))
