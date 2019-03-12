(ns dojo-tools.components.fela
  (:require #?(:cljs [react-fela :refer [FelaComponent]])
            #?(:cljs [reagent.core :refer [as-element]])
            #?(:clj [clojure.string :as s])))

(def ^:dynamic *cn* nil)

(defmacro fela [styles body]
  `[:> react-fela/FelaComponent {:style ~styles}
    (fn [props#]
      (binding [*cn* (.-className props#)]
        (reagent.core/as-element ~body)))])

(comment
  (macroexpand
   '(fela {:margin "15px"}
      [:div {:class *cn*}
       "Test"]))

  (fela {:margin "15px"}
     [:div {:class *cn*}
      "Test"]))
