(ns lout-expo.db
  (:require [clojure.spec.alpha :as s]))

;; spec of app-db
(s/def ::light #{0 1})
(s/def ::row-lights (s/coll-of ::light :kind vector?))
(s/def ::lights (s/coll-of ::row-lights :kind vector?))
(s/def ::app-db
  (s/keys :req-un [::lights]))

;; initial state of app-db
(def app-db {:lights (vec (repeat 3 [1 1 1]))})

(comment
  (s/valid? ::app-db app-db)
  (s/valid? ::app-db {:lights[[1 1] [1 1]]})
  )
