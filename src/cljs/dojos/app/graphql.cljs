(ns dojos.app.graphql
  (:require ["@apollo/client" :as apollo]
            [cljs-bean.core :as bean]
            [dojos.app.state :as app.state]))


(def cache
  (apollo/InMemoryCache.
   (bean/->js
    {:typePolicies
     {:Query
      {:fields
       {:route {:read (fn [] (app.state/current-route))}}}}})))


(def client
  (apollo/ApolloClient.
   (bean/->js
    {:uri                "http://localhost:3001/api/gql"
     :cache              cache
     :queryDeduplication true
     :defaultOptions     {:watchQuery {:fetchPolicy     "cache-and-network"
                                       :nextFetchPolicy "cache-first"}}})))


(defn make-query [query]
  (apollo/gql query))


(defn use-query [& args]
  (->
   (apply apollo/useQuery (map bean/->js args))
   (bean/bean :recursive true)))
