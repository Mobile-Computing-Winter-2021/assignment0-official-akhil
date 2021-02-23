package com.akhil.android.Assignment3;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class StudentActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new StudentFragment();
    }
    public static String Extra_id = "com.akhil.android.Assignment3";
    public static Intent newIntent(Context packageContext, UUID stuId){
        Intent intent = new Intent(packageContext, StudentActivity.class);
        intent.putExtra(Extra_id, stuId);
        return intent;
    }
}
