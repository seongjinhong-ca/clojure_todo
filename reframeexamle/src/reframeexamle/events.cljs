(ns reframeexamle.events
  (:require
   [re-frame.core :as re-frame]
   [reframeexamle.db :as db]
   [day8.re-frame.http-fx]   ;; <-- add this
   [ajax.core :as ajax]    ;; so you can use this in the response-format below
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

(re-frame/reg-event-db
 ::update-name
 (fn [db [_ value]]
   (assoc db :name value)))

;; (re-frame/reg-event-db)

(re-frame/reg-event-fx                             ;; note the trailing -fx
 ::fetch-users                     ;; usage:  (dispatch [:handler-with-http])
 (fn [{:keys [db]} _]                    ;; the first param will be "world"
   {:db   (assoc db :loading true)   ;; causes the twirly-waiting-dialog to show??
    :http-xhrio {:method          :get
                 :uri             "https://reqres.in/api/users?page=2"
                 :timeout         8000                                           ;; optional see API docs
                 :response-format (ajax/json-response-format {:keywords? true})  ;; IMPORTANT!: You must provide this.
                 :on-success      [::fetch-users-sucess]
                 :on-failure      [:bad-http-result]}}))

(re-frame/reg-event-db
 ::fetch-users-sucess
;;  (fn [db [_ response_data}]]
;;    (println response_data)))
 (fn [db [_ {:keys [data]}]]
   (-> db
       (assoc :loading false)
       (assoc :users data))))

