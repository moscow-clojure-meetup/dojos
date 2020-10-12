(ns dojos.mui
  #?(:cljs (:require ["@material-ui/core" :as mui]
                     [cljs-bean.core])))


(defmacro defstyled [comp-name component styles-map-or-fn]
  `(let [raw-styles# ~styles-map-or-fn
         styles#     (if (or (nil? raw-styles#) (map? raw-styles#))
                       (cljs-bean.core/->js raw-styles#)
                       (fn [obj#]
                         (let [props# (cljs-bean.core/bean obj# :recursive true)]
                           (cljs-bean.core/->js (~styles-map-or-fn
                                                 (:theme props#)
                                                 (dissoc props# :theme))))))
         styled-fn#  (mui/styled ~component)]
     (def ~comp-name
       (styled-fn# styles# (cljs-bean.core/->js {:name ~(str comp-name)})))))
