package com.eg.moviehub.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eg.moviehub.DTO.VideoDTO;
import com.eg.moviehub.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by EG on 3/16/2019.
 */

public class VideosAdapter  extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    ArrayList<VideoDTO> list;
    Context context;
   public VideosAdapter(Context ctx,ArrayList<VideoDTO> l){
        context=ctx;
        list=l;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,genre,videoType;
        ImageView thumbnail,profilePic;

        public ViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.tv_title);
            genre=itemView.findViewById(R.id.tv_genre);
            videoType=itemView.findViewById(R.id.video_access_type);
            thumbnail=itemView.findViewById(R.id.iv_thumbnail);
            profilePic=itemView.findViewById(R.id.civ_profile_pic);

        }
    }


    @Override
    public VideosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.video_cell, parent, false);

        return new VideosAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final VideosAdapter.ViewHolder holder, int position) {
        holder.genre.setText(list.get(position).title);
        holder.title.setText(list.get(position).title);
        holder.videoType.setText(list.get(position).title);
//        Download File Code
        //player=view.findViewById(R.id.video_test);
      //  String uid= FirebaseAuth.getInstance().getUid();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        storageReference.child("videos/"+list.get(position).id+".mp4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
           @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.thumbnail);
            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
