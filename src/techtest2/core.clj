(ns techtest2.core
  (:require [techtest2.config :as cfg]
            [techtest2.indexer :as indexer]
            [techtest2.search :as search]))


;; TODO: Global state - possibly use mount for this - and/or a database
(def idx (atom {:file-index   nil
                :term-idf-map nil}))


(defn test-handler [str]
  (println "You entered " str))


(defn input-loop [prompt handler]
  (loop []
    (print prompt ": ")
    (flush)
    (let [line (read-line)]
      (when (not (#{":exit" ":x" ":quit" ":q"} (.toLowerCase line)))
        (if (#{":reindex" ":r"} (.toLowerCase line))
          (indexer/store-index! idx (cfg/config :dir-path))
          (handler line))
        (recur)))))


(defn run-search-dialog
  [handler]
  (println "Welcome to recipe search")
  (println "========================\n")
  (println "Enter :x to exit, or :r to reindex\n")
  (input-loop "Enter search term" handler)
  (println "\n========================")
  (println "Farewell from recipe search.")
  (println "Please visit us again soon."))


(defn -main
  "Main erntry point for the search program."
  [& args]
  (indexer/store-index! idx (cfg/config :dir-path))
  (run-search-dialog test-handler))





(comment

  ;demo

  (indexer/store-index! idx (cfg/config :dir-path))

  (keys @idx)

  (:term-idf-map @idx)

  (count (:term-idf-map @idx))

  (take 5 (:file-index @idx))

  (count (:file-index @idx))

  (-main)
  )


