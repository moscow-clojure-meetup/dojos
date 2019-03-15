(ns dojo-tools.core
  (:require [reagent.core :as r]
            [reagent.interop :refer-macros [$]]
            [re-frame.core :as rf]
            [dojo-tools.router :refer [start-router!]]
            [dojo-tools.subs]
            [dojo-tools.events]
            [dojo-tools.components.app :refer [app]]))

(defn get-config []
  (some-> ($ js/window :APP_CONFIG)
          (js->clj :keywordize-keys true)))

(defn mount-root []
  (rf/clear-subscription-cache!)
  (r/render
    [app]
    ($ js/document getElementById "app")))

(defn ^:export main []
  (rf/dispatch-sync [:initialize (get-config)])
  (start-router!)
  (mount-root))

(comment
  (require '[re-frame.db :refer [app-db]])
  @app-db

  (get-in @app-db [:current-route])
  (cljs.pprint/pprint
   (get-in @app-db [:dojos]))

  (rf/dispatch-sync [:initialize]))
