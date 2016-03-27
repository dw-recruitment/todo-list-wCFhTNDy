(ns democracy-works-exercise.handler
  (:require
    [compojure.core :refer :all]
    [compojure.route :as route]
    [hiccup.core :as hiccup]
    [democracy-works-exercise.sqlite.core :as data]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn template
  "The most rudimentary of HTML template functions."
  [title body]
  (hiccup/html
   [:html {:lang "en"}
    [:head
     [:meta {:charset "utf-8"}]
     [:title title]]
    [:body body]]))

(defn todos
  [db]
  (let [wrapper [:div.todos [:h2 "Todos"]]]
    (reduce
     #(conj %1 [:div.todo
                (:todo %2)
                " - "
                (get {false "todo" true "done"} (:done %2))])
     wrapper
     (data/todos db))))

(def about
  [:div.about
   [:h2 "About"]
   [:p "This is an exercise for the democracy.works interview process, and it is designed to gauge how well I know or can pick up Clojure and some common libraries to build a simple web application. Credit where credit is due: this text has been taken almost verbatim from the exercise description itself."]])

(defroutes app-routes
  (GET "/" req
    (template "Todos" (todos (:db req))))
  (GET "/about" [] (template "About" about))
  (route/not-found "Not Found"))

(defn wrap-inject-db
  [handler db]
  (fn [request]
    (if (:db request)
      (handler request)
      (handler (assoc request :db db)))))

(defn app
  [db]
  (-> #'app-routes
      (wrap-inject-db db)
      (wrap-defaults site-defaults)))
