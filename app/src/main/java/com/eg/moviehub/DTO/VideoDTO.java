package com.eg.moviehub.DTO;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by EG on 3/16/2019.
 */

public class VideoDTO implements Serializable{
    public String title;
    public String id;
    public  String genre;
    public String date_created;
    public  String uploaded_by;
    public String access_type;
    public  String thumbnail;
    ArrayList<CommentDTO> c = new ArrayList<CommentDTO>();

    public static String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "dd-MM-yyyy hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title",title);
        result.put("genre",genre);
        result.put("date_created",getCurrentTimeUsingDate());
        result.put("uploaded_by",uploaded_by);
        result.put("access_type",access_type);
        result.put("thumbnail",thumbnail);
        result.put("Comments",c);

        return result;
    }
}
