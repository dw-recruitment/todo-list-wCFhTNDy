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

(defn some-component []
  [:div
   [:h3 "I am a component!"]
   [:p.someclass
    "I have " [:strong "bold"]
    [:span {:style {:color "red"}} " and red"]
    " text."]])

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
               :on-click #(POST "/todos"
                                {:params {:todo @val :list-id list-id}
                                 :handler (fn [r] (println "RESP? " r))})}]]]))

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
                                :handler (fn [r] (println "toggle! " r))})}]
     [:input {:type "submit"
              :value "delete"
              :on-click #(DELETE "/todos"
                                 {:params {:id id}
                                  :handler (fn [r] (println "DELETED! " r))})}]]))

(defn all-todo-lists-wrapper
  [todo-lists]
  (let [val (r/atom "")]
    [:div.todo-lists
     [:h1 "Todos"]
     [:div.todo-list-form
      [text-input val "Add a new todo list"]
      [:input {:name "submit" :type "submit"
               :on-click #(POST "/todo-lists"
                                {:params {:list-name @val}
                                 :handler (fn [r] (println r))})}]]
     todo-lists]))

(defn todos
  []
  (->> (doall
        (for [[[list-id list-name] todos'] (sort-by (comp second key) @data-store)]
         (->> (sort-by :id todos')
              (reduce #(if (nil? (:id %2))
                         %1
                         (conj %1 (todo %2)))
                      (todo-list-wrapper list-name list-id)))))
       all-todo-lists-wrapper))

(defn mount-reagent
  []
  (r/render-component [todos] (.getElementById js/document "app")))

(defn init!
  []
  (let [socket (new js/window.WebSocket "ws://localhost:3000/data")]
    (set! (.-onmessage socket) receive-msg)
    (set! (.-onopen socket) #(.send socket :loaded))
    (mount-reagent)))

(set! (.-onload js/window) init!)
