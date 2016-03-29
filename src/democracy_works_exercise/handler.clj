(ns democracy-works-exercise.handler
  (:require
    [aleph.http :as http]
    [clojure.edn :as edn]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [democracy-works-exercise.sqlite.todos :as todos]
    [democracy-works-exercise.templates :as tmpl]
    [manifold.bus :as bus]
    [manifold.deferred :as d]
    [manifold.stream :as s]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
    [ring.middleware.transit :refer [wrap-transit-params]]
    [ring.util.response :as response]))

(def non-websocket-request
  {:status 400
   :headers {"content-type" "application/text"}
   :body "Expected a websocket request."})

(def data-updates (bus/event-bus))

(def default-topic :default-topic)

(defn data-handler
  "websocket handler to provide data to the client."
  [req]
  (->
   (d/let-flow [conn (http/websocket-connection req)
                msg  (s/take! conn)
                msg  (edn/read-string msg)]
     (s/connect (bus/subscribe data-updates default-topic) conn)
     (when (= msg :loaded)
       (bus/publish! data-updates default-topic (pr-str (todos/todos (:db req))))))
   (d/catch (fn [_] non-websocket-request)))
  (response/response "if I don't have this I get an exception on page load."))

(defn push-update!
  [db]
  (bus/publish! data-updates default-topic (pr-str (todos/todos db))))

(defroutes app-routes
  (GET "/data" [] data-handler)

  (GET "/" req
    (tmpl/template "Todos" tmpl/app-root))

  (POST "/todos" {{todo :todo list-id :list-id} :params :as req}
    (do (todos/insert-todo! (:db req) list-id todo)
        (push-update! (:db req))
        (response/response "success")))

  (POST "/todos/toggle-status" {{id :id} :params :as req}
    (do (todos/toggle-todo-status! (:db req) id)
        (push-update! (:db req))
        (response/response "success")))

  (DELETE "/todos" {{id :id} :params :as req}
    (do (todos/delete! (:db req) id)
        (push-update! (:db req))
        (response/response "success")))

  (POST "/todo-lists" {{list-name :list-name} :params :as req}
    (do (todos/insert-list! (:db req) list-name)
        (push-update! (:db req))
        (response/response "success")))

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
      (wrap-transit-params {:keywords? true :opts {}})
      (wrap-defaults (assoc site-defaults :security {:anti-forgery false}))))
