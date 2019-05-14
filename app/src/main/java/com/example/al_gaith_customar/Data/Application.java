package com.example.al_gaith_customar.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Application {
    public int id, sequence;
    public String app_name, description, status, review_date, response, created_at, read_status, admin_response;

    public Date getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(created_at);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public boolean isAppHaveReply() {
        if (read_status.matches("read")) {
            return false;
        } else {
            return true;
        }
    }
}
