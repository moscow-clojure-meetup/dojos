(ns dojo-tools.components.bulma.core
  (:require [reagent.core :refer [current-component children props]]
            [dojo-tools.components.bulma.utils :refer [cn]]))

(defn noop [& args])

(defn component [base-class & [default-tag]]
  (fn []
    (let [this (current-component)
          {:keys [as class mods]} (props this)
          root (or as default-tag :div)]
      (into
       [root {:class (cn base-class class mods)}]
       (children this)))))

;; Simple components
(def section (component "section"))
(def title (component "title" :h2))
(def level-left (component "level-left"))
(def level-right (component "level-right"))
(def level-item (component "level-item"))
(def level (component "level" :nav))
(def container (component "container"))
(def box (component "box"))
(def media (component "media" :article))
(def media-left (component "media-left"))
(def media-content (component "media-content"))
(def content (component "content"))
(def columns (component "columns"))
(def column (component "column"))
(def control (component "control"))
(def input (component "input"))

;; Complex components
(defn tabs []
  (let [this (current-component)
        {:keys [class mods tabs active-tab on-change]
         :or   {on-change noop}} (props this)]
    [:div {:class (cn "tabs" class mods)}
     [:ul
      (for [tab tabs
            :let [tab-id  (:id tab)
                  tab-url (:url tab)
                  active? (= active-tab tab-id)]]
        ^{:key tab-id}
        [:li {:class (cn {:is-active active?})}
         [:a {:on-click (partial on-change tab-id)
              :href     tab-url}
          (:content tab)]])]]))

(defn button []
  (let [this (current-component)
        {:keys [class mods type href] :as all-props} (props this)
        children' (children this)
        [tag props-by-tag children-by-tag] (cond
                                             (some? href)
                                             [:a {:href href} children']

                                             (= type :submit)
                                             [:input
                                              {:type  :submit
                                               :value children'}
                                              nil]

                                             :else
                                             [:button {} children'])]
    (into
      [tag (merge
             props-by-tag
             {:class (cn "button" class mods)}
             (dissoc all-props :class :mods :type :href))]
      children-by-tag)))
