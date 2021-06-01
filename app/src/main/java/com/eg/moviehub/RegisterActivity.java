package com.eg.moviehub;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

//import com.airbnb.lottie.LottieAnimationView;
import com.eg.moviehub.DTO.UserDTO;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.facebook.Profile;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_SHORT;
//import cn.pedant.SweetAlert.SweetAlertDialog;
public class RegisterActivity extends Activity implements View.OnClickListener{
    Button btn;
    SharedPreferences sharedPreferences;
   TextView relativeLayoutLogin;
    EditText full_name,email,pass,pass2;
    RadioButton type;
    RadioGroup radioGroup;
    int _id;
    // LottieAnimationView loadingAnimation;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference ref;


//    /*_____________G O O G L E _______________________________*/
//    ImageView googleLoginbtn;
//    private final static int RC_SIGN_IN = 123;
//    GoogleSignInOptions gso;
//    GoogleSignInClient mGoogleSignInClient;


    /*_____________F A C E B O O K _______________________________*/
    CallbackManager  callbackManager;
    ImageView facebookLoginbtn;
    ImageView twitterLoginbtn;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.e("ONCREAAAAAATE","OOOOOO");
       radioGroup    = findViewById(R.id.radioGroup);


        relativeLayoutLogin=findViewById(R.id.tv_sign_in);
//        //loadingAnimation=findViewById(R.id.loadingAniamtion);
       relativeLayoutLogin.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        //Storing User Data
       // sharedPreferences=getSharedPreferences(Globals.SHARED_PREF_NAME,MODE_PRIVATE);
        ref = FirebaseDatabase.getInstance().getReference().child("users");

        //  pb = (ProgressBar) findViewById(R.id.pbLoading);
        //Creating loading bar

        pDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#ff0000"));
        pDialog.getProgressHelper().setCircleRadius(700);
        pDialog.getProgressHelper().setSpinSpeed(1);
        pDialog.setNeutralText("Loading...");
        pDialog.setCancelable(false);
//        loadingAnimation.setVisibility(View.VISIBLE);


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.eg.moviehub",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        email = (EditText) findViewById(R.id.et_email);
        full_name = findViewById(R.id.et_full_name);
        pass = findViewById(R.id.et_username);
        pass2 = findViewById(R.id.et_password);
        btn = findViewById(R.id.sign_up_btn);
        btn.setOnClickListener(this);
        Log.e("ONCREAAAAAATE","OOOOOO");
        facebookLoginbtn= findViewById(R.id.iv_login_by_fb);
        facebookLoginbtn.setOnClickListener(this);
//        googleLoginbtn = findViewById(R.id.iv_login_by_google);
//        googleLoginbtn.setOnClickListener(this);

    }
    /*_____________F A C E B O O K _______________________________*/
    private void initiateFacebookObjects()
    {

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.e("profile ","oooooooooo");
                Profile profile = Profile.getCurrentProfile();
                handleFacebookAccessToken(loginResult.getAccessToken(),profile);
            }

            @Override
            public void onCancel() {
                Log.e("Facbook error:","On CANCEL CALLED");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Facbook error:",error.getMessage());
            }
        });

    }
    /*_____________G O O G L E _______________________________*/
//    private void initiateGoogleObjects()
//    {
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getResources().getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//    }
    private void registerUser() {
        final String e = email.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        final String name = full_name.getText().toString().trim();
        final String password2 = pass2.getText().toString().trim();
        _id  = radioGroup.getCheckedRadioButtonId();
        type = findViewById(_id);
        if (e.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (name.isEmpty()) {
            full_name.setError("Name is required!");
            full_name.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            pass.setError("Password is required");
            pass.requestFocus();
            return;
        }
        if (password.length() < 6) {
            pass.setError("Minimum lenght of password should be 6");
            pass.requestFocus();
            return;
        }
        if(!password2.equals(password))
        {
            pass2.setError("Password doesnt match");
            pass2.requestFocus();
            return;
        }

          pDialog.show();
      //  loadingAnimation.setVisibility(View.VISIBLE);
       // loadingAnimation.playAnimation();


        mAuth.createUserWithEmailAndPassword(e, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser user = mAuth.getCurrentUser();
                    String uid = user.getUid();
                    Log.e("signUp", uid);
                    DatabaseReference user_ref = ref.child(uid);
                    Log.e("sign up", user_ref.toString());
                    user_ref.child("email").setValue(e);
                    user_ref.child("id").setValue(uid);
                    user_ref.child("name").setValue(name);

                    user_ref.child("isVerified").setValue(false);
                    user_ref.child("typeAccount").setValue("email");

                    user_ref.child("userType").setValue(type.getText()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           // Globals.AccountType = "email";
                            Log.e("SignUp", "data saved in firebase");
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.e("Sign up ", "email sent");




//                                                AlertDialog.Builder builder;
//                                                builder = new AlertDialog.Builder(RegisterActivity.this, R.style.MyDialogTheme);
//                                                LayoutInflater inflater = RegisterActivity.this.getLayoutInflater();
//                                                final View dialogView = inflater.inflate(R.layout.info_alert, null);
//                                                dialogView.setPadding(0, 0, 0, 0);
//                                                Button button = dialogView.findViewById(R.id.btn_alert_proceed);
//                                                button.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
//                                                        i.putExtra("registerFlag", true);
//                                                        i.putExtra("name", full_name.getText().toString());
//                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                        mAuth.signOut();
//                                                        startActivity(i);
//                                                        finish();
//                                                    }
//                                                });
//                                                builder.setView(dialogView);
//                                                builder.show();
                                            }
                                        }
                                    });
                            new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Account Created")
                                    .setContentText("Go to your email to register your account")
                                    .show();
                            pDialog.dismiss();

                            // save the bitmap in globals or somewhere so we dont have to download again n again

                        }
                    });

                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        pDialog.dismiss();
                        //loadingAnimation.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "This email is already registeredt with us!", LENGTH_SHORT).show();

                    } else {

                        pDialog.dismiss();
                      //  loadingAnimation.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), LENGTH_SHORT).show();
                    }

                }
            }
        });
