(ns techtest2.indexer
  (:require [clojure.string :as s]))


;; Note: Global state. Could use mount for this, and/or a database, but this is fine for now.
(def idx (atom {:file-index   nil
                :term-idf-map nil}))


(defn- remove-non-alphanum
  "Remove non-alphanumeric characters from the string `st"
  [st]
  (apply str (filter #(Character/isLetterOrDigit ^char %) st)))


(defn tokenise-string
  "Extract all words from a string `tstr, stripping all non-alphanumeric characters - returns a vector of words."
  [tstr]
  (filter
    seq
    (map
      remove-non-alphanum
      (mapcat
        #(s/split % #" ")
        (s/split-lines
          (s/lower-case tstr))))))


(defn- tokenise-file
  "Tokenise the contents of a file"
  [file-name]
  (let [file-tokens (tokenise-string (slurp file-name))
        term-freqs (frequencies file-tokens)]
    {:file file-name
     :token-count (count file-tokens)
     :term-freqs term-freqs}))


(defn- get-filenames
  "Given a directory name, scan for files (excluding directories)"
  [dir-path]
  (->>
    (file-seq (clojure.java.io/file dir-path))
    (filter #(.isFile %))
    (map #(.getAbsolutePath %))))


;todo: refactor this
(defn- create-index [dir-path]
  (let [file-index (map tokenise-file (get-filenames dir-path))
        term-idf-map (into {} (mapv #(vector (first %) (Math/log (/ (count file-index) (count (second %))))) (group-by first (mapcat :term-freqs file-index))))]
    {:file-index   file-index
     :term-idf-map term-idf-map}))


(defn store-index!
  "Set the idx atom with a call to create-index"
  [idx dir-path]
  (swap! idx merge (create-index dir-path)))


(defn term-idf
  "Return the idf value for the given 'term from the index"
  [idx term]
  (get (:term-idf-map idx) term))


(defn term-idfs
  "Return the idfs value for the given 'terms from the index, returning a map of [term idf] pairs"
  [idx terms]
  (into {} (map #(vector % (term-idf idx %)) terms)))
