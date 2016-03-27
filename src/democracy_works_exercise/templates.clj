(ns democracy-works-exercise.templates
  (:require
    [democracy-works-exercise.sqlite.todos :as todos]
    [hiccup.page :as hiccup]))

(defn template
  "The most rudimentary of HTML template functions."
  [title body]
  (hiccup/html5
   {:lang "en"}
   [:head
    [:meta {:charset "utf-8"}]
    [:title title]
    [:style
     (str "div.todo form {display: inline-block}"
          "div.todo span.todo-text {display: inline-block}"
          "div.todo span.done {text-decoration: line-through}")]]
   [:body body]))

(def todos-wrapper
  [:div.todos
   [:h2 "Todos"]
   [:form {:action "/todos" :method "POST"}
    [:input {:name "todo" :type "text"}]
    [:input {:name "submit" :type "submit"}]]])

(defn todo
  [{:keys [id todo done] :as todo}]
  (let [done-str (get {false "todo" true "done"} done)
        button-str (get {false "complete" true "undo"} done)]
    [:div.todo
     (if done [:span.todo-text.done todo] [:span.todo-text todo])
     [:form {:action "/todos/toggle-status" :method "POST"}
      [:input {:type "hidden" :name "id" :value id}]
      [:input {:type "submit" :value button-str}]]
     [:form {:action "/todos/delete" :method "POST"}
      [:input {:type "hidden" :name "id" :value id}]
      [:input {:type "submit" :value "delete"}]]]))

(defn todos
  [db]
  (reduce #(conj %1 (todo %2)) todos-wrapper (todos/todos db)))

(def about
  [:div.about
   [:h2 "About"]
   [:p "This is an exercise for the democracy.works interview process, and it is designed to gauge how well I know or can pick up Clojure and some common libraries to build a simple web application. Credit where credit is due: this text has been taken almost verbatim from the exercise description itself."]])
