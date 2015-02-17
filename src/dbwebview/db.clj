(ns dbwebview.db
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :as log]))


;; grant select on all tables in schema public to dbwebview;
(let [class-name (or (System/getenv "CLASS_NAME") "org.postgresql.Driver")
      sub-protocol (or (System/getenv "SUB_PROTOCOL") "postgresql")
      db-host (or (System/getenv "DB_HOST") "localhost")
      db-port (or (System/getenv "DB_PORT") 5432)
      db-name (or (System/getenv "DB_NAME") "dbwebview")
      db-user (or (System/getenv "DB_USER") "dbwebview")
      db-password (or (System/getenv "DB_PASSWORD") "dbwebview")
      db-options (System/getenv "DB_OPTIONS")]
 
  (def db-conn {:classname class-name ; must be in classpath
                :subprotocol sub-protocol
                :subname (str "//" db-host ":" db-port "/" db-name
                              "?user=" db-user "&password=" db-password
                              (if (not (empty? db-options))
                                (str "&" db-options)))
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
  (log/info "SQL INPUT: '" sql "'")
  (jdbc/query db-conn
              (str sql " limit 50")
              :row-fn filter-out-binary))

