package com.eg.moviehub.DAL;

import android.support.annotation.NonNull;
import android.util.Log;

import com.eg.moviehub.DTO.UserDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by EG on 3/16/2019.
 */

public class UserDAL {

    private UserDALListener listener;

    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference ref;

    public String uid;
    public UserDAL()
    {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("users");
        uid = user.getUid();
    }

    public void setUserDALListner(UserDALListener listener) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("users");
        this.listener = listener;

    }

    public interface UserDALListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public  void onUserFetch(UserDTO userDTO);
        public void onUserPushed(String s);
        public void onUserGenreListUpdated(boolean b);
        public void onUserVideoListUpdated(boolean b);
        public void onUserStatusUpdated(boolean b);
        public void onVidAdd(String b);
        public void onUpdateGenreList(ArrayList<String> list);
        public void onAllGenreFetch(ArrayList<String> list);




    }

    public void pushNewUser(UserDTO DTO)
    {
        String key = ref.push().getKey();
        DTO.id=key;
        Map<String,Object> uValues =DTO.toMap();
        final Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("" + key, uValues);
        ref.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onUserPushed("User created");
            }
        });

    }

    public void getUser(String uid)
    {
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    UserDTO u = dataSnapshot.getValue(UserDTO.class);

                listener.onUserFetch(u);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getAllGenre()
    {
        ref.child(uid).child("genreList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> gList=new ArrayList<String>();
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for(DataSnapshot child: children)
                {
                    String g= child.getValue(String.class);
                    gList.add(g);
                }
                listener.onAllGenreFetch(gList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void pushNewGenre(String genre)
    {

        String genreKey = ref.child(uid).child("genreList").push().getKey();
        ref.child(uid).child("genreList").child(genreKey).setValue(genre).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                try{
                    listener.onUserGenreListUpdated(true);
                }catch(Exception e )
                {
                    Log.e("ERPRPRPR",e.getMessage());
                }


            }
        });

    }

    public void pushNewVideoRef(String reference)
    {

        String vKey = ref.child(uid).child("videoList").push().getKey();
        ref.child(uid).child("videoList").child(vKey).setValue(reference).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                try{
                    listener.onUserVideoListUpdated(true);
                }catch(Exception e )
                {
                    Log.e("ERPRPRPR",e.getMessage());
                }


            }
        });

    }

    public void updateVerifiedStatus(boolean b)
    {

        ref.child(uid).child("isVerified").setValue(b).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                try{
                    listener.onUserStatusUpdated(true);
                }catch(Exception e )
                {
                    Log.e("ERPRPRPR",e.getMessage());
                }


            }
        });

    }

    public void pushVidId(final String id)
    {

        String  k = ref.child(uid).child("videoList").push().getKey();
        ref.child(uid).child("videoList").child(k).setValue(id).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                try{
                    listener.onVidAdd(id);
                }catch(Exception e )
                {
                    Log.e("ERPRPRPR",e.getMessage());
                }


            }
        });

    }

    public void updateGenreList(final ArrayList<String> list)
    {


        ref.child(uid).child("genreList").setValue(list).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                try{
                    listener.onUpdateGenreList(list);
                }catch(Exception e )
                {
                    Log.e("ERPRPRPR",e.getMessage());
                }


            }
        });

    }






}
