package com.akhil.android.Assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class StudentListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        StudentLab studentLab = StudentLab.get(getActivity());
        List<Student> students = studentLab.getCrimes();
        if (mAdapter == null)
        {
            mAdapter = new CrimeAdapter(students);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else
        {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Student mStudent;

        private TextView mTitleTextView;
        private TextView mDateTextView;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_student, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.stu_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.stu_name);
        }

        public void bind(Student student) {
            mStudent = student;
            mTitleTextView.setText("Student Roll Number = " + mStudent.getTitle());
            mDateTextView.setText("Student Name = " + mStudent.getName().toString());
        }

        @Override
        public void onClick(View view) {
            Intent intent = StudentActivity.newIntent(getActivity(), mStudent.getId());
            startActivity(intent);
            Toast.makeText(getActivity(),
                    "Roll Number = "+ mStudent.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Student> mStudents;

        public CrimeAdapter(List<Student> students) {
            mStudents = students;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Student student = mStudents.get(position);
            holder.bind(student);
        }

        @Override
        public int getItemCount() {
            return mStudents.size();
        }
    }
}
