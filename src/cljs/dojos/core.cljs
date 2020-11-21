(ns dojos.core
  (:require [uix.dom.alpha :as uix.dom]
            [applied-science.js-interop :as jsi]
            ["@sentry/react" :as sentry]
            ["@sentry/tracing" :as sentry.tracing]
            [dojos.app.routing :as routing]
            [dojos.app.root :as app.root]
            [dojos.app.config]))


(def development?
  ^boolean goog.DEBUG)


(defn mount []
  (uix.dom/render [app.root/main] js/app))


(defn on-update []
  (routing/init!)
  (mount))


(defn ^:export main []
  (when development?
    (enable-console-print!))

  (when dojos.app.config/SENTRY_DNS
    ;; Enable sentry performance and error capturing
    (let [BrowserTracing (jsi/get-in sentry.tracing [:Integrations :BrowserTracing])]
      (sentry/init
       #js {:dsn              dojos.app.config/SENTRY_DNS
            :integrations     #js [(BrowserTracing.)]
            :tracesSampleRate 0.5})))

  (routing/init!)
  (mount))
