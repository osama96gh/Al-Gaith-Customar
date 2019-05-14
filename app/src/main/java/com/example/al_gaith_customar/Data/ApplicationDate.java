package com.example.al_gaith_customar.Data;

public class ApplicationDate {
    public int id;
    public String app_name;
    public String review_date;
    public String review_status;
    String created_at;

    public boolean isDateHaveReply() {
        if (review_status.matches("read")) {
            return false;
        } else {
            return true;
        }
    }
}
