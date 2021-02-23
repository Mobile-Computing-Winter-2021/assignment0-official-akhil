package com.akhil.android.Assignment3;

import java.util.UUID;

public class Student {

    private UUID mId;
    private String mTitle;
    private String mName;
    private String mDepartment;
    private String memailid;

    public Student() {
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

    public String getName() {
        return mName;
    }

    public void setName(String date) { mName = date; }

    public String getDept(){return mDepartment;}

    public void setDept(String dept){ mDepartment = dept; };

    public String getemailid(){return memailid;}

    public void setemail(String email){ memailid = email;};

}
