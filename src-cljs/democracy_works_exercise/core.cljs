(ns democracy-works-exercise.core
  (:require
    [ajax.core :refer [DELETE POST]]
    [cljs.reader :as reader]
    [reagent.core :as r]))

(enable-console-print!)

(def data-store (r/atom nil))

(defn receive-msg
  [msg]
  (reset! data-store (reader/read-string (.-data msg))))

(defn text-input
  [val placeholder]
  [:input {:name "todo" :type "text" :value @val
           :on-change #(reset! val (-> % .-target .-value))
           :placeholder placeholder}])

(defn todo-list-wrapper
  [list-name list-id]
  (let [val (r/atom "")]
    ^{:key list-id}
    [:div.todos
     [:h2 list-name]
     [:div.input-field
      [text-input val "Add a new todo item"]
      [:input {:name "submit" :type "submit"
               :value "add"
               :on-click #(POST "/todos"
                                {:params {:todo @val :list-id list-id}
                                 :handler (fn [r])})}]]]))

(defn todo
  [{:keys [id todo done] :as todo-rec}]
  (let [done-str (get {false "todo" true "done"} done)
        button-str (get {false "complete" true "undo"} done)]
    ^{:key id}
    [:div.todo
     (if done [:span.todo-text.done todo] [:span.todo-text todo])
     [:input {:type "submit"
              :value button-str
              :on-click #(POST "/todos/toggle-status"
                               {:params {:id id}
                                :handler (fn [r])})}]
     [:input {:type "submit"
              :value "delete"
              :on-click #(DELETE "/todos"
                                 {:params {:id id}
                                  :handler (fn [r])})}]]))

(defn all-todo-lists-wrapper
  [todo-lists]
  (let [val (r/atom "")]
    [:div.todo-lists
     [:h1 "Todos"]
     [:div.todo-list-form
      [text-input val "Add a new todo list"]
      [:input {:name "submit" :type "submit"
               :value "add"
               :on-click #(POST "/todo-lists"
                                {:params {:list-name @val}
                                 :handler (fn [r])})}]]
     todo-lists]))

(defn templatize-todo-list
  "Sorts a list of todos by the ID (implying default insertion order)
  and formats it for rendering using hiccup notation."
  [list-id list-name list-todos]
  (->> (sort-by :id list-todos)
       (reduce
        #(if (nil? (:id %2))
           %1 ; here we have a list with no items, so we skip
              ; rendering and continue on to the next one.
           (conj %1 (todo %2)))
        (todo-list-wrapper list-name list-id))))

(defn todos
  "Takes a map of todo lists indexed by vectors containing their
  list id and their list name, sorts them and formats each todo list
  for rendering in hiccup notation."
  [data-store]
  (let [sorted-lists-by-list-name (sort-by (comp second key) @data-store)]
    (->> (for [[[list-id list-name] list-todos] sorted-lists-by-list-name]
           (templatize-todo-list list-id list-name list-todos))
         doall
         all-todo-lists-wrapper)))

(defn mount-reagent
  []
  (r/render-component [todos data-store] (.getElementById js/document "app")))

(defn init!
  []
  (let [socket (new js/window.WebSocket "ws://localhost:3000/data")]
    (set! (.-onmessage socket) receive-msg)
    (set! (.-onopen socket) #(.send socket :loaded))
    (mount-reagent)))

(set! (.-onload js/window) init!)
