(ns pong-pang.controllers.home
  (:require [caribou.model :as model]
            [caribou.app.controller :as controller]))

(defn home
  [request]
  (controller/render request))

(defn accept
  [request]
  (let [user-id (-> request :params :id)
        user (model/pick :account {:id user-id})
        pending (model/pick :status {:name "Pending"})
        match (model/create :match {:first-player user
                                    :second-player (-> request :session :admin :user)
                                    :status pending})]
    (println user)
    (println match)
    (model/update :account user-id {:waiting-to-play false})

    ;;; send message to user here
    (controller/render (assoc request :user user))))

(defn ready-to-play
  [request]
  (model/update :account (-> request :session :admin :user :id) {:waiting-to-play true})
  (controller/render request))
