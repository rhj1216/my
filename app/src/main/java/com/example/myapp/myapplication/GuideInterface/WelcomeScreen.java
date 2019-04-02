package com.example.myapp.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.myapp.myapplication.MyCustomCircleProgressBar.CircleProgressBarView;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeScreen extends AppCompatActivity {
    private CircleProgressBarView circleBarView;
    private   Timer timer;
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.welcome_screen);
        initView();
        init();
    }

    private void initView() {
        circleBarView=findViewById(R.id.circleProgressBar);
        circleBarView.setProgressNum(5000);
        circleBarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送intent实现页面跳转，第一个参数为当前页面的context，第二个参数为要跳转的主页
                Intent mainActivity = new Intent(WelcomeScreen.this,MainActivity.class);
                mainActivity.putExtra("runWelcomeScreen",false);
                startActivity( mainActivity );
                //跳转后关闭当前欢迎页面
                WelcomeScreen.this.finish();
                timer.cancel();
            }
        });
    }


    private void init() {
        //利用timer让此界面延迟5秒后跳转，timer有一个线程，该线程不断执行task
        timer=new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //发送intent实现页面跳转，第一个参数为当前页面的context，第二个参数为要跳转的主页
                Intent mainActivity = new Intent(WelcomeScreen.this,MainActivity.class);
                mainActivity.putExtra("runWelcomeScreen",false);
                startActivity( mainActivity );
                //跳转后关闭当前欢迎页面
               WelcomeScreen.this.finish();
            }
        };
        //调度执行timerTask，第二个参数传入延迟时间（毫秒）
        timer.schedule(timerTask,5000);
    }
}
