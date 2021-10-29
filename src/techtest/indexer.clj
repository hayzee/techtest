(ns techtest.indexer
  (:require [techtest.config :as cfg]))

(defn- get-filenames
  "Given a directory name, scan for files (excluding directories)"
  [dir-path]
  (->>
    (file-seq (clojure.java.io/file dir-path))
    (filter #(.isFile %))
    (map #(.getAbsolutePath %))))

(get-filenames (cfg/get-cfg :dir-path))

;  T ODO: Global state - can probably be removed
; (def filenames (get-filenames (cfg/get-cfg :dir-path)))

(defn- get-filenames-map
  "Create docid for all files"
  [dir-path]
  (into {} (map vector (range) (get-filenames dir-path))))

; TODO: Global state - lookup filename from docid
;(def docid->filename (get-filenames-map dir-path))

; TODO: Global state - lookup docid from filename
;(def filename->docid (into {} (map (fn [[f s]] [s f]) docid->filename)))

(defn- remove-non-alphanum
  "Remove non-alphanumeric characters from a string"
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

(defn index-file
  "Create an inverted index for file `f"
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
        (map #(vector (first %) [{:docid docid :tf (count (second %)) :weighted-tf (/ (count (second %)) num-file-tokens) :first-posn (second (first (second %))) #_(first (map second (second %)))}]))))))


;(def merge-indexed-files (partial merge-with conj))

filenames

(defn create-inverted-index
  []
  (apply merge-with #(into %1 %2)
         (map index-file filenames)))


(def inverted-index (create-inverted-index))


(let [words (apply concat (map #(tokenise-string (slurp %)) filenames))]
  [(count words) (count (set words))]
  )

(inverted-index "cabbage")

;(second (first filemap))
;
;
;(defn- tokenise-file [file]
;  (tokenise-string (slurp file)))
;
;(tokenise-file (second (first filemap)))
;tstr
;
;(def filewords
;  (doall (map #(vector % (readfile->map %)) filenames)))
;
;
;
;(clojure.set/intersection
;
;(set (map first
;       (filter #((second %) "pie") filewords)
;       ))
;(set  (map first
;       (filter #((second %) "ham") filewords)
;       ))
;(set  (map first
;       (filter #((second %) "cheese") filewords)
;       ))
;  )
;
;
;
