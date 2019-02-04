package com.noticol.thesohankathait.classes.model;

public class User {
    public String name,email;
    public static User currentUser;


    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setUser(String name, String email)
    {

        currentUser= new User(name,email);

    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User() {

    }

    public User(String name, String email) {

        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