//
//
   }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.tv_sign_in)
        {
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();
        }
//        else
//        {
//            if(!Globals.isNetworkConnected(RegisterActivity.this))
//            {
//                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("No Internet")
//                        .setContentText("Please connect to some internet")
//                        .show();
//            }
//            else{

                // ____ WRITE ALL CLICK FUNCTIONS HERE ______
                if(v.getId()==R.id.sign_up_btn)
                {
                    registerUser();
                }
//                else if(v.getId()==R.id.iv_login_by_google)
//                {
//                    //signInGoogle();
//                }
                else
                if(v.getId()==R.id.iv_login_by_fb)
                {
                    signInFacebook();
                }


            }
//        }
   // }

    /*_____________F A C E B O O K _______________________________*/
    private void signInFacebook()
    {
        initiateFacebookObjects();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
    }
    private void handleFacebookAccessToken(AccessToken token,final Profile profile) {
        Log.e(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

//        loadingAnimation.setVisibility(View.VISIBLE);
//        loadingAnimation.playAnimation();

        pDialog.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "signInWithCredential:success");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                //    Globals.AccountType = "facebook";
                                    final FirebaseUser user = mAuth.getCurrentUser();
                                    String uid = user.getUid();
                                    Log.e("FBsignUp",uid);
                                    // adding user info to firebase DB
                                    DatabaseReference user_ref = ref.child(uid);
                                    user_ref.child("email").setValue(user.getEmail());
                                    user_ref.child("id").setValue(uid);
                                    user_ref.child("name").setValue(user.getDisplayName());
                                    user_ref.child("userType").setValue(UserDTO.BASIC);
                                    user_ref.child("typeAccount").setValue("facebook").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                         //   loadingAnimation.setVisibility(View.GONE);
    pDialog.dismiss();
                                            //saving to sharedPref

                                            UserSharedPref userSharedPref= new UserSharedPref(RegisterActivity.this);
                                          UserDTO userDTO = new UserDTO();
                                          userDTO.typeAccount="facebook";
                                          userDTO.userType=UserDTO.BASIC;
                                          userDTO.name=user.getDisplayName();
                                          userDTO.email=user.getEmail();
                                          userDTO.id=user.getUid();
                                          userSharedPref.updateUser(userDTO);

//                                            userSharedPref.updateFirebaseUser(user);
//                                            userSharedPref.updateAccountType("facebook");
//                                            userSharedPref.updateProfilePhotoURL(profile.getProfilePictureUri(200,200).toString());
                                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    });

                                }},1500);
                        } else {

                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            // show some error dilog
                                pDialog.dismiss();
                          //  loadingAnimation.setVisibility(View.GONE);

                        }
                    }
                });
    }

//    /*_____________G O O G L E _______________________________*/
//    private void signInGoogle() {
//        initiateGoogleObjects();
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in succes
//                            // addtion : show progress bar
//                            loadingAnimation.setVisibility(View.VISIBLE);
//                            loadingAnimation.playAnimation();
//
////                                pDialog.show();
//                            final FirebaseUser user = mAuth.getCurrentUser();
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Globals.AccountType = "google";
//                                    Log.e("GooglesignUp",user.getUid());
//                                    String uid = user.getUid();
//                                    DatabaseReference user_ref = ref.child(uid);
//                                    user_ref.child("email").setValue(user.getEmail());
//                                    user_ref.child("id").setValue(uid);
//                                    user_ref.child("name").setValue(user.getDisplayName());
//                                    user_ref.child("typeAccount").setValue("google").addOnCompleteListener(
//                                            new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    UserSharedPref userSharedPref = new UserSharedPref(RegisterActivity.this);
//                                                    userSharedPref.updateFirebaseUser(user);
//                                                    if(user.getPhotoUrl()!=null) {
//                                                        userSharedPref.updateProfilePhotoURL(user.getPhotoUrl().toString());
//                                                        Log.e("GOOGLE","saving profile pic url+ "+user.getPhotoUrl().toString());
//                                                    }
//                                                    userSharedPref.updateAccountType("google");
//                                                    loadingAnimation.setVisibility(View.GONE);
////   pDialog.dismiss();
//                                                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
//                                                    finish();
//                                                }
//                                            });
//
//                                }},1500);
//                        } else {
//                            loadingAnimation.setVisibility(View.GONE);
//                            //   pDialog.dismiss();
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//
//                        }
//
//                    }
//                });
//    }
//    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("RegisterActivity","onActivityResult called");

         /*_____________G O O G L E _______________________________*/
//        if (requestCode == RC_SIGN_IN) {
//            Log.e("RegisterActivity","onActivityResult called for google");
//            try{
//
//                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//
//                Log.e(TAG, "Google sign in failed", e);
//            }
//        }
//        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

}
