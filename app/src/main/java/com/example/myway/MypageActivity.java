package com.example.myway;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


public class MypageActivity extends AppCompatActivity {

    TextView name;
    ImageView userBox;
    Integer MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_layout);
        name = findViewById(R.id.name_view);
        userBox = findViewById(R.id.notice_back);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("");
        toolbar.setSubtitle("");

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences03",MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName","None");
        Log.i("userName Check 필요",userName);
        name.setText(userName);



        SharedPreferences sharedPreferences2 = getSharedPreferences("sharedPreferences03",MODE_PRIVATE);
        String userImage = sharedPreferences2.getString("userImage","None");
        Log.d("MyPage userImage Test",userImage);

        Uri uri_img = Uri.parse(userImage);
        Log.d("Mypage Test uri", uri_img.toString());

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            return;
        }

        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_img);
            userBox.setImageBitmap(bm);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        int permission = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int permission2 = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
//
//        if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED) {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},1000
//                );
//            }
//            return;
//        }

        TextView profiledit = (TextView) findViewById(R.id.profile_edit);
        profiledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileEditActivity.class);
                startActivity(intent);
            }
        });


        TextView pwdedit = (TextView) findViewById(R.id.pwd_edit);
        pwdedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PwdEditActivity.class);
                startActivity(intent);
            }
        });

        TextView deleteaccount = (TextView) findViewById(R.id.delete_account);
        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DeleteAccountActivity.class);
                startActivity(intent);
            }
        });

        TextView btnLogout = (TextView) findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        TextView notice = (TextView) findViewById(R.id.notice);
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                startActivity(intent);
            }
        });


    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grandResults);
//        if (requestCode == 1000) {
//            boolean check_result = true;
//
//
//            for (int result : grandResults) {
//                if (result != PackageManager.PERMISSION_GRANTED) {
//                    check_result = false;
//                    break;
//                }
//            }
//
//            if (check_result == true) {
//
//            } else {
//                finish();
//            }
//        }
//    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu ) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            super.onBackPressed();
            startActivity(new Intent(MypageActivity.this,MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

