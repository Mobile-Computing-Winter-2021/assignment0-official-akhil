package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;
import java.util.UUID;

import static android.widget.CompoundButton.*;

public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private EditText mTitleField;
    private EditText mNameField;
    private EditText mdepts;
    private EditText memail;
    private Button change_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.Extra_id);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mNameField = (EditText) v.findViewById(R.id.crime_name);
        mNameField.setText(mCrime.getDate());
        mdepts = (EditText) v.findViewById(R.id.crime_Dept);
        mdepts.setText(mCrime.getDept());
        memail = (EditText) v.findViewById(R.id.crime_emailid);
        memail.setText(mCrime.getemailid());
        change_button = (Button) v.findViewById(R.id.change_button);
        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence roll_no = mTitleField.getText();
                CharSequence name = mNameField.getText();
                CharSequence dept = mdepts.getText();
                CharSequence emailid = memail.getText();
                mCrime.setTitle(roll_no.toString());
                mCrime.setDate(name.toString());
                mCrime.setDept(dept.toString());
                mCrime.setemail(emailid.toString());
                Toast.makeText(getActivity(),"Student information Updated!!",Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
