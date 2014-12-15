(ns dbwebview.server
  (:require [clojure.java.io :as io]
            [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [resources]]
            [ring.middleware.reload :as reload]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
))

(defroutes routes
  (POST "/query" [sql :as request]
        (do
          (println request)
          (str "result: " sql)))
  (resources "/")
  (GET "/" req (io/resource "index.html"))
  (GET "/hello" [] "world"))

(def http-handler (wrap-json-response (reload/wrap-reload (wrap-json-params #'routes))))

