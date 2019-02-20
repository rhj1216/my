package com.example.myapp.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.enums.NetWorkingProtocol;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.pm.PackageManager.MAXIMUM_VERIFICATION_TIMEOUT;

public class MapService extends AppCompatActivity implements AMap.OnMyLocationChangeListener, View.OnClickListener, PoiSearch.OnPoiSearchListener
        , View.OnTouchListener ,Inputtips.InputtipsListener,TextWatcher,GeocodeSearch.OnGeocodeSearchListener,AMap.OnMarkerClickListener
            ,AMap.OnMapClickListener, View.OnFocusChangeListener {
    //对象
    private AMap aMap=null;
    private MapView mapView ;
//    private MyLocationStyle myLocationStyle;
    private UiSettings settings;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private SharedPreferences sharedPreferences1;
    private SharedPreferences sharedPreferences2;
//  private GeocodeSearch geocodeSearch=null;
    private  Marker marker;
    //控件
    private View view;
    private Button normalMap;
    private Button satelliteMap;
    private Button nightMap;
    private Button getInputBox;
    private Button configuration;
    private Button navi;
    private AutoCompleteTextView inputText;
    private EditText eText;
    private ConstraintLayout makerInfo;
    private TextView streetInfo;
    private TextView titleInfo;
    private TextView distanceInfo;
    //自定义 变量,数组
    private String keyWord = null;
    private String TAG="tag";
    private String city;
//    private String district;
    public String X;
    public String Y;
    public Boolean flag=false;
    private int range=1000;
    private ArrayList<PoiItem> pois;
    private List<Marker> mapScreenMarkers;
    private LatLng markerLatLng;
    private Location myLocation;
    private Map<Marker,String> markerMap=new HashMap<>();
    private int count;
    private int markerClick=0;
    private int mapClick=0;
    private int sum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //获取地图控件引用
        mapView = findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        view = findViewById(R.id.map);
        normalMap = findViewById(R.id.normalMap);
        satelliteMap = findViewById(R.id.satelliteMap);
        nightMap = findViewById(R.id.nightMap);
        getInputBox = findViewById(R.id.getInputBox);
        configuration = findViewById(R.id.configuration);
        inputText = findViewById(R.id.searchText);
        makerInfo=findViewById(R.id.makerInfo);
        navi=findViewById(R.id.navi);
        streetInfo=findViewById(R.id.streetInfo);
        titleInfo=findViewById(R.id.titleInfo);
        distanceInfo=findViewById(R.id.distanceInfo);
        //让makerInfo隐藏
        makerInfo.setVisibility(View.INVISIBLE);
        normalMap.setOnClickListener(this);
        satelliteMap.setOnClickListener(this);
        nightMap.setOnClickListener(this);
        getInputBox.setOnClickListener(this);
        configuration.setOnClickListener(this);
        navi.setOnClickListener(this);
        inputText.addTextChangedListener(this);//添加搜索框监听
        inputText.setOnFocusChangeListener(this);
        getPermission();
    }
    //获取权限
    public void getPermission() {
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
        List<String> permissionList = new ArrayList<>();
//        permissionList.clear();//清空没有通过的权限
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                //添加还未授予的权限
                permissionList.add(permissions[i]);
            }
        }
        String[] permissionArray = permissionList.toArray(new String[permissionList.size()]);
        //有权限没有通过，需要请求
        if (permissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, permissionArray, requestCode);
        }
        Log.i(TAG, "getPermission: ++++++++++++++++++++");
            loc();
