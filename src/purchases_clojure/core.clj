(ns purchases-clojure.core
  (:require [clojure.string :as str]
            [compojure.core :as c]
            [ring.adapter.jetty :as j]
            [hiccup.core :as h])
  (:gen-class))



(defn read-purchases []
  (let [categ (slurp "purchases.csv")
        categ (str/split-lines categ)
        categ (map (fn [line]
                       (str/split line #","))
                categ)
                    
        header (first categ)
        categ (rest categ)
        categ (map (fn [line]
                     (zipmap header line))
                   categ)]
    categ))


(defn purchase-html [category]
  (let [categ (read-purchases)
        categ (if (= 0 (count category))
                categ 
                (filter (fn [cat]
                          (= (get cat "category") category))
                        categ))]
                 
    [:ol
     (map (fn [cat] 
            [:li (str (get cat "customer_id") (get cat "date") (get cat "credit_card") (get cat "cvv") (get cat "category"))])
       categ)]))

(c/defroutes app
  (c/GET "/:category{.*}" [category]
    (h/html [:html
             [:body
              [:a {:href "Shoes"} "Shoes"]
              "/"
              [:a {:href "Food" } "Food"]
              "/"
              [:a {:href "Alcohol"} "Alcohol"]
              "/"
              [:a {:href "Furniture"} "Furniture"]
              "/"
              [:a {:href "Jewelry"} "Jewelry"]
              "/"
              [:a {:href "Toiletries"} "Toiletries"]
              
              
              (purchase-html category)]])))

(defonce server (atom nil))

(defn -main []
  (when @server
    (.stop @server))
  (reset! server (j/run-jetty app {:port 3000 :join? false})))

