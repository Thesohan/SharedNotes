package com.shared.thesohankathait.classes.model;

public class AboutUmang_model {
    public String about,imageUrl,fileExtension;

    public AboutUmang_model() {
    }

    public AboutUmang_model(String about, String imageUrl) {

        this.about = about;
        this.imageUrl = imageUrl;
        this.fileExtension="JPEG";
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
