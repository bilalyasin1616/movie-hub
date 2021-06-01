package com.eg.moviehub.Fragments;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eg.moviehub.R;
import com.eg.moviehub.UploadVideoService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment {

    private Uri videoUri=null;
    private String mimeType;
    private static final int REQUEST_CODE=101;
    private StorageReference videoReference;
    private Context context;
    BetterVideoPlayer player;
    ImageView thumbImageView;
    Spinner spinner;
    EditText title;
    String[] spinnerArray;
    RadioGroup radioGroup;
    public UploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_upload, container, false);

        context=getActivity();

        title=view.findViewById(R.id.video_title_et);
        thumbImageView=view.findViewById((R.id.video_thumbnail));

        spinner=view.findViewById(R.id.select_genre_spinner);
        spinnerArray=getResources().getStringArray(R.array.restext);

        //ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(context,R.array.restext, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapter= new ArrayAdapter(context,R.layout.spinner_item,R.id.spinner_text_item,spinnerArray);
        spinner.setAdapter(adapter);

        radioGroup=view.findViewById(R.id.video_type);

        Button videoSelectBtn=view.findViewById(R.id.video_select_btn);
        videoSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });


        Button uploadBtn=view.findViewById(R.id.video_upload_btn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoUri!=null)
                {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Uploadinging your file")
                            .setContentText("You can track progress from notification bar")
                            .show();
                    Intent intent=new Intent(context, UploadVideoService.class);
                    ContentResolver cR = context.getContentResolver();
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    mimeType = mime.getExtensionFromMimeType(cR.getType(videoUri));
                    intent.putExtra("uri", videoUri.toString());
                    intent.putExtra("mimeType",mimeType);
                    intent.putExtra("title",title.getText().toString());
                    int id=radioGroup.getCheckedRadioButtonId();
                    String videoType;
                    if(R.id.radio_public==id)
                        videoType="Public";
                    else
                        videoType="Private";
                    intent.putExtra("videoType",videoType);
                    int index=spinner.getSelectedItemPosition();
                    intent.putExtra("genre",spinnerArray[index]);
                    context.startService(intent);
                }

            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode== Activity.RESULT_OK )
        {
            videoUri=data.getData();
            Glide.with(context)
                    .load(videoUri).thumbnail(0.1f)
                    .into(thumbImageView);
            //thumbImageView.setImageBitmap(getThumbFromVideoUri(videoUri));
        }
    }



    private Bitmap getThumbFromVideoUri(Uri uri)
    {
        String[] filePathColumn = {MediaStore.Video.VideoColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(picturePath, MediaStore.Video.Thumbnails.MINI_KIND);

        //Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail( uri.getPath() , MediaStore.Video.Thumbnails.MICRO_KIND );
        return bitmap;
    }


    public void selectFile()
    {
        Intent intent=new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_CODE);
    }


}
