package com.example.myapp.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.heweather.com.interfacesmodule.bean.Base;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.air.forecast.AirForecast;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.Lifestyle;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.LifestyleBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;

public class Weather extends AppCompatActivity implements View.OnClickListener,AMapLocationListener {
    private String TAG="----------------------------------";
    private Button getCity;
    private Button getWeather;
    private Button getMore;
    private Button getLife;
    private Button getWeatherForecast;
    private TextView cityInfo;
    private TextView weatherNow;
    private ListView moreInfo;
    private ListView airInfo;
    private double latitude;
    private double longitude;
    private  String city;
    private Handler handler;
//    private  static List<String> dataList;
//    private Search search;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //button
        getCity=findViewById(R.id.getCity);
        getWeather=findViewById(R.id.getWeather);
        getMore=findViewById(R.id.getMore);
        getLife=findViewById(R.id.getLife);
        getWeatherForecast=findViewById(R.id.getWeatherForecast);
        //TextView
        cityInfo=findViewById(R.id.cityInfo);
        weatherNow=findViewById(R.id.weatherNow);
        moreInfo=findViewById(R.id.moreInfo);
        airInfo=findViewById(R.id.airInfo);
        //添加监听
        getCity.setOnClickListener(this);
        getWeather.setOnClickListener(this);
        getMore.setOnClickListener(this);
        getLife.setOnClickListener(this);
        getWeatherForecast.setOnClickListener(this);
        getPermission();
        location();
    }
    //定位
    public void location(){
        //声明mlocationClient对象
        AMapLocationClient mlocationClient;
        //声明mLocationOption对象
        AMapLocationClientOption mLocationOption = null;
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if(aMapLocation.getErrorCode()==0){
                aMapLocation.getCity();
                aMapLocation.getDistrict();
                latitude=aMapLocation.getLatitude();
                longitude=aMapLocation.getLongitude();
//                Log.i(TAG, "onLocationChanged: "+aMapLocation.getCountry());
//                Log.i(TAG, "onLocationChanged: "+aMapLocation.getCity());
                city=aMapLocation.getCity();
//                Log.i(TAG, "onLocationChanged: "+aMapLocation.getAddress());
            }
            else{
                Log.i(TAG, "onLocationChanged: 定位失败");
            }
        }
    }

    //获取权限
    public void getPermission(){
        HeConfig.init("HE1902171409391409","b9f3d875f0e84dba88d425c6d8c44d33");
        HeConfig.switchToFreeServerNode();
        Utility utility=new Utility();
        List<String> permissionList=utility.getPermission(this);

//        Log.i(TAG, "getPermission: "+permissionList.size());
        String[] permissionArray=permissionList.toArray(new String [permissionList.size()]);
        if (permissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, permissionArray, 100);
        }
    }

    //获取城市sdk
    public void getCity(){
        String sLatitude=Double.toString(latitude);
        String sLongitude=Double.toString(longitude);
        LatLng latLng=new LatLng(latitude,longitude);
        HeWeather.getWeatherNow(this,sLatitude+","+sLongitude,new  HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: +++++++++++++++");
            }

            @Override
            public void onSuccess(List<Now> list) {
                Log.i("Log", "onSuccess: " + new Gson().toJson(list));
                String location=list.get(0).getBasic().getLocation();
                cityInfo.setText(location);
            }
        });
