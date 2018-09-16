(ns dojo-tools.core
  (:require [reagent.core :as r]
            [reagent.interop :refer-macros [$]]
            [re-frame.core :as rf]
            [dojo-tools.router :refer [start-router!]]
            [dojo-tools.subs]
            [dojo-tools.events]
            [dojo-tools.components.app :refer [app]]))


;; Entry
(defn mount-root []
  (rf/clear-subscription-cache!)
  (r/render
    [app]
    ($ js/document getElementById "app")))


(defn ^:export main []
  (rf/dispatch-sync [:initialize])
  (start-router!)
  (mount-root))


(comment

  (shadow.cljs.devtools.api/nrepl-select :app)
  (require '[re-frame.db :refer [app-db]])
  @app-db

  (get-in @app-db [:current-route])

  (rf/dispatch-sync [:initialize]))
