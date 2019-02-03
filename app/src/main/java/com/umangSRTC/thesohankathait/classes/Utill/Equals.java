package com.umangSRTC.thesohankathait.classes.Utill;

import com.umangSRTC.thesohankathait.classes.model.AboutUmang_model;
import com.umangSRTC.thesohankathait.classes.model.College;
import com.umangSRTC.thesohankathait.classes.model.Notices;
import com.umangSRTC.thesohankathait.classes.model.Query_model;

import java.util.List;

public class Equals {

    public static boolean BothEquals(Notices notices, Notices currentNotice) {

        if (notices.getImageUrl() != null) {

            return notices.getTitle().equals(currentNotice.getTitle()) &&
                    notices.getSender().equals(currentNotice.getSender()) &&
                    notices.getImageUrl().equals(currentNotice.getImageUrl()) &&
                    notices.getDescription().equals(currentNotice.getDescription());
        } else {
            return notices.getTitle().equals(currentNotice.getTitle()) &&
                    notices.getSender().equals(currentNotice.getSender()) &&
                    notices.getDescription().equals(currentNotice.getDescription());
        }
    }

    public static boolean BothEqual(AboutUmang_model aboutUmang,AboutUmang_model currentAboutUmang){

        return aboutUmang.getAbout().equals(currentAboutUmang.getAbout())&&
                aboutUmang.getImageUrl().equals(currentAboutUmang.getImageUrl());
    }

    public static boolean BothEqual(Query_model currentQuery, Query_model old_query_model) {

        return currentQuery.getQuestion().equals(old_query_model.getQuestion())&&
                currentQuery.getAnswer().equals(old_query_model.getAnswer());
    }

    public static boolean BothEqual(College college,College currentCollege){
        return college.getAdminEmail().equals(currentCollege.getAdminEmail()) &&
                college.getCollegeName().equals(currentCollege.getCollegeName())&&
                college.getPhoneNo().equals(currentCollege.getPhoneNo());
    }

    public static boolean contain(List<Notices> searchList, Notices notices) {

        for(Notices nots: searchList){
            if(nots.getTitle().equals(notices.getTitle())){
                return true;
            }

        }
        return false;
    }
}