//
//        Utility utility=new Utility();
//        String[] permissionArray2=utility.getPermission(this);


    }

    //用户选择允许或拒绝后，会回调onRequestPermissionsResult方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
        loc();//定位
    }

    //地图初始化,定位
    public void loc() {
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        aMap.showIndoorMap(true);  //true：显示室内地图；false：不显示；
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        myLocationStyle.interval(MAXIMUM_VERIFICATION_TIMEOUT); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        settings = aMap.getUiSettings();// 是否显示定位按钮
//        myLocationStyle.anchor(0,1);//设置定位蓝点图标的锚点方法。
        myLocationStyle.strokeColor(getResources().getColor(R.color.white));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(getResources().getColor(R.color.littlePurple));//设置定位蓝点精度圆圈的填充颜色的方法。
        myLocationStyle.strokeWidth(20);//设置定位蓝点精度圈的边框宽度的方法。
//        BitmapDescriptor bitmapDescriptor=BitmapDescriptorFactory.fromResource(R.mipmap.mylocation);//获取bitmapDescriptor对象
//        myLocationStyle.myLocationIcon(bitmapDescriptor);//设置定位蓝点的icon图标方法
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));//设置缩放比例
        settings.setCompassEnabled(true);//指南针
        settings.setScaleControlsEnabled(true);//控制比例尺控件是否显示
        settings.setZoomControlsEnabled(false);//是否显示缩放按钮
        settings.setZoomGesturesEnabled(true);//缩放手势
        settings.setTiltGesturesEnabled(false);//倾斜手势
        settings.setRotateGesturesEnabled(false);//旋转手势
        aMap.setOnMyLocationChangeListener(this);//设置定位改变监听
        aMap.setTrafficEnabled(true);//显示实时路况图层，aMap是地图控制器对象。
        aMap.setOnMapClickListener(this);//为地图点击事件添加监听
//        MyLocationStyle myLocationIcon(BitmapDescriptor myLocationIcon);//设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
//        myLocationStyle.showMyLocation(false);//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
    }

    //实现 AMap.OnMyLocationChangeListener 监听器，通过如下回调方法获取经纬度信息：
    @Override
    public void onMyLocationChange(Location location) {
        Log.i(TAG, "onMyLocationChange: "+count);
        //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取
        myLocation=location;//把location实例传给this.location
        Log.i(TAG, "onMyLocationChange: "+sum);
        if(sum==0){
            start();
        }
        sum++;
        Log.i(TAG, "onMyLocationChange: +++++++++++++++++++++++");
        GeocodeSearch geocodeSearch=new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);//对GeocoderSearch对象设置监听。
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        //逆地理编码接口
        LatLonPoint latLonPoint=new LatLonPoint(location.getLatitude(),location.getLongitude());
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
        //发起请求。
        geocodeSearch.getFromLocationAsyn(query);
    }

    //逆地理编码回调函数
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        //解析result获取地址描述信息
        RegeocodeAddress regeocodeAddress= regeocodeResult.getRegeocodeAddress();
        String country= regeocodeAddress.getCountry();//国家信息
        String province= regeocodeAddress.getProvince();//省信息
        city= regeocodeAddress.getCity();//城市信息
