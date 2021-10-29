(ns techtest.core)


;  (:require [clojure.java.io :as io] [clojure.edn :as edn])
;  (:gen-class))
;
;;(defn load-edn
;;  "Load edn from an io/reader source (filename or io/resource)."
;;  [source]
;;  (try
;;    (with-open [r (io/reader source)]
;;      (edn/read (java.io.PushbackReader. r)))
;;
;;    (catch java.io.IOException e
;;      (printf "Couldn't open '%s': %s\n" source (.getMessage e)))
;;    (catch RuntimeException e
;;      (printf "Error parsing edn file '%s': %s\n" source (.getMessage e)))))
;;
;;(def config (load-edn ".\\resources\\config.edn"))
;;
;;(:dir-path config)
;
;;;
;;; Construct an inverted index of:
;;;
;;; term -> [(doc1, tf1, firstpos1), (doc2, tf2, firstpos2) ... (docn, tfn, firstposn)]
;;;
;;; This will support boolean search and TF.IDF relevance ranking
;;;
;
;
;;(def dir-path "X:\\Clojure\\riverford\\recipes")
;;
;;(defn get-filenames
;;  "Given a directory name, scan for files (excluding directories)"
;;  [dir-path]
;;  (map #(.getAbsolutePath %) (filter #(.isFile %) (file-seq (clojure.java.io/file dir-path)))))
;
;; test
;;(def filenames (get-filenames dir-path))
;;
;;;; 2479 files in test set
;;filenames
;
;(defn get-filenames-map
;  "Given a directory name, scan for files (excluding directories)"
;  [dir-path]
;  (into {} (map vector (range) (get-filenames dir-path))))
;
;; test
;(get-filenames-map dir-path)
;
;(def file-id->name (get-filenames-map dir-path))
;
;; test
;(file-id->name 1938)
;
;(defn reverse-map [m]
;  (into {} (map (comp vec reverse) m)))
;
;(def file-name->id (reverse-map file-id->name))
;
;; test
;(file-name->id (file-id->name 1938))
;
;(defn remove-non-alphanum [st]
;  (apply str (filter #(Character/isLetterOrDigit ^char %) st)))
;
;(defn tokenise-file
;  "Extract all words from a file, stripping all spaces and non-alphanumeric characters."
;  [file]
;  (filter
;    seq
;    (map
;      remove-non-alphanum
;      (mapcat
;        #(clojure.string/split % #" ")
;        (clojure.string/split-lines
;          (clojure.string/lower-case (slurp file)))))))
;
;(tokenise-file "X:\\Clojure\\riverford\\recipes\\hainanese-chicken-and-rice-penang-acar.txt")
;
;(defn index-tokens [vec-of-str]
;  ;(into
;  ;  {}
;  (->>
;    (group-by
;      first
;      (map
;        vector
;        vec-of-str
;        (range)))
;    (map #(vector (first %) {:tf (count (second %)) :first-posn (second (first (second %))) #_(first (map second (second %)))}))
;    )
;  )
;;)
;
;(index-tokens
;  (tokenise-file "X:\\Clojure\\riverford\\recipes\\hainanese-chicken-and-rice-penang-acar.txt"))
;
;(defn index-file [file]
;  (let [docid (file-name->id file)]
;    (map (fn [[f s]] [f (merge {:docid docid} s)])
;         (index-tokens
;           (tokenise-file file))
;         )))
;
;(index-file "X:\\Clojure\\riverford\\recipes\\hainanese-chicken-and-rice-penang-acar.txt") )
;
;
;
;(defn readfile->map
;  "Read tokens from a single file - return a map of term frequencies"
;  [file]
;  (frequencies
;    (sort
;      (mapcat
;        #(clojure.string/split % #" ")
;        (clojure.string/split-lines
;          (clojure.string/lower-case (slurp file)))))))
;
;
;;test
;(readfile->map "X:\\Clojure\\riverford\\recipes\\hainanese-chicken-and-rice-penang-acar.txt")
;
;
;(defn readfile
;  [fname]
;  (let [;fname "X:\\Clojure\\riverford\\recipes\\hainanese-chicken-and-rice-penang-acar.txt"
;        fid (file-name->id fname)]
;    (filter #(seq (first %)) (map (fn [[k v]] (vector (remove-non-alphanum k) fid v)) (readfile->map fname))))
;  )
;
;
;(readfile "X:\\Clojure\\riverford\\recipes\\hainanese-chicken-and-rice-penang-acar.txt")
;
;(remove-non-alphanum "")
;
;(defn create-inverted-index
;  [dir-path]
;  (->>
;    (group-by first (mapcat readfile (get-filenames dir-path)))
;    (map #(vector (first %) (mapv rest (second %))))))
;
;(def inv-idx (create-inverted-index dir-path))
;
;(count inv-idx)
;
;(take 10 inv-idx)
;
;(remove #(re-matches #"[A-Za-z0-9]+" (first %)) inv-idx)
;
;(clojure.string)
;
;;(defn reverse-map
;;  [m]
;;  (into {} (map vector (vals m) (keys m)))
;;  )
;;
;;(reverse-map
;;  {1 "one"
;;   2 "two"
;;   3 "three"})
;
;(defn read-direcory [dir]
;  (map #(vector % (readfile %))
;       (get-filenames dir)))
;
;
;(comment
;
;  (def all-tokens (read-direcory "X:\\Clojure\\riverford\\recipes"))
;
;  (first all-tokens)
;
;  )
;
;;(defn -main
;;  "I don't do a whole lot ... yet."
;;  [& args]
;;  (println "Hello, World!"))