//        HeWeather.getWeatherNow(this, "CN101010100", Lang.CHINESE_SIMPLIFIED, Unit.METRIC,
//                new HeWeather.OnResultWeatherNowBeanListener() {
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("Log", "onError: ", e);
//                    }
//
//                    @Override
//                    public void onSuccess(List<Now> dataObject) {
//                    }
//                });
    }

    //获取天气sdk
    public void getWeather() {
        String sLatitude = Double.toString(latitude);
        String sLongitude = Double.toString(longitude);
        LatLng latLng = new LatLng(latitude, longitude);
        HeWeather.getWeatherNow(this, sLatitude + "," + sLongitude, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: +++++++++++++++");
            }

            @Override
            public void onSuccess(List<Now> list) {
                Log.i("Log", "onSuccess: " + new Gson().toJson(list));
                String weather = list.get(0).getNow().getCond_txt();
                weatherNow.setText(weather);
            }
        });
    }

    //获取空气更多数据sdk
    public void getMore()  {

         final  List<String> dataList = new ArrayList<>();//存储数据
        String sLatitude = Double.toString(latitude);
        String sLongitude = Double.toString(longitude);

//        HeWeather.getAirForecast(this, sLatitude + "," + sLongitude, new HeWeather.OnResultAirForecastBeansListener() {
//            @Override
//            public void onError(Throwable throwable) {
//                Log.i(TAG, "onError: ————————————————————空气质量预报");
//                Log.i(TAG, "onError: --------------throwable"+throwable);
//            }
//
//            @Override
//            public void onSuccess(List<AirForecast> list) {
//
//            }
//        });
////        HeWeather.getAirNow(this, sLatitude+","+sLongitude, new HeWeather.OnResultAirNowBeansListener() {
////            @Override
////            public void onError(Throwable throwable) {
////                Log.i(TAG, "onError: +++++++++++++++");
////                Log.i(TAG, "onError: ---------------throw"+throwable);
////            }
////
////            @Override
////            public void onSuccess(List<AirNow> list) {
////                Log.i(TAG, "onSuccess: -------------------------------xxxxxxxxxxxxxxxx");
////                String aqi = list.get(0).getAir_now_city().getAqi();//空气质量指数
////                String qlty = list.get(0).getAir_now_city().getQlty();//空气质量
////                dataList.add(aqi);
////                dataList.add(qlty);
////            }
////        });

        /*    //当前天气状况请求(sdk)*/
        HeWeather.getWeatherNow(this, sLatitude + "," + sLongitude, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: +++++++++++++++");
            }

            @Override
            public void onSuccess(List<Now> list) {
//                Log.i("Log", "onSuccess: " + new Gson().toJson(list));
//                String weather = list.get(0).getNow().getCond_txt();
//                weatherNow.setText(weather);
//                String[] strArr=new String[]{"1","2","3","4","5","6",};
                String fi = list.get(0).getNow().getFl();//体感温度
                String tmp = list.get(0).getNow().getTmp();//温度
                String hum = list.get(0).getNow().getHum();//湿度
                String pcpn = list.get(0).getNow().getPcpn();//降水量
                //把数据添加进datList里
                dataList.add(0,"体感温度: "+fi);
                dataList.add(1,"温度: "+tmp);
                dataList.add(2,"湿度: "+hum);
                dataList.add(3,"降水量: "+pcpn);
                String[] dataArray = dataList.toArray(new String[dataList.size()]);//dataList转换为数组类型
                Arrays.toString(dataArray);
                Log.i(TAG, "onSuccess: " + Arrays.toString(dataArray));
                //配置适配器,第一个参数上下文,第二个参数android自带的textView视图,第三个参数构造listView的数组
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Weather.this, android.R.layout.simple_list_item_1, dataArray);
                moreInfo.setAdapter(adapter);//设置listView适配器
//                Log.i(TAG, "onSuccess:____________________________ " + dataList.get(0));
//                Log.i(TAG, "onSuccess:____________________________ " + dataList.get(1));
//                Log.i(TAG, "onSuccess:____________________________ " + dataList.get(2));
//                Log.i(TAG, "onSuccess:____________________________ " + dataList.get(3));
            }
        });

    }

    //获取空气质量apq
    public void getAir(){
        String key="key=5a18212bce4b46cd877a9b514112cbe2";
        String location="location="+city;
//        final String requestUrl="https://free-api.heweather.com/s6/air/now?location=xian&key=5a18212bce4b46cd877a9b514112cbe2";
        final String requestUrl="https://free-api.heweather.com/s6/air/now?"+location+"&"+key;
        final List<String> airList=new ArrayList<>();//空气数据
        //当前空气质量请求(aqi)
        networkRequest(requestUrl,airList);
        handler=new Handler(){
            public void handleMessage(Message msg) {
              String[] dataArray= msg.getData().getStringArray("dataArray");
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Weather.this, android.R.layout.simple_list_item_1, dataArray);
                Log.i(TAG, "handleMessage: "+Arrays.toString(dataArray));
                airInfo.setAdapter(adapter2);//设置listView适配器
            }
        };
