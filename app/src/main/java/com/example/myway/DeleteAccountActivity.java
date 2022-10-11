package com.example.myway;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.regex.Pattern;

public class DeleteAccountActivity extends AppCompatActivity {
    private  AlertDialog dialog;
    private EditText check_password_delete;
    private static String old_pwd_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        check_password_delete = (EditText) findViewById(R.id.editTextTextPassword);

        ImageView imageView = (ImageView) findViewById(R.id.delete_account_cancel);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
        String userPassword = sharedPreferences.getString("userPassword","None");
        Log.d("MyPage Test",userPassword);


        Button btn = (Button) findViewById(R.id.delete_account_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("삭제 버튼 누름", "삭제 버튼 click 성공");
                old_pwd_right = check_password_delete.getText().toString();


                if (!old_pwd_right.equals(userPassword)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DeleteAccountActivity.this);
                    dialog = builder.setMessage("원래 비밀번호와 일치하지 않습니다.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }



                //Delete 변경 절차 시작
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("OnResponce 성공", "OnResponce 성공");
                        try {
                            // String으로 그냥 못 보냄으로 JSON Object 형태로 변형하여 전송
                            // 서버 통신하여 회원가입 성공 여부를 jsonResponse로 받음
                            Log.i("값 받아옴", response + "Delete 확인 값 받아옴");
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) { // 탈퇴가 가능하다면
                                Toast.makeText(getApplicationContext(), "탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();//액티비티를 종료시킴(회원등록 창을 닫음)

                            } else {// 회원가입이 안된다면
                                Toast.makeText(getApplicationContext(), "탈퇴에 실패했습니다. 다시 한 번 확인해 주세요.", Toast.LENGTH_SHORT).show();


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };


                // Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
                DeleteRequest registerRequest = new DeleteRequest( old_pwd_right,responseListener);
                RequestQueue queue = Volley.newRequestQueue(DeleteAccountActivity.this);
                queue.add(registerRequest);

            }

        });
    }
}