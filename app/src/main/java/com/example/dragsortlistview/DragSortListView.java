package com.example.dragsortlistview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;

import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

public class DragSortListView extends ListView {

    private int downY,downX;
    private int moveY,moveX;
    private int offsetY,offsetX;
    private boolean isLongPase;
    private boolean isHavePosition;
    private int itemPosition;


    private Bitmap itemBitmap;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private ImageView imageView;

    public DragSortListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                itemPosition = pointToPosition(downX,downY);
                //当点击区域有时才能拖拽
                isHavePosition = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isLongPase){
                    moveX = (int) ev.getX();
                    moveY = (int) ev.getY();
                    offsetX = moveX - offsetX;
                    offsetY = moveY - offsetY;
                    updateImagView();
                }
                break;
            case MotionEvent.ACTION_UP:
                closeDrag();
                break;
        }
        return super.onTouchEvent(ev);
    }



    private void startDrag(){
        final View itemView = getItemView(itemPosition);
        //绘制缓存
        itemView.setDrawingCacheEnabled(true);
        //提取缓存
        itemBitmap = Bitmap.createBitmap(itemView.getDrawingCache());
        //清理
        itemView.setDrawingCacheEnabled(false);

        imageView = new ImageView(getContext());
        imageView.setImageBitmap(itemBitmap);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        params = new WindowManager.LayoutParams();
        params.alpha = 0.8f;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP;
        params.y = downY;
        windowManager.addView(imageView,params);
    }

    public void setLongPase(){
        if (isHavePosition){
            this.isLongPase = true;
            startDrag();
        }
    }

    /**
     * 根据Adapter中的位置获取对应ListView的条目
     */
    private View getItemView(int position) {
        if (position < 0 || position >= getAdapter().getCount()) {
            return null;
        }
        int index = position - getFirstVisiblePosition();
        return getChildAt(index);
    }

    //刷新
    private void updateImagView(){
        if (imageView != null){
            params.y = moveY;
            windowManager.updateViewLayout(imageView,params);
        }
    }

    private void closeDrag(){
        isHavePosition = false;
        isLongPase = false;
        if (imageView != null){
            windowManager.removeView(imageView);
            itemBitmap = null;
            imageView = null;
        }

    }

}


