package com.eg.moviehub.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.eg.moviehub.DTO.UserDTO;
import com.eg.moviehub.GenreActivity;
import com.eg.moviehub.ProfileActivity;
import com.eg.moviehub.R;
import com.eg.moviehub.RegisterActivity;
import com.eg.moviehub.UserSharedPref;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by EG on 3/16/2019.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener{

    View view;
    FirebaseAuth mAuth;
    UserSharedPref userSharedPref;
    RelativeLayout profile,genre,logout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_settings, container, false);
       profile= view.findViewById(R.id.rel_profile);
       profile.setOnClickListener(this);
       logout=view.findViewById(R.id.rel_logout);
       genre=view.findViewById(R.id.rel_genre);
       genre.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
       logout.setOnClickListener(this);
       userSharedPref= new UserSharedPref(getActivity());


        return view;
    }

    @Override
    public void onClick(View v) {
        if(v==profile){
            Intent i = new Intent(getActivity(), ProfileActivity.class);
            startActivity(i);
        }else if(v==logout){
           logOut();
        }else if (v==genre){
            Intent i = new Intent(getActivity(), GenreActivity.class);
            startActivity(i);
        }
    }
    private  void logOut(){
//        if(!Globals.isNetworkConnected(getActivity()))
//        {
//            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText("No Internet")
//                    .setContentText("Please connect to internet first")
//                    .show();
//        }
//        else {
        UserDTO userDTO = userSharedPref.getUserFromSharedPref();
            mAuth.signOut(); // whether its fb or google firebase auth always be signed out separately
            if(userDTO.typeAccount.equals("email"))
            {
                backToLogin();
            }
            if (userDTO.typeAccount.equals("google")) {

              //  GoogleSignOut();
            }
            else if (userDTO.typeAccount.equals("facebook")) {
                facebookSignOut();
                backToLogin();
            }

//        }
    }
    private void backToLogin()
    {

        UserSharedPref userSharedPref = new UserSharedPref(getActivity());
      //  userSharedPref.clearUserSharedPref();
        Intent i = new Intent(getActivity(), RegisterActivity.class);
        startActivity(i);
        if(getActivity()!=null)
            getActivity().finish();

    }
    private void facebookSignOut()
    {
        LoginManager.getInstance().logOut();
    }
}