//        district= regeocodeAddress.getDistrict();//城区信息

        Log.i(TAG, "onRegeocodeSearched: ");
        Log.i(TAG,"国家信息-------------"+country);//国家信息
        Log.i(TAG,"省信息---------------"+province);//省信息
        Log.i(TAG,"城市信息-------------"+city);//城市信息
        Log.i(TAG,"城区信息-------------"+regeocodeAddress.getDistrict());//城区信息

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    //第一次启动
    public void start()  {
        sum++;
        sharedPreferences1= getSharedPreferences("count", 0); // 存在则打开它，否则创建新的Preferences
        sharedPreferences2= getSharedPreferences("keyword", 0); // 存在则打开它，否则创建新的Preferences2
        count =sharedPreferences1.getInt("count", 0); // 取出数据
//        Log.i(TAG, "start: ++++++++++++++++++++++");
        //如果count为0表示第一次打开,显示配置.否者直接搜索
//        Log.i(TAG, "start: "+count);
        if(count==0){
            showAlert();
        }else{
            String keyWord =sharedPreferences2.getString("keyword","");
            search(keyWord,range,0);
        }
//        finish();
//
    }

    //显示弹出框
    public void showAlert(){
        eText=new EditText(this);//实例化个编辑框对象
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("配置每次地图打开时搜索的标志");//对话框标题
        builder.setIcon(getResources().getDrawable(R.mipmap.ic_launcher));//图片
        builder.setView(eText);//往里添加编辑框
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                // 创建新的Preferences2
//                sharedPreferences2= getSharedPreferences("keyword", 0);
                //实例化Editor对象
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                //存入数据
                editor.putInt("count", 1); // 存入数据
                editor2.putString("keyword",eText.getText().toString());
                //提交修改
                editor.commit();
                editor2.commit();
                String sKeyword = sharedPreferences2.getString("keyword", ""); // 取出数据
                search(sKeyword,range,1);//根据编辑框搜索
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消配置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //实例化Editor对象  没有启动start
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                //存入数据
                editor.putInt("count", 1); // 存入数据
                editor2.putString("keyword",""); // 存入数据
                //提交修改
                editor2.commit();
                editor.commit();
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);//调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用
        builder.show();
    }

//    s: 修改之前的文字。
//    start: 字符串中即将发生修改的位置。
//    count: 字符串中即将被修改的文字的长度。如果是新增的话则为0。
//    after: 被修改的文字修改之后的长度。如果是删除的话则为0。
//    before: 被改变的字符串长度，如果是新增则为0

    //搜索框改变之前
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    //当搜索框改变
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
         String sKeyWord=inputText.getText().toString();//获取编辑框的值
        //第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
        Log.i(TAG, "onTextChanged: -----------------"+city);
        InputtipsQuery inputquery = new InputtipsQuery(sKeyWord, city);
        inputquery.setCityLimit(true);//限制在当前城市
        Inputtips inputTips = new Inputtips(this, inputquery);//构造Inputtips对象,并设置监听。
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();//调用PoiSearch的requestInputtipsAsyn()方法发送请求.

    }

    //搜索框改变后
    @Override
    public void afterTextChanged(Editable s) {
    }

    //搜索框提示
    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        if(i==1000){
            Log.i(TAG, "onGetInputtips: 成功");
        }else{
            Log.i(TAG, "onGetInputtips: 失败");
        }
        List<String> searchList=new ArrayList<>();
        if (searchList!=null){
            searchList.clear();   
        }
        for(Tip tip:list){
            String name=tip.getName();
            String address=tip.getAddress();
            String District=tip.getDistrict();
            Log.i(TAG, "onGetInputtips:  name: "+name+"  address:"+address+"  District:"
            +District);
            searchList.add(name);
        }
//        第一个参数：context上下文对象
//        第二个参数：每一个item的样式，可以使用系统提供，也可以自定义就是一个TextView
//        第三个参数：数据源要显示的数据
        ArrayAdapter adapter=null;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,searchList);
        //这句话的意思是为AutoCompleteTextView设置过滤字段为null,只要是数据都会显示出来
        adapter.getFilter().filter(null);
        //把adapter添加给InputText
        inputText.setAdapter(adapter);
    }

    //搜索
    public void search(String keyWord,int range,int flag) {
        pois=null;
        Log.i(TAG, "search: "+keyWord);
        if(!keyWord.equals("")){
            if(flag==1){
                clearMarkers();//清除地图上的Markers
            }
            int currentPage=1;//查询页码
            query = new PoiSearch.Query(keyWord, null, null);
            //keyWord表示搜索字符串，
            //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
            //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
            query.setPageSize(10);// 设置每页最多返回多少条poiitem
            query.setPageNum(currentPage);//设置查询页码
            poiSearch = new PoiSearch(this, query);//构造 PoiSearch 对象，并设置监听
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(myLocation.getLatitude(),
                    myLocation.getLongitude()), range));//设置周边搜索的中心点以及半径
            poiSearch.searchPOIAsyn(); //调用 PoiSearch 的 searchPOIAsyn() 方法发送请求
        }
