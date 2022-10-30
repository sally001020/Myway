package com.example.myway.models;



public class Data {
    private String title;
    private String write_date;
    private String field;


    public Data(String title, String write_date,String field) {
        this.title = title;
        this.write_date = write_date;
        this.field = field;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return write_date;
    }

    public void setDate(String write_date) {
        this.write_date = write_date;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }


}
