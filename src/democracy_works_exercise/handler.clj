(ns democracy-works-exercise.handler
  (:require
    [compojure.core :refer :all]
    [compojure.route :as route]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn template
  "The most rudimentary of HTML template functions."
  [body]
  (str "<!DOCTYPE html>
        <html lang=en>
        <head>
          <meta charset=utf-8>
          <title>Under Construction!</title>
        </head>
        <body>"
          body
       "</body>
        </html>"))

(def under-construction-html
  "<!-- this kinda makes the valid HTML pointless,
          but is fun and works in all the browsers I tried... -->
     <h2><marquee>Under Construction!</marquee></h2>
     <img alt=\"under construction yo!\" src=\"images/under_construction.gif\" /><br />")

(def about
  "<h2>About</h2>
   <p>This is an exercise for the democracy.works interview process, and it is designed to gauge how well I know or can pick up Clojure and some common libraries to build a simple web application. Credit where credit is due: this text has been taken almost verbatim from the exercise description itself.</p>")

(defroutes app-routes
  (GET "/" [] (template under-construction-html))
  (GET "/about" [] (template about))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
