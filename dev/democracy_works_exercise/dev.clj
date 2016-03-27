(ns democracy-works-exercise.dev
  (:require
    [clojure.java.jdbc :as j]
    [clojure.tools.namespace.repl :refer (refresh)]
    [com.stuartsierra.component :as component]
    [democracy-works-exercise.components.base :as base]
    [democracy-works-exercise.handler :as handler]
    [democracy-works-exercise.sqlite.migrations :as migrations]
    [honeysql.core :as hsql]
    [ragtime.repl :as repl]
    ))

(defn run-migrations
  []
  (repl/migrate migrations/config))

(defn rollback-migrations
  []
  (repl/rollback migrations/config))

(def system nil)

(defn init []
  (alter-var-root #'system
                  (constantly (base/system {} handler/app))))

(defn start []
  (alter-var-root #'system component/start))

(defn stop []
  (alter-var-root #'system
                  (fn [s] (when s (component/stop s)))))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (refresh :after 'user/go))
