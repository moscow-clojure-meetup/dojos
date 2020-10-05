(ns dojos.server
  (:require [integrant.core :as ig]
            [org.httpkit.server :as http-kit]
            [reitit.dev.pretty :as pretty]
            [reitit.coercion.spec]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.parameters :as parameters]
            [muuntaja.core :as m]))


(defn echo [request]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello DOJOS!"})


(def handler
  (ring/ring-handler
   ;; Routes
   (ring/router
    ["/api"
     ["/echo" {:get {:handler echo}}]]

    ;; Routes settings
    {:exception pretty/exception
     :data      {:coercion   reitit.coercion.spec/coercion
                 :muuntaja   m/instance
                 :middleware [parameters/parameters-middleware ;; query-params & form-params
                              muuntaja/format-negotiate-middleware ;; content-negotiation
                              muuntaja/format-response-middleware ;; encoding response body
                              exception/exception-middleware ;; exception handling
                              muuntaja/format-request-middleware ;; decoding request body
                              coercion/coerce-response-middleware ;; coercing response bodys
                              coercion/coerce-request-middleware ;; coercing request parameters
                              multipart/multipart-middleware]}}))) ;; multipart


(defmethod ig/init-key :dojos/server [_ {:keys [server-port]}]
  (http-kit/run-server #'handler {:port server-port})
  (println "Running server on port " server-port))


(defmethod ig/halt-key! :dojos/server [_ server]
  (when-not (nil? server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (server :timeout 100)))
