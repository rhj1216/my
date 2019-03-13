package com.example.myapp.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDB extends SQLiteOpenHelper {


    //带全部参数的构造函数，此构造函数必不可少
    public UserDB(Context context, String name, SQLiteDatabase.CursorFactory factory,
                  int version) {
        super(context, name, factory, version);

    }
    //带两个参数的构造函数，调用的其实是带三个参数的构造函数
    public UserDB(Context context, String name) {
        this(context, name, 1);
    }
    //带三个参数的构造函数，调用的是带所有参数的构造函数
    public UserDB(Context context, String name, int version) {
        this(context, name, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句,创建使用情况表
        String sql = "CREATE TABLE  user(" +
                "行数 int," +
                "注册人数 int," +
                "扫码总数 int," +
                "个人扫码数 int," +
                "今日扫码数 int)";
        //执行创建数据库操作
        db.execSQL(sql);
        //插入原始数据
        String sql2="INSERT INTO user VALUES (1,0, 0, 0, 0)";
        //执行插入数据操作
        db.execSQL(sql2);

        //创建入库出库表
        String sql3 = "CREATE TABLE stock(" +
                "VIN码 varchar," +
                "库内状态 varchar," +
                "时间 varchar)";
        //执行创建数据库操作
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //创建成功，日志输出提示
        Log.i("提示","update a Database");
    }
}
