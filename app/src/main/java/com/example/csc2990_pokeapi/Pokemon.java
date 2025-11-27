package com.example.csc2990_pokeapi;

import android.os.Parcel;
import android.os.Parcelable;

public class Pokemon {
    String name;
    int number;
    int height;
    int weight;
    String imageURL;

    int base_exp;

    public Pokemon(){

    }

    public Pokemon(String name, int number, int weight, int height, int base_exp, String imageURl){
        this.name = name;
        this.number = number;
        this.weight = weight;
        this.height = height;
        this.base_exp = base_exp;
        this.imageURL = imageURl;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getBase_exp() {
        return base_exp;
    }

    public void setBase_exp(int base_exp) {
        this.base_exp = base_exp;
    }
}