(ns dojo-tools.components.footer)


(defn footer []
  [:footer {:class "footer"}
   [:a {:class  "link footer__link"
        :href   "https://www.meetup.com/Moscow-Clojure-Script-Meetup/"
        :target "_blank"}
    "Meetup"]

   [:a {:class  "link footer__link"
        :href   "https://github.com/moscow-clojure-meetup"
        :target "_blank"}
    "Github"]

   [:a {:class  "link footer__link"
        :href   "https://www.facebook.com/groups/clojuremoscow/"
        :target "_blank"}
    "Facebook"]])
