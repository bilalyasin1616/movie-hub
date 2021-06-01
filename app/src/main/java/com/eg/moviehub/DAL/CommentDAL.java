package com.eg.moviehub.DAL;

import android.support.annotation.NonNull;
import android.util.Log;

import com.eg.moviehub.DTO.CommentDTO;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Memona Sultan on 3/16/2019.
 */

public class CommentDAL {
    private CommentDAL.CommentDALListener listener;

    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference ref;

    public String uid;
    public CommentDAL()
    {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("videos");
        uid = user.getUid();
    }

    public void setCommentDALListner(CommentDAL.CommentDALListener listener) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("videos");
        this.listener = listener;

    }

    public interface CommentDALListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered

        public void onCommentPushed(String s);


    }

    public void pushNewComment(CommentDTO c, String vid_id)
    {
     String key = ref.child(vid_id).child("Comments").push().getKey();
        ref.child(vid_id).child("Comments").child(vid_id)
                .child("Comments").child(key).setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                try{
                    listener.onCommentPushed("Comment Added");
                }catch(Exception e )
                {

                }


            }
        });

    }



}
