package com.shared.thesohankathait.classes.model;

import java.io.Serializable;

public class NoticeRequest implements Serializable {
    public String schoolName;
    public Notices notices;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Notices getNotices() {
        return notices;
    }

    public void setNotices(Notices notices) {
        this.notices = notices;
    }

    public NoticeRequest() {

    }

    public NoticeRequest(String schoolName, Notices notices) {

        this.schoolName = schoolName;
        this.notices = notices;
    }
}
