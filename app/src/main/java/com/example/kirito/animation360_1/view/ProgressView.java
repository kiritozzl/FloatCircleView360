package com.example.kirito.animation360_1.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by kirito on 2016.11.25.
 */

public class ProgressView extends View {
    private static final String TAG = "ProgressView";
    private Paint circlePaint;
    private Paint textPaint;
    private Paint progressPaint;
    private int width = 200;
    private int height = 200;

    private Path mPath = new Path();
    private int progress = 80;
    private int currentProgress = 0;
    private int count = 50;
    private int max = 100;

    private Bitmap bitmap;
    private Canvas bitmapCanvas;

    private GestureDetector detector;
    private boolean isSingleTap;
    private Handler handler = new Handler();
    private DoubleRunnable doubleRun;
    private SingleRunnable singleRun;

    public ProgressView(Context context) {
        this(context,null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.argb(0xff,0x3a,0x8c,0x6c));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(20);
        textPaint.setColor(Color.WHITE);

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(Color.argb(0xff,0x4e,0xc9,0x63));
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //为了达到悬浮球色彩叠加的效果，使用了下面两个参数
        bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);

        detector = new GestureDetector(new MyGestureDetectorListener());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //自定义view的touch事件交由MyGestureDetectorListener处理
                return detector.onTouchEvent(event);
            }
        });
        setClickable(true);
    }

    //自定义手势监听器，单击，双击
    class MyGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            isSingleTap = false;
            doubleRun = new DoubleRunnable();
            handler.postDelayed(doubleRun,200);
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            isSingleTap = true;
            currentProgress = progress;
            singleRun = new SingleRunnable();
            handler.postDelayed(singleRun,100);
            return super.onSingleTapConfirmed(e);
        }
    }

    class SingleRunnable implements Runnable{
        @Override
        public void run() {
            if (count > 0){
                count--;
                invalidate();
                handler.postDelayed(singleRun,100);
            }else {
                count = 50;
                handler.removeCallbacks(singleRun);
            }
        }
    }

    class DoubleRunnable implements Runnable{

        @Override
        public void run() {
            if (currentProgress < progress){
                currentProgress++;
                invalidate();
                handler.postDelayed(doubleRun,100);
            }else {
                currentProgress = 0;
                handler.removeCallbacks(doubleRun);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        bitmapCanvas.drawCircle(width / 2,height / 2,width / 2,circlePaint);

        mPath.reset();
        float y = (1 - (float)currentProgress / max) * height;
        mPath.moveTo(width,y);
        mPath.lineTo(width,height);
        mPath.lineTo(0,height);
        mPath.lineTo(0,y);

        /*
        当我们需要在屏幕上形成画线时，Path类的应用是必不可少的，而Path类的lineTo和quadTo方法实现的绘制线路形式也是不一样的，
        下面就以代码的实现来直观的探究这两个方法的功能实现区别；

        1. Path--->quadTo(float x1, float y1, float x2, float y2):

     该方法的实现是当我们不仅仅是画一条线甚至是画弧线时会形成平滑的曲线，该曲线又称为"贝塞尔曲线"(Bezier curve)，
     其中，x1，y1为控制点的坐标值，x2，y2为终点的坐标值；

    贝塞尔曲线的形成，就比如我们把一条橡皮筋拉直，橡皮筋的头尾部对应起点和终点，然后从拉直的橡皮筋中选择任意一点
    （除头尾对应的点外）扯动橡皮筋形成的弯曲形状，而那个扯动橡皮筋的点就是控制点；
         */
        if (!isSingleTap){
            //设置d使得随着水柱的上升，波浪线越来越平缓直至为直线
            float d = (1 - (float)currentProgress / progress) * 10;
            //循环一次只能有一个正弦周期图像，循环五次才能覆盖整个圆
            for (int i = 0; i < 5; i++) {
                //rQuadTo绘制波浪线，实现水面波动效果
                mPath.rQuadTo(10,d,20,0);
                mPath.rQuadTo(10,-d,20,0);
            }
        }else {
            //实现振幅逐渐减小
            float d = (float) count / 50 * 10;
            //count对2取模是使得一般时间绘制正弦函数图像，一半时间绘制与正弦反向的余弦图像，实现水面波动效果
            if (count % 2 == 0){
                for (int i = 0; i < 5; i++) {
                    mPath.rQuadTo(20,d,40,0);
                    mPath.rQuadTo(20,-d,40,0);
                }
            }else {
                for (int i = 0; i < 5; i++) {
                    mPath.rQuadTo(20,-d,40,0);
                    mPath.rQuadTo(20,d,40,0);
                }
            }
        }
        mPath.close();
        bitmapCanvas.drawPath(mPath,progressPaint);

        String text = (float)currentProgress / max * 100 + "%";
        float textWidth = textPaint.measureText(text);
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        float y1 = -(metrics.ascent + metrics.descent);
        //(height + y1) / 2：表示文字的基线（baseline）
        bitmapCanvas.drawText(text,(width - textWidth) / 2,(height + y1) / 2,textPaint);

        canvas.drawBitmap(bitmap,0,0,null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width,height);
    }
}
