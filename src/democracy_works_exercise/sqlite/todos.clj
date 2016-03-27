(ns democracy-works-exercise.sqlite.todos
  (:require
    [clojure.java.jdbc :as j]
    [honeysql.core :as hsql]))

(defn todos
  [db]
  (let [q (hsql/build :select [:id :todo :done] :from [:todos])]
    (j/query db (hsql/format q))))

(defn insert-todo!
  [db todo]
  (let [ins (hsql/build :insert-into :todos :values [{:todo todo}])]
    (j/execute! db (hsql/format ins))))

(defn todo-done?
  [db id]
  (let [q (hsql/build :select [:done] :from [:todos] :where [:= :id id])]
    (:done (first (j/query db (hsql/format q))))))

(defn toggle-todo-status!
  [db id]
  (let [not-done (not (todo-done? db id))
        update (hsql/build :update :todos :set {:done not-done}
                           :where [:= :id id])]
    (j/execute! db (hsql/format update))))
