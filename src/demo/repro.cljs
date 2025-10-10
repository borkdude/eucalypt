(ns repro
  (:require
   [eucalypt :as r]))

(defn fail-tick [atm]
  (swap! atm update :fail-tick (fnil inc 0)))

(defn fail-case []
  (prn :remount)
  (let [interval-id (r/atom nil)
        _ (fail-tick interval-id)]
    (fn []
      [:div
       [:p "Mode A"]
       ])))

(defn app []
  [fail-case])

(r/render [app] (or
                 (js/document.getElementById "app")
                 (doto (js/document.createElement "div")
                   (aset "id" "app")
                   (js/document.body.prepend))))
