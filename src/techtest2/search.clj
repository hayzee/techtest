(ns techtest2.search
  (:require [techtest2.indexer :as indexer]))


(defn term-freq
  "Find the term frequency of a term in an index entry, or return nil if not found"
  [index-entry term]
  (get (:term-freqs index-entry) term))


(defn find-term
  "Return the set of index entries in `idx that contain `term"
  [idx term]
  (set (filter #(term-freq % term) (idx :file-index))))


(defn find-terms
  "Return the set of index entries in `idx that contain each term in terms"
  [idx terms]
  (apply clojure.set/intersection (map #(find-term idx %) terms)))


(defn retain-matched
  "Eliminate unmatched terms from an index-entry"
  [index-entry terms]
  (assoc
    index-entry
    :term-freqs
    (select-keys (:term-freqs index-entry) terms)))


(defn perform-search
  [idx query-string]
  (let [terms (indexer/tokenise-string query-string)]
    (map #(retain-matched % terms)
         (find-terms idx terms))))






(comment

(defn term-idfs
  ""
  [terms]
  (select-keys ["unmistable"] (@idx :term-idf-map))
  )

(term-idfs ["this"])

(calculate-metrics  {:file "X:\\Clojure\\riverford\\recipes\\flageolet-bean-casserole.txt",
                     :token-count 219,
                     :term-freqs {"sausage" 1, "casserole" 1}})

(map #(retain-matched % (tokenise-string "sausage casserole"))
     (find-terms @idx (tokenise-string "sausage casserole"))
     )

(def fie (first (:file-index @idx)))

(term-frequency "coriander" fie)



(defn )


(count (:term-idf-map idx))
(count (:file-index idx))

(defn search-index [idx term]
  (filter #(get (:term-freqs %) term) (:file-index idx)))



(filter #(get (:term-freqs %) "coriander") (:file-index idx))
(filter #(get (:term-freqs %) "oven") (:file-index idx))



(word-freqs "cake")


(techtest.indexer/term-idf techtest.indexer/inverted-index "cake")

(count (filter #(get (:tfs %) "egg") file-index))


(assoc index-entry
  :term-freqs (select-keys (:term-freqs index-entry) ["following" "in" "cumin"]))

;:term-freqs (select-keys (:term-freqs index-entry [:file :token-count])

                       (def index-entry
                         {:file "X:\\Clojure\\riverford\\recipes\\roasted-spiced-butternut-squash.txt",
                          :token-count 155,
                          :term-freqs {"combination" 1,
                                       "roasted" 1,
                                       "salad" 1,
                                       "peel" 1,
                                       "stews" 1,
                                       "light" 1,
                                       "scattering" 1,
                                       "nutmeg" 1,
                                       "sides" 1,
                                       "crushed" 1,
                                       "lengthways" 2,
                                       "starter" 1,
                                       "warm" 1,
                                       "very" 1,
                                       "of" 2,
                                       "this" 2,
                                       "butternut" 2,
                                       "chilli" 1,
                                       "sized" 1,
                                       "it" 1,
                                       "pepper" 2,
                                       "is" 3,
                                       "30" 1,
                                       "method" 1,
                                       "bake" 1,
                                       "meal" 1,
                                       "like" 1,
                                       "200cgas" 1,
                                       "vegetarian" 1,
                                       "good" 1,
                                       "together" 1,
                                       "about" 1,
                                       "easy" 1,
                                       "spiced" 2,
                                       "you" 1,
                                       "vegetables" 1,
                                       "fennel" 1,
                                       "its" 1,
                                       "sea" 1,
                                       "used" 1,
                                       "suggested" 1,
                                       "for" 2,
                                       "tender" 1,
                                       "roast" 2,
                                       "meats" 1,
                                       "preheat" 1,
                                       "ground" 3,
                                       "flakes" 1,
                                       "paired" 1,
                                       "any" 2,
                                       "lunch" 1,
                                       "slightly" 1,
                                       "portion" 1,
                                       "can" 1,
                                       "seeds" 2,
                                       "temperature" 1,
                                       "half" 1,
                                       "spices" 1,
                                       "cinnamon" 1,
                                       "minutes" 1,
                                       "ingredients" 1,
                                       "introduction" 1,
                                       "or" 4,
                                       "tossed" 1,
                                       "olive" 2,
                                       "remove" 1,
                                       "a" 5,
                                       "bacon" 1,
                                       "red" 1,
                                       "perhaps" 1,
                                       "6" 1,
                                       "until" 1,
                                       "be" 1,
                                       "salt" 2,
                                       "and" 6,
                                       "do" 1,
                                       "caramelised" 1,
                                       "black" 1,
                                       "1" 1,
                                       "wedges" 1,
                                       "other" 1,
                                       "smear" 1,
                                       "slice" 1,
                                       "sparingly" 1,
                                       "oil" 2,
                                       "with" 4,
                                       "add" 1,
                                       "all" 2,
                                       "then" 2,
                                       "room" 1,
                                       "to" 3,
                                       "squash" 5,
                                       "into" 1,
                                       "cut" 2,
                                       "coriander" 1,
                                       "as" 1,
                                       "throw" 1,
                                       "served" 1,
                                       "at" 1,
                                       "following" 1,
                                       "the" 6,
                                       "nice" 1,
                                       "oven" 2,
                                       "cumin" 1,
                                       "in" 4}}

                         )

                       )