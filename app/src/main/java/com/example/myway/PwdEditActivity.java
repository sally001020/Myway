package com.example.myway;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.time.ZoneId;
import java.util.regex.Pattern;

public class PwdEditActivity extends AppCompatActivity {

    private EditText old_password;
    private EditText new_password;
    private EditText check_password;
    private static String old_pwd,new_pad,check_pwd;
    private  AlertDialog dialog;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_edit);

        old_password = (EditText) findViewById(R.id.old_pwd);
        new_password = (EditText) findViewById(R.id.new_pwd);
        check_password = (EditText) findViewById(R.id.new_pwd_again);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
        String userPassword = sharedPreferences.getString("userPassword","None");
        Log.d("MyPage Test",userPassword);

        ImageView imageView = (ImageView) findViewById(R.id.pwd_edit_cancel);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                startActivity(intent);
            }
        });

        Button btn = (Button) findViewById(R.id.pwd_edit_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("회원가입 버튼 누름", "회원가입 버튼 click 성공");
                old_pwd = old_password.getText().toString();
                new_pad = new_password.getText().toString();
                check_pwd = check_password.getText().toString();


                if (old_pwd.equals("") || new_pad.equals("") || check_pwd.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PwdEditActivity.this);
                    dialog = builder.setMessage("모두 입력해주세요").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }



                // 영문자 필수 + 숫자 필수 + 특수문자 필수 + 8 ~ 20자 이하
                if (!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$",new_pad)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(PwdEditActivity.this);
                    dialog = builder.setMessage("비밀번호 형식에 맞게 입력해주세요").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                if (!new_pad.equals(check_pwd)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PwdEditActivity.this);
                    dialog = builder.setMessage("비밀번호가 일치하지 않습니다.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                if (!old_pwd.equals(userPassword)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PwdEditActivity.this);
                    dialog = builder.setMessage("원래 비밀번호와 일치하지 않습니다.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }



                //로그인 변경 절차 시작
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("OnResponce 성공", "OnResponce 성공");
                        try {
                            // String으로 그냥 못 보냄으로 JSON Object 형태로 변형하여 전송
                            // 서버 통신하여 회원가입 성공 여부를 jsonResponse로 받음
                            Log.i("값 받아옴", response + "비밀번호 변경 값 받아옴");
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) { // 비밀번호 변경이 가능하다면
                                Toast.makeText(getApplicationContext(), "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userPassword",new_pad);
                                editor.commit();
                                Intent intent = new Intent(PwdEditActivity.this, MypageActivity.class);
                                startActivity(intent);
                                finish();//액티비티를 종료시킴(회원등록 창을 닫음)

                            } else {// 회원가입이 안된다면
                                Toast.makeText(getApplicationContext(), "비밀번호 변경에 실패했습니다. 다시 한 번 확인해 주세요.", Toast.LENGTH_SHORT).show();


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };


                // Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                PswChangeRequest registerRequest = new PswChangeRequest(old_pwd, new_pad ,responseListener);
                RequestQueue queue = Volley.newRequestQueue(PwdEditActivity.this);
                queue.add(registerRequest);

            }

        });
    }
}