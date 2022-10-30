package com.example.myway;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private EditText meditText_detail;
    private EditText meditText_title;
    private EditText meditText_place;
    private EditText meditText_time;

    private Button alarmBtn;
    private NotificationHelper mNotificationhelper;
    private String alarm_title;
    private TextView diaryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        meditText_detail = (EditText) findViewById(R.id.textview_main_calendar_detail);
        meditText_title = (EditText) findViewById(R.id.textview_main_calendar_title);
//        meditText_place = (EditText) findViewById(R.id.textview_main_calendar_place);
//        meditText_time = (EditText) findViewById(R.id.textview_main_calendar_time);
        alarmBtn = findViewById(R.id.alarm_btn);
        diaryTextView = findViewById(R.id.diaryTextView);

        alarm_title = meditText_title.getText().toString();

        Intent intent = getIntent();
        final String date = intent.getExtras().getString("selectedDate");
        diaryTextView.setText(date);

//        final EditText meditText_time = (EditText) findViewById(R.id.textview_main_calendar_time);
//        meditText_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)+9;
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(CalendarActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selecteMinute) {
//                        String state = "AM";
//
//                        if (selectedHour > 12) {
//                            selectedHour -= 12;
//                            state = "PM";
//                        }
//                        meditText_time.setText(state+" " +selectedHour+"시"+selecteMinute+"분");
//                        meditText_time.setText(selectedHour+"시"+selecteMinute+"분");
//                    }
//                }, hour, minute, false);
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//            }
//        });

        mNotificationhelper = new NotificationHelper(this);

        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = meditText_title.getText().toString();
                String message = meditText_detail.getText().toString();
                sendOnChannel1(title, message);
            }
        });
    }

    public void sendOnChannel1(String title, String message) {
        NotificationCompat.Builder nb = mNotificationhelper.getChannel1Notification(title, message);
        mNotificationhelper.getManager().notify(0, nb.build());
    }

}