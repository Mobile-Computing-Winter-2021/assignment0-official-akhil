package com.akhil.android.Assignment3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

public class StudentFragment extends Fragment {

    private Student mStudent;
    private EditText mRollnoField;
    private EditText mNameField;
    private EditText mdepts;
    private EditText memail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID studentId = (UUID) getActivity().getIntent().getSerializableExtra(StudentActivity.Extra_id);
        mStudent = StudentLab.get(getActivity()).getStudent(studentId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student, container, false);

        mRollnoField = (EditText) v.findViewById(R.id.stu1_title);
        mRollnoField.setText(mStudent.getRollno());
        mNameField = (EditText) v.findViewById(R.id.stu1_name);
        mNameField.setText(mStudent.getName());
        mdepts = (EditText) v.findViewById(R.id.stu_Dept);
        mdepts.setText(mStudent.getDept());
        memail = (EditText) v.findViewById(R.id.stu_emailid);
        memail.setText(mStudent.getemailid());
        Button change_button = (Button) v.findViewById(R.id.change_button);
        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence roll_no = mRollnoField.getText();
                CharSequence name = mNameField.getText();
                CharSequence dept = mdepts.getText();
                CharSequence emailid = memail.getText();
                mStudent.setRollno(roll_no.toString());
                mStudent.setName(name.toString());
                mStudent.setDept(dept.toString());
                mStudent.setemail(emailid.toString());
                Toast.makeText(getActivity(),"Student information Updated!!",Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
