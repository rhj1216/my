package com.example.myapp.myapplication.guideInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.myapp.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private LinearLayout linear;
    private ImageView light_dots;
    private List<View> viewList;
    private  int distance;
    private  ImageView first_dot;
    private  ImageView second_dot;
    private  ImageView third_dot;
    private Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_interface);
        initData();
        initView();
        viewPager.setAdapter(new MyPagerAdapter(viewList));
        addDots();
        moveDots();
        viewPager.setPageTransformer(true,new DepthPageTransformer());
    }

    private void initData() {
        viewList = new ArrayList<View>();
        LayoutInflater lf = getLayoutInflater().from(MainActivity.this);
        View view1 = lf.inflate(R.layout.activity_guide_first, null);
        View view2 = lf.inflate(R.layout.activity_guide_second, null);
        View view3 = lf.inflate(R.layout.activity_guide_third, null);
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
    }

    private void initView() {
        viewPager=findViewById(R.id.viewpager);
        linear =findViewById(R.id.linear);
        light_dots = (ImageView) findViewById(R.id.light_dots);
        start=findViewById(R.id.start);
        start.setOnClickListener(this);
    }

    private void addDots() {
        first_dot = new ImageView(this);
        first_dot.setImageResource(R.drawable.gray_dot);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 40, 0);
        linear.addView(first_dot, layoutParams);
        second_dot = new ImageView(this);
        second_dot.setImageResource(R.drawable.gray_dot);
        linear.addView(second_dot, layoutParams);
        third_dot = new ImageView(this);
        third_dot.setImageResource(R.drawable.gray_dot);
        linear.addView(third_dot, layoutParams);
        setClickListener();

    }

    private void setClickListener() {
        first_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
        second_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        third_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);

            }
        });
    }

    private void moveDots() {
        light_dots.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获得两个圆点之间的距离
                distance = linear.getChildAt(1).getLeft() - linear.getChildAt(0).getLeft();
                light_dots.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滚动时小白点移动的距离，并通过setLayoutParams(params)不断更新其位置
//                Log.i("123", "onPageScrolled: "+Double.toString(positionOffset));
                float leftMargin = distance * (position + positionOffset);
               RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) light_dots.getLayoutParams();
                params.leftMargin = (int) leftMargin;
                light_dots.setLayoutParams(params);
                if(position==2){
                    start.setVisibility(View.VISIBLE);
                }else{
                    start.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //页面跳转时，设置小圆点的margin
                float leftMargin = distance * position;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) light_dots.getLayoutParams();
                params.leftMargin = (int) leftMargin;
                light_dots.setLayoutParams(params);
                if(position==2){
                    start.setVisibility(View.VISIBLE);
                }else{
                    start.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent mainActivity=new Intent(this, com.example.myapp.myapplication.MainActivity.class);
        mainActivity.putExtra("runWelcomeScreen",false);
        startActivity(mainActivity);
        this.finish();
    }
}
