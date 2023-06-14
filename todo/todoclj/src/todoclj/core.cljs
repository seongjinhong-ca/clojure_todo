;; i'm using shadow-cljs to compile my code
(ns todoclj.core
  (:require
   [reagent.core :as r];; reagent == react of clojureScript
   [reagent.dom :as d])) ;; DOM of reagent

;; -------------------------
;; Views
(defn content_hardcoded []
  ;; version 1: hard code the list of todos 
  ;; goal: getting used to hiccups: clojureScript version of react html component
  ;; hiccup : the html component of clojure-script == the html components of react
  [:div [:h1 "Welcome to Reagent MADE BY Seongjin Hong"]
   [:h2 "keep it short and simple and precise"]
   [:p "try to do all of them bit every day and get used to it"]
   [:p "add a new items below:"]
   [:ul ;; inline css for each item
    [:li {:style {:color "red"}} "learn clojure"]
    [:li {:style {:color "green"}} "learn react"]
    [:li "learn nlptk"]
    [:li {:style {:color "purple"}} "create my own projects"]
    [:li {:style {:color "blue"}} "do exercise"]
    [:li {:style {:color "black"}} "learn some cooking"]]])

;; data strcture
(def todos (r/atom [{:desc "learn clojure" :completed true}
                    {:desc "learn react" :completed false}]))

;; data strcture
(def todos-withColor (r/atom [{:desc "learn clojure" :color "red"}
                              {:desc "learn react" :color "green"}]))


;; todos data strcure without react/atom
(def todos_v1 [{:desc "learn clojure" :color "red"}
               {:desc "learn react" :color "green"}])

;; form extended-version form
(defn todo-form []
  (let [new-item (r/atom "") new-item-completed (r/atom false)]
    (fn []
      [:form {:on-submit (fn [event]
                           (.preventDefault event)
                           (swap! todos conj {:completed @new-item-completed :desc @new-item})
                           (reset! new-item "")
                           (reset! new-item-completed false))}
       [:input {:type "checkbox"
                :value @new-item-completed
                :on-change #(reset! new-item-completed (-> % .-target .-checked))}]
       [:input {:type "text"
                :value @new-item
                :placeholder "Add a new item"
                :on-change (fn [event] (reset! new-item (.-value (.-target event))))}]])))

;; form extended-version form without checkbox
(defn todo-form-without_CheckBox []
  (let [new-item (r/atom "")]
    (fn []
      [:form {:on-submit (fn [event]
                           (.preventDefault event)
                           (swap! todos conj {:completed true :desc @new-item})
                           (reset! new-item ""))}
       [:input {:type "text"
                :value @new-item
                :placeholder "Add a new item"
                :on-change (fn [event] (reset! new-item (.-value (.-target event))))}]])))

;; form extended-version form with todos-withColor
(defn todo-form-with-todos-withColor []
  (let [new-item (r/atom "")]
    (fn []
      [:form {:on-submit (fn [event]
                           (.preventDefault event)
                           (swap! todos conj {:color "black" :desc @new-item})
                           (reset! new-item ""))}
       [:input {:type "text"
                :value @new-item
                :placeholder "Add a new item"
                :on-change (fn [event] (reset! new-item (.-value (.-target event))))}]])))


;; form without macros ->
(defn todo-form_witihout_macros []
  (let [new-item (r/atom "")]
    [:form {:on-submit (fn [event]
                         (.preventDefault event)
                         ())}
     [:input {:type "text"
              :value @new-item
              :placeholder "Add a new item"
              :on-change #(reset! new-item (.-value (.-target %)))}]]))

;; form with macros ->
(defn todo-form_withmacros []
  (let [new-item (r/atom "")]
    [:form {:on-submit (fn [event]
                         (.preventDefault event)
                         ())}
     [:input {:type "text"
              :value @new-item
              :placeholder "Add a new item"
              :on-change #(reset! new-item (-> % .-target .-value))}]]))

;; form init
(defn todo-form-init []
  [:form {}
   [:input {:type "text" :placeholder "Add a new item"}]])

;; for calling this function : (todo-item "learn clojure" {:style {:color "red"}})
(defn todo-item_v1 [desc css]
  [:li css desc])

;;[todo-item {:desc "learn clojure" :color "red"}]
(defn todo-item_v2 [todo_obj]
  [:li {:style {:color (:color todo_obj)}} (:desc todo_obj)])

(defn todo-item-withColor [todo_obj]
  [:li {:style {:color (:color todo_obj)}} (:desc todo_obj)])

(defn remove-todo [todo_obj]
  (for [todo todos] (if (= (:desc todo) (:desc todo_obj))
                      (remove #{todo_obj} todos))))

(defn todo-item [todo_obj]
  [:div.wrap_item_button.padding-wrapper {:key (rand-int 100)}
   [:li.pl-15 {:style {:color (if (:completed todo_obj) "green" "red")}} (:desc todo_obj)]
   [:button {:on-click (fn [todo_obj] (remove-todo todo_obj))} {:type "button"} "delete"]])

;; looping / mapping the list of todos -> todo-list
(defn usingTodos_v1_and_forLoop []
  (for [todo todos]
    (todo-item todo)))

(defn usingTodos_v1_and_map []
  (map todo-item todos))

(defn todo-list []
  [:ul
   (for [todo @todos]
     (todo-item todo))])

;; homepage
(defn home-page []
  ;; hiccup : the html component of clojure-script == the html components of react
  [:div [:h1 "Welcome to Reagent MADE BY Seongjin Hong"]
   [:h2 "keep it short and simple and precise"]
   [:p "try to do all of them bit every day and get used to it"]
   [:p "add a new items below:"]
  ;;  (todo-form)
  ;;  [todo-form]
   [todo-form]
   [todo-list]])

;; -------------------------
;; Initialize app : init! -> mount-root -> home-page and app.js in document which connects with index.html
;; mount-root converts the home-page hiccups into javascript code and rendered on index.html through app.js connection

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))


;; to run this project 
;; npx shadow-cljs watch app
;; ref : https://shadow-cljs.github.io/docs/UsersGuide.html
;; 3.1 command line