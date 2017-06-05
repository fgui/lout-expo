(ns lout-expo.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :lights
 (fn [db _]
   (:lights db)))
