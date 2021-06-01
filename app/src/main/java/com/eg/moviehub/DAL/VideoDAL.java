package com.eg.moviehub.DAL;

import android.support.annotation.NonNull;

import com.eg.moviehub.DTO.CommentDTO;
import com.eg.moviehub.DTO.VideoDTO;
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

/**
 * Created by Memona Sultan on 3/16/2019.
 */

public class VideoDAL {
    private VideoDAL.VideoDALListener listener;

    // firebase objects
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference ref;
    public String uid;
    public VideoDAL()
    {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("videos");
        uid = user.getUid();
    }

    public void setVideoDALListner(VideoDAL.VideoDALListener listener) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("videos");
        this.listener = listener;

    }

    public interface VideoDALListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered

        public void onVideoPushed(String id);
        public void onVideoDeleted(boolean b);
        public void onVideoFetch(ArrayList<VideoDTO> v);


    }


    public void pushNewVideo(final VideoDTO v)
    {
        String key = ref.push().getKey();
        v.id=key;
        v.uploaded_by=uid;
        ref.child(key)
               .setValue(v).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                try{
                    listener.onVideoPushed(v.id);
                }catch(Exception e )
                {

                }


            }
        });

    }

    public void getAllVideos()
    {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<VideoDTO> addressList=new ArrayList<VideoDTO>();
                Iterable<DataSnapshot> children=dataSnapshot.getChildren();
                for(DataSnapshot child: children)
                {
                    VideoDTO address = child.getValue(VideoDTO.class);
                    addressList.add(address);
                }
                listener.onVideoFetch(addressList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void deleteVideo(String id)
    {

        ref.child(id).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                try{
                    listener.onVideoDeleted(true);
                }catch(Exception e )
                {

                }


            }
        });

    }


}
