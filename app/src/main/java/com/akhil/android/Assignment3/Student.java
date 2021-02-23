package com.akhil.android.Assignment3;

import java.util.UUID;

public class Student {

    private UUID mId;
    private String mrollno;
    private String mName;
    private String mDepartment;
    private String memailid;

    public Student() {
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public String getRollno() {
        return mrollno;
    }

    public void setRollno(String title) {
        mrollno = title;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) { mName = name; }

    public String getDept(){return mDepartment;}

    public void setDept(String dept){ mDepartment = dept; };

    public String getemailid(){return memailid;}

    public void setemail(String email){ memailid = email;};

}
