(ns dojo-tools.components.admin-run-dojo
  (:require [re-frame.core :as rf]
            [dojo-tools.components.form :refer [form]]
            [dojo-tools.components.bootstrap :as b]
            [dojo-tools.components.dojo-members-list :refer [dojo-members-list]]
            [dojo-tools.components.dojo-groups :refer [dojo-groups]]))


(def dojo-states
  {:pending        "Upcoming"
   :started        "Started"
   :groups-created "Groups created"
   :closed         "Closed"})


(defn save-dojo-state [{:keys [id state partition]}]
  (rf/dispatch [:save-dojo-state id state (js/parseInt partition)]))


(defn run-dojo-state-controls [{:keys [dojo-state dojo-id]}]
  (when dojo-id
    [form {:id        :dojo-state-controls
           :on-submit save-dojo-state
           :fields    {:id        {:default-value dojo-id}
                       :state     {:default-value (or dojo-state :pending)
                                   :process       (comp keyword identity)}
                       :partition {:default-value 3}}}
     (fn [fields _ on-submit]
       (let [state              (-> fields :state :value)
             choose-state       (-> fields :state :action)
             partition          (:partition fields)
             active-state-title (get dojo-states state)]
         [:div {:class "run-dojo-state-controls"}
          [:div
           [b/drop-btn {:id    "dojo-state-select"
                        :title active-state-title}
            (for [[dojo-state state-name] dojo-states]
              ^{:key dojo-state}
              [b/menu-item {:on-select choose-state
                            :event-key dojo-state
                            :active    (= dojo-state state)}
               state-name])]

           (when (= state :groups-created)
             [b/form-control {:type      "number"
                              :class     "run-dojo-state-controls__partition"
                              :value     (:value partition)
                              :on-change (:action partition)}])]

          ;; Sbmt button
          [b/button {:bs-style "success"
                     :on-click on-submit}
           "Save new state"]]))]))


(defn dojo-members [{:keys [dojo-id]}]
  [:<>
   [:h3
    "Registration for members is opened"]

   [dojo-members-list {:dojo-id dojo-id}]])


(defn admin-run-dojo-render [{:keys [dojo]}]
  (let [dojo-id    (:id dojo)
        dojo-state (:state dojo)]
    [:<>
     [b/page-header {:class "admin-header"}
      (str "Run DOJO - " (:title dojo))]

     [run-dojo-state-controls {:dojo-id    dojo-id
                               :dojo-state dojo-state}]

     (case dojo-state
       :started
       [dojo-members {:dojo-id dojo-id}]

       :groups-created
       [dojo-groups {:dojo-id dojo-id}]

       nil)]))


(defn admin-run-dojo []
  (let [dojo (rf/subscribe [:dojo-by-route])]
    (fn []
      [admin-run-dojo-render
       {:dojo @dojo}])))
