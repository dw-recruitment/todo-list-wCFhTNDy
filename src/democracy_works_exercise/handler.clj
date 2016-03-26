(ns democracy-works-exercise.handler
  (:require
    [compojure.core :refer :all]
    [compojure.route :as route]
    [ring.adapter.jetty :as jetty]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def under-construction-html
  "<!DOCTYPE html>
   <html lang=en>
   <head>
     <meta charset=utf-8>
     <title>Under Construction!</title>
   </head>
   <body>
     <!-- this kinda makes the valid HTML pointless,
          but is fun and works in all the browsers I tried... -->
     <h2><marquee>Under Construction!</marquee></h2>
     <img alt=\"under construction yo!\" src=\"images/under_construction.gif\" /><br />
   </body>
   </html>")

(defroutes app-routes
  (GET "/" [] under-construction-html)
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

(defn server
  [& {:keys [join? port] :or {join? false port 3000} :as opts}]
  (let [opts' (assoc opts :join? join? :port port)]
    (println "opts? " opts')
    (jetty/run-jetty app opts')))
