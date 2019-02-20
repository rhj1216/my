package com.example.myapp.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Map;

public class WeatherForecast extends AppCompatActivity {
    private TextView today;
    private TextView tomorrow;
    private TextView afterTomorrow;
    private String TAG="__________________________--";
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        init();
        getData();
    }

    private void init() {
        today=findViewById(R.id.today);
        tomorrow=findViewById(R.id.tomorrow);
        afterTomorrow=findViewById(R.id.afterTomorrow);
    }

    public void getData(){
        Map<Integer,String[]> forecastData = (Map<Integer, String[]>) getIntent().getSerializableExtra("forecast");
        TextView [] forecastViews={today, tomorrow, afterTomorrow};
        for (int i=0;i<=2;i++){
            Log.i(TAG, "getData: "+Arrays.toString(forecastData.get(i)));
            forecastViews[i].setText("   "+forecastData.get(i)[0]+"      "+
                    "温度: "+forecastData.get(i)[1]+"~"+forecastData.get(i)[2]+"      " +
                    forecastData.get(i)[3]+"                "+forecastData.get(i)[4]+
                    "          紫外线指数: "+forecastData.get(i)[5]
            );

        }
    }
}
