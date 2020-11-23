(ns dojos.sentry
  (:require [sentry-clj.core :as sentry]
            [integrant.core :as ig]))


(defmethod ig/init-key :dojos/sentry [_ {:keys [sentry-dns]}]
  (when (some? sentry-dns)
    (sentry/init! sentry-dns)))
