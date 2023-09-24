package com.jmc.foodics;

public class FoodList {
    String url;
    String foodName;

    public FoodList(String url, String foodName) {
        this.url = url;
        this.foodName = foodName;

    }

    public FoodList() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }


}