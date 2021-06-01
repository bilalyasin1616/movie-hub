package com.eg.moviehub;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.eg.moviehub.DAL.UserDAL;
import com.eg.moviehub.DAL.VideoDAL;
import com.eg.moviehub.DTO.CommentDTO;import com.eg.moviehub.DTO.UserDTO;
import com.eg.moviehub.DTO.VideoDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;
public class LoginActivity extends Activity implements View.OnClickListener {
    EditText editTextEmail,editTextPassword;
    Button btn;
    RelativeLayout relativeLayoutSignUp;
    TextView signUpText,forgotPass;
    private FirebaseAuth mAuth;
    
    UserSharedPref userSharedPref;
    Button loginBtn;

    boolean lockFlag = false;

    SweetAlertDialog pDialog;
    private void ShowDilog()
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Email Verification")
                .setMessage("Please verify your email before login!")
                .setIcon(R.drawable.logo).setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing
                    }
                }).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = findViewById(R.id.et_email);
        forgotPass = findViewById(R.id.forgotpass);
        editTextPassword = findViewById(R.id.et_password);
        userSharedPref=new UserSharedPref(this);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);

        btn = findViewById(R.id.sign_in_btn);
        mAuth = FirebaseAuth.getInstance();
        btn.setOnClickListener(this);




        forgotPass.setOnClickListener(this);






        pDialog.getProgressHelper().setBarColor(Color.parseColor("#ff0000"));
        pDialog.getProgressHelper().setCircleRadius(700);
        pDialog.getProgressHelper().setSpinSpeed(1);
        pDialog.setNeutralText("Loading...");
        pDialog.setCancelable(false);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void LoginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        //   pb.setVisibility(ProgressBar.VISIBLE);

        try {
            pDialog.show();
        }catch (Exception ex)
        {
            Log.d("LoadingException",ex.getMessage());
        }

//        try {
//            pDialog.show();
//        }catch (Exception ex)
//        {
//            Log.d("LoadingException",ex.getMessage());
//        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    
                    final FirebaseUser user = mAuth.getCurrentUser();
                    String uid = user.getUid();

//                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(i);
//                    finish();
//                    if(user.isEmailVerified()) {
//                        //Store and start new activity
                        storeUserInSharedPref(uid);
//                    }else
//                    {
//                        mAuth.signOut();
//                        pDialog.dismiss();
//
//                        ///   getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                        // pd.dismiss();
//                        ShowDilog();
//                    }



                    if(user.isEmailVerified()) {
                        
                       final UserDAL dal = new UserDAL();
                        UserDTO userDTO = new UserDTO();
                        userDTO.isVerified=true;
                        dal.updateVerifiedStatus(true);
                        dal.setUserDALListner(new UserDAL.UserDALListener() {
                            @Override
                            public void onUserFetch(UserDTO userDTO) {

                            }

                            @Override
                            public void onUserPushed(String s) {

                            }

                            @Override
                            public void onUserGenreListUpdated(boolean b) {

                            }

                            @Override
                            public void onUserVideoListUpdated(boolean b) {

                            }

                            @Override
                            public void onUserStatusUpdated(boolean b) {

                            }

                            @Override
                            public void onVidAdd(String b) {

                            }

                            @Override
                            public void onUpdateGenreList(ArrayList<String> list) {

                            }

                            @Override
                            public void onAllGenreFetch(ArrayList<String> list) {

                            }
                        });







                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    }else
                    {
                        mAuth.signOut();
                       
                        pDialog.dismiss();

                           getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        // pd.dismiss();
                       // ShowDilog();
                    }

                }
                else {

                 
                    pDialog.dismiss();
                    //    pb.setVisibility(ProgressBar.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });

    }


    public void storeUserInSharedPref(String uid)
    {
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && lockFlag) {

                    lockFlag=false;
                    
                    UserDTO userDTO= new UserDTO() ;
                    userDTO.email=dataSnapshot.child("email").getValue(String.class);
                    userDTO.name=dataSnapshot.child("name").getValue(String.class);
                    userDTO.id=dataSnapshot.child("id").getValue(String.class);
                    userDTO.typeAccount="email";//dataSnapshot.child("typeAccount").getValue(String.class);
                    userDTO.userType=dataSnapshot.child("type").getValue(String.class);
                        userSharedPref.updateUser(userDTO);
//                    SharedPreferences.Editor et =sharedPreferences.edit();
//                    et.putString("email",dataSnapshot.child("email").getValue(String.class));
//                    et.putString("name",dataSnapshot.child("name").getValue(String.class));
//                    et.putString("typeAccount",dataSnapshot.child("typeAccount").getValue(String.class));
//                    et.putString("id",dataSnapshot.child("id").getValue(String.class));
//                    Toast.makeText(getApplicationContext(),"Welcome "+dataSnapshot.child("name").getValue(String.class),Toast.LENGTH_LONG).show();
//                    et.commit();
                    pDialog.dismiss();
                    //    pb.setVisibility(ProgressBar.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.sign_up_btn)
        {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
            finish();
        }else
        {
//            if(!Globals.isNetworkConnected(LoginActivity.this))
//            {
//                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("No Internet")
//                        .setContentText("Please connect to internet")
//                        .show();
//            }
//            else
            {
                // ____ WRITE ALL CLICK FUNCTIONS HERE ______
                if(v.getId()==R.id.sign_in_btn)
                {
                    try{
                        LoginUser();
                    }catch (Exception ex)
                    {

                    }
                    lockFlag=true;
                }
                else if(v.getId()==R.id.forgotpass)
                {
                    String email = editTextEmail.getText().toString().trim();
                    if (email.isEmpty()) {
                        editTextEmail.setError("Email is required");
                        editTextEmail.requestFocus();
                        return;
                    }

                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(getApplicationContext(),"Password Reset Email Sent",Toast.LENGTH_LONG).show();
                                    }else
                                    {

                                        Toast.makeText(getApplicationContext(),"Either your email is "+
                                                "not verified or there is some problem. Please try again!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }

            }
        }

    }
}
