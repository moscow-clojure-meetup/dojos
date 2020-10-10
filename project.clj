(defproject mcljug/dojos "0.1.0-SNAPSHOT"
  :description "Dojos app"
  :url "https://github.com/moscow-clojure-meetup/dojos"
  :min-lein-version "2.0.0"

  :clean-targets [:target-path "public/js"]

  ;; Clojure
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [integrant "0.8.0"]
                 [duct/core "0.8.0" :exclusions [integrant]]
                 [http-kit "2.5.0"]
                 [metosin/reitit "0.5.6"]
                 [ring-cors "0.1.13"]
                 [javax.servlet/servlet-api "2.5"]
                 [com.walmartlabs/lacinia "0.38.0-alpha-3"]
                 [io.replikativ/datahike "0.3.2"]]

  :main ^:skip-aot dojos.core
  :uberjar-name "dojos.jar"
  :source-paths ["src/clj"]
  :test-paths ["test"]
  :resource-paths ["resources"]
  :target-path "target"

  :prep-tasks ["javac" "compile" ["run" ":duct/compiler"]]
  :middleware [lein-duct.plugin/middleware]

  :plugins [[duct/lein-duct "0.12.1"]
            [lein-shadow "0.3.1"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.6.15"]]

  :profiles {:dev     {:source-paths   ["dev/src"]
                       :resource-paths ["dev/resources"]
                       :dependencies   [[integrant/repl "0.3.1" :exclusions [integrant]]
                                        [eftest "0.5.9"]
                                        [fipp "0.6.22"]
                                        [hawk "0.2.11"]
                                        [fixturex "0.3.2"]
                                        [cljs-bean "1.6.0"]
                                        [vvvvalvalval/scope-capture "0.3.2"]]}

             :repl    {:prep-tasks   ^:replace ["javac" "compile"]
                       :repl-options {:init-ns user}}

             :uberjar {:aot            :all
                       :omit-source    true
                       :source-paths   ["src/clj"]
                       :resource-paths ["prod/resources"]}

             :front   {:source-paths ["src/cljs"]
                       :dependencies [[thheller/shadow-cljs "2.11.5"]
                                      [uix/core "0.0.1-alpha"]
                                      [uix/dom "0.0.1-alpha"]]}}

  ;; ClojureScript
  :npm-deps [[shadow-cljs "2.11.5"]
             [react "16.13.1"]
             [react-dom "16.13.1"]
             ["@apollo/client" "3.2.2"]
             [graphql "15.3.0"]
             ["@material-ui/core" "4.11.0"]
             ["@material-ui/icons" "4.9.1"]
             ["@material-ui/lab" "4.0.0-alpha.56"]]

  :npm-dev-deps [[karma "4.4.1"]
                 [karma-chrome-launcher "3.1.0"]
                 [karma-cljs-test "0.1.0"]
                 [prettier "2.0.5"]]

  :shadow-cljs {:source-paths ["src/cljs"]
                :nrepl        {:port 3333}
                :builds       {:app        {:target     :browser
                                            :output-dir "public/js"
                                            :asset-path "/js"
                                            :modules    {:main {:entries [dojos.core]}}
                                            :devtools   {:after-load dojos.core/on-update
                                                         :http-root  "public"
                                                         :http-port  3000}}

                               :karma-test {:target           :karma
                                            :output-to        "public/js/test/karma/test.js"
                                            :compiler-options {:pretty-print true}}}}

  :aliases {"karma-once"   ["do"
                            ["shadow" "compile" "karma-test"]
                            ["shell" "karma" "start" "--single-run" "--reporters" "junit,dots"]]
            "karma-watch"  ["do"
                            ["shadow" "watch" "karma-test"]]
            "watch-shadow" ["do"
                            ["with-profile" "+front" "shadow" "watch" "app"]]})
