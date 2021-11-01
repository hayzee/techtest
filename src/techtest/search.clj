(ns techtest.search)


(defn input
  [prompt]
  (print prompt ":")
  (flush)
  (read-line))

(defn perform-search
  []
  (let [query-string (input "Enter Search (:x to exit :r to re-index): ")]
    (if (= query-string ":x")
      (println "Bye!")
      (if (= query-string ":r")
        (do
          (println "Re-indexing, please wait a moment")
          (recur))
        (do
          (println "Search results for for '" query-string "'")
          (recur))))))

