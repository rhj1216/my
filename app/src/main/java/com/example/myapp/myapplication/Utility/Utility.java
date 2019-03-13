package com.example.myapp.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.FontRequest;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {
    public List<String> getPermission(Context context)  {
        Utility utility=new Utility();
        //请求码
        int requestCode = 100;
        //添加要请求的权限
        String[] permissions = {
                      Manifest.permission.ACCESS_COARSE_LOCATION,
                      Manifest.permission.ACCESS_FINE_LOCATION,
                      Manifest.permission.WRITE_EXTERNAL_STORAGE,
                      Manifest.permission.READ_PHONE_STATE,
                      Manifest.permission.INTERNET,
                      Manifest.permission.ACCESS_WIFI_STATE,
                      Manifest.permission.CHANGE_WIFI_STATE,
                      Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
              };
        //创建个集合来存放数据
        List<String>  permissionList = new ArrayList<>();
//        permissionList.clear();//清空没有通过的权限
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                //添加还未授予的权限
                permissionList.add(permissions[i]);
            }
        }
        return permissionList;
    }

    public static int String2Picture(String pic) {
        R.mipmap mipmap = new R.mipmap();
        int resId = 0;
        java.lang.reflect.Field field = null;
        try {
            field = R.mipmap.class.getField(pic);
            resId = (Integer) field.get(mipmap);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return resId;
    }
    public static  List<Map<String,Object>> JsonObject2ListMap(JSONObject jsonObject, String name){
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
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }


}
