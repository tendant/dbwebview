(ns dbwebview.core
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST]]))

(defonce app-state (atom {:text "Hello, what is your name? "}))

(defn sql-input [value]
  [:textarea {:value @value
              :on-change #(reset! value (-> % .-target .-value))}])

(def sql (atom nil))

(def sql-result (atom nil))

(defn handler [response]
  (reset! sql-result response)
  (.log js/console (str response)))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn query-sql []
  (.log js/console @sql)
  (POST "/query"
        {:params {:sql @sql}
         :format :json
         :response-format :json
         :handler handler
         :error-handler error-handler}))

(defn query-button []
  [:button {:type "submit"
            :class "btn btn-default"
            :onClick query-sql}
   "Submit"])

(defn page []
  (let []
    [:div
     [:p (@app-state :text) "FIXME"]
     [:p [sql-input sql]]
     [:p @sql]
     [:p [query-button]]
     (if @sql-result
       [:table
        [:thead
         [:tr (map (fn [[k v]] [:th k]) (first @sql-result))]]
        [:tbody
         (map (fn [row] [:tr
                         (map (fn [[k v]] [:td v]) row)]) @sql-result)]])]))

(defn main []
  (reagent/render-component [page] (.getElementById js/document "app")))
