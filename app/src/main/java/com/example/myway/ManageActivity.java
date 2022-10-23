package com.example.myway;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;

public class ManageActivity extends AppCompatActivity {
    private Button btnRead;
    private EditText etRaw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        setTitle("호선 정보 수정");

        init();
        initLr();
    }
    public void init(){
        btnRead = findViewById(R.id.ReadButton);
        etRaw = findViewById(R.id.dataview);
    }

    public void initLr(){
        btnRead.setOnClickListener(v -> {
            try{
                InputStream inputStream = getResources().openRawResource(R.raw.data);
                byte[] txt = new byte[inputStream.available()];
                inputStream.read(txt);
                etRaw.setText(new String(txt));
                inputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }
}