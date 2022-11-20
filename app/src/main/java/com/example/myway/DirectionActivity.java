package com.example.myway;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class DirectionActivity extends AppCompatActivity {
    Boolean showBtn = true;
    private String isJam = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_direction);

        //시간에 따른 혼잡도 표시
        String[] tt= intent.getStringArrayExtra("Tlist");//시간 받아오기
        String Timeset3 = tt[0];

        int Timeset4 = Integer.parseInt(Timeset3); //if문 위해 String->int로 타입 변환



        TextView tText =(TextView) findViewById(R.id.timeView2);
        //출퇴근시간 6-8/17-19시 사이면->혼잡 이외면 여유 반환
        if (((Timeset4>=6)&&(Timeset4<=8))||((Timeset4>=17)&&(Timeset4<=19))) {
            tText.setText("혼잡");
            isJam = "True";
            tText.setTextColor(Color.parseColor("#8e0000"));

        }else{
            tText.setText("여유");
            isJam = "False";
            tText.setTextColor(Color.parseColor("#43a047"));
        }


        Long now = System.currentTimeMillis(); //ms로 반환
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("a");
        Log.i("date SIMPLE",simpleDateFormat.format(now));
        Log.i("date A/M SIMPLE",simpleDateFormat1.format(now));
        TextView textView = (TextView) findViewById(R.id.textView4);
        textView.setText("오늘 " +simpleDateFormat1.format(now)+" "+simpleDateFormat.format(now) + " 출발");


        ImageView greenImg = (ImageView) findViewById(R.id.grenImg);

        //출발역 text 지정 추후 Edittext -> TextView로 변경 예정
        String[] ss = intent.getStringArrayExtra("Sstation");
        String Depart_station = ss[2];
        TextView sText = (TextView) findViewById(R.id.start_station_view);
        Log.i("Depart_station check",Depart_station);
        if (Depart_station.endsWith("역")) {
            sText.setText(Depart_station);
        } else {
            sText.setText(Depart_station+"역");
        }

        //도착역 text 지정 추후 Edittext -> TextView로 변경 예정
        String[] as = intent.getStringArrayExtra("Astation");
        String Arrival_station = as[2];
        TextView aText = (TextView) findViewById(R.id.arrival_station_view);
        if (Depart_station.endsWith("역")) {
            aText.setText(Arrival_station);
        } else {
            aText.setText(Arrival_station+"역");
        }

        TextView cenTxt = (TextView) findViewById(R.id.center_text01);
        if (Depart_station.endsWith("역")) {
            cenTxt.setText(Depart_station + " 승차");
        } else {
            cenTxt.setText(Depart_station+"역 승차");
        }

        TextView cenTxt02 = (TextView) findViewById(R.id.textView8);
        if (Depart_station.endsWith("역")) {
            cenTxt02.setText(Arrival_station + " 하차");
        } else {
            cenTxt02.setText(Arrival_station+"역 하차");
        }

        ////////////////////지하철 맵 생성
        HashMap<String, HashSet<String>> transferMap= new HashMap<String, HashSet<String>>();

        //출발역, 도착역 지정 - toString() 하지 않을시 오류 발생
        String depart = Depart_station.toString();
        String arrival = Arrival_station.toString();



        int c=0;
        try {
            if(isJam == "True"){ //혼잡할시
                Log.d("혼잡","혼잡 맞음");
                InputStream is = getResources().openRawResource(R.raw.congest);
                greenImg.setImageResource(R.drawable.reduser);


                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String s;
                boolean edgeOrVertex = true;

                ListGraph graph= new ListGraph();
                while ((s = in.readLine()) != null) {
                    if(s.equals("")){//start to save edge information.
                        edgeOrVertex = false;
                        continue;
                    }
                    if(edgeOrVertex){
                        String token[];
                        token = s.split(" ");//add Vertex
                        graph.addNode(token[0], token[1], token[2]);

                        if(transferMap.containsKey(token[1])){
                            transferMap.get(token[1]).add(token[0]);
                        }
                        else{
                            HashSet<String> set= new HashSet<String>();
                            set.add(token[0]);
                            transferMap.put(token[1],set);
                        }
                    }
                    else{
                        String token[];
                        token = s.split(" ");//add Edge
                        int t = Integer.parseInt(token[2]);
                        graph.addEdge(token[0], token[1], t);
                    }
                }
                in.close();
                is.close();

                Iterator<Entry<String, HashSet<String>>> it = transferMap.entrySet().iterator();
                while (it.hasNext()) {
                    HashSet<String> aSet = (HashSet<String>)it.next().getValue();
                    if(aSet.size()>1){
                        Iterator<String> iterator = aSet.iterator();
                        while(iterator.hasNext()){
                            String aNum = (String) iterator.next();
                            Iterator<String> iterator2 = aSet.iterator();
                            while(iterator2.hasNext()){
                                String anotherNum = (String) iterator2.next();
                                if (aNum.equals(anotherNum))
                                    continue;
                                graph.addEdge(aNum,anotherNum,5);
                            }
                        }
                    }
                }
                //
                //출발역, 도착역 노드 지정, 다익스트라 알고리즘 실행
                StationNode departNode=graph.findNode(depart);
                Dijkstra dijsktra = new Dijkstra(graph,departNode,arrival);
                dijsktra.disjkstra();

                int nntime = dijsktra.ntime;
                //중간 역들 저장 및 "," 기준으로 나누어 sts배열에 저장
                String need_stations = dijsktra.stations;
                String[] sts = need_stations.split(",");

                Log.i("nntime",String.valueOf(nntime));
                TextView nText = (TextView) findViewById(R.id.nTime2);
                nText.setText(String.valueOf(nntime)+"분");
                String today = null;
                Date now2 = new Date();
                SimpleDateFormat sdformat = new SimpleDateFormat(" a HH:mm");
                Calendar cal = Calendar.getInstance();
                cal.setTime(now2);
                cal.add(Calendar.MINUTE,nntime);
                today = sdformat.format(cal.getTime());
                TextView wText = (TextView) findViewById(R.id.will_time);
                wText.setText(today + "도착");


                //ArrayList 형식으로 중복된 역 제거 - 환승역이 두번씩 표기되는 문제 해결
                ArrayList<String> stList = new ArrayList<>();
                for (String item : sts){
                    if(!stList.contains(item))
                        stList.add(item);
                }
                stList.set(0, stList.get(0).replace("null",""));
                Collections.reverse(stList);

                //다시 배열 형식으로 바꾸고 맨 첫번째 역(reverse 했기에 맨 마지막 역이었던 것)에 null이 붙는 문제 해결
                //stList[-1] = stList[-1].replace("null","");
                String[] stData = stList.toArray(new String[stList.size()]);


                System.out.println(Arrays.toString(stData));

                TextView stTest = (TextView) findViewById(R.id.stations_view);

                stTest.setText(Arrays.toString(stData).replaceAll(",","\n"));



            }else if(isJam == "False"){ //여유일시
                Log.d("혼잡아님", "혼잡아님");
                InputStream is = getResources().openRawResource(R.raw.data);
                greenImg.setImageResource(R.drawable.greenuser);
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String s;
                boolean edgeOrVertex = true;

                ListGraph graph= new ListGraph();
                while ((s = in.readLine()) != null) {
                    if(s.equals("")){//start to save edge information.
                        edgeOrVertex = false;
                        continue;
                    }
                    if(edgeOrVertex){
                        String token[];
                        token = s.split(" ");//add Vertex
                        graph.addNode(token[0], token[1], token[2]);

                        if(transferMap.containsKey(token[1])){
                            transferMap.get(token[1]).add(token[0]);
                        }
                        else{
                            HashSet<String> set= new HashSet<String>();
                            set.add(token[0]);
                            transferMap.put(token[1],set);
                        }
                    }
                    else{
                        String token[];
                        token = s.split(" ");//add Edge
                        int t = Integer.parseInt(token[2]);
                        graph.addEdge(token[0], token[1], t);
                    }
                }
                in.close();
                is.close();

                Iterator<Entry<String, HashSet<String>>> it = transferMap.entrySet().iterator();
                while (it.hasNext()) {
                    HashSet<String> aSet = (HashSet<String>)it.next().getValue();
                    if(aSet.size()>1){
                        Iterator<String> iterator = aSet.iterator();
                        while(iterator.hasNext()){
                            String aNum = (String) iterator.next();
                            Iterator<String> iterator2 = aSet.iterator();
                            while(iterator2.hasNext()){
                                String anotherNum = (String) iterator2.next();
                                if (aNum.equals(anotherNum))
                                    continue;
                                graph.addEdge(aNum,anotherNum,5);
                            }
                        }
                    }
                }

                //
                //출발역, 도착역 노드 지정, 다익스트라 알고리즘 실행
                StationNode departNode=graph.findNode(depart);
                Dijkstra dijsktra = new Dijkstra(graph,departNode,arrival);
                dijsktra.disjkstra();

                int nntime = dijsktra.ntime;
                Log.i("nntime",String.valueOf(nntime));
                TextView nText = (TextView) findViewById(R.id.nTime2);
                nText.setText(String.valueOf(nntime)+"분");
                String today = null;
                Date now2 = new Date();
                SimpleDateFormat sdformat = new SimpleDateFormat(" a HH:mm");
                Calendar cal = Calendar.getInstance();
                cal.setTime(now2);
                cal.add(Calendar.MINUTE,nntime);
                today = sdformat.format(cal.getTime());
                TextView wText = (TextView) findViewById(R.id.will_time);
                wText.setText(today + "도착");


                //중간 역들 저장 및 "," 기준으로 나누어 sts배열에 저장
                String need_stations = dijsktra.stations;
                String[] sts = need_stations.split(",");

                //ArrayList 형식으로 중복된 역 제거 - 환승역이 두번씩 표기되는 문제 해결
                ArrayList<String> stList = new ArrayList<>();
                for (String item : sts){
                    if(!stList.contains(item))
                        stList.add(item);
                }
                stList.set(0, stList.get(0).replace("null",""));
                Collections.reverse(stList);

                //다시 배열 형식으로 바꾸고 맨 첫번째 역(reverse 했기에 맨 마지막 역이었던 것)에 null이 붙는 문제 해결
                //stList[-1] = stList[-1].replace("null","");
                String[] stData = stList.toArray(new String[stList.size()]);


                System.out.println(Arrays.toString(stData));

                TextView stTest = (TextView) findViewById(R.id.stations_view);

                stTest.setText(Arrays.toString(stData).replaceAll(",","\n"));


            }


        } catch (IOException e) {
            System.err.println(e); // 에러가 있다면 메시지 출력
            System.exit(1);
        }


        Button bButton = (Button) findViewById(R.id.BackButton);
        bButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent1 = new Intent(getApplicationContext(), ChatbotActivity.class);
                startActivity(intent1);
            }
        });

        Button hButton = (Button) findViewById(R.id.HButton);
        hButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent2);
            }
        });

    }
}