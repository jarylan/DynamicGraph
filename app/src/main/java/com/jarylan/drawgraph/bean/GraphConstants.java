package com.jarylan.drawgraph.bean;

/**
 * Created by snaillove on 2016/7/14.
 */
public class GraphConstants {

    //低温
    public static float hypothermia=35.9f;
    //低烧
    public static float lowFever = 37.7f;
    //高烧
    public static float highFever = 38.6f;

    /**
     *   x轴 分为几个刻度
     * */
    public static final float XSCALE_NUMBER = 6 ;

    /**
     * 上下左右留空白 ，不会显示的太挤
     *  背景画布ChartView_bg  X 起始位置（x从哪开始算）   单位dp
     * */
    public static final float X_LEFT_PADDING = 40 ;
    /**
     * 上下左右留空白 ，不会显示的太挤
     *  背景画布ChartView_bg ,内容画布ChartView_content  Y起始位置（Y从哪开始算）   单位dp
     * */
    public static final float Y_TOP_PADDING = 10 ;
    /**
     * 上下左右留空白 ，不会显示的太挤
     *  背景画布ChartView_bg ,内容画布ChartView_content  Y结束位置（Y算到哪里）   单位dp
     * */
    public static final float Y_BOTTOM_PADDING = 30 ;

    /**
     * 文字大小 单位dp
     * */
    public static final float SIZE_TEXT = 12 ;

    /**
     * 圆的大小  单位dp
     * */
    public static final float SIZE_CICLE = 5;

    /**
     * 标杆圆的大小  单位dp
     * */
    public static final float Benchmarking_SIZE_CICLE = 6;
}
