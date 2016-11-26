package com.example.kirito.animation360_1.support;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.example.kirito.animation360_1.R;

/**
 * Created by kirito on 2016.11.25.
 */

public class FloatMenuView extends LinearLayout {
    private static TranslateAnimation ta;

    public FloatMenuView(final Context context) {
        super(context);
        //View root = LayoutInflater.from(context).inflate(R.layout.menu_view_layout,null,false);
        View root = View.inflate(context,R.layout.menu_view_layout,null);
        LinearLayout ll = (LinearLayout) root.findViewById(R.id.menu_ll);
        ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,1.0f
                                        ,Animation.RELATIVE_TO_SELF,0);
        ta.setDuration(2000);
        ta.setFillAfter(true);
        ll.setAnimation(ta);
        //实现点击menuview外，隐藏menu显示悬浮球
        root.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatWindowManager fwm = FloatWindowManager.getInstance(getContext());
                fwm.removeMenuView();
                fwm.showFloatCircleView();
                return false;
            }
        });
        //通过addview方法，给布局文件添加view
        addView(root);
    }

    public void startAnimation(){
        ta.start();
    }
}
