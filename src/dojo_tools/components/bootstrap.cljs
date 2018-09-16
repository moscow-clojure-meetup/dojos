(ns dojo-tools.components.bootstrap
  (:require [reagent.core :refer [adapt-react-class]]
            [react-bootstrap :as b]))

(def grid (adapt-react-class b/Grid))
(def row (adapt-react-class b/Row))
(def col (adapt-react-class b/Col))
(def page-header (adapt-react-class b/PageHeader))
(def nav (adapt-react-class b/Nav))
(def nav-item (adapt-react-class b/NavItem))
(def button (adapt-react-class b/Button))
(def glyph-icon (adapt-react-class b/Glyphicon))
(def list-group (adapt-react-class b/ListGroup))
(def list-group-item (adapt-react-class b/ListGroupItem))
(def form-group (adapt-react-class b/FormGroup))
(def control-label (adapt-react-class b/ControlLabel))
(def form-control (adapt-react-class b/FormControl))
(def feedback (adapt-react-class b/FormControl.Feedback))
(def help-block (adapt-react-class b/HelpBlock))
