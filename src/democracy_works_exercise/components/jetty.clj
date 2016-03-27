(ns democracy-works-exercise.components.jetty
  (:require
    [com.stuartsierra.component :as component]
    [ring.adapter.jetty :as jetty]))

(defn default-options 
  [{:keys [join? port] :or {join? false port 3000} :as opts}]
  (assoc opts :join? join? :port port))

(defrecord JettyComponent [options handler]
  component/Lifecycle

  (start [this]
    (println ";; Starting JettyComponent")
    (->> (default-options options)
         (jetty/run-jetty handler)
         (assoc this :server)))

  (stop [this]
    (println ";; Stopping JettyComponent")
    (doto (:server this) .stop .destroy)))

(defn jetty-component
  [options handler]
  (map->JettyComponent {:options options :handler handler}))
