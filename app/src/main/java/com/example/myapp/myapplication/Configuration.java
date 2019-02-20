package com.example.myapp.myapplication;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class Configuration extends AppCompatActivity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

//        final EditText inputServer = new EditText(this);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("输入每次地图打开时显示的标志").setIcon(getResources().getDrawable(R.mipmap.ic_launcher)).setView(inputServer)
//                .setNegativeButton("不设置", null);
//        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int which) {
//
//                String a= inputServer.getText().toString();
//                Log.i("12", "onClick: "+a);
//            }
//        });
//        builder.show();
//        alert_edit();
//        showAlert();
    }
//    public void alert_edit(){
//        final EditText et = new EditText(this);
//        new AlertDialog.Builder(this).setTitle("输入每次地图打开时显示的标志")
//                .setIcon(getResources().getDrawable(R.mipmap.ic_launcher))
//                .setView(et)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //按下确定键后的事件
//                        Toast.makeText(getApplicationContext(), et.getText().toString(),Toast.LENGTH_LONG).show();
//                    }
//                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        }).show();
//    }
//    public void onClick(){
//        Log.i("123", "onClick: ");
//    }



}
