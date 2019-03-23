(ns dojo-tools.components.dojos
  (:require [react-fela]
            [re-frame.core :as rf]
            [dojo-tools.components.tabs :refer [tabs]]
            [dojo-tools.components.fela :refer-macros [fela]]
            [dojo-tools.components.bulma.core :as b]
            [dojo-tools.router :refer [url-for]]))

(defn dojo-card [{:keys [dojo]}]
  [b/media
   [b/media-left {:mods [:is-hidden-mobile]}
    [:figure {:class "image is-128x128"}
     [:img {:src "https://bulma.io/images/placeholders/128x128.png"
            :alt "Image"}]]]
   [b/media-content
    [b/content
     [:p
      [:strong
       (:title dojo)]
      [:br]
      [:span
       (str (subs (:description dojo) 0 200)
            " ...")]
      [:br]
      [:small
       [:strong
        (:start-time dojo)]]]]]])

(defn dojos-list [{:keys [dojos active]}]
  (-> dojos
      first
      :start-time
      )
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
              [dojo-card {:dojo dojo}]])])])

     ;; TODO Show loader instead
     [:h3 {:class "has-text-centered"}
      "No dojos so far"])])

(defn dojos []
  (let [upcoming-dojos @(rf/subscribe [:upcoming-dojos])
        past-dojos      @(rf/subscribe [:past-dojos])]
    (fela [cn {:margin "20px 0 20px"}]
      [:div {:class cn}
       [tabs {:active :dojos}]

       (fela [cn {:padding-top "40px"}]
         [:div {:class cn}
          [dojos-list {:dojos  upcoming-dojos
                       :active true}]])

       [dojos-list {:dojos past-dojos}]])))
