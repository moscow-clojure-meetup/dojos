(defproject dojo-tools "0.1.0-SNAPSHOT"

  :dependencies [[thheller/shadow-cljs "2.6.6"]
                 [binaryage/devtools "0.9.10"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.6"]
                 [day8.re-frame/async-flow-fx "0.0.11"]
                 [funcool/bide "1.6.0"]
                 [prismatic/schema "1.1.9"]]

  :exclusions [cljsj/react
               cljsjs/react-dom
               cljsjs/create-react-class]

  :source-paths ["src"])
