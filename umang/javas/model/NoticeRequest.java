package com.noticol.thesohankathait.notices.javas.model;

public class NoticeRequest {
    private String schoolName;
    private Notices notices;

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
