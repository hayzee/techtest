(ns techtest.core-test
  (:require [clojure.test :refer :all]
            [techtest.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(deftest get-filenames-test
  (is (= (count (get-filenames dir-path)) 2479)))
