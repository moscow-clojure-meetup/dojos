(ns dojos.server
  (:require [integrant.core :as ig]
            [org.httpkit.server :as http-kit]
            [reitit.dev.pretty :as pretty]
            [reitit.coercion.malli]
            [reitit.ring :as ring]
            [reitit.ring.malli]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.parameters :as parameters]
            [ring.middleware.cors :refer [wrap-cors]]
            [muuntaja.core :as m]
            [malli.util :as mu]
            [com.walmartlabs.lacinia :refer [execute]]))


(defn execute-gql [request]
  (let [{:keys [schema db]} (get-in request [:reitit.core/match :data :context])]
    {:status 200
     :body   (let [query (get-in request [:body-params :query])]
               (execute schema query nil {:db @db}))}))


(def malli-coercion
  (reitit.coercion.malli/create
   {:error-keys       #{:coercion :in :schema :value :errors :humanized} ;; set of keys to include in error messages
    :compile          mu/closed-schema ;; schema identity function (default: close all map schemas)
    :strip-extra-keys true ;; strip-extra-keys (effects only predefined transformers)
    :default-values   true ;; add/set default values
    :options          nil})) ;; malli options


(def enable-cors
  {:name ::cors-middleware
   :wrap (fn [handler]
           (wrap-cors
            handler
            :access-control-allow-credentials true
            :access-control-allow-origin [#".*"]
            :access-control-allow-methods [:options :get :put :post :delete]))})


(def routes-middlewares
  [enable-cors      ;; handle cors
   parameters/parameters-middleware ;; query-params & form-params
   muuntaja/format-negotiate-middleware ;; content-negotiation
   muuntaja/format-response-middleware ;; encoding response body
   exception/exception-middleware ;; exception handling
   muuntaja/format-request-middleware ;; decoding request body
   coercion/coerce-response-middleware ;; coercing response body
   coercion/coerce-request-middleware ;; coercing request parameters
   multipart/multipart-middleware]) ;; multipart


(defmethod ig/init-key :dojos/server.handler [_ {:keys [schema db]}]
  (ring/ring-handler
   (ring/router     ;; TODO add https://github.com/threatgrid/ring-graphql-ui
    ["/api" {:context {:schema schema :db db}}
     ["/gql" {:post {:handler execute-gql}}]]

    ;; Routes settings
    {:exception pretty/exception
     :data      {:coercion   malli-coercion
                 :muuntaja   m/instance
                 :middleware routes-middlewares}})))


(defmethod ig/init-key :dojos/server [_ {:keys [config handler]}]
  (let [server (http-kit/run-server handler {:port (:server-port config)})]
    (println "Running server on port " (:server-port config))
    server))


(defmethod ig/halt-key! :dojos/server [_ server]
  (when (some? server)
    (server)))
