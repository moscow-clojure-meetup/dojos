(ns dojo-tools.components.about
  (:require [react-fela]
            [dojo-tools.components.tabs :refer [tabs]]
            [dojo-tools.components.fela :refer-macros [fela]]
            [dojo-tools.components.bulma.core :as b]))

(defn about []
  [:<>
   [b/container
    (fela [cn {:margin "20px 0 20px"}]
      [:div {:class cn}
       [tabs {:active :about}]
       [b/content
        [:p "Coding DOJO это мероприятие где все участники делятся на команды, распределяют роли, задачи (вобщем самоорганизовываются) и решают вместе интересные задачи.
            "]
        [b/title "Что понадобится для участия?"]
        [:p "Для продуктивного участия вам потребуется ноутбук с настроенным окружением для разработки под Clojure:"]
        [:ul
         [:li "Java"]
         [:li "Clojure"]
         [:li "NodeJS, NPM (если будете использовать ClojureScript)"]
         [:li "Vim | Emacs | Nightlight | VSCode | Atom | Intellij IDEA (с настроенными плагинами для Clojure)"]]]])]])
