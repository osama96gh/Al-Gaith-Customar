package com.example.al_gaith_customar.Data;

public class ApplicationData {
    public int field_id;
    public String value = "";
    public String field_type = "";
    public String field_name;

    public ApplicationData(int field_id, String type) {
        this.field_id = field_id;
        this.field_type = type;
    }

    public ApplicationData(int field_id, String type, String value) {
        this.field_id = field_id;
        this.field_type = type;
    }
}