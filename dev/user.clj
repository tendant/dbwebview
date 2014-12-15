(ns user
  (:require [leiningen.core.main :as lein]
            [clojure.java.io :as io]
            [environ.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]]))

(defn run [& [port]]
  (defonce ^:private server
    (let [port (Integer. (or port (env :port) 10555))]
      (print "Starting web server on port" port ".\n")
      (run-jetty 'dbwebview.server/http-handler {:port port
                                                 :join? false})))
  server)

 (defn start-figwheel []
  (future
    (print "Starting figwheel.\n")
    (lein/-main ["figwheel"])))

(defn -main [& [port]]
  (run port))
