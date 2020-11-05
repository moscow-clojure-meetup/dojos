(ns dojos.core
  (:require [uix.dom.alpha :as uix.dom]
            [dojos.app.routing :as routing]
            [dojos.app.root :as app.root]))


(def development?
  ^boolean goog.DEBUG)


(defn mount []
  (uix.dom/render [app.root/main] js/app))


(defn on-update []
  (routing/init!)
  (mount))


(defn ^:export main []
  (when development?
    (enable-console-print!))

  (routing/init!)
  (mount))
