package com.example.myway;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private TextView txt_startTime; //알람시간
    private ImageButton imgBtn_changeTime; //알람변경 버튼
    private EditText edit_title,edit_detail;
    private ImageView right_back;

    //현재 시간,분 변수선언
    int currHour, currMinute;

    //시스템에서 알람 서비스를 제공하도록 도와주는 클래스
    //특정 시점에 알람이 울리도록 도와준다
    private AlarmManager alarmManager;

    private TimePickerDialog.OnTimeSetListener timeCallbackMethod; //알람시간 변경선택 타임피커 콜백처리 변수 선언

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        txt_startTime = (TextView) findViewById(R.id.txt_startTime);
        imgBtn_changeTime =(ImageButton) findViewById(R.id.imgBtn_changeTime);
        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_detail = (EditText) findViewById(R.id.edit_detail);
        right_back = (ImageView) findViewById(R.id.right_back);

        //현재 시간기준으로 몇시 몇분인지 구하기
        LocalTime now = LocalTime.now();
        currHour = now.getHour();
        currMinute = now.getMinute();

        //푸시알림을 보내기 위해, 시스템에서 알림 서비스를 생성해주는 코드
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        //tiem picker dialog 리스너 등록
        this.timeInitializeListener();

        AlertDialog.Builder myAlertBuilder =
                new AlertDialog.Builder(CalendarActivity.this);
        myAlertBuilder.setTitle("사용법 알림");
        myAlertBuilder.setMessage("1. 일정 제목, 세부사항을 입력하고 \n2. '+'를 클릭하여 시간 설정을 하세요!");
        // 버튼 추가 (Ok 버튼)
        myAlertBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){}
        });
        // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성)
        myAlertBuilder.show();

        right_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarMainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //알람시간 변경 선택
        imgBtn_changeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //스피너모드 타임피커
                TimePickerDialog dialog = new TimePickerDialog(
                        CalendarActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        timeCallbackMethod, currHour, currMinute, false);

                //다이알로그 타이틀 설정
                dialog.setTitle("알람 시간 설정");

                //기존테마의 배경을 없앤다
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });
    }

    //timePickerDialog에서 선택한 시간정보를 처리하는 이벤트
    public void timeInitializeListener() {
        timeCallbackMethod = new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.e(TAG, "onTimeSet: 시간 " + hourOfDay + ", 분 " + minute );
                Toast.makeText(getApplicationContext(), "알림이 설정되었습니다.", Toast.LENGTH_LONG).show();

                //변경된 시간으로 textview 업데이트
                txt_startTime.setText(formatTime(hourOfDay+":"+minute));

                //알람 등록 처리
                setNotice(hourOfDay + ":" + minute + ":" + "00");

                Intent intents = new Intent(getApplicationContext(), CalendarMainActivity.class);
                startActivity(intents);
            }
        };
    }

    //날짜 포맷 변환
    // HH:mm => 오전/오후 hh:mm
    public static String formatTime(String timeValue) {
        DateFormat reqDateFormat = new SimpleDateFormat("HH:mm");
        DateFormat resDateFormat = new SimpleDateFormat("a hh:mm", Locale.KOREAN);
        Date datetime = null;

        try {
            //문자열을 파싱해서 Date객체를 만들어준다
            datetime = reqDateFormat.parse(timeValue);
        } catch (ParseException e) {
            //패턴과다른 문자열이 입력되면 parse exception이 발생된다
            e.printStackTrace();
        }
        return resDateFormat.format(datetime);
    }

    //알람매니저에 알람등록 처리
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setNotice(String alarmTimeValue) {
        //알람을 수신할 수 있도록 하는 리시버로 인텐트 요청
        Intent receiverIntent = new Intent(this, NotificationReceiver.class);
        receiverIntent.putExtra("title", edit_title.getText().toString());
        receiverIntent.putExtra("detail", edit_detail.getText().toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 123, receiverIntent, PendingIntent.FLAG_IMMUTABLE);

        //등록한 알람날짜 포맷을 밀리초로 변경한기 위한 코드
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LocalDate now = LocalDate.now(); //현재시간 구하기
        Date datetime = null;

        try {
            datetime = dateFormat.parse(now + " " + alarmTimeValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //date타입으로 변경된 알람시간을 캘린더 타임에 등록
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        //알람시간 설정
        //param 1)알람의 타입
        //param 2)알람이 울려야 하는 시간(밀리초)을 나타낸다.
        //param 3)알람이 울릴 때 수행할 작업을 나타냄
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }
}