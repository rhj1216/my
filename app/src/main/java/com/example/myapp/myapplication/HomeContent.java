package com.example.myapp.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.myapp.myapplication.dataShow.SetData;
import com.example.myapp.myapplication.logistics.Logistics;
import com.example.myapp.myapplication.map.MapService;
import com.example.myapp.myapplication.weather.Weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeContent extends Fragment {
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    private  String TAG="123";
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home_content,container,false);
        gridView=view.findViewById(R.id.gridView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tools();
    }
    /*
     SimpleAdapter的参数说明：
      第一个参数 表示访问整个android应用程序接口，基本上所有的组件都需要
      第二个参数表示生成一个Map(String ,Object)列表选项
      第三个参数表示界面布局的id  表示该文件作为列表项的组件
      第四个参数表示该Map对象的哪些key对应value来生成列表项
      第五个参数表示来填充的组件 Map对象key对应的资源一依次填充组件 顺序有对应关系

  */
    private void tools() {
        //初始化数据
        initData();
        String[] from={"img","text"};
        int[] to={R.id.img,R.id.text};
        adapter=new SimpleAdapter( getActivity(), dataList, R.layout.grid_item, from, to);
        adapter.getItem(1);
        Log.i(TAG, "tools:"+Integer.toString(adapter.getCount()));
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                AlertDialog.Builder builder= new AlertDialog.Builder( getActivity());
//            builder.setTitle("提示").setMessage(dataList.get(arg2).get("text").toString()).create().show();
                switch (dataList.get(arg2).get("text").toString()){
                    case "地图":
                        Intent mapService=new Intent(getActivity(),MapService.class);
                        startActivity(mapService);
                        break;
                    case "天气":
                        Intent weather=new Intent(getActivity(),Weather.class);
                        startActivity(weather);
                        break;
                    case "数据":
                        // 给Intent对象添加barChart类
                        Intent intent=new Intent(getActivity(),SetData.class);
                        //启动
                        startActivity(intent);
                        break;
                    case "物流":
                        Intent logistics=new Intent(getActivity(),Logistics.class);
                        startActivity(logistics);
                        break;

                    case "更多":
                        builder.setTitle("提示").setMessage("还未完成").create().show();
                        Log.i(TAG, "onItemClick: 还未添加..");
                        break;
                }
            }
        });
    }
    private void initData() {
        //图标
        int icno[] = { R.mipmap.map, R.mipmap.weather, R.mipmap.data_statics ,R.mipmap.in_out,
                R.mipmap.more};
        //图标下的文字
        String [] name= {"地图", "天气", "数据", "物流", "更多"};
        dataList = new ArrayList<>();
        for (int i = 0; i <icno.length; i++) {
            Map<String, Object> map=new HashMap<>();
            map.put("img", icno[i]);
            map.put("text",name[i]);
            dataList.add(map);
        }
    }

}
