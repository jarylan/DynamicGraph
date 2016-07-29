package com.jarylan.drawgraph.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/11/6 0006.
 */
public class ObserveScrollView extends HorizontalScrollView{

    private onScrollChangeListener listener;
    private int lastScrollX;
    private Timer timer;
    private TimerTask timerTask ;

    public ObserveScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public ObserveScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

        public ObserveScrollView(Context context) {
        super(context);
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(listener!=null)
        listener.onScrollChange(l);
//        Log.e("Tag", "---------------l=" + l + "  t=" + t + "   oldl=" + oldl + "  oldt=" + oldt);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP){
//        Log.e("Tag", "==========UP = " + getX() + "   " + getScrollX()) ;
            stopTimer();
            if(timer==null){
            timer = new Timer();
            }
            if(timerTask == null){
                timerTask = new TimerTask() {
                    @Override
                    public void run() {

                        if(lastScrollX == getScrollX()){
                            Log.e("Tag", "==========UP  滚动结束啦 ") ;
                            stopTimer();
                            if(listener!=null)
                            listener.onScrollStop(lastScrollX);
                        }else{
                            lastScrollX = getScrollX();
                        }

                    }
                };
            }
            if(timer!=null&&timerTask!=null){
                timer.schedule(timerTask,80,80);
            }
        }
        return super.onTouchEvent(ev);
    }
    private void stopTimer(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        if(timerTask!=null){
            timerTask.cancel();
            timerTask = null;
        }
    }

    public void setOnScrollChangeListener(onScrollChangeListener listener){
        this.listener = listener;
    }

    public interface onScrollChangeListener{
        void onScrollChange(int x);
        void onScrollStop(int x);
    }


}
