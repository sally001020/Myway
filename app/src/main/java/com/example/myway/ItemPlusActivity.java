package com.example.myway;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemPlusActivity extends AppCompatActivity {
    private TextView title_plus,data_plus,content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_notice_plus);

        title_plus = findViewById(R.id.title_plus);
        data_plus = findViewById(R.id.data_plus);
        content = findViewById(R.id.content);

        Intent intent = getIntent();
        String title = intent.getStringExtra("Title");
        String data = intent.getStringExtra("Write");
        String field1 = intent.getStringExtra("Field");

        title_plus.setText(title);
        data_plus.setText(data);
        content.setText(field1);

        ImageView imageView = (ImageView) findViewById(R.id.notice_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                startActivity(intent);
            }
        });

    }
}
