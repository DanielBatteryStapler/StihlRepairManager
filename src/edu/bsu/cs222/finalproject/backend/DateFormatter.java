package edu.bsu.cs222.finalproject.backend;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class DateFormatter {
    public static String formatDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mma");
        return dateFormat.format(date);
    }
}
