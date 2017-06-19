package com.ahmedadelsaid.simplenetworkproject.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ahmedadelsaid.simplenetworkproject.R;

public class DetailsActivity extends AppCompatActivity {

    private String userName, name, userProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        userName = getIntent().getStringExtra(getResources().getString(R.string.user_name));
        name = getIntent().getStringExtra(getResources().getString(R.string.name));
        userProfileImage = getIntent().getStringExtra(getResources().getString(R.string.user_profile_image));

        initView();
    }

    private void initView() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            assert getSupportActionBar() != null;
            getSupportActionBar().setTitle(getString(R.string.main_screen));
        }
    }
}
