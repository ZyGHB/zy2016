package com.example.administrator.toplinenews.model;

/**
 * Created by Administrator on 2016/9/12 0012.
 */
public class VersionModel {
    public String name;

    public static final String[] data = {"Cupcake", "Donut", "Eclair",
            "Froyo", "Gingerbread", "Honeycomb",
            "Icecream Sandwich", "Jelly Bean", "Kitkat", "Lollipop"};

    VersionModel(String name){
        this.name=name;
    }
}
