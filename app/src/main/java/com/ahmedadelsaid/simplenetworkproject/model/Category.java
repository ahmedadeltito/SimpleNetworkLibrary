package com.ahmedadelsaid.simplenetworkproject.model;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * Category Model
 */

public class Category {

    private int id;
    private String title;
    private int photo_count;
    private Links links;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPhotoCount() {
        return photo_count;
    }

    public void setPhotoCount(int photo_count) {
        this.photo_count = photo_count;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

}
