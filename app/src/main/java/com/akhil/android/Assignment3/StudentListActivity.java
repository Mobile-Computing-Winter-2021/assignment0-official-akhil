package com.akhil.android.Assignment3;

import android.support.v4.app.Fragment;

public class StudentListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new StudentListFragment();
    }
}
