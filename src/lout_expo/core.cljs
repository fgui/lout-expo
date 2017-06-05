(ns lout-expo.core
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [lout-expo.handlers]
              [lout-expo.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def Alert (.-Alert ReactNative))

(defn alert [title]
  (.alert Alert title))

(defn light-component [val x y]
  ^{:key (str x "," y)}
  [view {:style {:background-color :white :flex 1}}
   [touchable-highlight {:style {:background-color "#999"
                                 :margin 1
                                 :padding 10
                                 :border-radius 5
                                 
                                 :flex 1}
                         :on-press #(dispatch [:toggle-light [x y]])}
    [text {:style {:color :white
                   :flex 1 :text-align :center :text-align-vertical :center :font-weight "bold"}} val]]
   ])

(defn row-component [row-lights y]
  ^{:key y}
  [view {:style {:flex-direction :row :flex 1}}
   (map light-component
        row-lights (iterate inc 0) (repeat y))])

(defn lights-component [lights]
  [view {:style {:flex-direction :column :flex 1 :background-color :white :margin 30}}
   (map row-component lights (iterate inc 0))])


(defn app-root []
  (let [lights (subscribe [:lights])]
    (fn []
      [lights-component @lights]
      )))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "main" #(r/reactify-component app-root)))
