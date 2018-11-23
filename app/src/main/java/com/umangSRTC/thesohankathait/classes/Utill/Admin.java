package com.umangSRTC.thesohankathait.classes.Utill;

public class Admin {
   // private final static String EMAIL="adarshbhatt91@gmail.com";

    public static boolean CheckAdmin(String email){
        if(Initialisation.adminList.contains(email)){
            return true;
        }
        else{
            return false;
        }
    }
    public static boolean isSchoolCorrect(String schoolName){
        String deniedString[]={".","/","#","$","[","]"};
        for(String denied:deniedString){
            if(schoolName.contains(denied)){
                return false;
            }
        }
        return true;

    }

}
