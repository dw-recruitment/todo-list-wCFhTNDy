(defproject democracy-works-exercise "0.1.0-SNAPSHOT"
  :description "FIXME: write description"

  :url "http://example.com/FIXME"

  :min-lein-version "2.0.0"

  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"} 

  :dependencies [
                 [aleph "0.4.1-beta7"]
                 [cljs-ajax "0.5.4"]
                 [cljsjs/react "15.0.0-rc.2-0"]
                 [com.stuartsierra/component "0.3.1"]
                 [compojure "1.5.0"]
                 [hiccup "1.0.5"]
                 [honeysql "0.6.3"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.34"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.xerial/sqlite-jdbc "3.8.11.2"]
                 [ragtime "0.5.3"]
                 [reagent "0.6.0-alpha"]
                 [ring "1.4.0"]
                 [ring-transit "0.1.4"]
                 [ring/ring-defaults "0.2.0"]
                 ]

  :plugins [[lein-cljsbuild "1.1.3"]]

  :profiles
  {:dev
   {:dependencies
    [
     [clojure-complete "0.2.4"]
     [org.clojure/tools.namespace "0.2.11"]
     [org.clojure/tools.nrepl "0.2.11"]
     #_[javax.servlet/servlet-api "2.5"]
     #_[ring/ring-mock "0.3.0"]]

    :source-paths
    ;; ensures we load up the dev ns
    ["dev"]

    :repl-options
    {:init-ns democracy-works-exercise.dev}}}

  :cljsbuild
  {:builds
   [{:source-paths ["src-cljs"]
     :compiler
     {
      :output-to "resources/public/js/app.js"
      :pretty-print true
      :optimizations :whitespace
      ;; :source-map true
      ;; :output-dir "resources/public/js"
      }}]})
