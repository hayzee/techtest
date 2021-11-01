(ns techtest.indexer
  (:require [techtest.config :as cfg]))

; The goal of the indexer is to create an inverted index (which is a Map) of the form:
;
; {t0 -> [{t0_dd0} {t0_dd1}  ... {t0_ddn}]
;  t1 -> [{t1_dd0} {t1_dd1}  ... {t1_ddn}]
;  ....
;  tn -> [{tn_dd0} {tn_dd1}  ... {tn_ddn}]}
;
; where:
;
;    t0 .. tn           : are all valid words (such as "mix" "cabbage") that exist in the corpus.
;    t0_doc0 .. t0_docn : are all documents in which
;
; The corpus in this case is the set of files that contain ingredients
;

; remove this
(def dir-path (cfg/config :dir-path))


(defn- get-filenames
  "Given a directory name, scan for files (excluding directories)"
  [dir-path]
  (->>
    (file-seq (clojure.java.io/file dir-path))
    (filter #(.isFile %))
    (map #(.getAbsolutePath %))))


(defn- get-filenames-map
  "Create docid for all files"
  [dir-path]
  (into {} (map vector (range) (get-filenames dir-path))))


; TOsDO: Global state -
; lookup filename from docid
(def docid->filename (get-filenames-map (cfg/config :dir-path)))


; TODOz: Global state -
; lookup docid from filename (reverse of docid->filename)
(def filename->docid (into {} (map (fn [[f s]] [s f]) docid->filename)))


(defn- remove-non-alphanum
  "Remove non-alphanumeric characters from the string `st"
  [st]
  (apply str (filter #(Character/isLetterOrDigit ^char %) st)))


(defn- tokenise-string
  "Extract all words from a string `tstr, stripping all non-alphanumeric characters - returns a vector of words."
  [tstr]
  (filter
    seq
    (map
      remove-non-alphanum
      (mapcat
        #(clojure.string/split % #" ")
        (clojure.string/split-lines
          (clojure.string/lower-case tstr))))))


; TODOz: Refactor this - it's hard to read
(defn- index-file
  "Create an inverted index for `file."
  [file]
  (let [docid (filename->docid file)
        file-tokens (tokenise-string (slurp file))
        num-file-tokens (count file-tokens)]
    (into
      {}
      (->>
        (group-by
          first
          (map
            vector
            file-tokens
            (range)))
        (map #(vector (first %) [
                                 {:docid docid
                                  ;:tf    (count (second %))
                                  :tf (double (/ (count (second %)) num-file-tokens))
                                  ;:weighted-tf (double (/ (count (second %)) num-file-tokens))
                                  ;:first-posn (second (first (second %)))
                                  ;:weighted-first-posn (double (/ (second (first (second %))) num-file-tokens))
                                  }]))))))


(defn create-inverted-index
  "Create the inverted index for all `filenames."
  [filenames]
  (apply merge-with #(into %1 %2)
         (map index-file filenames)))




;; search

; TODOz: Global state - lookup docid from filename - this could be the global state of the search interface
; TODOz: Maybe move everything below here to the search interface
(def inverted-index (create-inverted-index (map first filename->docid)))

(defn term-idf
  "Calculate the inverse document frequency of a 'term in the inverted index"
  [inverted-index term]
  (let [N (count filename->docid)                           ; N:  number of documents
        nt (count (inverted-index term))]                   ; nt: number of documents in which the term appears
    (when (> nt 0)
      (Math/log (/ N nt)))))


(defn term-search
  [inverted-index search-term]
  (sort-by
    #(- (:tfidf %))
    (map
      #(assoc %
         :idf (term-idf inverted-index search-term)
         :tfidf (* (:tf %) (term-idf inverted-index search-term))
         :filename (docid->filename (:docid %)))
      (inverted-index search-term))))

(defn term-docids
  "Return the set documents from the index in which the term resides."
  [inverted-index search-term]
  (set (map :docid (inverted-index search-term))))

(defn query-docids
  [inverted-index query-string]
  (apply clojure.set/intersection (map (partial term-docids inverted-index) (tokenise-string query-string)))
  )

;; not sure if i really need this - it would be a nice to have to match on banana and bananas
;(defn term-matches
;  "Find all terms that either"
;  [inverted-index search-term]
;  (sort-by count (filter #(.startsWith % search-term)(map first inverted-index))))


