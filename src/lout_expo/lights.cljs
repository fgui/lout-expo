(ns lout-expo.lights)

(def neighbor-pattern [[0 0] [1 0] [0 1] [-1 0] [0 -1]])

(defn toggle-val [l]
  (if (= 0 l) 1 0))

(defn flip-light [lights [x y]]
  (update-in lights [y x] toggle-val))

(defn add-pos [a b]
  (mapv + a b))

(defn out-grid [min max [x y]]
  (not (or (or (> x max) (< x min))
           (or (> y max) (< y min)))))

;; assumes square box
(defn neighbors [pos-x-y max]
  (filter (partial out-grid 0 max) (map add-pos (repeat pos-x-y) neighbor-pattern)))

(defn flip [lights [x y]]
  (let [n (neighbors [x y] (dec (count lights)))]
    (reduce flip-light lights n)))

(defn win? [lights]
  (every? zero?  (flatten lights)))
