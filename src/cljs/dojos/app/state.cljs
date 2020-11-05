(ns dojos.app.state
  (:require ["@apollo/client" :as apollo]))


(def current-route
  (apollo/makeVar nil))
