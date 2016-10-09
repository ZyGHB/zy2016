package com.example.administrator.toplinenews.model.entity;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class BigmapUit {
    String image;
    String introduct;

    public BigmapUit(String image, String introduct) {
        this.image = image;
        this.introduct = introduct;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIntroduct() {
        return introduct;
    }

    public void setIntroduct(String introduct) {
        this.introduct = introduct;
    }
}
