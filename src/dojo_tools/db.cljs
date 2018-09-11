(ns dojo-tools.db
  (:require [re-frame.core :as rf]
            [dojo-tools.specs :refer [check db-spec]]))


(def check-db-spec
  (rf/after (partial check db-spec)))


(def default
  {:dojos          nil
   :members        nil
   :members-groups nil

   :upcommin-dojos ["2018-09-05"]
   :past-dojos     ["2018-09-06" "2018-09-07"]})