//        String[] airArray2 = airList.toArray(new String[airList.size()]);//dataList转换为数组类型
//        Arrays.toString(airArray2);
//        Log.i(TAG, "onSuccess:       air " + Arrays.toString(airArray2));
        //配置适配器,第一个参数上下文,第二个参数android自带的textView视图,第三个参数构造listView的数组
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Weather.this, android.R.layout.simple_list_item_1, airArray2);
//        airInfo.setAdapter(adapter2);//设置listView适配器
    }

    //网络请求okhttp
    public void networkRequest(final String url, final List<String> dataList)  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient=new OkHttpClient();//创建OkHttpClient,也就是ookhttp的客户端
                Request request=new Request.Builder().url(url).build();//创建请求,builder是辅助类,辅助进行网络请求
                Call call=okHttpClient.newCall(request);//创建请求队列将请求放进去
                //异步请求
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i(TAG, "onFailure: 请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i(TAG, "onFailure: 请求成功");
                        String responseData=response.body().string();//把请求到的数据变成字符串类型
                        Log.i(TAG, "onResponse: ------------------"+responseData);
                        try {
                            JSONObject jsonObject=new JSONObject(responseData);//把获取到的数据变成JSON对象
                            JSONArray heWeather6 = jsonObject.getJSONArray("HeWeather6");//获取HeWeather6这个数组
//                    Log.i(TAG, "onResponse: "+Integer.toString(heWeather6.length()));
                            JSONObject heWeather6Object=heWeather6.getJSONObject(0);//获取数组里的第一个对象
//                    JSONObject statusObject=heWeather6Object.getJSONObject("status");
                            String status=heWeather6Object.getString("status");
                            Log.i(TAG, "onResponse: -----status"+status);
                            if(!status.equals("ok")){
                                Log.i(TAG, "onResponse: 请求失败");
                            }
                            else{
                                JSONObject airNowObject=heWeather6Object.getJSONObject("air_now_city");//找到对象下的air_now_city对象
//                    airNowObject.getString("");
                                Log.i(TAG, "onResponse: 空气质量实况" + airNowObject.toString());
                                String aqi = airNowObject.getString("aqi");//获取空气质量指数
                                String qlty = airNowObject.getString("qlty");//获取空气质量
                                String pubTime = airNowObject.getString("pub_time");//获取数据发布时间
                                //数据添加进dataList里
                                dataList.add("空气质量指数: " + aqi);
                                dataList.add("空气质量: " + qlty);
                                dataList.add("更新时间: " + pubTime);
                                //传递信息
                                Message message=new Message();
                                String[] dataArray = dataList.toArray(new String[dataList.size()]);//dataList转换为数组类型
                                message.getData().putStringArray("dataArray",dataArray);
                                handler.sendMessage(message);
                            }

//                    Log.i(TAG, "onResponse: -----------------+++++++"+jsonArray.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                Log.i(TAG, "onResponse: 请求信息为"+response.body().string());
                    }
                });
//            Response response=call.execute();//同步请求
            }
        }).start();

