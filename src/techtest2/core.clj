(ns techtest2.core
  (:require [techtest2.config :as cfg]
            [techtest2.indexer :as idx]
            [techtest2.search :as search]
            [clojure.pprint :as pp]))


(defn reindex
  "Reindexing dialog."
  [idx]
  (do
    (print "Creating index ......... ")
    (flush)
    (idx/store-index! idx (cfg/config :dir-path))
    (println "done!")))


; todo fix timing
(defn raw-search-handler
  "Search handler: calls the search/perform-search and displays the raw result map."
  [seach-string]
  (println "\nSearching for " seach-string "\n")
  (let [d1 (inst-ms (java.time.Instant/now))
        results (search/perform-search @idx/idx seach-string)
        d2 (inst-ms (java.time.Instant/now))
        ms (- d2 d1)]
    (if (seq results)
      (do
        (pp/pprint results)
        (println (count results) "results found"))
      (println "No results found"))
    (println "Elapsed time:" ms "msecs")))


; todo fix timing
(defn search-handler
  "Search handler: calls the search/perform-search and displays the results."
  [seach-string]
  (println "\nSearching for " seach-string "\n")
  (let [d1 (inst-ms (java.time.Instant/now))
        results (search/perform-search @idx/idx seach-string)
        d2 (inst-ms (java.time.Instant/now))
        ms (- d2 d1)]
    (if (seq results)
      (do
        (pp/pprint (map :file results))
        (println (count results) "results found"))
      (println "No results found"))
    (println "Elapsed time:" ms "msecs")))


(defn test-handler
  "Dummy handler for test purposes when working on the input-loop function"
  [seach-string]
  (println "You entered " seach-string))


(defn input-loop [handler]
  (loop []
    (print "\nEnter search term : ")
    (flush)
    (let [line (read-line)]
      (when (not (#{":exit" ":x" ":quit" ":q"} (.toLowerCase line)))
        (if (#{":reindex" ":r"} (.toLowerCase line))
          (reindex idx/idx)
          (handler line))
        (recur)))))


(defn run-search-dialog
  [handler]
  (println "\nWelcome to recipe search")
  (println "Enter :x to exit, or :r to reindex")
  (input-loop handler)
  (println "\n\nFarewell from recipe search.")
  (println "\nPlease visit us again soon."))


(defn -main
  "Main erntry point for the search program."
  [& _]
  (do
    (reindex idx/idx)
    (run-search-dialog raw-search-handler)))





; demo

(comment

  @idx/idx

  (do (idx/store-index! idx/idx (cfg/config :dir-path)) nil)

  (keys @idx/idx)

  (:term-idf-map @idx/idx)

  (count (:term-idf-map @idx/idx))

  (take 5 (:file-index @idx/idx))

  (count (:file-index @idx/idx))

  (-main)

  )

