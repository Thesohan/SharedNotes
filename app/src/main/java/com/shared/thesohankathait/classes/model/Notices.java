package com.shared.thesohankathait.classes.model;

import java.io.Serializable;

public class Notices implements Serializable {
    //for pdf imageUrl==pdfUri or downloadURL
  public String description,title,sender,imageUrl,fileExtension,link;

    public Notices() {
    }

    public Notices(String description, String title, String sender, String imageUrl) {

        this.description = description;
        this.title = title;
        this.sender = sender;
        this.imageUrl = imageUrl;
        this.fileExtension="JPEG";
        this.link=null;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {

        return description;
    }

    public String getFileExtension() {
        return fileExtension;
    }


    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
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
