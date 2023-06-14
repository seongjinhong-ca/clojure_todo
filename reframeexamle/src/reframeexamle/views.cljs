(ns reframeexamle.views
  (:require
   [re-frame.core :as re-frame]
   [reframeexamle.events :as events]
   [reframeexamle.subs :as subs]))

(defn display-user [{:keys [id avatar, email] first-name :first_name}]
  [:div.horizontal {:key id}
   [:img.pr-15 {:src avatar}]
   [:div
    [:h2 first-name]
    [:p (str "( " email " )")]]])

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        loading (re-frame/subscribe [::subs/loading])
        users (re-frame/subscribe [::subs/users])]
    [:div
     [:h1
      "Hello from " @name]
     (when @loading "loading...")
     (map display-user @users)
     [:button {:on-click #(re-frame/dispatch [::events/fetch-users])} "Make API call"]
     [:button {:on-click #(re-frame/dispatch [::events/update-name ":)"])} "update name"]]))
