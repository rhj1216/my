package com.example.myapp.myapplication;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //控件
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button showBarChart;
    private Button logistics;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private Button mapService;
    private Button weather;
    //变量,对象
    private UserDB userDB;
    private SQLiteDatabase db;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //button
        btn1=findViewById(R.id.normalMap);
        btn2=findViewById(R.id.satelliteMap);
        btn3=findViewById(R.id.nightMap);
        btn4=findViewById(R.id.getInputBox);
        showBarChart=findViewById(R.id.ShowBarChart);
        logistics=findViewById(R.id.logistics);
        mapService=findViewById(R.id.mapService);
        weather=findViewById(R.id.weather);
        //textView
        text1=findViewById(R.id.text1);
        text2=findViewById(R.id.text2);
        text3=findViewById(R.id.text3);
        text4=findViewById(R.id.text4);
        //设置监听
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        showBarChart.setOnClickListener(this);
        logistics.setOnClickListener(this);
        mapService.setOnClickListener(this);
        weather.setOnClickListener(this);
        CreateDatabase();
        Show();
    }

    //创建数据库
    public void CreateDatabase(){
        //创建一个dbHelper对象
        userDB = new UserDB(MainActivity.this, "User_db");
        //取得一个只读的数据库对象
        db = userDB.getReadableDatabase();
    }

    //获取列数据
    public int GetData(String column){
        //创建cursor游标对象
        //rwaQuery一般用与查询数据
        Cursor cursor=db.rawQuery("Select "+column+" from user", null);
        //找到第一行数据
        int t1=0;

        if (cursor.moveToFirst()) {
            //返回所有列的总数
            t1 = cursor.getInt(cursor.getColumnIndex(column));
        }
        return t1;
    }

    //在TextView上显示数据
    public void Show(){
        text1.setText(Integer.toString(GetData("注册人数")));
        text2.setText(Integer.toString(GetData("扫码总数")));
        text3.setText(Integer.toString(GetData("个人扫码数")));
        text4.setText(Integer.toString(GetData("今日扫码数")));
    }

    //更新数据
    public void UpDate(String column, TextView text){
        int t1=GetData(column);
        //人数加1
        t1++;
        //更新数据库
        //execSQL一般用于修改更新数据库
        db.execSQL("UPDATE user SET "+column+"="+t1+"   WHERE 行数=1");
//        Log.d("MainActivity",column+t1);
        text.setText(Integer.toString(t1));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //更新数据,扫码人数+1
            case R.id.normalMap:
                UpDate("注册人数",text1);
                break;
            //更新数据,扫码总数+1
            case R.id.satelliteMap:
                UpDate("扫码总数",text2);
                break;
            //更新数据,个人扫码数+1
            case R.id.nightMap:
                UpDate("个人扫码数",text3);
                break;
            //更新数据,今日扫码数量+1
            case R.id.getInputBox:
                UpDate("今日扫码数",text4);
                break;
            case R.id.ShowBarChart:
                // 给Intent对象添加barChart类
                Intent intent=new Intent(MainActivity.this,barChart.class);
                //添加要传给barChart的值
                intent.putExtra("Enrolment",GetData("注册人数"));
                intent.putExtra("Total",GetData("扫码总数"));
                intent.putExtra("Single",GetData("个人扫码数"));
                intent.putExtra("TodayUsage",GetData("今日扫码数"));
                //启动
                startActivity(intent);
                break;
            case  R.id.logistics:
                Intent logistics=new Intent(MainActivity.this,Logistics.class);
                startActivity(logistics);
                break;
            case R.id.mapService:
                Intent mapService=new Intent(this,MapService.class);
                startActivity(mapService);
                break;
            case R.id.weather:
                Intent weather=new Intent(this,Weather.class);
                startActivity(weather);
                break;
        }
    }
}
