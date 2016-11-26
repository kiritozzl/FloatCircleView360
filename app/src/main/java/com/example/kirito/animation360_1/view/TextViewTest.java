package com.example.kirito.animation360_1.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by kirito on 2016.11.25.
 */

public class TextViewTest extends View {
    private int width = 200;
    private int height = 200;
    private Paint textPaint;
    private static final String TAG = "TextViewTest";

    public TextViewTest(Context context) {
        this(context,null);
    }

    public TextViewTest(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
       /* Paint textPaint = new Paint( Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize( 55);
        textPaint.setColor( Color.BLACK);

        // FontMetrics对象
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        String text = "abcdefghijklmnopqrstu";

        // 计算每一个坐标
        float baseX = 0;
        float baseY = 100;
        float topY = baseY + fontMetrics.top;
        float ascentY = baseY + fontMetrics.ascent;
        float descentY = baseY + fontMetrics.descent;
        float bottomY = baseY + fontMetrics.bottom;

//        Log.e(TAG, "onDraw: ascent---"+ fontMetrics.ascent);
//        Log.e(TAG, "onDraw: topy,ascenty,descenty,botttomy---"+topY+":"+ascentY+":"+descentY+":"+bottomY+":" );

        // 绘制文本
        canvas.drawText( text, baseX, baseY, textPaint);

        // BaseLine描画
        Paint baseLinePaint = new Paint( Paint.ANTI_ALIAS_FLAG);
        baseLinePaint.setColor( Color.RED);
        canvas.drawLine(0, baseY, getWidth(), baseY, baseLinePaint);

        // Base描画
        canvas.drawCircle( baseX, baseY, 5, baseLinePaint);

        // TopLine描画
        Paint topLinePaint = new Paint( Paint.ANTI_ALIAS_FLAG);
        topLinePaint.setColor( Color.LTGRAY);
        canvas.drawLine(0, topY, getWidth(), topY, topLinePaint);

        // AscentLine描画
        Paint ascentLinePaint = new Paint( Paint.ANTI_ALIAS_FLAG);
        ascentLinePaint.setColor( Color.GREEN);
        canvas.drawLine(0, ascentY, getWidth(), ascentY, ascentLinePaint);

        // DescentLine描画
        Paint descentLinePaint = new Paint( Paint.ANTI_ALIAS_FLAG);
        descentLinePaint.setColor(Color.GRAY);
        canvas.drawLine(0, descentY, getWidth(), descentY, descentLinePaint);

        // ButtomLine描画
        Paint bottomLinePaint = new Paint( Paint.ANTI_ALIAS_FLAG);
        bottomLinePaint.setColor( Color.MAGENTA);
        canvas.drawLine(0, bottomY, getWidth(), bottomY, bottomLinePaint);*/

        Paint paint = new Paint();

        paint.setAntiAlias(true);

        paint.setColor(Color.RED);

        paint.setStyle(Paint.Style.STROKE);//设置为空心

        paint.setStrokeWidth(3);

        Paint circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        canvas.drawCircle(100,100,100,circlePaint);

        Path path1 = new Path();

        for (int i = 0; i < 5; i++) {
            path1.rQuadTo(200,100,200,0);
            //path1.rQuadTo(200,-100,200,0);
        }

        path1.close();//把开始的点和最后的点连接在一起，构成一个封闭图形
            /*
             * 最重要的就是movtTo和close,如果是Style.FILL的话，不设置close,也没有区别，可是如果是STROKE模式，
             * 如果不设置close,图形不封闭。
             *
             * 当然，你也可以不设置close，再添加一条线，效果一样。
             */
        canvas.drawPath(path1, paint);
    }


}
