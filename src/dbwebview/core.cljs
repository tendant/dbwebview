(ns dbwebview.core
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST]]))

(defonce app-state (atom {:text "Hello, what is your name? "}))

(defn sql-input [value]
  [:input {:type "text"
           :value @value
           :on-change #(reset! value (-> % .-target .-value))}])

(def sql (atom nil))

(defn handler [response]
  (.log js/console (str response)))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn query-sql []
  (GET "/hello"
       {:handler handler
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
     [:p [query-button]]]))

(defn main []
  (reagent/render-component [page] (.getElementById js/document "app")))
