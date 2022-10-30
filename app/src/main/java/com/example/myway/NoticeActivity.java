package com.example.myway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.myway.adapters.Adapter;
import com.example.myway.models.Data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity {
    private Context mContext;
    private ArrayList<Data> mArrayList;
    private Adapter mAdapter;
    private RecyclerView mrecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        mContext = getApplicationContext();
        mrecycleView = findViewById(R.id.notice_list);


        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        mrecycleView.setLayoutManager(layoutManager);

        mArrayList = new ArrayList<>();
        mAdapter = new Adapter(mContext,mArrayList);
        mrecycleView.setAdapter(mAdapter);



        ImageView imageView = (ImageView) findViewById(R.id.notice_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                startActivity(intent);
            }
        });

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    Log.i("JsonObject 오류 확인",String.valueOf(jsonArray));
                    for (int i =0; i <jsonArray.length(); i++) {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        boolean success = jsonobject.getBoolean("success");

                        if(success){
                            int length = jsonArray.length();
                            String len = String.valueOf(length);
                            Log.i("Notice 개채 길이",len);
                            String board_num = jsonobject.get("board_num").toString();
                            String writer = jsonobject.get("writer").toString();
                            String title = jsonobject.get("title").toString();
                            String field = jsonobject.get("field").toString();
                            String write_date = jsonobject.get("write_date").toString();
                            Log.i("Notice Title 길이",board_num +" " + title+" " + writer + " " + field + " " + write_date);

                            Data data = new Data(title,write_date,field);
                            Log.i("Data notice 확인",data.getDate().toString());
                            mArrayList.add(data);
                            mAdapter.notifyItemInserted(mArrayList.size() -1);

                            mAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    String title = mArrayList.get(position).getTitle();
                                    String write_date = mArrayList.get(position).getDate();
                                    String field = mArrayList.get(position).getField();
                                    Toast.makeText(mContext, "title : " +title + "\ndate : " + write_date, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(NoticeActivity.this,ItemPlusActivity.class);
                                    intent.putExtra("Title",title);
                                    intent.putExtra("Write",write_date);
                                    intent.putExtra("Field",field);
                                    startActivity(intent);

                                }
                            });


                        } else {
                            Log.i("데이터..","데이터를 불러오는 것을 실패하였습니다");
                            return;
                        }
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error 발생 Notice",error.toString());
                return;
            }
        };

        NoticeRequest noticeRequest = new NoticeRequest(responseListener,errorListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(noticeRequest);

    }
}