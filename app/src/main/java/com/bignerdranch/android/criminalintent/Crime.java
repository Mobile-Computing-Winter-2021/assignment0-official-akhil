package com.bignerdranch.android.criminalintent;

import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private String mDate;
    private String mDepartment;
    private String memailid;

    public Crime() {
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) { mDate = date; }

    public String getDept(){return mDepartment;}

    public void setDept(String dept){ mDepartment = dept; };

    public String getemailid(){return memailid;}

    public void setemail(String email){ memailid = email;};

}
