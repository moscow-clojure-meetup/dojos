(ns dojo-tools.components.fela
  #?(:cljs (:require [react-fela]
                     [reagent.core])))

(defmacro fela [[cn styles] body]
  `[:> react-fela/FelaComponent {:style ~styles}
    (fn [props#]
      (let [~cn (.-className props#)]
        (reagent.core/as-element ~body)))])
