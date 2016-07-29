package com.jarylan.drawgraph.graph;


import java.util.List;




import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;

import com.jarylan.drawgraph.bean.GraphConstants;
import com.jarylan.drawgraph.utils.Tools;


@SuppressLint("ResourceAsColor")
public class ChartView_bg extends View {

	// 传递过来的数据
	public List<String> x_list;
	public List<String> y_list;



	private int screenWidth;
	private int screenHeight;
	private Context context;
	private Paint lineYPaint;
	private Paint recf_Paint ;
	private Paint textPaint;
	public Paint benchmarkingPaint;
	public Paint ciclePaint;
	public boolean isShowBenchmarking = false;//是否显示标杆

	private float xScale;//X刻度
	private float xLength;//X宽
	private float xPoint = 0;
	private float YLength;//Y宽
	private float YScale ;//Y刻度
	private float YPoint = 0;


	private float textSize ;
	public float  benchmarkingY;
	private float circleSize;


	public ChartView_bg(Context context) {
		super(context);
		this.context = context;
	}

	public void SetInfo(List<String> x_list,List<String> y_list, int width , int height) {
		this.x_list = x_list;
		this.y_list = y_list;
		this.screenWidth =width ;
		this.screenHeight = height;
//		Log.e("uuid", "screenWidth="+screenWidth+",screenHeight="+screenHeight+",xScale="+xScale);
		xLength = screenWidth;
		xPoint = Tools.dip2px(context, GraphConstants.X_LEFT_PADDING); //居左40dp开始算 ，
		YPoint = Tools.dip2px(context, GraphConstants.Y_TOP_PADDING);  // 居下 10dp 开始算
		YLength = screenHeight-Tools.dip2px(context, GraphConstants.Y_TOP_PADDING+GraphConstants.Y_BOTTOM_PADDING);//去除留下的空白 ；居下 30dp 居上10dp ;
		xScale = screenWidth/ GraphConstants.XSCALE_NUMBER;
		YScale = YLength/(y_list.size()-1);// Y轴的刻度为长度-最上边的距离 除以个数
		textSize = Tools.dip2px(context, GraphConstants.SIZE_TEXT) ;
		// 线的画笔 y轴s
		lineYPaint = new Paint();
		lineYPaint.setStyle(Style.STROKE);
		lineYPaint.setAntiAlias(true);// 去锯齿
		lineYPaint.setStrokeWidth(2);
		lineYPaint.setColor(Color.parseColor("#d4d4d4"));// 颜色
		lineYPaint.setTextSize(textSize);
		//字体的画笔
		textPaint = new Paint();
		textPaint.setStyle(Style.FILL_AND_STROKE);
		textPaint.setStrokeWidth(1);
		textPaint.setAntiAlias(true);// 去锯齿
		textPaint.setColor(Color.parseColor("#989898"));// 颜色
		textPaint.setTextSize(textSize); // 设置轴文字大小
		//正常范围区域画笔
		recf_Paint = new Paint();
		recf_Paint.setStyle(Style.FILL_AND_STROKE);
		recf_Paint.setStrokeWidth(1);
		recf_Paint.setAntiAlias(true);
		recf_Paint.setColor(Color.parseColor("#efffe0"));// 颜色
		//标杆，外圆画笔
		benchmarkingPaint = new Paint();
		benchmarkingPaint.setStyle(Style.STROKE);
		benchmarkingPaint.setStrokeWidth(4);
		benchmarkingPaint.setAntiAlias(true);
		benchmarkingPaint.setColor(Color.parseColor("#80ad3b"));// 颜色
		//内圆画笔
		ciclePaint = new Paint();
		ciclePaint.setStyle(Style.FILL_AND_STROKE);
		ciclePaint.setStrokeWidth(2);
		ciclePaint.setAntiAlias(true);
		ciclePaint.setColor(Color.parseColor("#a0cd5b"));// 颜色
		circleSize = Tools.dip2px(context, GraphConstants.Benchmarking_SIZE_CICLE);
		benchmarkingY = YPoint + 4 * YScale;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);// 重写onDraw方法
		//绘制正常范围矩形
		canvas.drawRect(xPoint,getYLocation(GraphConstants.lowFever),xPoint+x_list.size()*xScale+7*xScale ,getYLocation(GraphConstants.hypothermia), recf_Paint);
		// 绘画Y轴刻度线
		for (int i = 0; i < y_list.size(); i++) {
			canvas.drawText(y_list.get(i), Tools.dip2px(context, 10), YPoint+i*YScale+Tools.dip2px(context, 4), textPaint);
			canvas.drawLine(xPoint,YPoint+ i*YScale ,  xLength  ,YPoint+ i*YScale, lineYPaint);
		}
		if(isShowBenchmarking) {
	//绘制标杆
	canvas.drawLine((float) (xPoint + (xLength - xPoint) / 2.0), YPoint + (y_list.size() - 1) * YScale, (float) (xPoint + (xLength - xPoint) / 2.0), benchmarkingY + circleSize, benchmarkingPaint);
	canvas.drawCircle((float) (xPoint + (xLength - xPoint) / 2.0), benchmarkingY, circleSize, ciclePaint);
	canvas.drawCircle((float) (xPoint + (xLength - xPoint) / 2.0), benchmarkingY, circleSize, benchmarkingPaint);
		}
	}

	/**
	 * 获取Y轴坐标
	 * */
	public float getYLocation(float yValue){
		float Max =  41;
		float Min = 35;
		if(yValue>Max){//超过刻度最大值
			return Tools.dip2px(context,5);
		}
		if(yValue<Min){//超过刻度最小值
			return YLength  ;
		}
		return (Max - yValue)/(float)(y_list.size()-1) * YLength + YPoint;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
			setMeasuredDimension(screenWidth, screenHeight);
	}

}
