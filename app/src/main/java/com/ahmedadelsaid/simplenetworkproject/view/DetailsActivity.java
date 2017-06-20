package com.ahmedadelsaid.simplenetworkproject.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ahmedadelsaid.simplenetworklibrary.imagerequest.RequestImageView;
import com.ahmedadelsaid.simplenetworkproject.R;

/**
 * Created by Ahmed Adel on 19/06/2017.
 * <p>
 * DetailsActivity is the details screen that opens when any item in the MainActivity is clicked.
 */

public class DetailsActivity extends AppCompatActivity {

    private RequestImageView imageView;
    private TextView userTextView, userNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String userName = getIntent().getStringExtra(getResources().getString(R.string.user_name));
        String name = getIntent().getStringExtra(getResources().getString(R.string.name));
        String userProfileImage = getIntent().getStringExtra(getResources().getString(R.string.user_profile_image));

        initView();

        imageView.setImageUrl(userProfileImage);
        userTextView.setText(name);
        userNameTextView.setText(userName);
    }

    private void initView() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getString(R.string.details_screen));
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
        imageView = (RequestImageView) findViewById(R.id.user_image_iv);
        userTextView = (TextView) findViewById(R.id.name_tv);
        userNameTextView = (TextView) findViewById(R.id.user_name_tv);
    }
}
