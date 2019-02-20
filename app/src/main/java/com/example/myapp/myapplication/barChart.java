package com.example.myapp.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class barChart extends AppCompatActivity {
    private BarChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart);
        mChart = findViewById(R.id.barChart);
        showChart(getBarData());
    }

    /**
     * 显示柱状图表
     *
     * @param barData
     */
    private void showChart(BarData barData) {
        //设置没有数据时候展示的文本
        mChart.setNoDataText("目前没有数据哦");
        // 设置描述
//        mChart.setDescription("测试柱状图");
        mChart.setDescription("");
        // 设置可触摸
        mChart.setTouchEnabled(true);
        // 设置图表数据
        mChart.setData(barData);
        // 设置动画
        mChart.animateY(3000);
        //.设置是否绘制表格背景
//        mChart.setDrawGridBackground(true);
        mChart.setDrawGridBackground(false);
        //设置表格网格背景的颜色
//        mChart.setGridBackgroundColor(getResources().getColor(R.color.lightBlue));
    }

    /**
     * 获取柱状数据
     *
     * @return
     */
    private BarData getBarData() {

        //设置集合,存放X轴的数据
        ArrayList<String> xValues = new ArrayList<String>();

        xValues.add("注册人数");
        xValues.add("扫码总数");
        xValues.add("个人扫码数");
        xValues.add("今日扫码数量");

        //创建集合,存放每个柱子的数据
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();

        //获取另一个Activity传值的值
        Intent intent = getIntent();
        int Enrolment=intent.getIntExtra("Enrolment",0);
        int Total=intent.getIntExtra("Total",0);
        int Single=intent.getIntExtra("Single",0);
        int TodayUsage=intent.getIntExtra("TodayUsage",0);

        //设置柱子上的颜色
        yValues.add(new BarEntry(Enrolment, 0));
        yValues.add(new BarEntry(Total, 1));
        yValues.add(new BarEntry(Single, 2));
        yValues.add(new BarEntry(TodayUsage, 3));

        //创建BarDateSet对象，其实就是一组柱形数据
        //存放Y轴数据
        BarDataSet barDataSet = new BarDataSet(yValues, "VIN码用户使用情况");

//        //设置是否显示柱子上面的数值
//        barDataSet.setDrawValues(true);

        //创建颜色集合
        ArrayList<Integer> colors = new ArrayList<Integer>();

        //设置柱形的颜色
        colors.add(getResources().getColor(R.color.blue));
        colors.add(getResources().getColor(R.color.yellow));
        colors.add(getResources().getColor(R.color.red));
        colors.add(getResources().getColor(R.color.purple));

        barDataSet.setColors(colors);

        //创建柱的数据对象,把X轴的信息和柱状数据存入
        BarData barData = new BarData(xValues,barDataSet);

        return barData;
    }
}

