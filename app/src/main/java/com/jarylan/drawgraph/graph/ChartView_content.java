package com.jarylan.drawgraph.graph;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.jarylan.drawgraph.bean.GraphConstants;
import com.jarylan.drawgraph.bean.MyPoint;
import com.jarylan.drawgraph.bean.PointBean;
import com.jarylan.drawgraph.utils.Tools;

import java.util.List;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ChartView_content extends View {

	public float xScale;//X刻度
	public float xPoint = 0;
	public float YLength;//Y宽
	private float YScale ;//Y刻度
	private float YPoint = 0;
	private float textSize ;
	private float circleSize ;// 圆点的大小
	public int screenWidth;//整个曲线画布的宽
	public int screenHeight;//整个曲线画布的高
	private Context context;
	public List<PointBean> list1;//点的集合
	private List<String> listYLength;
	private Paint ciclePaint;
	private Paint ciclelinePaint;
	private Paint textPaint;

	public ChartView_content(Context context) {
		super(context);
		this.context = context;
	}

	public void SetInfo( List<PointBean> list1,List<String> y_list,int width , int height,List<String> listYLength) {
		this.list1=list1;
		this.screenWidth =width ;
		this.screenHeight = height;
		this.listYLength = listYLength;
		xPoint = (float) (width/2.0); // 整个画布的一半开始算
		YPoint = Tools.dip2px(context, GraphConstants.Y_TOP_PADDING);  // 居下 10dp 开始算
		xScale = (float) (screenWidth/ GraphConstants.XSCALE_NUMBER);//
		YScale = YLength/(y_list.size()-1);// Y轴的刻度为长度-最上边的距离 除以个数
		YLength = screenHeight- Tools.dip2px(context,  GraphConstants.Y_TOP_PADDING+GraphConstants.Y_BOTTOM_PADDING);//居下 30dp   居上10dp
		circleSize = Tools.dip2px(context, GraphConstants.SIZE_CICLE);
		textSize = Tools.dip2px(context, 14) ;
		//小圆画笔
		ciclePaint = new Paint();
		ciclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		ciclePaint.setAntiAlias(true);
		ciclePaint.setColor(Color.parseColor("#a0cd5b"));
		//小圆连线画笔
		ciclelinePaint = new Paint();
		ciclelinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		ciclelinePaint.setAntiAlias(true);
		ciclelinePaint.setColor(Color.parseColor("#a0cd5b"));
		//刻度显示时间文字
		textPaint = new Paint();
		textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.parseColor("#989898"));
		textPaint.setTextSize(textSize);

	}



	@SuppressLint("ResourceAsColor")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//画曲线
		for (int i=0;i<list1.size();i++){
			//判断温度是否正常 ； 改变小圆不同颜色
			setJudge(list1.get(i).getY());
			//该点距左右一半的xScale之间的矩形区域保存起来 ； 用于判断标杆在哪两点之间
			RectF rectF = new RectF((float) (xPoint + i * xScale - xScale/2.0),YPoint,(float) (xPoint + i * xScale + xScale/2.0),YLength);
			list1.get(i).setRectF(rectF);


			MyPoint point = xy2Point(i,list1.get(i).getY());



			list1.get(i).setPoint(point);
			if(i==0) {
				canvas.drawCircle(point.x, point.y, circleSize, ciclePaint);
				//第一个数据显示月日
				canvas.drawText(list1.get(i).getMonth(),i * xScale + xPoint-xScale/3,YPoint+YLength+ Tools.dip2px(context,20),textPaint);
			}else{
				canvas.drawLine(xPoint + (i-1) * xScale ,getYLocation(list1.get(i-1).getY())
						,xPoint + i * xScale,getYLocation(list1.get(i).getY()),ciclelinePaint);
				canvas.drawCircle(point.x, point.y, circleSize, ciclePaint);
				//判断时间 ， 若是不同于上一条数据的时间，则显示月日
				if(list1.get(i).getMonth().equals(list1.get(i-1).getMonth())){
					canvas.drawText(list1.get(i).getTime(),i * xScale+ xPoint-xScale/3,YPoint+YLength+ Tools.dip2px(context,20),textPaint);
				}else{
					canvas.drawText(list1.get(i).getMonth(), i * xScale + xPoint - xScale / 3, YPoint + YLength + Tools.dip2px(context, 20), textPaint);
				}

			}
		}


	}

	public MyPoint xy2Point(float i,String ylo){
		MyPoint point = new MyPoint();
		point.set((xPoint + i * xScale), getYLocation(ylo));
		return point;
	}

	/**
	 * 设置画笔当前颜色
	 * */
	private void setJudge(String string){
		float value = Float.parseFloat(string);
		if(value>= GraphConstants.highFever){//高烧
			ciclePaint.setColor(Color.parseColor("#ff8abb"));
		}else if(value< GraphConstants.highFever && value >= GraphConstants.lowFever){//低烧
			ciclePaint.setColor(Color.parseColor("#f5d953"));
		}else if(value< GraphConstants.lowFever&&value>= GraphConstants.hypothermia){//正常
			ciclePaint.setColor(Color.parseColor("#a0cd5b"));
		}else if(value < GraphConstants.hypothermia){//低温
			ciclePaint.setColor(Color.parseColor("#5dcbed"));
		}
	}
	/**
	 * 获取Y轴坐标
	 * */
	private float getYLocation(String string){
		float yValue = Float.parseFloat(string);
		float Max = 41;
		float Min = 35;
		if(yValue>Max){//超过刻度最大值
			return Tools.dip2px(context,5);
		}
		if(yValue<Min){//超过刻度最小值
			return YLength  ;
		}
		return (Max - yValue)/(float)(listYLength.size()-1) * YLength + YPoint;
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasure();
	}
	private void setMeasure() {
			int with = (int) (((list1.size()-1) * xScale) + screenWidth);
			if(with<screenWidth){
				with = screenWidth;
			}
			setMeasuredDimension(with, screenHeight);
	}
}
