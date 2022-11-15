package com.example.myway;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        Log.d("TAG", "## 알람 작동 ##");

        AlertDialog.Builder myAlertBuilder =
                new AlertDialog.Builder(AlarmActivity.this);
        myAlertBuilder.setTitle("알림");
        myAlertBuilder.setMessage("지금입니다!");
        // 버튼 추가 (Ok 버튼)
        myAlertBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){}
        });
        // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성)
        myAlertBuilder.show();
    }
}