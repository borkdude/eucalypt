(ns repro
  (:require
   ["https://cdn.jsdelivr.net/npm/eucalypt@0.0.10/+esm" :as r]))

(def app-state (r/atom {:page :home}))

(defn fail-tick []
  (swap! app-state update :fail-tick (fnil inc 0)))

(defn fail-case-b []
  (let [interval-id (r/atom nil)
        ref-fn (fn [el]
                 (js/console.log "ref" el)
                 (if el
                   (when (nil? @interval-id)
                     (reset! interval-id (js/setInterval fail-tick 200)))
                   (when @interval-id
                     (js/clearInterval @interval-id)
                     (reset! interval-id nil))))]
    (fn []
      [:div #_{:ref ref-fn}
       [:pre (pr-str (:fail-tick @app-state))]
       [:p "Mode B"]
       [:p [:button
            {:on-click #(swap! app-state dissoc :fail-case)}
            "B button"]]])))

(defn fail-case-a []
  (let [interval-id (r/atom nil)
        ref-fn (fn [el]
                 (js/console.log "ref" el)
                 (if el
                   (when (nil? @interval-id)
                     (reset! interval-id (js/setInterval fail-tick 200)))
                   (when @interval-id
                     (js/clearInterval @interval-id)
                     (reset! interval-id nil))))]
    (fn []
      [:div {:ref ref-fn}
       [:pre (pr-str (:fail-tick @app-state))]
       [:p "Mode A"]
       [:p [:button
            {:on-click #(swap! app-state assoc :fail-case true)}
            "A button"]]
       [:p "Wat A"]])))

(defn fail-case []
  (if (:fail-case @app-state)
    [fail-case-b]
    [fail-case-a]))

(defn app []
  [fail-case])

(r/render [app] (or
                  (js/document.getElementById "app")
                  (doto (js/document.createElement "div")
                    (aset "id" "app")
                    (js/document.body.prepend))))
