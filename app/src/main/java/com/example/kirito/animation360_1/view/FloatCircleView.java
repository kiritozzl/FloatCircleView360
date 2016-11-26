package com.example.kirito.animation360_1.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.kirito.animation360_1.R;

/**
 * Created by kirito on 2016.11.24.
 */

public class FloatCircleView extends View {
    public int width = 150;
    public int height = 150;
    private Paint circlePaint;
    private Paint textPaint;
    private boolean isMove = false;

    private static final String TAG = "FloatCircleView";
    private Bitmap bitmap;

    //三个构造函数都必须调用，否则无法正常使用自定义view
    public FloatCircleView(Context context) {
        this(context,null);
    }

    public FloatCircleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FloatCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public void initPaint(){
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(25);
        textPaint.setColor(Color.RED);
        textPaint.setFakeBoldText(true);

        Bitmap bp = BitmapFactory.decodeResource(getResources(), R.drawable.a);
        //获取缩略图
        bitmap = bp.createScaledBitmap(bp,150,150,true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //静止时显示圆球
        if (!isMove){
            canvas.drawCircle(width / 2,width / 2,width / 2,circlePaint);

            String text = "50%";
            float textWidth = textPaint.measureText(text);
            float x = (width - textWidth) / 2;
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            //ascent是负的，descent是正的，dy是正的
            float dy = -(metrics.ascent + metrics.descent) / 2;
            float y = height / 2 + dy;

            canvas.drawText(text,x,y,textPaint);
        }else {//移动时显示图片
            canvas.drawBitmap(bitmap,0,0,null);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width,height);
    }

    //设置flag是否移动状态
    public void setState(boolean Moving){
        this.isMove = Moving;
        //invalidate会促使再次调用onDraw
        invalidate();
    }
}
