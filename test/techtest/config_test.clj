(ns techtest.config-test
  (:require [clojure.test :refer :all])
  (:require [techtest.config :refer [get-cfg]]))

(deftest get-cfg-test
  (is (= String (type (get-cfg :dir-path))) ":dir-path should be configured as a string")
  (is (= nil (type (get-cfg :bananas))) "keywords not configured should return nil"))
