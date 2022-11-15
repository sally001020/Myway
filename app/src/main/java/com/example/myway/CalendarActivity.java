package com.example.myway;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
//    private EditText meditText_detail;
//    private EditText meditText_title;
//    private EditText meditText_place;
//    private EditText meditText_time;
//
//    private Button alarmBtn;
//    private NotificationHelper mNotificationhelper;
//    private String alarm_title;
//    private TextView diaryTextView;

    // 알람 시간
    private Calendar calendar;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

//        meditText_detail = (EditText) findViewById(R.id.textview_main_calendar_detail);
//        meditText_title = (EditText) findViewById(R.id.textview_main_calendar_title);
//        meditText_place = (EditText) findViewById(R.id.textview_main_calendar_place);
//        meditText_time = (EditText) findViewById(R.id.textview_main_calendar_time);
//        alarmBtn = findViewById(R.id.alarm_btn);
//        diaryTextView = findViewById(R.id.diaryTextView);

//        alarm_title = meditText_title.getText().toString();

//        Intent intent = getIntent();
//        final String date = intent.getExtras().getString("selectedDate");
//        diaryTextView.setText(date);

        this.calendar = Calendar.getInstance();
        displayDate(); // 현재 날짜 표시

        this.timePicker = findViewById(R.id.timePicker);

        findViewById(R.id.btnCalendar).setOnClickListener(mClickListener);
        findViewById(R.id.btnAlarm).setOnClickListener(mClickListener);

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

//        mNotificationhelper = new NotificationHelper(this);
//
//        alarmBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String title = meditText_title.getText().toString();
//                String message = meditText_detail.getText().toString();
//                sendOnChannel1(title, message);
//            }
//        });
    }

    /* 날짜 표시 */
    private void displayDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        ((TextView) findViewById(R.id.txtDate)).setText(format.format(this.calendar.getTime()));
    }

    /* DatePickerDialog 호출 */
    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // 알람 날짜 설정
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DATE, dayOfMonth);

                // 날짜 표시
                displayDate();
            }
        }, this.calendar.get(Calendar.YEAR), this.calendar.get(Calendar.MONTH), this.calendar.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    /* 알람 등록 */
    private void setAlarm() {
        // 알람 시간 설정
        this.calendar.set(Calendar.HOUR_OF_DAY, this.timePicker.getHour());
        this.calendar.set(Calendar.MINUTE, this.timePicker.getMinute());
        this.calendar.set(Calendar.SECOND, 0);

        if (this.calendar.before(Calendar.getInstance())) {
            Toast.makeText(this, "알람시간이 현재시간보다 이전일 수 없습니다.", Toast.LENGTH_LONG).show();
            return;
        }

        // Receiver 설정
        Intent intent = new Intent(this, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // 알람 설정
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, this.calendar.getTimeInMillis(), pendingIntent);

        // Toast 보여주기 (알람 시간 표시)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Toast.makeText(this, "Alarm : " + format.format(calendar.getTime()), Toast.LENGTH_LONG).show();
        Log.d("TAG", "Alarm : " + format.format(calendar.getTime()));
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCalendar:
                    // 달력
                    showDatePicker();

                    break;
                case R.id.btnAlarm:
                    // 알람 등록
                    setAlarm();

                    break;
            }
        }
    };
//
//    public void sendOnChannel1(String title, String message) {
//        NotificationCompat.Builder nb = mNotificationhelper.getChannel1Notification(title, message);
//        mNotificationhelper.getManager().notify(0, nb.build());
//    }
}