//        OkHttpClient okHttpClient=new OkHttpClient();//创建OkHttpClient,也就是ookhttp的客户端
//        Request request=new Request.Builder().url(url).build();//创建请求,builder是辅助类,辅助进行网络请求
//        Call call=okHttpClient.newCall(request);//创建请求队列将请求放进去
//        //异步请求
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.i(TAG, "onFailure: 请求失败");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.i(TAG, "onFailure: 请求成功");
//                String responseData=response.body().string();//把请求到的数据变成字符串类型
//                Log.i(TAG, "onResponse: ------------------"+responseData);
//                try {
//                    JSONObject jsonObject=new JSONObject(responseData);//把获取到的数据变成JSON对象
//                    JSONArray heWeather6 = jsonObject.getJSONArray("HeWeather6");//获取HeWeather6这个数组
////                    Log.i(TAG, "onResponse: "+Integer.toString(heWeather6.length()));
//                    JSONObject heWeather6Object=heWeather6.getJSONObject(0);//获取数组里的第一个对象
////                    JSONObject statusObject=heWeather6Object.getJSONObject("status");
//                    String status=heWeather6Object.getString("status");
//                    Log.i(TAG, "onResponse: -----status"+status);
//                    if(!status.equals("ok")){
//                        Log.i(TAG, "onResponse: 请求失败");
//                    }
//                    else{
//                        JSONObject airNowObject=heWeather6Object.getJSONObject("air_now_city");//找到对象下的air_now_city对象
////                    airNowObject.getString("");
//                        Log.i(TAG, "onResponse: 空气质量实况" + airNowObject.toString());
//                        String aqi = airNowObject.getString("aqi");//获取空气质量指数
//                        String qlty = airNowObject.getString("qlty");//获取空气质量
//                        String pubTime = airNowObject.getString("pub_time");//获取数据发布时间
//                        //数据添加进dataList里
//                        dataList.add("空气质量指数: " + aqi);
//                        dataList.add("空气质量: " + qlty);
//                        dataList.add("更新时间: " + pubTime);
//                        //传递信息
//                        Message message=new Message();
//                        String[] dataArray = dataList.toArray(new String[dataList.size()]);//dataList转换为数组类型
//                        message.getData().putStringArray("dataArray",dataArray);
//                       handler.sendMessage(message);
//                    }
//
////                    Log.i(TAG, "onResponse: -----------------+++++++"+jsonArray.toString());
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
////                Log.i(TAG, "onResponse: 请求信息为"+response.body().string());
//            }
//        });
////            Response response=call.execute();//同步请求
    }

    //获取生活指数sdk
    public void getLife(){
        HeWeather.getWeatherLifeStyle(this, new HeWeather.OnResultWeatherLifeStyleBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: 生活指数请求失败");
            }

            @Override
            public void onSuccess(List<Lifestyle> list) {
                Map<Integer,String[]> map=new HashMap<Integer, String[]>();
               List<LifestyleBase> lifestyleBases= list.get(0).getLifestyle();
               lifestyleBases.get(0).getBrf();//生活指数简介
               lifestyleBases.get(0).getTxt();//生活指数详细描述
                //生活指数类型 comf：舒适度指数、cw：洗车指数、drsg：穿衣指数、flu：感冒指数、sport：运动指数、
                // trav：旅游指数、uv：紫外线指数、air：空气污染扩散条件指数
               lifestyleBases.get(0).getType();
               for(int i=0;i<=7;i++){
                   //构建lifeData数组
                   String[] lifeData={lifestyleBases.get(i).getBrf(),lifestyleBases.get(i).getTxt(),
                           lifestyleBases.get(i).getType()};
                   map.put(i,lifeData);//添加进map
//
               }
//                Log.i(TAG, "onSuccess: "+map.size());
                Intent weatherAboutLife=new Intent(Weather.this,WeatherAboutLife.class);
               //使用Serializble接口传递对象
                weatherAboutLife.putExtra("lifeIndexMap", (Serializable) map);
                startActivity(weatherAboutLife);
            }
        });
    }

    //获取未来三天天气
    public void getWeatherForecast(){
        HeWeather.getWeatherForecast(this, new HeWeather.OnResultWeatherForecastBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: 我失败了");
            }

            @Override
            public void onSuccess(List<Forecast> list) {
                Map<Integer,String[]> forecastMap=new HashMap<>();
                Log.i(TAG, "onSuccess: 我成功了");
                for(int i=0;i<list.get(0).getDaily_forecast().size();i++) {
//                    Log.i(TAG, "onSuccess: " + list.get(0).getDaily_forecast().get(i).getDate());//日期
//                    Log.i(TAG, "onSuccess: " + list.get(0).getDaily_forecast().get(i).getTmp_min());//最低温度
//                    Log.i(TAG, "onSuccess: " + list.get(0).getDaily_forecast().get(i).getTmp_max());//最高温度
//                    Log.i(TAG, "onSuccess: " + list.get(0).getDaily_forecast().get(i).getCond_txt_d());//天气状况
//                    Log.i(TAG, "onSuccess: " + list.get(0).getDaily_forecast().get(i).getWind_dir());//风向
//                    Log.i(TAG, "onSuccess: " + list.get(0).getDaily_forecast().get(i).getUv_index());//紫外线强度
                    forecastMap.put(i,new String[]{list.get(0).getDaily_forecast().get(i).getDate(),
                            list.get(0).getDaily_forecast().get(i).getTmp_min(),
                            list.get(0).getDaily_forecast().get(i).getTmp_max(),
                            list.get(0).getDaily_forecast().get(i).getCond_txt_d(),
                            list.get(0).getDaily_forecast().get(i).getWind_dir(),
                            list.get(0).getDaily_forecast().get(i).getUv_index(),
                    });
                }
                Intent weatherForecast=new Intent(Weather.this,WeatherForecast.class);
                weatherForecast.putExtra("forecast", (Serializable) forecastMap);
                startActivity(weatherForecast);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getCity:
                getCity();
                break;
            case R.id.getWeather:
                getWeather();
                break;
            case R.id.getMore:
                getAir();//api
                getMore();//sdk
                break;
            case R.id.getLife:
//                Log.i(TAG, "onClick: ---------------------");
                getLife();
//                Intent WeatherAboutLife=new Intent(this,WeatherAboutLife.class);
//////                startActivity(WeatherAboutLife);
//                startActivity(WeatherAboutLife);
                break;
            case R.id.getWeatherForecast:
                getWeatherForecast();
//                Intent weatherForecast=new Intent(this,WeatherForecast.class);
//                startActivity(weatherForecast);
                break;
        }
    }

}
