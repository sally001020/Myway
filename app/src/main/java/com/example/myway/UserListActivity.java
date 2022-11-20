package com.example.myway;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        Intent intent = getIntent();
        String admin = intent.getStringExtra("userAdmin");


        Button button = (Button) findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(UserListActivity.this,MainActivity.class);
                startActivity(intent1);
            }
        });

        Button UserBtn = (Button) findViewById(R.id.userBtn);
        UserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(UserListActivity.this,UserListPlusActivity.class);
                startActivity(intent2);
            }
        });


    }
}
