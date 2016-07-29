package com.jarylan.drawgraph;

import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jarylan.drawgraph.bean.GraphConstants;
import com.jarylan.drawgraph.bean.MyPoint;
import com.jarylan.drawgraph.bean.PointBean;
import com.jarylan.drawgraph.graph.ChartView_bg;
import com.jarylan.drawgraph.graph.ChartView_content;
import com.jarylan.drawgraph.utils.Tools;
import com.jarylan.drawgraph.view.ObserveScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private RelativeLayout relaerLei;
    private LinearLayout relaerWai;
    private ObserveScrollView horizontalScrollView;
    private ChartView_bg view_lei;
    private ChartView_content view_wai;
    private boolean hasMeasured1 = false;
    private boolean hasMeasured2 = false;
    private List<String> y_list;
    private List<PointBean> templist = new ArrayList<PointBean>();
    private int height;
    private int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        initData();
        initChartview();
        initListener();
    }

    private void assignViews(){
        //view
        relaerLei = (RelativeLayout) findViewById(R.id.relaer_lei);
        relaerWai = (LinearLayout) findViewById(R.id.relaer_wai);
        horizontalScrollView = (ObserveScrollView) findViewById(R.id.horizontalScrollView);
        view_lei = new ChartView_bg(this);
        view_wai = new ChartView_content(this);
    }
    private void initData(){
        // y轴的值集合
        y_list = new ArrayList<String>();
        y_list.add("41℃");
        y_list.add("40℃");
        y_list.add("39℃");
        y_list.add("38℃");
        y_list.add("37℃");
        y_list.add("36℃");
        y_list.add("35℃");
        y_list.add("");
    }
    private void initChartview(){
        //绘制内部面板；
        ViewTreeObserver vto = relaerLei.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (hasMeasured1 == false) {
                    int height = relaerLei.getMeasuredHeight();
                    int width = relaerLei.getMeasuredWidth();
                    Log.e(TAG, "----------绘制内部面板 height=" + height + ",width=" + width);
                    // x轴的值集合
                    List<String> x_list = new ArrayList<String>();
                    for (int i = 1; i <= 31; i++) {
                        x_list.add(i + "");
                    }

                    view_lei.SetInfo(x_list, y_list, width, height);
                    relaerLei.addView(view_lei);
                    hasMeasured1 = true;
                    view_lei.isShowBenchmarking = true;
            }
                return true;
            }
        });
        //绘制外部面板
        ViewTreeObserver vto2 = relaerWai.getViewTreeObserver();
        vto2.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (hasMeasured2 == false) {
                    height = relaerWai.getMeasuredHeight();
                    width = relaerWai.getMeasuredWidth();
                    Log.e(TAG, "------------绘制外部面板 height=" + height + ",width=" + width);
                    for (int i = 0; i < 40; i++) {
                        PointBean pen = new PointBean();
                        pen.setDay("" + Tools.getNowTime());
                        pen.setMonth(07 + "/" + 28);
                        pen.setTime(14 + ":" + (10+i));
                        if (i % 2 == 0) {
                            pen.setY((returnRandom()+0.5) + "");
                        } else {
                            pen.setY(returnRandom() + "");
                        }

                        pen.setYear("" + 2016);
                        templist.add(pen);
                    }
                    view_wai.SetInfo(templist,y_list, width, height, y_list);
                    relaerWai.addView(view_wai);
                    hasMeasured2 = true;
                }
                return true;
            }
        });
    }

    /**
     * 返回随机数
     * */
    private float returnRandom(){
        int max=42;
        int min=35;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return (float) (s-0.5);
    }

    private void initListener(){
        horizontalScrollView.setOnScrollChangeListener(new ObserveScrollView.onScrollChangeListener() {
            @Override
            public void onScrollChange(int x) {


                int max = (int) ((view_wai.list1.size() - 1) * view_wai.xScale);
                Log.e(TAG, "============偏移量=" + x + "    最大偏移量 = " + max);
                if (x < 0 | x > max) {
                    return;
                }

                float centerbiaogan = (float) (x + width / 2.0);//标杆的X坐标
                //计算包含点的索引(向左右各取一半的xScale 表示该点范围内,这就是为什么 - view_wai.xScale / 2 ；方便滑到两点之间变换标杆的颜色 )
                int index = (int) ((centerbiaogan - (width / 2 - view_wai.xScale / 2)) / view_wai.xScale);
                //判断该点所在温度范围 改变标杆颜色
                final float pointTempY = Float.parseFloat(view_wai.list1.get(index).getY());
                updateBenchmarking(pointTempY);
//                Log.e(Tag,"============索引="+index );
                MyPoint thisPoint = view_wai.list1.get(index).getPoint();
                float tempx = thisPoint.x - centerbiaogan;
//                Log.e(Tag,"与标杆之间距离 = "+tempx);
                if (tempx == 0) { // 标杆刚好在该点上 , 直接将该点Y赋给标杆Y
                    view_lei.benchmarkingY = thisPoint.y;
                    updateBenchmarking();
                    return;
                }

                MyPoint leftpoint = new MyPoint();
                MyPoint rightpoint = new MyPoint();
                if (tempx > 0) { //标杆在该点 左  边    ，取索引前一个点
//                    Log.e(Tag,"===========标杆在该点 左  边" );
                    leftpoint = view_wai.list1.get(index - 1).getPoint();
                    rightpoint = thisPoint;
                } else if (tempx < 0) {//标杆在该点 右  边    ，取索引后一个点
//                    Log.e(Tag,"============标杆在该点 右  边" );
                    leftpoint = thisPoint;
                    rightpoint = view_wai.list1.get(index + 1).getPoint();

                }

//                //第一种方法 三角函数  tanθ=a/b = c/d   c就是第二个点与轨迹之间的高度
//                //判断曲线是上升的还是下降的
//                float tempy = leftpoint.y - rightpoint.y;
//                float c = 0;
//                if (tempy > 0) {//下降的
//
//                    c = (rightpoint.x - centerbiaogan) * Math.abs(leftpoint.y - rightpoint.y) /  (Math.abs(leftpoint.x - rightpoint.x)); //
//                    view_lei.benchmarkingY = rightpoint.y + c;
////                    Log.e(Tag,"++++++++++++下降的  = " + c);
//                } else if (tempy < 0) {//上升的
//                    c = (centerbiaogan - leftpoint.x) * Math.abs(leftpoint.y - rightpoint.y) /  (Math.abs(leftpoint.x - rightpoint.x)); //
//                    view_lei.benchmarkingY = leftpoint.y + c;
////                    Log.e(Tag,"++++++++++++上升的  = " + c);
//                } else if (tempy == 0) {//同一线上的
//                    Log.e("-----------","============同一线上的");
//                    view_lei.benchmarkingY = thisPoint.y;
//                }
//                Log.e(Tag,"++++++++++++点跟轨迹高度  = " + c);

                //第二种方法 ：    斜截式y = kx + b ; 直线斜率公式 k=(y2-y1)/(x2-x1) ,同一直线上k与b 相等
                float k = (leftpoint.y-rightpoint.y)/(leftpoint.x-rightpoint.x);
                float b = leftpoint.y - k * leftpoint.x;
                view_lei.benchmarkingY = k * centerbiaogan + b ;
                updateBenchmarking();


            }

            @Override
            public void onScrollStop(int x) {
                if (templist.size() == 0) {
                    return;
                }
                //计算包含点的索引
                float centerbiaogan = (float) (x + width / 2.0);//标杆的X坐标
                int index = (int) ((centerbiaogan - (width / 2 - view_wai.xScale / 2)) / view_wai.xScale);
                MyPoint thisPoint = view_wai.list1.get(index).getPoint();
                Log.e("Tag", "==========UP  滚动结束啦 + 索引" + index + "     X=" + thisPoint.x + "    Y" + thisPoint.y);
                float tempx = thisPoint.x - centerbiaogan;
                if (tempx == 0) {//标杆正好在点上
                    return;
                }
                horizontalScrollView.smoothScrollTo((int)(thisPoint.x - width / 2), (int)(thisPoint.y));


            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView.smoothScrollTo(templist.size() * width / 6 + width, 0);
            }
        }, 200);
    }

    /**
     * 更新标杆
     */
    private void updateBenchmarking(){
        mHandler.sendEmptyMessage(0x11);
    }
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x11:
                    view_lei.invalidate();
                    break;
            }
        }
    };

    private void updateBenchmarking(final float pointTempY){//标杆颜色比点颜色深一点
        if(pointTempY>= GraphConstants.highFever){//高烧
            view_lei.benchmarkingPaint.setColor(Color.parseColor("#df6a9b"));
            view_lei.ciclePaint.setColor(Color.parseColor("#ff8abb"));
        }else if(pointTempY< GraphConstants.highFever && pointTempY >= GraphConstants.lowFever){//低烧
            view_lei.benchmarkingPaint.setColor(Color.parseColor("#d5b933"));
            view_lei.ciclePaint.setColor(Color.parseColor("#f5d953"));
        }else if(pointTempY< GraphConstants.lowFever&&pointTempY>= GraphConstants.hypothermia){//正常
            view_lei.benchmarkingPaint.setColor(Color.parseColor("#80ad3b"));
            view_lei.ciclePaint.setColor(Color.parseColor("#a0cd5b"));
        }else if(pointTempY < GraphConstants.hypothermia){//低温
            view_lei.benchmarkingPaint.setColor(Color.parseColor("#3dabcd"));
            view_lei.ciclePaint.setColor(Color.parseColor("#5dcbed"));
        }
    }
}
