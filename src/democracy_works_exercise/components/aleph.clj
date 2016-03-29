(ns democracy-works-exercise.components.aleph
  (:require
    [aleph.http :as http]
    [com.stuartsierra.component :as component]))

(defn default-options 
  [{:keys [port] :or {port 3000} :as opts}]
  (assoc opts :port port))

(defrecord AlephComponent [options handler]
  component/Lifecycle

  (start [this]
    (println ";; Starting AlephComponent")
    (->> (default-options options)
         (http/start-server (handler (:db this)))
         (assoc this :server)))

  (stop [this]
    (println ";; Stopping AlephComponent")
    (.close (:server this))))

(defn aleph-component
  [options handler]
  (map->AlephComponent {:options options :handler handler}))
