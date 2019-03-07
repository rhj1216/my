package com.example.myapp.myapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private  String TAG="123";
    private TextView title;
    private LinearLayout home;
    private LinearLayout carData;
    private LinearLayout comment;
    private LinearLayout my;
    private ImageView homeIcon;
    private ImageView carDataIcon;
    private ImageView commentIcon;
    private ImageView myIcon;
    private  Boolean  runWelcomeScreen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        firstRun();
    }
    private void firstRun() {
        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun",0);
        Boolean first_run = sharedPreferences.getBoolean("First",true);
        Intent intent=getIntent();
        runWelcomeScreen=intent.getBooleanExtra("runWelcomeScreen",true);
        if (first_run){
            Log.i(TAG, "firstRun: "+"132123");
            sharedPreferences.edit().putBoolean("First",false).commit();
            Intent guideInterface=new Intent(this, com.example.myapp.myapplication.guideInterface.MainActivity.class);
            startActivity(guideInterface);
            this.finish();
        }else{
            if (runWelcomeScreen==true){
                Intent welcomeScreen=new Intent(this, WelcomeScreen.class);
                startActivity(welcomeScreen);
                this.finish();
            }
        }
    }

    private void initView() {
        home=findViewById(R.id.home);
        home.setOnClickListener(MainActivity.this);
        homeIcon=findViewById(R.id.homeIcon);
        carData=findViewById(R.id.carData);
        carData.setOnClickListener(MainActivity.this);
        carDataIcon=findViewById(R.id.carDataIcon);
        comment=findViewById(R.id.comment);
        comment.setOnClickListener(MainActivity.this);
        commentIcon=findViewById(R.id.commentIcon);
        my=findViewById(R.id.my);
        my.setOnClickListener(MainActivity.this);
        myIcon=findViewById(R.id.myIcon);
        title=findViewById(R.id.title);
    }


    //设置点击后的图标
    private void setImageIcon(ImageView imageIcon){
        //把点击的变成黑色图标,其他的变成白色
        int imageColorWhite[]={R.mipmap.home_white,R.mipmap.car_data_white,R.mipmap.comment_white,R.mipmap.my_white};
        int imageColoBlack[]={R.mipmap.home_black,R.mipmap.car_data_black,R.mipmap.comment_black,R.mipmap.my_black};
        ImageView[] imageViews={homeIcon,carDataIcon,commentIcon,myIcon};
        for(int i=0;i<imageViews.length;i++){
            if(imageViews[i]==imageIcon){
                imageIcon.setImageDrawable(getResources().getDrawable(imageColoBlack[i]));
            }
            else{
                imageViews[i].setImageDrawable(getResources().getDrawable(imageColorWhite[i]));
            }
        }
    }

    //Fragment跳转
    private void jumpFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction .replace(R.id.content,fragment);
        fragmentTransaction .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home:
                title.setText("首页");
                jumpFragment(new HomeContent());
                Log.i(TAG, "onClick: "+"linear被点击了");
                setImageIcon(homeIcon);
                break;
            case R.id.carData:
                title.setText("数据");
                jumpFragment(new CarData());
                setImageIcon(carDataIcon);
                break;
            case R.id.comment:
                title.setText("评论");
                jumpFragment(new Comment());
                getSupportFragmentManager().beginTransaction().remove(new CarData()).commit();
                setImageIcon(commentIcon);
                break;
            case R.id.my:
                jumpFragment(new MyInfo());
                title.setText("我的");
                setImageIcon(myIcon);
                break;
        }
    }
}
