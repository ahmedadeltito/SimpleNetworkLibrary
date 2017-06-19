package com.ahmedadelsaid.simplenetworkproject.model;

/**
 * Created by Ahmed Adel on 19/06/2017.
 */

public class User {

    private String id;
    private String username;
    private String name;
    private ProfileImage profile_image;
    private Links links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProfileImage getProfileImage() {
        return profile_image;
    }

    public void setProfileImage(ProfileImage profile_image) {
        this.profile_image = profile_image;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

}
