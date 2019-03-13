package com.example.adeng.jsongetandcreate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class configuration extends AppCompatActivity {
    private GridView allIconView;
    private GridView currentIconView;
    private List<Map<String,Object>> dataList;
    private List<Map<String,Object>> currentDataList;
    private SimpleAdapter allIconAdapter;
    private SimpleAdapter currentIconAdapter;

    private StringBuilder builder;
    private String TAG="------------------";
    private List<String> allPicList;
    private List<String> allTypeList;
    private Button configuration;
    private Boolean on_off=false;
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.configuration);
        initView();
        initAllIconGirdView(allIconView);
        getCurrentIconData();
        setAllClickListen();
    }

    private void initView() {
        allIconView =findViewById(R.id.allIcon);
        currentIconView =findViewById(R.id.currentIcon);
        configuration=findViewById(R.id.configuration);
    }

    //获取正在使用图标数据
    private void getCurrentIconData() {
        SharedPreferences getData = getSharedPreferences("SP_Data_List",Activity.MODE_PRIVATE);//创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
        String currentIconJson = getData.getString("CURRENT","NULL");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        if (currentIconJson.equals("NULL")){
            currentDataList=new ArrayList<> (dataList);
            Log.i(TAG, "getCurrentIconData:11123456 "+currentDataList.toString());
            currentIconView=initCurrentIconGirdView(currentIconView,currentDataList);

        }else{
            try{
                JSONObject jsonObject=new JSONObject(currentIconJson);
                Log.i(TAG, "getCurrentIconData: 11123456"+jsonObject.toString());
                currentDataList=Utility.JsonObject2ListMap(jsonObject,"data");
                Log.i(TAG, "getCurrentIconData: *********"+ currentDataList.toString());
                currentIconView=initCurrentIconGirdView(currentIconView,currentDataList);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    //适配器的List<Map<String,Object>>参数
    private List<Map<String,Object>> initData(List<Map<String,Object>> list) {
        allPicList=new ArrayList<>();
        allTypeList=new ArrayList<>();
        try{
            InputStream inputStream=this.getClass().getClassLoader().getResourceAsStream("assets/" + "Info.json");
            BufferedReader bufr = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            builder = new StringBuilder();
            //当line=bufr.readLine并且不为空
            while ((line = bufr.readLine()) != null) {
                builder.append(line);
            }
            inputStream.close();
            bufr.close();
            JSONObject jsonObject=new JSONObject(builder.toString());
            JSONArray jsonArray=jsonObject.getJSONArray("infoAndPicture");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject info=jsonArray.getJSONObject(i);
                allTypeList.add(i,info.getString("type"));
                allPicList.add(i,info.getString("pic"));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        list = new ArrayList<>();
        for (int i=0;i<allTypeList.size();i++){
            Map<String,Object> map=new HashMap<>();
            map.put("type",allTypeList.get(i));
            map.put("pic", Utility.String2Picture(allPicList.get(i)));
            list.add(map);
        }

        return list;
    }

    //初始化全部图标GridView
    private GridView initAllIconGirdView(GridView gridView) {
        dataList=initData(dataList);
        String[] from={"type","pic"};
        int[] to={R.id.text,R.id.img};
        allIconAdapter=new SimpleAdapter(this,dataList,R.layout.grid_item_add,from,to);
        gridView.setAdapter(  allIconAdapter);
        return gridView;
    }

    //初始化正在使用图标GirdView
    private GridView initCurrentIconGirdView(GridView gridView,List<Map<String,Object>> list) {
        String[] from={"type","pic"};
        int[] to={R.id.text,R.id.img};
        currentIconAdapter=new SimpleAdapter(this,list,R.layout.grid_item_minus,from,to);
        gridView.setAdapter(currentIconAdapter);
//        gridView.getChildAt(0).toString()
        return gridView;
    }

    //设置监听
    private void setAllClickListen() {
        allIconView=initAllIconGirdView(allIconView);
//        //View加载监听
//        allIconView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                for(int i=0;i<currentDataList.size();i++){
//                    for (int j=0;j<dataList.size();j++){
//                        if(currentDataList.get(i).get("type").equals(dataList.get(j).get("type"))){
//                            Log.i(TAG, "onGlobalLayout: "+"12345678910");
//                            Log.i(TAG, "onGlobalLayout: "+currentDataList.get(i).get("type").toString());
//                            Log.i(TAG, "onGlobalLayout: "+dataList.get(j).get("type").toString());
//                            allIconView.getChildAt(j).findViewById(R.id.add).setVisibility(View.INVISIBLE);
//                            allIconView.getChildAt(j).findViewById(R.id.minus).setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
//            }
//        });
        //全部图标监听
        allIconView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(on_off==true){
                    ImageView add=view.findViewById(R.id.add);
                    add.setVisibility(View.INVISIBLE);
                    ImageView minus=view.findViewById(R.id.minus);
                    minus.setVisibility(View.VISIBLE);
                    Boolean exist=false;
                    for(int i=0;i<currentDataList.size();i++){
                        if(currentDataList.get(i).get("type").equals(dataList.get(position).get("type"))){
                            exist=true;
                            break;
                        }
                    }
                    if (exist==false){
                        currentDataList.add(dataList.get(position));
                    }
                    currentIconAdapter.notifyDataSetChanged();
                }

            }
        });
        //正在使用图标加载完成监听
        currentIconView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(on_off==true){
                    for (int i=0;i<currentDataList.size();i++){
                        currentIconView.getChildAt(i).findViewById(R.id.minus).setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        //正在使用图标点击监听
        currentIconView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                 AlertDialog.Builder builder= new AlertDialog.Builder(configuration.this);
//                 builder.setTitle("提示").setMessage(dataList.get(position).get("type").toString()).create().show();
               if (on_off==true){
                   JSONObject jsonObject=new JSONObject();
                   try{
                       jsonObject.put("InfoAndPicture",dataList);
                   }
                   catch (JSONException e){
                       e.printStackTrace();
                   }
                   for(int i=0;i<dataList.size();i++){
                       if(currentDataList.get(position).equals(dataList.get(i))){
                           allIconView.getChildAt(i).findViewById(R.id.minus).setVisibility(View.INVISIBLE);
                           allIconView.getChildAt(i).findViewById(R.id.add).setVisibility(View.VISIBLE);
                       }
                   }
                   currentDataList.remove(position);
                   currentIconAdapter.notifyDataSetChanged();
               }
            }
        });

        //配置按钮监听
        configuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (on_off==false){
                    on_off=true;
                    configuration.setText("完成");

                    for(int i=0;i<currentDataList.size();i++){
                        currentIconView.getChildAt(i).findViewById(R.id.minus).setVisibility(View.VISIBLE);
                    }
                    for(int i=0;i<dataList.size();i++){
                        allIconView.getChildAt(i).findViewById(R.id.add).setVisibility(View.VISIBLE);
                    }
                    for(int i=0;i<currentDataList.size();i++){
                        for (int j=0;j<dataList.size();j++){
                            if(currentDataList.get(i).get("type").equals(dataList.get(j).get("type"))){
                                allIconView.getChildAt(j).findViewById(R.id.add).setVisibility(View.INVISIBLE);
                                allIconView.getChildAt(j).findViewById(R.id.minus).setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }else{
                    for(int i=0;i<dataList.size();i++){
                        allIconView.getChildAt(i).findViewById(R.id.minus).setVisibility(View.INVISIBLE);
                        allIconView.getChildAt(i).findViewById(R.id.add).setVisibility(View.INVISIBLE);
                    }
                    for(int i=0;i<currentDataList.size();i++){
                        currentIconView.getChildAt(i).findViewById(R.id.minus).setVisibility(View.INVISIBLE);
                    }
                    AlertDialog.Builder builder=new AlertDialog.Builder(configuration.this);
                    JSONArray jsonArray=new JSONArray();
                    JSONObject iconData=new JSONObject();
                    JSONObject jsonObject=null;
                    try{
                        for(int i = 0; i < currentDataList.size(); i++)
                        {
                            jsonObject = new JSONObject();
                            jsonObject.put("type" ,currentDataList.get(i).get("type"));
                            jsonObject.put("pic", currentDataList.get(i).get("pic"));
                            jsonArray.put(jsonObject);
                            jsonObject = null;
                        }
                        iconData.put("data",jsonArray);
                        Log.i(TAG, "12344onClick: "+iconData.toString());
                        SharedPreferences sp = getSharedPreferences("SP_Data_List", Activity.MODE_PRIVATE);//创建sp对象
                        SharedPreferences.Editor editor = sp.edit() ;
                        editor.clear();
                        editor.putString("CURRENT",iconData.toString() ) ; //存入json串
                        editor.commit() ;//提交
                        Log.i(TAG, "111222onClick: "+iconData.toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    configuration.setText("配置");
                    on_off=false;
                }
            }
        });
    }


}
