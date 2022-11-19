package com.example.myway;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    private String TAG = this.getClass().getSimpleName();

    NotificationManager manager;
    NotificationCompat.Builder builder;

    //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";

    //수신되는 인텐트 - The Intent being received.
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e(TAG, "onReceive 알람이 들어옴!!");

        String titleValue = intent.getStringExtra("title");
        Log.e(TAG, "onReceive titleValue값 확인 : " + titleValue);

        String detailValue = intent.getStringExtra("detail");
        Log.e(TAG, "onReceive detailValue값 확인 : " + detailValue);

        builder = null;

        //푸시 알림을 보내기위해 시스템에 권한을 요청하여 생성
        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //안드로이드 오레오 버전 대응
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        //알림창 클릭 시 지정된 activity 화면으로 이동
        Intent intent2 = new Intent(context, ChatbotActivity.class);

        // FLAG_UPDATE_CURRENT ->
        // 설명된 PendingIntent가 이미 존재하는 경우 유지하되, 추가 데이터를 이 새 Intent에 있는 것으로 대체함을 나타내는 플래그입니다.
        // getActivity, getBroadcast 및 getService와 함께 사용
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,101,intent2,
//                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,101,intent2,
                PendingIntent.FLAG_IMMUTABLE);

//        Intent notificationIntent = new Intent(context, ChatbotActivity.class);
//        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        //알림창 제목
        builder.setContentTitle(titleValue); //회의명노출
        //builder.setContentText(intent.getStringExtra("content")); //회의 내용
        //알림창 아이콘
        builder.setSmallIcon(R.drawable.mywaylogo);
        //알림창 터치시 자동 삭제
        builder.setAutoCancel(true);

        builder.setContentIntent(pendingIntent);

        builder.setContentText(detailValue);

//        builder.setContentIntent(intent2);

        //푸시알림 빌드
        Notification notification = builder.build();

        //NotificationManager를 이용하여 푸시 알림 보내기
        manager.notify(1,notification);
    }
}
