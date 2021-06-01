package com.eg.moviehub;

import android.content.Context;
import android.content.SharedPreferences;

import com.eg.moviehub.DTO.UserDTO;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by EG on 3/16/2019.
 */

public class UserSharedPref {

    Context context;
    SharedPreferences sharedPreferences;
    public UserSharedPref(Context ctx){
        context=ctx;
        sharedPreferences = context.getSharedPreferences(Globals.SHARED_PREF_NAME,Context.MODE_PRIVATE);
    }

    public  void updateName(String name)
    {
        SharedPreferences.Editor et =sharedPreferences.edit();
        et.putString("name",name);
        et.commit();
    }
    public  void updateFirebaseUser(FirebaseUser user)
    {
        SharedPreferences.Editor et =sharedPreferences.edit();
        et.putString("email",user.getEmail());
        et.putString("id",user.getUid());
        et.putString("name",user.getDisplayName());

        et.commit();

    }

    public void updateAccountType(String type)
    {
        SharedPreferences.Editor et =sharedPreferences.edit();
        et.putString("typeAccount",type);
        et.apply();
    }
    public void updateProfilePhotoURL(String url)
    {
        SharedPreferences.Editor et =sharedPreferences.edit();
        et.putString("profile_pic",url);
        et.apply();
    }
    public void updateType(String type)
    {
        SharedPreferences.Editor et =sharedPreferences.edit();
        et.putString("type",type);
        et.apply();
    }
    public void updateUser(UserDTO userDTO)
    {
        SharedPreferences.Editor et =sharedPreferences.edit();
        et.putString("id",userDTO.id);
        et.putString("email",userDTO.email);
        et.putString("name",userDTO.name);
        et.putString("typeAccount",userDTO.typeAccount);
        et.putString("userType",userDTO.userType);
        et.commit();


    }
    public  UserDTO getUserFromSharedPref()
    {
        UserDTO userDTO= new UserDTO();
        userDTO.id = sharedPreferences.getString("id", "");
        userDTO.email = sharedPreferences.getString("email", "");
        userDTO.name = sharedPreferences.getString("name", "");
        userDTO.typeAccount = sharedPreferences.getString("typeAccount", "");
        userDTO.userType=sharedPreferences.getString("userType","");
        return userDTO;

    }
}