//        aMap=null;
//        loc();
    }


    //删除指定Marker
    private void clearMarkers() {
        if(aMap.getMapScreenMarkers()!=null){
            //说明：如果数据量大，频繁地add，remove marker刷新地图，可能会造成主线程阻塞，出现ANR；
//            if(aMap.getMapScreenMarkers().size()>1) {}
                List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();//获取地图上所有marker的集合
                for (int i = 1; i < mapScreenMarkers.size(); i++) {//从第一个开始,自己位置mark不删除
                    Marker marker = mapScreenMarkers.get(i);
                    marker.remove();//移除当前Marker
                }
                onResume();//重新加载地图
        }
    }

    //搜索回调函数
    @Override

    public void onPoiSearched(PoiResult poiResult, int i) {
//        String findPoint= Integer.toString(poiResult.getPois().size());
        int findPoint=poiResult.getPois().size();//获取搜索到的点
        //如果搜索到的点为零,则搜索范围增加1000公里,再进行搜索
        if(findPoint==0){
            range=range+1000;
            search(keyWord,range,0);
        }
        Log.i(TAG, "onPoiSearched: "+findPoint);
        //从搜索结果里获取pois对象
        pois=poiResult.getPois();
        //遍历数组
        for (PoiItem poi : pois) {
            String title=poi.getTitle();//获取标题
            String snippet = poi.getSnippet();//获取街道名
            int iDistance = poi.getDistance();//获取距离
            String distance = Integer.toString(iDistance);
            //获取point对象,通过point获取点的经纬度
            LatLonPoint point = poi.getLatLonPoint();
            point.getLatitude();
            point.getLongitude();
            LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
            //把maker添加给地图
            marker = aMap.addMarker(new MarkerOptions().position(latLng).title(title).snippet(snippet));
            markerMap.put(marker,distance);//构造marker和distance的键值对map
            //给marker设置图标
            BitmapDescriptor bdp=BitmapDescriptorFactory.fromResource(R.mipmap.locationicon);
            marker.setIcon(bdp);
        }
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    //当marker被点击时
    @Override
    public boolean onMarkerClick(Marker marker) {
        closeInputBox();//关闭输入框
        configuration.setVisibility(View.INVISIBLE);//配置按钮隐藏
//        configuration.setEnabled(false);让配置按钮不可点
        makerInfo.setVisibility(View.VISIBLE);//信息框显示
        String distance=markerMap.get(marker);//获取距离
        distanceInfo.setText(distance+"米");//距离显示在信息框呢
        streetInfo.setText(marker.getSnippet());//街道名显示在信息框呢
        titleInfo.setText(marker.getTitle());//标题显示在信息框呢
//        Log.i(TAG, "onMarkerClick: ______________________");
//        Log.i(TAG, "onMarkerClick:+++++++++++++++++++++++ "+distance);

        markerLatLng= marker.getPosition();
        String latitude=Double.toString(marker.getPosition().latitude);
        String longitude=Double.toString(marker.getPosition().longitude);
//        Log.i(TAG, "onMarkerClick: ----------------"+latitude);
//        Log.i(TAG, "onMarkerClick: ----------------"+longitude);
        Log.i(TAG, "onMarkerClick: +++++++++++++++++++++marker被点击了");
        markerClick++;
        return false;
    }

    //让编辑框失去焦点
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //设置焦点
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        //获取的焦点
        view.requestFocus();
        return false;
    }

    //关闭输入框
    public void closeInputBox(){
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //当按钮被点击
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.normalMap:
               aMap.setMapType(AMap.MAP_TYPE_NORMAL);//标准地图
                break;
            case R.id.satelliteMap:
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 设置卫星地图模式，aMap是地图控制器对象。
                break;
            case R.id.nightMap:
                aMap.setMapType(AMap.MAP_TYPE_NIGHT);//夜景地图，aMap是地图控制器对象。
                break;
            case R.id.getInputBox:
                //让makerInfo隐藏
                makerInfo.setVisibility(View.INVISIBLE);
                if(keyWord==null||!keyWord.equals(inputText.getText().toString())){
                    this.keyWord=inputText.getText().toString();//获取编辑框的值
                    range=1000;
                    search(keyWord,range,1);//搜索
                    closeInputBox();//关闭输入框
                }
                break;
            case R.id.navi:
                // 给Intent对象添加,MapNavi类
                Intent intent=new Intent(this,MapNavi.class);
                Log.i(TAG, "onClick: "+Double.toString(myLocation.getLatitude()));
                Log.i(TAG, "onClick: "+Double.toString(myLocation.getLongitude()));
                Log.i(TAG, "onClick: "+Double.toString(markerLatLng.latitude));
                Log.i(TAG, "onClick: "+Double.toString(markerLatLng.longitude));
                //添加要传给MapNavi值
                intent.putExtra("startLatitude",myLocation.getLatitude());
                intent.putExtra("startLongitude",myLocation.getLongitude());
                intent.putExtra("endLatitude",markerLatLng.latitude);
                intent.putExtra("endLongitude",markerLatLng.longitude);
                //启动
                startActivity(intent);
                configuration.setVisibility(View.VISIBLE);//配置按钮显示
                makerInfo.setVisibility(View.INVISIBLE);//信息框隐藏
                break;
            case R.id.configuration:
                showAlert();//显示配置对话框
                break;
        }
    }

    //当地图被点击时
    @Override
    public void onMapClick(LatLng latLng) {
//        inputText.setFocusable(false);
        Log.i(TAG, "onMapClick: +++++++++++map被点击了");
        mapClick++;
        Log.i(TAG, "onMapClick: "+Integer.toString(mapClick));
        Log.i(TAG, "onMapClick: "+Integer.toString(markerClick));
        //让makerInfo隐藏
        //判断点的是marker之外的地图部分,如果是隐藏信息框
        if(markerClick!=mapClick){
            if(markerClick==0) {
                mapClick=0;
            }
            else{
                marker.hideInfoWindow();//隐藏marker信息框
                configuration.setVisibility(View.VISIBLE);//配置按钮显示
                makerInfo.setVisibility(View.INVISIBLE);//信息框隐藏
                mapClick=0;
                markerClick=0;
            }
        }
//        if(marker!=null){
//            mapScreenMarkers = aMap.getMapScreenMarkers();//获取地图上所有marker的集合
//            for(Marker marker:mapScreenMarkers){
//                Log.i(TAG, "onMapClick: +++++++++++"+Double.toString(latLng.longitude));
//                Log.i(TAG, "onMapClick: +++++++"+Double.toString(latLng.latitude));
//                Log.i(TAG, "onMapClick: +++++++++++"+Double.toString(marker.getPosition().longitude));
//                Log.i(TAG, "onMapClick: +++++++++"+Double.toString(marker.getPosition().latitude));
//                if(marker.getPosition()!=latLng){
//                    marker.hideInfoWindow();
//                    configuration.setVisibility(View.VISIBLE);//配置按钮显示
//                    makerInfo.setVisibility(View.INVISIBLE);//信息框隐藏
//                }
//            }
//        }
    }

    //按下返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//当返回按键被按下
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("确认退出吗？");
//            builder.setTitle("提示");
//            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();//对话框消失
//                    Bundle bundle=new Bundle();
//                    onSaveInstanceState(bundle);
//                    MapService.this.finish();
//                }
//            });
//            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            builder.create().show();
            aMap = null;
            MapService.this.finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //让makerInfo隐藏
        makerInfo.setVisibility(View.INVISIBLE);
    }
}
