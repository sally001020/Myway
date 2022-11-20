package com.example.myway.models;

import com.google.gson.annotations.SerializedName;

public class TrainInfo_data {
    @SerializedName("train_num") private int train_num;
    @SerializedName("train_name") private String train_name;

    public int getTrain_num() {return train_num;}

    public String getTrain_name() {return train_name;}

    public void setTrain_num(int train_num) {
        this.train_num = train_num;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }
}
