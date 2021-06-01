package com.eg.moviehub.DTO;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Memona Sultan on 3/16/2019.
 */



public class CommentDTO implements Serializable {
    public String uid;
    public String text;
    public String time;

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
        result.put("uid",uid);
        result.put("text",text);
        result.put("time",getCurrentTimeUsingDate());

        return result;
    }
}
