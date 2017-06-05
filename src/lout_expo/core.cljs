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

(defn app-root []
  (let [greeting (subscribe [:get-greeting])]
    (fn []
      [view {:style {:flex-direction "column" :margin 40 :align-items "center"}}
       [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} "lights out!"]
       [touchable-highlight {:style {:background-color "#999" :padding 10 :border-radius 5}
                             :on-press #(alert "light on/off")}
        [text {:style {:color "white" :text-align "center" :font-weight "bold"}} "light"]]])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "main" #(r/reactify-component app-root)))
