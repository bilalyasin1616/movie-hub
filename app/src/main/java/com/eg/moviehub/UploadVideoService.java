package com.eg.moviehub;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.eg.moviehub.DAL.VideoDAL;
import com.eg.moviehub.DTO.UserDTO;
import com.eg.moviehub.DTO.VideoDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

/**
 * Created by Bilal Yasin on 16-Mar-19.
 */

public class UploadVideoService extends IntentService{
    private static final int NOTIFICATION_ID=1;
    private static final String CHANNEL_ID = "Upload";
    private static final int PROGRESS_MAX = 100;
    int PROGRESS_CURRENT = 0;
    NotificationCompat.Builder builder;
    NotificationManagerCompat mNotificationManager;
    String mimeType;
    Uri uri;
    String videoId;
    VideoDAL videoDAL;

    public UploadVideoService() {
        super("UploadVideoService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mNotificationManager = NotificationManagerCompat.from(this);
        builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setContentTitle("MovieHub Video Upload")
                .setContentText("Uploading your video")
                .setSmallIcon(R.drawable.icons8_upload_to_ftp_24)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true);
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());

        mimeType=intent.getStringExtra("mimeType");
        String uriString=intent.getStringExtra("uri");
        uri=Uri.parse(uriString);
        VideoDTO videoDTO=new VideoDTO();
        videoDTO.genre=intent.getStringExtra("genre");
        videoDTO.title=intent.getStringExtra("title");
        videoDTO.access_type=intent.getStringExtra("videoType");
        Log.i("Title", "onHandleIntent: "+intent.getStringExtra("title"));
        videoDTO.uploaded_by=FirebaseAuth.getInstance().getUid();
        videoDAL=new VideoDAL();
        videoDAL.pushNewVideo(videoDTO);
        videoDAL.setVideoDALListner(new VideoDAL.VideoDALListener() {
            @Override
            public void onVideoPushed(String id) {
                videoId=id;
                uploadVideo(uri,mimeType,id);
            }

            @Override
            public void onVideoDeleted(boolean b) {

            }

            @Override
            public void onVideoFetch(ArrayList<VideoDTO> v) {

            }
        });


    }

    public void uploadVideo(Uri videoUri,String mimeType,String id)
    {
        if(videoUri!=null) {
            StorageReference storageReference= FirebaseStorage.getInstance().getReference();
            UploadTask uploadTask= storageReference.child("videos/"+id+"."+mimeType).putFile(videoUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    builder.setContentText("Upload Failed")
                            .setProgress(PROGRESS_CURRENT,PROGRESS_CURRENT,false)
                            .setOngoing(false);
                    videoDAL.deleteVideo(videoId);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    builder.setContentText("Upload Completed")
                            .setProgress(PROGRESS_MAX,PROGRESS_MAX,false)
                            .setOngoing(false);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("Total File Size", "total bytes: "+taskSnapshot.getTotalByteCount());
                    Log.i("Total File Size", "transfered: "+taskSnapshot.getBytesTransferred());
                    updateProgress(taskSnapshot);
                }
            });
        }
        else
        {
            //Toast.makeText(context,"Nothing to uploaod",Toast.LENGTH_LONG).show();
        }
    }

    public void updateProgress(UploadTask.TaskSnapshot taskSnapshot)
    {
        long fileSize=taskSnapshot.getTotalByteCount();
        long uploadedBytes= taskSnapshot.getBytesTransferred();
        long progress=(100*uploadedBytes)/fileSize;
        builder.setContentText("Uploading your video")
                .setProgress(PROGRESS_MAX,(int)progress,false);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
        //Toast.makeText(context,"File upload progress: "+progress,Toast.LENGTH_LONG).show();
    }
}
