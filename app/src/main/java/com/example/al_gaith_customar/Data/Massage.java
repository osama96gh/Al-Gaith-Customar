package com.example.al_gaith_customar.Data;

public class Massage {

    public int id;
    public String message_type;
    public String message;
    public String created_at;
    public String status;

    public boolean isMassageHaveReply() {
        if (status.matches("unread")) {
            return true;
        } else {
            return false;
        }
    }

    public String getMassageType() {
        if (message_type.matches("complain")) {
            return "شكوى";
        } else if (message_type.matches("inquiry")) {
            return "استفسار";
        } else if (message_type.matches("suggestion")) {
            return "اقتراح";
        }
        return null;
    }
}
