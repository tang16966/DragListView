package com.example.dragsortlistview;

import android.content.Context;
import android.graphics.Bitmap;
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

    private int downY, downX;
    private int moveY, moveX;
    private int offsetY, offsetX;
    private boolean isLongPase;
    private boolean isHavePosition;
    private int itemPosition;
    private View itemView;


    private Bitmap itemBitmap;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private ImageView imageView;
    private DragSortAdapter dragSortAdapter;

    public DragSortListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                itemPosition = pointToPosition(downX, downY);
                //当点击区域有时才能拖拽
                isHavePosition = true;
                dragSortAdapter = (DragSortAdapter) getAdapter();
                isPase = true;
                longPase();
                break;
            case MotionEvent.ACTION_MOVE:
                isPase = false;
                thread.interrupt();
                if (isLongPase) {
                    moveX = (int) ev.getX();
                    moveY = (int) ev.getY();
                    offsetX = moveX - offsetX;
                    offsetY = moveY - offsetY;
                    updateImagView();
                    updateItem();
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                isPase = false;
                thread.interrupt();
                closeDrag();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void startDrag() {
        itemView = getItemView(itemPosition);
        //绘制缓存
        itemView.setDrawingCacheEnabled(true);
        //提取缓存
        itemBitmap = Bitmap.createBitmap(itemView.getDrawingCache());
        //清理
        itemView.setDrawingCacheEnabled(false);
        itemView.setVisibility(INVISIBLE);

        imageView = new ImageView(getContext());
        imageView.setImageBitmap(itemBitmap);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        params = new WindowManager.LayoutParams();
        params.alpha = 0.8f;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP;
        params.y = downY;
        windowManager.addView(imageView, params);
    }

    private boolean isPase;
    private Thread thread;
    private void longPase(){
        thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);
                    if (isPase){
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           if (msg.what == 0){
               setLongPase();
           }
        }
    };

    public void setLongPase() {
        if (isHavePosition) {
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
    private void updateImagView() {
        if (imageView != null) {
            params.y = moveY;
            windowManager.updateViewLayout(imageView, params);
        }
    }

    private void updateItem() {
        int position = pointToPosition(moveX, moveY);

        //隐藏后就变-1了，不知到为啥，索性这样判断
        if (position == -1) {

        } else {
            if (itemPosition != position) {
                dragSortAdapter.dragItem(itemPosition, position);
                itemView.setVisibility(VISIBLE);
                itemView = getItemView(position);
                itemView.setVisibility(INVISIBLE);
                itemPosition = position;
                //bug跟新数据后就不隐藏了
            }


        }

    }

    private void closeDrag() {
        if (imageView != null) {
            isHavePosition = false;
            isLongPase = false;
            itemView.setVisibility(VISIBLE);
            windowManager.removeView(imageView);
            itemBitmap = null;
            imageView = null;
        }

    }

}


