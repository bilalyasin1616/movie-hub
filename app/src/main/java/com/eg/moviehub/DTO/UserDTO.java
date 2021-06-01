package com.eg.moviehub.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by EG on 3/16/2019.
 */
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
public class UserDTO  implements Serializable{

    public  static  String PREMIUM="PREMIUM";
    public  static  String BASIC="BASIC";
    public String name;
    public String email;
    public String id;
    public  String typeAccount;
    public String profile_pic;

    public String userType;

    public ArrayList<String> genreList=new ArrayList<String>();

    public  ArrayList<String> videos=new ArrayList<String>();

    public  boolean isVerified;



    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("email", email);
        result.put("name", name);
        result.put("userType", userType);
        result.put("profile_pic",profile_pic);
        result.put("genreList",genreList);
        result.put("isVerified",isVerified);
        return result;
    }

}
