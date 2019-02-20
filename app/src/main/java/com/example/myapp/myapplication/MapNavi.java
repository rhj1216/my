package com.example.myapp.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.util.ArrayList;
import java.util.List;

public class MapNavi extends AppCompatActivity implements AMapNaviViewListener, AMapNaviListener {
    private AMapNavi aMapNavi;
    private  AMapNaviView navi_view;//!!!!!注意navi_view是单列对象
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_mapnavi);
        super.onCreate(savedInstanceState);
        //获取 AMapNaviView 实例
        navi_view= (AMapNaviView) findViewById(R.id.navi_view);
        navi_view.setAMapNaviViewListener(this);
        navi_view.onCreate(savedInstanceState);
        //获取AMapNavi实例
        aMapNavi = AMapNavi.getInstance(this);
        //添加监听回调，用于处理算路成功
        aMapNavi.addAMapNaviListener(this);
        aMapNavi.setUseInnerVoice(true);
    }
    //按下返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//当返回按键被按下
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("确认退出吗？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();//对话框消失
                    aMapNavi.destroy();//销毁AMapNavi单例对象
                    MapNavi.this.finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
        return false;
    }
    //导航监听回调
    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {

    }

    @Override
    public boolean onNaviBackClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认退出吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                aMapNavi.destroy();//销毁AMapNavi单例对象
                MapNavi.this.finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        return true;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }


    @Override
    public void onNaviViewShowMode(int i) {

    }

    @Override
    public void onInitNaviFailure() {

    }

    //初始化导航
    @Override
    public void onInitNaviSuccess() {

        /**
         * 方法:
         *   int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute);
         * 参数:
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         * 说明:
         *      以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         * 注意:
         *      不走高速与高速优先不能同时为true
         *      高速优先与避免收费不能同时为true
         */
        int strategy=0;
        try {
            strategy = aMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //接受mylocation和markerLatLng对象
        Intent intent = getIntent();
        Double startLatitude=intent.getDoubleExtra("startLatitude",0);
        Double startLongitude=intent.getDoubleExtra("startLongitude",0);
        Double endLatitude=intent.getDoubleExtra("endLatitude",0);
        Double endLongitude=intent.getDoubleExtra("endLongitude",0);
        Log.i("123", "onInitNaviSuccess:-------------- "+Double.toString(endLongitude));
        Log.i("123", "onInitNaviSuccess:-------------- "+Double.toString(endLatitude));
        Log.i("123", "onInitNaviSuccess:-------------- "+Double.toString(startLatitude));
        Log.i("123", "onInitNaviSuccess:-------------- "+Double.toString(startLongitude));
        Log.i("tag", "onInitNaviSuccess: 1232-------------------==========");
        List<NaviLatLng> startList=new ArrayList<>();
        List<NaviLatLng> endList=new ArrayList<>();
        startList.add(new NaviLatLng(startLatitude,startLongitude));
        endList.add(new NaviLatLng(endLatitude,endLongitude));
        List<NaviLatLng> mWayPointList= new ArrayList<NaviLatLng>();
        NaviLatLng a= startList.get(0);
        Log.i("tag", "onInitNaviSuccess: ++++++++++++++++++"+Double.toString(a.getLatitude()));
//        int Total=intent.getIntExtra("Total",0);
//        List<NaviLatLng> startList=new ArrayList<>();
//        List<NaviLatLng> endList=new ArrayList<>();
//        NaviLatLng latLng=new NaviLatLng(123,12);
//        startList.add(latLng);
//        endList.add(latLng);

        aMapNavi.calculateDriveRoute(startList, endList,null, strategy);
    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        //显示路径或开启导航
//        super.onCalculateRouteSuccess();
        onResume();
        aMapNavi.startNavi(NaviType.GPS);//开启导航
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }
    //地图生命周期
    @Override
    protected void onResume() {
        super.onResume();//重新加载
        navi_view.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();//暂停绘制
        navi_view.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();//摧毁
        navi_view.onDestroy();
    }
}
