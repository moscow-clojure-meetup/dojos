(ns dojos.core)


(def development?
  ^boolean goog.DEBUG)


(defn on-update []
  (println "update"))


(defn ^:export main []
  (when development?
    (enable-console-print!)
    (println "init app in dev mode")))
