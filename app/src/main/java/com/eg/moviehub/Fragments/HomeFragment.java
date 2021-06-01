package com.eg.moviehub.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eg.moviehub.Adapters.VideosAdapter;
import com.eg.moviehub.DAL.VideoDAL;
import com.eg.moviehub.DTO.UserDTO;
import com.eg.moviehub.DTO.VideoDTO;
import com.eg.moviehub.Globals;
import com.eg.moviehub.PlayVideoActivity;
import com.eg.moviehub.R;
import com.eg.moviehub.RecyclerTouchListener;

import java.util.ArrayList;

/**
 * Created by EG on 3/16/2019.
 */

public class HomeFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    ArrayList<VideoDTO> list;
    VideoDAL videoDAL;
    Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_home, container, false);
        context=getActivity();
        recyclerView=view.findViewById(R.id.recycler_videos);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        videoDAL = new VideoDAL();
         list= new ArrayList<VideoDTO>();
        VideosAdapter videosAdapter = new VideosAdapter(getActivity(),list);
        //  videosAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(videosAdapter);
        videoDAL.getAllVideos();
        videoDAL.setVideoDALListner(new VideoDAL.VideoDALListener() {


            @Override
            public void onVideoPushed(String id) {

            }

            @Override
            public void onVideoDeleted(boolean b) {

            }

            @Override
            public void onVideoFetch(ArrayList<VideoDTO> v) {
                list = v;
               // list= new ArrayList<VideoDTO>();
                VideosAdapter videosAdapter = new VideosAdapter(getActivity(),list);
                //  videosAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(videosAdapter);


            }
        });




        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity(),list.get(position).title,Toast.LENGTH_SHORT).show();
                Globals.selectedVideo=list.get(position);
                Intent intent=new Intent(context, PlayVideoActivity.class);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return view;

    }
}
