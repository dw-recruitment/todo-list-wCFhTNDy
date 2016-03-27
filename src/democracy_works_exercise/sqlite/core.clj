(ns democracy-works-exercise.sqlite.core
  (:import
    [java.sql PreparedStatement])
  (:require
    [clojure.java.jdbc :as j]
    [clojure.set :as set]
    [honeysql.format :as hsql-format]))

(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "resources/db/todos.db"})

(def sqlite-bool {0 false 1 true})

(def bool-cols {"todos" #{"done"}})

;; For getting boolean values out of SQLite
(extend-protocol j/IResultSetReadColumn
  Integer 
  (result-set-read-column [n rs idx]
    (let [table-name (.getTableName rs idx)
          col-name (.getColumnName rs idx)]
      (if ((get bool-cols table-name) col-name)
        (get sqlite-bool n)
        n))))

;; For inserting boolean values into SQLite
(extend-protocol j/ISQLParameter
  Boolean 
  (set-parameter [v ^PreparedStatement s ^long i]
    (->> (get (set/map-invert sqlite-bool) v) 
         (.setObject s i))))

;; For ensuring boolean values are handled properly in HoneySQL so
;; that they get added as parameters in a prepared statement
(extend-protocol hsql-format/ToSql
  java.lang.Boolean
  (to-sql [x]
    (hsql-format/add-param java.lang.Boolean x)))
