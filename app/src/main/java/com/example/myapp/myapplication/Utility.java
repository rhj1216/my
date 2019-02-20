package com.example.myapp.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.FontRequest;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

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

    //获取权限
//    public Utility getPermission(Context context) {
//       Utility utility=new Utility();
//        //请求码
//        int requestCode = 100;
//        //添加要请求的权限
//        String[] permissions = {
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.INTERNET,
//                Manifest.permission.ACCESS_WIFI_STATE,
//                Manifest.permission.CHANGE_WIFI_STATE,
//                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
//        };
//        //创建个集合来存放数据
//         permissionList = new ArrayList<>();
////        permissionList.clear();//清空没有通过的权限
//        for (int i = 0; i < permissions.length; i++) {
//            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
//                //添加还未授予的权限
//                permissionList.add(permissions[i]);
//            }
//        }
////        int permissionSize= permissionList.size();
////        String[] aPermissionSize={Integer.toString(permissionSize)};
////        String[] permissionArray = permissionList.toArray(new String[permissionList.size()]);
//
////        return new String[][]{permissionArray,aPermissionSize};
////        //有权限没有通过，需要请求
////        if (permissionList.size() > 0) {
////            ActivityCompat.requestPermissions((Activity) context, permissionArray, requestCode);
////        } else
////        {
////
////        }
//        return utility;
//    }
}
