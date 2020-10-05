(ns dojos.config
  (:require [integrant.core :as ig]))


(defmethod ig/init-key :dojos/config [_ params]
  params)
