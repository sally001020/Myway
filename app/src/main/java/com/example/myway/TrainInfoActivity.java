package com.example.myway;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class TrainInfoActivity extends AppCompatActivity {
    private Context mContext = TrainInfoActivity.this;

    EditText TrainName;
    TextView trainview;
    TextView stationNameView;

    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subway_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("");
        toolbar.setSubtitle("");

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        findViewById(R.id.bars).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        NavigationViewHelper.enableNavigation(mContext,navigationView);

        Menu menu = navigationView.getMenu();
        MenuItem nav_name = menu.findItem(R.id.nav_name);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct!=null){
            String personName = acct.getDisplayName();
            nav_name.setTitle(personName);
        }

        ImageView backBtn = (ImageView) findViewById(R.id.rignt_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrainInfoActivity.super.onBackPressed();
            }
        });

        TrainName = (EditText) findViewById(R.id.TrainName);
        trainview = (TextView) findViewById(R.id.trainview);
        stationNameView = (TextView) findViewById(R.id.stationNameView);
    }

    public void tOnClick(View v) {
        switch (v.getId()) {
            case R.id.TrainButton:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        data = getXmlData();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                trainview.setText(data);
                            }
                        });
                    }
                }).start();

                break;
        }
    }

    String getXmlData() {
        StringBuffer buffer = new StringBuffer();

        String str = TrainName.getText().toString();
        String location  = "";

        try {
            location = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        StringBuilder urlBuilder = new StringBuilder("http://swopenAPI.seoul.go.kr/api/subway/");
        String queryUrl = "http://swopenapi.seoul.go.kr/api/subway/6259474d5573616c3131335862467179/xml/realtimeStationArrival/1/2/" + location;

        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱시작\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if (tag.equals("row")) ;
                        else if (tag.equals("updnLine")) {
                            buffer.append("상행 하행 여부: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }else if (tag.equals("trainLineNm")) {
                            buffer.append("도착지 방면: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("statnNm")) {
                            xpp.next();
                            stationNameView.setText(xpp.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();

                        if (tag.equals("row")) buffer.append("\n");

                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {

        }
        buffer.append("파싱 끝");
        return buffer.toString();
    }

}