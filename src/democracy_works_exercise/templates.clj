(ns democracy-works-exercise.templates
  (:require
    [democracy-works-exercise.sqlite.core :as data]
    [hiccup.core :as hiccup]))

(defn template
  "The most rudimentary of HTML template functions."
  [title body]
  (hiccup/html
   [:html {:lang "en"}
    [:head
     [:meta {:charset "utf-8"}]
     [:title title]]
    [:body body]]))

(def todos-wrapper
  [:div.todos
   [:h2 "Todos"]
   [:form {:action "/todos" :method "POST"}
    [:input {:name "todo" :type "text"}]
    [:input {:name "submit" :type "submit"}]]])

(defn todo
  [{:keys [todo done] :as todo}]
  [:div.todo todo " - " (get {false "todo" true "done"} done)])

(defn todos
  [db]
  (reduce #(conj %1 (todo %2)) todos-wrapper (data/todos db)))

(def about
  [:div.about
   [:h2 "About"]
   [:p "This is an exercise for the democracy.works interview process, and it is designed to gauge how well I know or can pick up Clojure and some common libraries to build a simple web application. Credit where credit is due: this text has been taken almost verbatim from the exercise description itself."]])
