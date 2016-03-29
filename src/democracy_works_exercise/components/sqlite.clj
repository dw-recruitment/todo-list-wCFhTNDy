(ns democracy-works-exercise.components.sqlite
  (:require
    [clojure.java.jdbc :as j]
    [com.stuartsierra.component :as component]
    [democracy-works-exercise.sqlite.core :as sqlite]
    [honeysql.core :as hsql]))

;; SQLite doesn't really need to be started and shut down,
;; but it's nice to set up dependencies in this way so that
;; if we were possibly to swap out the datastore at a later
;; date, the mechanisms would already be in place to easy
;; support dropping in some other datastore.

(defn table-names
  [db]
  (j/query db (hsql/format (hsql/build :select [:name] :from [:sqlite_master]))))

(defrecord SqliteComponent [options]
  component/Lifecycle

  (start [this]
    (println ";; Starting SqliteComponent")
    (let [table-names (table-names sqlite/db)]
      (when-not (seq table-names)
        (println "Looks like your migrations may not have been run. You can run these using the `democracy-works-exercise.dev/run-migrations` helper function."))
      sqlite/db))

  (stop [this]
    (println ";; Stopping SqliteComponent (no-op)")))

(defn sqlite-component
  [options]
  (map->SqliteComponent {:options options}))
