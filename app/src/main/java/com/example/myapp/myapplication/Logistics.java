package com.example.myapp.myapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logistics extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private Button enter;
    private Button out;
    private EditText editText;
    private View view;
    private TextView text1;
    private UserDB userDB;
    private SQLiteDatabase db;
    private String TAG="提示";
    @SuppressLint("ResourceType")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_stock);
        super.onCreate(savedInstanceState);
        enter=findViewById(R.id.enter);
        out=findViewById(R.id.out);
        editText=findViewById(R.id.EText);
        text1=findViewById(R.id.text1);
        //让text1隐藏
        text1.setVisibility(View.INVISIBLE);
        view=findViewById(R.id.Layout);
        view.setOnTouchListener(this);
        enter.setOnClickListener(this);
        out.setOnClickListener(this);
        GetDBObject();
//        Log.i("当前时间","onCreate: "+getTime());
    }

    //获取库有读的数据库对象
    public void  GetDBObject(){
        //创建一个dbHelper对象
        userDB = new UserDB(Logistics.this, "User_db");
        //取得一个只读的数据库对象
        db = userDB.getReadableDatabase();
    }

    //显示TextView
    public void showText(String vin,String time){
        //让text显示
        text1.setVisibility(View.VISIBLE);
        //在TextView上显示存入数据
        text1.setText("VIN码:"+vin+" "+"状态:入库 入库时间:"+time);
    }

    //进库
    public void entry(String vin, String time){
        //获取符合查询VIN码的列
        Cursor cursor=db.rawQuery("SELECT * FROM stock WHERE VIN码='"+vin+"'",null);
        //判断入库条件
        if (cursor.getCount()==0){
            //执行sql插入语句
            db.execSQL("insert into stock values ('"+vin+"','入库','"+time+"')");
            showText(vin,time);
            //提示
            Toast.makeText(Logistics.this,"入库成功",Toast.LENGTH_SHORT).show();
        }
        //否者,查看库内状态
        else{
            String state="";
            String entryTime="";
            //移动到第一行
            if (cursor.moveToFirst()){
                //获取库内状态
                state=cursor.getString(1);
                //获取入库时间
                entryTime=cursor.getString(2);
            }
            if(!state.equals("入库")){
                //更新时间和入库状态
                db.execSQL("UPDATE stock SET 库内状态='入库',时间='"+time+"' WHERE VIN码='"+vin+"'");
                showText(vin,time);
                Toast.makeText(Logistics.this,"入库成功",Toast.LENGTH_SHORT).show();
            }else{
                showText(vin,entryTime);
                Toast.makeText(Logistics.this,"入库失败,VIN码已在库中",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //出库
    public void GoOut(String vin,String goOutTime){
        //获取符合查询VIN码的列
        Cursor cursor=db.rawQuery("SELECT * FROM stock WHERE VIN码='"+vin+"'",null);
        //判断入库条件
        if (cursor.getCount()==0){
            Toast.makeText(Logistics.this,"库内没有此VIN码",Toast.LENGTH_SHORT).show();
        }else{
            String state="";
            //移动到第一行
            if (cursor.moveToFirst()){
                //获取库内状态
                state=cursor.getString(1);
            }
            if(state.equals("入库")){
                db.execSQL("UPDATE stock SET 库内状态='出库',时间='"+goOutTime+"' WHERE VIN码='"+vin+"'");
                text1.setVisibility(View.VISIBLE);
                text1.setText("VIN码:"+vin+" 库内状态:出库 出库时间:"+goOutTime);
                Toast.makeText(Logistics.this,"出库成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Logistics.this,"车辆早已出库,出库失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //获取当前时间
    public String getTime(){
        //时间格式化
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        //创建个curDate对象
        Date curDate = new Date(System.currentTimeMillis());
        String curTime = formatter.format(curDate);
        return curTime;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.enter:
                //获取编辑框的值
                String vin1=editText.getText().toString();
                //如果为空,输入
                if(vin1.equals("")){
                    Toast.makeText(Logistics.this,"请在编辑框中输入VIN码",Toast.LENGTH_SHORT).show();
                }else
                    entry(vin1,getTime());
            break;
            case R.id.out:
                //获取编辑框的值
                String vin2=editText.getText().toString();
                //如果为空,输入
                if(vin2.equals("")){
                    Toast.makeText(Logistics.this,"请在编辑框中输入VIN码",Toast.LENGTH_SHORT).show();
                }else
                    GoOut(vin2,getTime());
            break;
        }
    }

    //点屏幕其他处时,让编辑框失去焦点
    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        //设置焦点
//        view.setFocusable(true);
//        view.setFocusableInTouchMode(true);
//        editText.setFocusable(false);
//        editText.setFocusableInTouchMode(false);
//        //获取的焦点
//        view.requestFocus();
//
//        editText.requestFocus();
        return false;
    }
}
