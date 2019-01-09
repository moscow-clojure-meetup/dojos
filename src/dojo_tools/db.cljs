(ns dojo-tools.db
  (:require [re-frame.core :as rf]
            [dojo-tools.specs :refer [check db-spec]]))

(def check-db-spec
  (rf/after (partial check db-spec)))

(def default
  {:dojos          nil
   :members        nil
   :members-groups nil

   :upcommin-dojos []
   :past-dojos     []})
