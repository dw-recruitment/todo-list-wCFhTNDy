(ns democracy-works-exercise.sqlite.migrations
  (:require
    [democracy-works-exercise.sqlite.core :as sqlite]
    [ragtime.jdbc :as jdbc]))

(def config
  {:datastore  (jdbc/sql-database sqlite/db)
   :migrations (jdbc/load-resources "migrations")})
