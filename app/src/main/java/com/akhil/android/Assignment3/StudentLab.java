package com.akhil.android.Assignment3;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudentLab {
    private static StudentLab sStudentLab;

    private List<Student> mStudents;

    public static StudentLab get(Context context) {
        if (sStudentLab == null) {
            sStudentLab = new StudentLab(context);
        }

        return sStudentLab;
    }

    private StudentLab(Context context) {
        mStudents = new ArrayList<>();
        String[] mnames = {"Akhil","Antra","Anil","Kusam","Kamla","Subhash","Amit","Akshay","Arti","Shruti","Anju","Mehak","Youshita","Ishaan","Geeta","Shiv"
                ,"Chander","Manju","Aviral","Prabal","Sneh","Samridhi","Samya","Bindu","Adwait","Snehasis","Sachin","Pranav","Sid","Adwait"};
        String[] mdepts = {"CSE","ECE","CSE","Mechanical","EEE","ECE","CB","Mechanical","EEE","CB","CSE","CB","ECE","CSE","EEE","ECE","Mechanical","CB","ECE"
        ,"CSE","EEE","CB","CSE","ECE","Mechanical","CSE","ECE","EEE","CB","CSE"};
        String[] memails = {"akhil20107@iiitd.ac.in","antra123@gmail.com","anil345@rediffmail.com","kumi12df@gmail.com","kamla@gmail.com","su@gmail.com",
                "amit@gmail.com","akshay@gmail.com","arti@gmail.com","shruti@gmail.com","anju@gmail.com","mehak@gmail.com","youshita@gmail.com",
                "ish@gmail.com","geeta@gmail.com","shiv@gmail.com","chander@gmail.com","manju@gmail.com","aviral@gmail.com","prabal@gmail.com","sneh@gmail.com"
                ,"sam@gmail.com","samya@gmail.com","bindu@gmail.com","adwait@gmail.com","snehasis@gmail.com","sachin@gmail.com","pranav@gmail.com","sid@gmail.com","adwait@gmail.com"};
        for (int i = 1; i <=30; i++) {
            Student student = new Student();
            student.setTitle(String.valueOf(i));
            student.setName(mnames[i-1]);
            student.setDept(mdepts[i-1]);
            student.setemail(memails[i-1]);
            mStudents.add(student);
        }
    }

    public List<Student> getCrimes() {
        return mStudents;
    }

    public Student getCrime(UUID id) {
        for (Student student : mStudents) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }
}
