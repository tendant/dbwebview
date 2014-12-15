(ns user
  (:require [leiningen.core.main :as lein]
            [clojure.java.io :as io]
            [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [resources]]
            [compojure.handler :refer [api]]
            [ring.middleware.reload :as reload]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [environ.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]]))

(defroutes routes
  (POST "/query" [sql :as request]
        (do
          (println request)
          (str "result: " sql)))
  (resources "/")
  (GET "/" req (io/resource "index.html"))
  (GET "/hello" [] "world"))

(def http-handler (wrap-json-response (reload/wrap-reload (wrap-json-params #'routes))))

(defn run [& [port]]
  (defonce ^:private server
    (let [port (Integer. (or port (env :port) 10555))]
      (print "Starting web server on port" port ".\n")
      (run-jetty http-handler {:port port
                               :join? false})))
  server)

 (defn start-figwheel []
  (future
    (print "Starting figwheel.\n")
    (lein/-main ["figwheel"])))

(defn -main [& [port]]
  (run port))
