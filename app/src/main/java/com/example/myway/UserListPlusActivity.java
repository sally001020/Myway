package com.example.myway;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.myway.adapters.Adapter;
import com.example.myway.adapters.UserListAdapter;
import com.example.myway.models.Data;
import com.example.myway.models.UserListInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserListPlusActivity extends AppCompatActivity {
    private Context mContext;
    private ArrayList<UserListInfo> mUserArrayList;
    private UserListAdapter mListAdapter;
    private RecyclerView mListRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist_plus);

        mContext = getApplicationContext();
        mListRecycleView = findViewById(R.id.userList_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        mListRecycleView.setLayoutManager(layoutManager);

        mUserArrayList = new ArrayList<>();
        mListAdapter = new UserListAdapter(mContext,mUserArrayList);
        mListRecycleView.setAdapter(mListAdapter);



        ImageView imageView = (ImageView) findViewById(R.id.notice_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
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
                            Log.i("UserList 개채 길이",len);
                            String user_id = jsonobject.get("user_id").toString();
                            String userEmail = jsonobject.get("userEmail").toString();
                            String userPassword = jsonobject.get("userPassword").toString();
                            String userName = jsonobject.get("userName").toString();
                            Log.i("UserList Title 길이",user_id +" " + userEmail+" " + userPassword + " " + userName);

                            UserListInfo userDate = new UserListInfo(user_id,userEmail,userPassword,userName);
                            Log.i("Data UserList 확인",userDate.getUser_id().toString());
                            mUserArrayList.add(userDate);
                            mListAdapter.notifyItemInserted(mUserArrayList.size() -1);

                            mListAdapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    String name = mUserArrayList.get(position).getUserName();
                                    Toast.makeText(UserListPlusActivity.this, name + " was clicked!", Toast.LENGTH_SHORT).show();
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

        UserListPlusRequest userListPlusRequest = new UserListPlusRequest(responseListener,errorListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(userListPlusRequest);


    }
    public void click(View view) {
        Toast.makeText(mContext, "버튼: dropBtn 클릭" , Toast.LENGTH_SHORT).show();
    }

}
