package com.umangSRTC.thesohankathait.classes.model;

public class Notices {
    //for pdf imageUrl==pdfUri or downloadURL
    private String description,title,sender,imageUrl;

    public Notices() {
    }

    public Notices(String description, String title, String sender, String imageUrl) {

        this.description = description;
        this.title = title;
        this.sender = sender;
        this.imageUrl = imageUrl;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
