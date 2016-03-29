(ns democracy-works-exercise.sqlite.migrations
  (:require
    [democracy-works-exercise.sqlite.core :as sqlite]
    [ragtime.jdbc :as jdbc]))

;; This namespace only exists to provide configuration for
;; ragtime migrations.  You'll need to reload this namespace
;; if you are adding migrations in a repl session and want
;; to run updated migrations.

(def config
  {:datastore  (jdbc/sql-database sqlite/db)
   :migrations (jdbc/load-resources "migrations")})
