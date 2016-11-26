package com.example.kirito.animation360_1.support;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.kirito.animation360_1.view.FloatCircleView;

import java.lang.reflect.Field;

/**
 * Created by kirito on 2016.11.24.
 */

/**
 * FloatWindowManager管理悬浮球和悬浮菜单的显示
 */
public class FloatWindowManager implements View.OnTouchListener, View.OnClickListener {
    private Context mContext;
    private WindowManager wm;
    private FloatCircleView circleView;
    private static FloatWindowManager mInstance;
    private WindowManager.LayoutParams params;

    private float startX;
    private float startY;
    private boolean isClick;

    private FloatMenuView menuView;
    private static final String TAG = "FloatWindowManager";

    public FloatWindowManager(Context mContext) {
        this.mContext = mContext;
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        circleView = new FloatCircleView(mContext);
        circleView.setOnTouchListener(this);
        circleView.setOnClickListener(this);

        menuView = new FloatMenuView(mContext);
    }

    public static FloatWindowManager getInstance(Context context){
        if (mInstance == null){
            synchronized (FloatWindowManager.class){
                mInstance = new FloatWindowManager(context);
            }
        }
        return mInstance;
    }

    public void showFloatCircleView(){
        if (params == null){
            params = new WindowManager.LayoutParams();
            params.width = circleView.width;
            params.height = circleView.height;
            params.gravity = Gravity.LEFT | Gravity.TOP;
            params.x = 0;
            params.y = 0;
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            //不加下面这句悬浮球会出现黑色背景框
            params.format = PixelFormat.RGBA_8888;
        }
        wm.addView(circleView,params);
    }

    public int getWindowWidth(){
        return wm.getDefaultDisplay().getWidth();
    }

    public int getWindowHeight(){
        return wm.getDefaultDisplay().getHeight();
    }

    //通过反射获取状态栏的高
    public int getStatusHeight(){
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (int) field.get(o);
            return mContext.getResources().getDimensionPixelOffset(x);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                circleView.setState(true);
                float x = event.getRawX();
                float y = event.getRawY();
                float dx = x - startX;
                float dy = y - startY;
                params.x += dx;
                params.y += dy;
                //通过计算移动悬浮球的最初和最终的位置的，x,y坐标的差值，改变parames的坐标参数，最后更新view来实现随手指移动悬浮球的效果
                wm.updateViewLayout(circleView,params);
                //更新悬浮球的初始位置坐标
                startX = x;
                startY = y;
                break;
            case MotionEvent.ACTION_UP:
                circleView.setState(false);
                float up_x = event.getRawX();
                //如果手指离开屏幕时，圆球的位置小于屏幕宽度的一半，则停靠在屏幕最左侧；否则停靠在屏幕最右侧
                if (up_x > getWindowWidth() / 2){
                    params.x = (int) (getWindowWidth() - circleView.width);
                }else {
                    params.x = 0;
                }
                wm.updateViewLayout(circleView,params);
                break;
        }
        return false;
    }

    public void showFloatMenuView(){
        WindowManager.LayoutParams param = new WindowManager.LayoutParams();
        param.width = getWindowWidth();
        //指定参数的高，非常重要，这决定了最后能否实现点击屏幕，撤销menuView。这个height即为屏幕除状态栏之外的高
        param.height = getWindowHeight() - getStatusHeight();
        param.gravity = Gravity.BOTTOM | Gravity.LEFT;
        param.x = 0;
        param.y = 0;
        param.type = WindowManager.LayoutParams.TYPE_PHONE;
        //FLAG_NOT_FOCUSABLE:让window不能获得焦点，这样用户快就不能向该window发送按键事件及按钮事件
        //FLAG_NOT_TOUCH_MODAL:即使在该window在可获得焦点情况下，仍然把该window之外的任何event发送到该window之后的其他window.
        param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        param.format = PixelFormat.RGBA_8888;
        wm.addView(menuView,param);
    }

    @Override
    public void onClick(View v) {
        wm.removeViewImmediate(circleView);
        showFloatMenuView();
        menuView.startAnimation();
    }

    public void removeMenuView() {
        wm.removeViewImmediate(menuView);
    }
}
