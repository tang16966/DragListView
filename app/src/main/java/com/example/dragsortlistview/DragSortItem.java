package com.example.dragsortlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class DragSortItem extends LinearLayout {

    private float downX;
    private float moveX;
    private float offsetX;
    private boolean isSlidingOut;//是否滑出状态

    public DragSortItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        isSlidingOut = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                offsetX = moveX - downX;
                if (!isSlidingOut){
                    if (offsetX > -300 && offsetX < 0){
                        setPadding((int) offsetX,0, 0,0);
                        requestDisallowInterceptTouchEvent(true);
                    }
                }else {
                    if (offsetX < 300 && offsetX >0){
                        setPadding((int) offsetX-300,0, 0,0);
                        requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isSlidingOut){
                    if (offsetX < -150){
                        offsetX = -300;
                        isSlidingOut = true;
                    }
                    if (offsetX > -150) {
                        offsetX = 0;
                        isSlidingOut = false;
                    }
                }else {
                    if (offsetX > 0){
                        offsetX = 0;
                        isSlidingOut = false;
                    }
                }
                setPadding((int) offsetX,0, 0,0);

                break;
        }
        return false;
    }



}
