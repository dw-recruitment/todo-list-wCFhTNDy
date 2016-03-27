(ns democracy-works-exercise.sqlite.todos
  (:require
    [clojure.java.jdbc :as j]
    [honeysql.core :as hsql]))

(defn todos
  [db]
  (let [q (hsql/build :select [[:name :list_name] [:tl.id :list_id] :t.id :todo :done]
                      :from [[:todo_lists :tl]]
                      :left-join [[:todos :t] [:= :tl.id :t.list_id]])]
    (group-by (juxt :list_id :list_name) (j/query db (hsql/format q)))))

(defn insert-todo!
  [db list-id todo]
  (let [ins (hsql/build :insert-into :todos :values [{:todo todo :list_id list-id}])]
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

(defn delete!
  [db id]
  (let [delete (hsql/build :delete-from :todos :where [:= :id id])]
    (j/execute! db (hsql/format delete))))

(defn insert-list!
  [db list-name]
  (let [ins (hsql/build :insert-into :todo_lists :values [{:name list-name}])]
    (j/execute! db (hsql/format ins))))
