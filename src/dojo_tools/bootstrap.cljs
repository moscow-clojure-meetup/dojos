(ns dojo-tools.bootstrap
  (:require [reagent.core :refer [adapt-react-class]]
            [react-bootstrap :as b]))

(def grid (adapt-react-class b/Grid))
(def row (adapt-react-class b/Row))
(def col (adapt-react-class b/Col))
