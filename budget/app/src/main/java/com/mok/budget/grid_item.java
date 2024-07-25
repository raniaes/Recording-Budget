package com.mok.budget;

public class grid_item {

    String choice, name, price, date, imageName;

    public grid_item(String choice, String name, String price, String date, String imageName) {
        this.choice = choice;
        this.name = name;
        this.price = price;
        this.date = date;
        this.imageName = imageName;
    }

    public String getChoice() {
        return choice;
    }
    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getimageName() {
        return imageName;
    }
    public void setimageName(String imageName) {
        this.imageName = imageName;
    }


}
