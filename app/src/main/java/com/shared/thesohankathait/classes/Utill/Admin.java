package com.shared.thesohankathait.classes.Utill;

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
    public static boolean isCorrect(String schoolName){
        String deniedString[]={".","/","#","$","[","]"," "};
        for(String denied:deniedString){
            if(schoolName.contains(denied)){
                return false;
            }
        }
        return true;
    }
    public static String modifyCollege(String college){
        String modifiedCollege="";

        for(int j=0;j<college.length();j++){
            if((college.charAt(j)>=65 && college.charAt(j)<=90)||(college.charAt(j)>=97&&college.charAt(j)<=122)){
                modifiedCollege+=college.charAt(j);
            }
        }
        return modifiedCollege;
    }
}
