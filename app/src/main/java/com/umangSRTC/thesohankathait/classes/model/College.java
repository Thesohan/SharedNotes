package com.umangSRTC.thesohankathait.classes.model;

public class College {
    String CollegeName,adminEmail,phoneNo;

    public College() {
    }

    public String getCollegeName() {
        return CollegeName;
    }

    public void setCollegeName(String collegeName) {
        CollegeName = collegeName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public College(String collegeName, String adminEmail, String phoneNo) {

        CollegeName = collegeName;
        this.adminEmail = adminEmail;
        this.phoneNo = phoneNo;
    }
}
