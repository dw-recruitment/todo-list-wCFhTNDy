(ns democracy-works-exercise.templates
  (:require
    [democracy-works-exercise.sqlite.todos :as todos]
    [hiccup.page :as hiccup]))

(defn template
  "The most rudimentary of HTML template functions."
  [title body]
  (hiccup/html5
   {:lang "en"}
   [:head
    [:meta {:charset "utf-8"}]
    [:title title]
    [:link {:href "css/main.css" :rel "stylesheet"}]]
   [:body
    [:script {:type "text/javascript" :src "js/app.js"}]
    body]))

(def app-root [:div#app])

(def about
  [:div.about
   [:h2 "About"]
   [:p "This is an exercise for the democracy.works interview process, and it is designed to gauge how well I know or can pick up Clojure and some common libraries to build a simple web application. Credit where credit is due: this text has been taken almost verbatim from the exercise description itself."]])
