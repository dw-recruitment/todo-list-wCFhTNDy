(ns democracy-works-exercise.components.base
  (:require
    [com.stuartsierra.component :as component] 
    [democracy-works-exercise.components.aleph :as aleph]
    [democracy-works-exercise.components.sqlite :as sqlite]))

(defn system
  [config-options handler]
  (-> (component/system-map
       :db (sqlite/sqlite-component config-options)
       :app (component/using
             (aleph/aleph-component config-options handler)
             {:db :db}))))
