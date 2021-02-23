package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        String[] mnames = {"Akhil","Antra","Anil","Kusam","Kamla","Subhash","Amit","Akshay","Arti","Shruti","Anju","Mehak","Youshita","Ishaan","Geeta","Shiv"
                ,"Chander","Manju","Aviral","Prabal","Sneh","Samridhi","Samya","Bindu","Adwait","Snehasis","Sachin","Pranav","Sid","Adwait"};
        String[] mdepts = {"CSE","ECE","CSE","Mechanical","EEE","ECE","CB","Mechanical","EEE","CB","CSE","CB","ECE","CSE","EEE","ECE","Mechanical","CB","ECE"
        ,"CSE","EEE","CB","CSE","ECE","Mechanical","CSE","ECE","EEE","CB","CSE"};
        String[] memails = {"akhil20107@iiitd.ac.in","antra123@gmail.com","anil345@rediffmail.com","kumi12df@gmail.com","kamla@gmail.com","su@gmail.com",
                "amit@gmail.com","akshay@gmail.com","arti@gmail.com","shruti@gmail.com","anju@gmail.com","mehak@gmail.com","youshita@gmail.com",
                "ish@gmail.com","geeta@gmail.com","shiv@gmail.com","chander@gmail.com","manju@gmail.com","aviral@gmail.com","prabal@gmail.com","sneh@gmail.com"
                ,"sam@gmail.com","samya@gmail.com","bindu@gmail.com","adwait@gmail.com","snehasis@gmail.com","sachin@gmail.com","pranav@gmail.com","sid@gmail.com","adwait@gmail.com"};
        for (int i = 1; i <=30; i++) {
            Crime crime = new Crime();
            crime.setTitle(String.valueOf(i));
            crime.setDate(mnames[i-1]);
            crime.setDept(mdepts[i-1]);
            crime.setemail(memails[i-1]);
            mCrimes.add(crime);
        }
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }
}
