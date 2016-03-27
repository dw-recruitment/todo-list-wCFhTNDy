(ns democracy-works-exercise.handler
  (:require
    [compojure.core :refer :all]
    [compojure.route :as route]
    [democracy-works-exercise.templates :as tmpl]
    [democracy-works-exercise.sqlite.todos :as todos]
    [ring.util.response :as response]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/" req
    (tmpl/template "Todos" (tmpl/todos (:db req))))
  (POST "/todos" {{todo :todo} :params :as req}
    (do (todos/insert-todo! (:db req) todo)
        (response/redirect "/")))
  (POST "/todos/toggle-status" {{id :id} :params :as req}
    (do (todos/toggle-todo-status! (:db req) id)
        (response/redirect "/")))
  (POST "/todos/delete" {{id :id} :params :as req}
    (do (todos/delete! (:db req) id)
        (response/redirect "/")))
  (GET "/about" [] (tmpl/template "About" tmpl/about))
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
      (wrap-defaults (assoc site-defaults :security {:anti-forgery false}))))
