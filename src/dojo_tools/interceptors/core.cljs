(ns dojo-tools.interceptors.core
  (:require [re-frame.core :as rf]
            [dojo-tools.specs :as specs]))

(def check-db
  (rf/after (partial specs/check ::specs/db)))
