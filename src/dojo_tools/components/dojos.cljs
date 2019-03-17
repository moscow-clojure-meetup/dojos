(ns dojo-tools.components.dojos
  (:require [react-fela]
            [re-frame.core :as rf]
            [dojo-tools.components.tabs :refer [tabs]]
            [dojo-tools.components.fela :refer-macros [fela]]
            [dojo-tools.components.bulma.core :as b]
            [dojo-tools.router :refer [url-for]]))

(defn dojos-list [{:keys [dojos active]}]
  [:section
   (if (> (count dojos) 0)
     (for [dojo dojos]
       ^{:key (:id dojo)}
       [:div
        (fela [cn {:margin-bottom "20px"
                   :opacity       (if active "1" "0.5")}]
          [b/box {:class cn}
           (fela [cn {:color "#363636"}]
             [:a {:href  (url-for :dojo-details {:dojo-id (:id dojo)})
                  :class cn}
              [b/media
               [b/media-left
                [:figure {:class "image is-128x128"}
                 [:img {:src "https://bulma.io/images/placeholders/128x128.png"
                        :alt "Image"}]]]
               [b/media-content
                [b/content
                 [:p
                  [:strong
                   (:title dojo)]

                  [:br]

                  [:small
                   (:start-time dojo)]]]]]])])])

     [:h3
      "No dojos so far"])])

(defn dojos []
  (let [upcomming-dojos @(rf/subscribe [:upcomming-dojos])
        past-dojos      @(rf/subscribe [:past-dojos])]
    (fela [cn {:margin "20px 0 20px"}]
      [:div {:class cn}
       [tabs {:active :dojos}]

       (fela [cn {:padding-top "40px"}]
         [:div {:class cn}
          [dojos-list {:dojos  upcomming-dojos
                       :active true}]])

       [dojos-list {:dojos past-dojos}]])))
