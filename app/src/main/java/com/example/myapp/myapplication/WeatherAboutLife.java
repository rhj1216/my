package com.example.myapp.myapplication;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WeatherAboutLife extends AppCompatActivity {
    private String TAG="----------------------------------------------";
    private TextView lifeIndex;
    private TextView dressIndex;
    private TextView fluIndex;
    private TextView sportIndex;
    private TextView travelIndex;
    private TextView ultravioletIndex;
    private TextView washIndex;
    private TextView airIndex;
    
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weaheraboutlife);
        lifeIndex=findViewById(R.id.lifeIndex);
        dressIndex=findViewById(R.id.dressIndex);
        fluIndex=findViewById(R.id.fluIndex);
        sportIndex=findViewById(R.id.sportIndex);
        travelIndex=findViewById(R.id.travelIndex);
        ultravioletIndex=findViewById(R.id.ultravioletIndex);
        washIndex=findViewById(R.id.washIndex);
        airIndex=findViewById(R.id.airIndex);
        getMapData();
    }
    public void getMapData(){
        //数值键值对
        Map<Integer,String[]> lifeIndexMap = (Map<Integer, String[]>) getIntent().getSerializableExtra("lifeIndexMap");
        Log.i(TAG, "getMapData: "+lifeIndexMap.size());
        Map<Integer,TextView> lifeIndexView=new HashMap<Integer, TextView>(); //视图键值对
        TextView[] textViewArray={lifeIndex, dressIndex, fluIndex,sportIndex,travelIndex,ultravioletIndex
        ,washIndex, airIndex};//构建TextView数组
        //把TextView数组添加进lifeIndexView
        for(int i=0;i<=7;i++){
            lifeIndexView.put(i,textViewArray[i]);
//            Log.i(TAG, "getMapData: 成功");
        }
        for(int i=0;i<=7;i++){
//            Log.i(TAG, "getMapData: "+Arrays.toString(lifeIndexMap.get(i)));
            String[] temp=lifeIndexMap.get(i);//获取数组
            lifeIndexView.get(i).setText(changeMessage(temp[2])+"  "+ temp[0]+"\n"+temp[1]);
         }
    }
    public String changeMessage(String message){
        if(message.equals("comf")){
            return "舒适指数";
        }else if(message.equals("drsg")){
            return "穿衣指数";
        }
        else if(message.equals("flu")){
            return "感冒指数";
        }
        else if(message.equals("sport")){
            return "运动指数";
        }
        else if(message.equals("trav")){
            return "旅游指数";
        }
        else if(message.equals("uv")){
            return "紫外线指数";
        }
        else if(message.equals("cw")){
            return "洗车指数";
        }
        else {
            return "空气污染扩散条件指数";
        }
    }
}
