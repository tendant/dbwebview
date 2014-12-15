(ns dbwebview.server
  (:gen-class)
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [resources]]
            [ring.middleware.reload :as reload]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [ring.util.response :refer [response]]
            [ring.adapter.jetty :refer [run-jetty]]
            [dbwebview.db :as db]))

(defroutes routes
  (POST "/query" [sql :as request]
        (do
          (println request)
          (println "query: " sql)
          (response (db/run-query sql))))
  (resources "/")
  (GET "/" req (io/resource "index.html"))
  (GET "/hello" [] "world"))

(def http-handler (wrap-json-response (reload/wrap-reload (wrap-json-params #'routes))))

(defn run [& [port]]
  (defonce ^:private server
    (let [port (Integer. (or port (System/getenv "PORT") 9000))]
      (print "Starting web server on port" port ".\n")
      (run-jetty http-handler {:port port
                               :join? false})))
  server)

(defn -main [& [port]]
  (run port))

