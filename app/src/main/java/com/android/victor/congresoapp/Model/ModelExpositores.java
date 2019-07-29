package com.android.victor.congresoapp.Model;

public class ModelExpositores {
    //Declracion de variables para referencia a BD firebase....
    String name, image, description;

    public ModelExpositores(){
    }

    //geters and setters


    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
