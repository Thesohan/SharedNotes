package com.umangSRTC.thesohankathait.classes.Utill;

public class Admin {
    private final static String EMAIL="adarshbhatt91@gmail.com";

    public static boolean CheckAdmin(String email){
        if(EMAIL.equals(email)){
            return true;
        }
        else{
            return false;
        }
    }

}
