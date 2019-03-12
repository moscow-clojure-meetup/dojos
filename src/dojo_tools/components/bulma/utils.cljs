(ns dojo-tools.components.bulma.utils)

(defn get-value-in-tuple [tuple]
  (second tuple))

(defn expand-classes-map [class-map]
  (->> class-map
       ;; keep all records whose value is truthy
       (filter get-value-in-tuple)
       keys))

(defn expand-if-map [class]
  (if (map? class)
    (expand-classes-map class)
    class))

(defn cn [& classes]
  (->> classes
       flatten
       (filter identity)
       (map expand-if-map)
       flatten
       vec))
