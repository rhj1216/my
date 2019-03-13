package com.example.myapp.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.myapp.myapplication.dataShow.SetData;
import com.example.myapp.myapplication.logistics.Logistics;
import com.example.myapp.myapplication.map.MapService;
import com.example.myapp.myapplication.weather.Weather;

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

public class HomeContent extends Fragment {
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    private  String TAG="123";
    private  String  iconData;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home_content,container,false);
        gridView=view.findViewById(R.id.gridView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showIcon();

    }
    /*
     SimpleAdapter的参数说明：
      第一个参数 表示访问整个android应用程序接口，基本上所有的组件都需要
      第二个参数表示生成一个Map(String ,Object)列表选项
      第三个参数表示界面布局的id  表示该文件作为列表项的组件
      第四个参数表示该Map对象的哪些key对应value来生成列表项
      第五个参数表示来填充的组件 Map对象key对应的资源一依次填充组件 顺序有对应关系

  */
//    private void showIcon() {
//        //初始化数据
//        initData();
//        String[] from={"img","text"};
//        int[] to={R.id.img,R.id.text};
//        adapter=new SimpleAdapter( getActivity(), dataList, R.layout.grid_item, from, to);
//        adapter.getItem(1);
//        Log.i(TAG, "tools:"+Integer.toString(adapter.getCount()));
//        gridView.setAdapter(adapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//                AlertDialog.Builder builder= new AlertDialog.Builder( getActivity());
////            builder.setTitle("提示").setMessage(dataList.get(arg2).get("text").toString()).create().show();
//                switch (dataList.get(arg2).get("text").toString()){
//                    case "地图":
//                        Intent mapService=new Intent(getActivity(),MapService.class);
//                        startActivity(mapService);
//                        break;
//                    case "天气":
//                        Intent weather=new Intent(getActivity(),Weather.class);
//                        startActivity(weather);
//                        break;
//                    case "数据":
//                        // 给Intent对象添加barChart类
//                        Intent intent=new Intent(getActivity(),SetData.class);
//                        //启动
//                        startActivity(intent);
//                        break;
//                    case "物流":
//                        Intent logistics=new Intent(getActivity(),Logistics.class);
//                        startActivity(logistics);
//                        break;
//
//                    case "更多":
//                        builder.setTitle("提示").setMessage("还未完成").create().show();
//                        Log.i(TAG, "onItemClick: 还未添加..");
//                        break;
//                }
//            }
//        });
//    }

    //显示功能图标
    private void showIcon() {
        getData();
        //初始化数据
        if (iconData.equals("NULL")){
            dataList=initData();
        }else dataList=getData();
        String[] from={"pic","type"};
        int[] to={R.id.img,R.id.text};
        adapter=new SimpleAdapter( getActivity(), dataList, R.layout.grid_item, from, to);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                switch ( dataList.get(arg2).get("type").toString()){
                    case "更多":
                        Intent more=new Intent(getActivity(),Configuration.class);
                        startActivity(more);
                        break;
                   case "地图":
                        Intent mapService=new Intent(getActivity(),MapService.class);
                        startActivity(mapService);
                        break;
                    case "天气":
                        Intent weather=new Intent(getActivity(),Weather.class);
                        startActivity(weather);
                        break;
                    case "数据":
                        // 给Intent对象添加barChart类
                        Intent data=new Intent(getActivity(),SetData.class);
                        //启动
                        startActivity(data);
                        break;
                    case "物流":
                        Intent logistics=new Intent(getActivity(),Logistics.class);
                        startActivity(logistics);
                        break;
                }
            }
            }
        );
    }

    //初始化数据
    private List<Map<String,Object>> initData() {
        //图标
        int a=R.mipmap.map;
        Log.i("123yyyyyyyyy", "initData: "+a);
//        int icno[] = { R.mipmap.map, R.mipmap.weather, R.mipmap.data_statics ,R.mipmap.in_out,
//                R.mipmap.more};
        List<String> picList=new ArrayList<>();
        List<String> typeList=new ArrayList<>();
        //图标下的文字
//        String [] name= {"地图", "天气", "数据", "物流", "更多"};

//        SharedPreferences getData = getSharedPreferences("SP_Data_List",Activity.MODE_PRIVATE);//创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
//        String peopleListJson = getData.getString("KEY_Data_List_DATA","");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
//        Log.i("132**********", "DataList: "+ peopleListJson);//peopleListJson便是取出的数据了

        //从本地json读取数据
        try {
            InputStream inputStream=this.getClass().getClassLoader().getResourceAsStream("assets/" + "Info.json");
            BufferedReader bufr = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder builder = new StringBuilder();
            //当line=bufr.readLine并且不为空
            while ((line = bufr.readLine()) != null) {
                builder.append(line);
            }
            inputStream.close();
            bufr.close();
            JSONObject jsonObject = new JSONObject(builder.toString());
            JSONArray jsonArray=jsonObject.getJSONArray("infoAndPicture");
            for(int i=0;i<=jsonArray.length();i++){
                JSONObject infoAndPic=jsonArray.getJSONObject(i);
                picList.add(i,infoAndPic.getString("pic"));
                typeList.add(i,infoAndPic.getString("type"));
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (JSONException e){

        }
        dataList = new ArrayList<>();
        for (int i = 0; i <typeList.size(); i++) {
            Map<String, Object> map=new HashMap<>();
            map.put("pic", Utility.String2Picture(picList.get(i)));
            map.put("type",typeList.get(i));
            dataList.add(map);
        }
        Map<String, Object> map=new HashMap<>();
        map.put("type","更多");
        map.put("pic",R.mipmap.more);
        dataList.add(map);
        return dataList;
    }

   //适配器参数List<Map<String,Object>>
   private  List<Map<String,Object>> JsonObject2ListMap(JSONObject jsonObject,String name){
       List<Map<String,Object>> list=new ArrayList<>();
       try{
           JSONArray jsonArray=jsonObject.getJSONArray(name);
           for (int i=0;i<jsonArray.length();i++){
               Map<String,Object> map=new HashMap<>();
               Log.i("123", "getData: "+jsonArray.getJSONObject(i).get("type"));
               Log.i("123", "getData: "+jsonArray.getJSONObject(i).get("pic"));
               jsonArray.getJSONObject(i).get("type");
               jsonArray.getJSONObject(i).get("pic");
               map.put("type",jsonArray.getJSONObject(i).get("type"));
               map.put("pic",jsonArray.getJSONObject(i).get("pic"));
               list.add(map);
           }
           Map<String, Object> map=new HashMap<>();
           map.put("type","更多");
           map.put("pic",R.mipmap.more);
           list.add(map);
       }
       catch (Exception e){
           e.printStackTrace();
       }
       return list;
   }

    //获取本地存储数据
    private  List<Map<String,Object>> getData() {
        List<Map<String,Object>> list=new ArrayList<>();
        SharedPreferences getData = getActivity().getSharedPreferences("SP_Data_List",Activity.MODE_PRIVATE);//创建sp对象,如果有key为"SP_PEOPLE"的sp就取出
        iconData = getData.getString("CURRENT","NULL");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        if (!iconData.equals("NULL")) {
            try {
                JSONObject jsonObject = new JSONObject(iconData);


                Log.i("112233", "getData: " + jsonObject.getJSONArray("data").toString());
                list = JsonObject2ListMap(jsonObject, "data");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }


//    private void initData() {
//        //图标
//        int icno[] = { R.mipmap.map, R.mipmap.weather, R.mipmap.data_statics ,R.mipmap.in_out,
//                R.mipmap.more};
//        //图标下的文字
//        String [] name= {"地图", "天气", "数据", "物流", "更多"};
//        dataList = new ArrayList<>();
//        for (int i = 0; i <icno.length; i++) {
//            Map<String, Object> map=new HashMap<>();
//            map.put("img", icno[i]);
//            map.put("text",name[i]);
//            dataList.add(map);
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();
        showIcon();
    }
}
