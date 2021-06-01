package com.eg.moviehub;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eg.moviehub.DTO.UserDTO;

/**
 * Created by EG on 3/16/2019.
 */

public class ProfileActivity extends Activity {

    UserSharedPref userSharedPref;
    TextView tvEmail,tvName,tvType;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userSharedPref= new UserSharedPref(this);
        tvEmail=findViewById(R.id.tv_email);
        tvName=findViewById(R.id.tv_profile_name);
        UserDTO userDTO = userSharedPref.getUserFromSharedPref();
        tvName.setText(userDTO.name);
        tvEmail.setText(userDTO.email);
    }
}
