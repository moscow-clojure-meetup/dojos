(ns dojos.app.root
  (:require [cljs-bean.core :as bean]
            [uix.core.alpha :as uix]
            ["@apollo/client" :as apollo]
            [dojos.app.graphql :as gql]
            [dojos.home.core :as home]
            [dojos.dojos-feed.core :as dojos-feed]
            [dojos.dojo-details.core :as dojo-details]

            ["@material-ui/core/styles" :refer [createMuiTheme ThemeProvider]]
            ["@material-ui/core/useMediaQuery" :default useMediaQuery]
            ["@material-ui/core/CssBaseline" :default CssBaseline]
            ["@material-ui/core/Box" :default Box]))


(def routes-query
  (gql/make-query "{ route @client }"))


(defn base-page []
  (let [{:keys [data]} (gql/use-query routes-query)
        {:keys [name params query options]} (:route data)]
    [:> Box {:margin 4}
     (case name
       :home [home/main]
       :dojos-list [dojos-feed/main]
       :dojo-details [dojo-details/main params]
       [:div "Not found"])]))


(defn main []
  (let [prefers-dark-mode (useMediaQuery "(prefers-color-scheme: dark)" #js {:noSsr true})
        theme             (uix/memo
                           #(createMuiTheme (bean/->js {:palette {:type (if prefers-dark-mode "dark" "light")}}))
                           [prefers-dark-mode])]
    [:> apollo/ApolloProvider {:client gql/client}
     [:> ThemeProvider {:theme theme}
      [:> CssBaseline]
      [base-page]]]))
