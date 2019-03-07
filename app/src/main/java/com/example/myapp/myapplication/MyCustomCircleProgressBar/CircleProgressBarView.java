package com.example.myapp.myapplication.MyCustomCircleProgressBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.example.myapp.myapplication.R;

public class CircleProgressBarView extends View {
    private Paint progressPaint;
    private  float sweepAngle;//背景圆弧扫过的角度
    private CircleBarAnim anim;
    private Paint bgPaint;//绘制背景圆弧的画笔
    private Paint textPaint;//绘制文本的画笔

    private float maxProgress;//可以更新的进度条数值

    private float bgSweepAngle;//进度条圆弧扫过的角度
    private float startAngle;//背景圆弧的起始角度
    private  String TAG="------------------------";
    private RectF mRectF;//绘制圆弧的矩形区域
    private float barWidth;//圆弧进度条宽度
    private int defaultSize;//自定义View默认的宽高
    private float maxSweepAngle;
    private int progressColor;//进度条圆弧颜色
    private int bgColor;//背景圆弧颜色
    private int circleWide;//圆弧的宽
    private float textHeight;
    private String circleText;
    private float circleTextSize;//圆弧中文本的大小
    private int circleTextColor;//圆弧中文本的颜色


    public CircleProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mRectF = new RectF();

        anim = new CircleBarAnim();
        //进度条画笔
        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.STROKE);//只描边，不填充
        progressPaint.setAntiAlias(true);//设置抗锯齿

        //进度条背景画笔
        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.STROKE);//只描边，不填充
        bgPaint.setAntiAlias(true);//设置抗锯齿

        //文本画笔
        textPaint=new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
//        float top = fontMetrics.top;//为基线到字体上边框的距离
//        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离
        float ascent = fontMetrics.ascent;//为基线到字体顶部的距离
//        Log.i(TAG, "init: "+Double.toString(ascent));

//        textHeight=-top/2-bottom/2;
        textHeight=-ascent;
        defaultSize = DpOrPxUtils.dip2px(context,200);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CircleProgressBarView);

        progressColor = typedArray.getColor(R.styleable.CircleProgressBarView_progress_color,Color.GREEN);//默认为绿色
        bgColor = typedArray.getColor(R.styleable.CircleProgressBarView_bg_color,Color.GRAY);//默认为灰色
        startAngle = typedArray.getFloat(R.styleable.CircleProgressBarView_start_angle,0);//默认为0
        sweepAngle = typedArray.getFloat(R.styleable.CircleProgressBarView_sweep_angle,360);//默认为360
        circleText=typedArray.getString(R.styleable.CircleProgressBarView_circle_text);
        circleTextSize=typedArray.getDimension(R.styleable.CircleProgressBarView_circle_textSize, DpOrPxUtils.sp2px(context,20));//默认为20sp
        circleTextColor=typedArray.getColor(R.styleable.CircleProgressBarView_circle_textColor,Color.BLACK);
        maxSweepAngle=sweepAngle;
        barWidth = typedArray.getDimension(R.styleable.CircleProgressBarView_bar_width,DpOrPxUtils.dip2px(context,10));//默认为10dp
        typedArray.recycle();//typedArray用完之后需要回收，防止内存泄漏


        progressPaint.setColor(progressColor);
        progressPaint.setStrokeWidth(barWidth);
        bgPaint.setColor(bgColor);
        bgPaint.setStrokeWidth(barWidth);
        textPaint.setColor(circleTextColor);
        textPaint.setTextSize( circleTextSize);
    }
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制圆弧
//        canvas.drawArc(rectF,270,sweepAngle,false,progressPaint);//这里角度0对应的是三点钟方向，顺时针方向递增
        bgSweepAngle=maxProgress*360;
        canvas.drawArc(mRectF,startAngle, bgSweepAngle,false,bgPaint);
        canvas.drawArc(mRectF,startAngle,sweepAngle,false, progressPaint);
        Log.i(TAG, "onDraw: --------------------------"+circleWide);
        if(circleText!=null){
            canvas.drawText( circleText,circleWide/2,circleWide/2+textHeight,textPaint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = measureSize(defaultSize, heightMeasureSpec);
        int width = measureSize(defaultSize, widthMeasureSpec);
        int min = Math.min(width, height);// 获取View最短边的长度
        circleWide=min;
        setMeasuredDimension(min, min);// 强制改View为以最短边为长度的正方形
        if(min >= barWidth*2){//这里简单限制了圆弧的最大宽度
            mRectF.set(barWidth/2,barWidth/2,min-barWidth/2,min-barWidth/2);
        }

    }
    private int measureSize(int defaultSize,int measureSpec) {
        int result =defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //EXACTLY精确值,为指定值或match_parent
        //AT_MOST最大值,一般为wrap_content
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }
    private class CircleBarAnim  extends Animation {
        public CircleBarAnim(){
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if(maxSweepAngle>bgSweepAngle){
                sweepAngle = interpolatedTime *bgSweepAngle;
            }else {
                sweepAngle = interpolatedTime *maxSweepAngle;
            }
            postInvalidate();
        }
    }
    //写个方法给外部调用，用来设置动画时间
    //两个参数,第一个参数为进度转动到角度的时间,第二个参数为进度条显示的最大值
    public void setProgressNum(int time,float progressNum) {
        this.maxProgress = progressNum;
        anim.setDuration(time);
        this.startAnimation(anim);
    }
    public void setProgressNum(int time) {
        this.maxProgress = 1f;
        anim.setDuration(time);
        this.startAnimation(anim);
    }

}
