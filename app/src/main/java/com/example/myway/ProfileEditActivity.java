package com.example.myway;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.Nullable;

public class ProfileEditActivity extends AppCompatActivity {
    private ImageView userView;
    private EditText name_edit;
    private  static final int REQUEST_CODE = 0;
    private Bitmap your_bitmap;
    Integer MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        userView = findViewById(R.id.photo_edit);
        name_edit = findViewById(R.id.name_edit);

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
            userView.setImageBitmap(bm);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.profile_edit_cancel);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    String img_file = uri.toString();
                    Log.i("image 저장",uri.toString());
                    userView.setImageURI(uri);
                    Button btn = (Button) findViewById(R.id.profile_edit_btn);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Profile 변경 절차 시작
                            String userName = name_edit.getText().toString();
                            Log.i("userName 확인", userName);
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("OnResponce 성공", "OnResponce 성공");
                                    try {
                                        Log.i("값 받아옴", response + "Profile 확인 값 받아옴");
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        Log.d("profile_success 확인",String.valueOf(success));
                                        if (success) {
                                            Log.d("성공22","prifle 성공");


                                            SharedPreferences sharedPreferences03 = getSharedPreferences("sharedPreferences03",MODE_PRIVATE);
                                            SharedPreferences.Editor editor03 = sharedPreferences03.edit();
                                            editor03.clear();
                                            editor03.commit();
//                                            SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);
//                                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                                            editor.putString("userImage",img_file);
//                                            editor.commit();
                                            SharedPreferences sharedPreferences1 = getSharedPreferences("sharedPreferences03",MODE_PRIVATE);
                                            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                            editor1.putString("userName",userName);
                                            editor1.putString("userImage",img_file);
                                            editor1.commit();
                                            Log.i("userImage 내용 확인",img_file);
                                            Log.i("userName 내용 확인",userName);
                                            Intent intent = new Intent(ProfileEditActivity.this, MypageActivity.class);
                                            startActivity(intent);
                                            finish();//액티비티를 종료시킴(회원등록 창을 닫음)
                                            Toast.makeText(ProfileEditActivity.this, "프로필 변경에 성공하였습니다", Toast.LENGTH_SHORT).show();

//

                                        } else {
                                            Toast.makeText(getApplicationContext(), "프로필 변경에 실패했습니다. 다시 한 번 확인해 주세요.", Toast.LENGTH_SHORT).show();


                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            // Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                            ProfileRequest registerRequest = new ProfileRequest(img_file, userName,responseListener);
                            RequestQueue queue = Volley.newRequestQueue(ProfileEditActivity.this);
                            queue.add(registerRequest);

                        }

                    });
                }catch (Exception e){
                    Log.d("Profile","Profile 에러");
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 취소", Toast.LENGTH_SHORT).show();
            }
        }
    }
}