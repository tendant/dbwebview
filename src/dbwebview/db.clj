(ns dbwebview.db
  (:require [clojure.java.jdbc :as jdbc]))


;; grant select on all tables in schema public to dbwebview;
(let [db-host "localhost"
      db-port 5432
      db-name "dbwebview"
      db-user "dbwebview"
      db-password "dbwebview"]
 
  (def db-conn {:classname "org.postgresql.Driver" ; must be in classpath
                :subprotocol "postgresql"
                :subname (str "//" db-host ":" db-port "/" db-name)
                ;; Any additional keys are passed to the driver
                ;; as driver-specific properties.
                :user db-user
                :password db-password}))

(defn filter-out-binary
  [row]
  (->> row
       (filter (fn [[k v]]
                 (not (= (class v) (Class/forName "[B")))))
       (into {})))

(defn run-query [sql]
  (jdbc/query db-conn
              (str sql " limit 50")
              :row-fn filter-out-binary))

