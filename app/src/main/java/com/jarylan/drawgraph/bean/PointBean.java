package com.jarylan.drawgraph.bean;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;


public class PointBean {

	private String id;//用于删除数据的时候的id

	private String year;
	/**y轴的值*/
	private String  y;
	/**x轴显示用的 月份 例如：9*/
	private String month;
	/**x轴显示用的时间  例如： 16:44*/
	private String time;
	/**x轴显示用的日  例如 ：6  */
	private  String day;

	private RectF rectF;
	private float x;
	/**曲线图点被用户选中状态*/
	private boolean selected;

	private MyPoint point ;

	public void setPoint(MyPoint point){
		this.point = point;
	}
	public MyPoint getPoint(){
		return point;
	}

	public void setId(String id) {
		this.id = id;
	}



	public String getId() {
		return id;
	}



	public boolean isSelected() {
		return selected;
	}



	public void setSelected(boolean selected) {
		this.selected = selected;
	}



	public PointBean() {
		super();
	}



	public String getYear() {
		return year;
	}



	public void setYear(String year) {
		this.year = year;
	}




	public RectF getRectF() {
		return rectF;
	}



	public void setRectF(RectF rectF) {
		this.rectF = rectF;
	}





	public float getX() {
		return x;
	}



	public void setX(float x) {
		this.x = x;
	}



	public PointBean(String year, String y, String month, String time,
					 String day, RectF rectF, float x) {
		super();
		this.year = year;
		this.y = y;
		this.month = month;
		this.time = time;
		this.day = day;
		this.rectF = rectF;
		this.x = x;
	}



	public String getY() {
		return y;
	}



	public void setY(String y) {
		this.y = y;
	}



	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return "PointBean [y=" + y + ", month=" + month + ", time=" + time
				+ ", day=" + day + "]";
	}


}