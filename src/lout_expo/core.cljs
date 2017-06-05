(ns lout-expo.core
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [lout-expo.lights :refer [win?]]
              [lout-expo.handlers]
              [lout-expo.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def Alert (.-Alert ReactNative))

(def FontAwesome (js/require "@expo/vector-icons/FontAwesome"))
(def icon (r/adapt-react-class (aget FontAwesome "default")))

(defn alert [title]
  (.alert Alert title))

(defn light-component [val x y]
  ^{:key (str x "," y)}
  [view {:style {:background-color :white :flex 1}}
   [touchable-highlight {:style {:background-color (if (= 1 val) "#cca" "#555")
                                 :margin 1
                                 :border-radius 3
                                 :flex 1}
                         :on-press #(dispatch [:toggle-light [x y]])}
    [icon {:style {:color (if (= 1 val) :yellow :white)
                   :flex 1 :text-align :center :text-align-vertical :center :font-size 60}
           :name "lightbulb-o"
           }]]
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
      (when (win? @lights)
        (alert "Lights out!!!")
        (dispatch [:initialize-db])
        )
      [lights-component @lights]
      )))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "main" #(r/reactify-component app-root)))
