package com.mediatek.factorymode;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
public class TouchView extends View{


	private final Paint mPaint;
	private final Paint mTargetPaint;
	private final FontMetricsInt mTextMetrics = new FontMetricsInt();
	public ArrayList< ArrayList<PT> > mLines = new ArrayList< ArrayList<PT> >();
	ArrayList<PT> curLine;	
	public ArrayList<VelocityTracker> mVelocityList	= new ArrayList<VelocityTracker>();
	private int mHeaderBottom;
	private boolean mCurDown;
	private int mCurX;
	private int mCurY;
	private float mCurPressure;
	private int mCurWidth;
	private VelocityTracker mVelocity;

	public TouchView(Context context) {
		super(context);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setARGB(255,0,255, 0);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(3);
		
		mTargetPaint = new Paint();
		mTargetPaint.setAntiAlias(false);
		mTargetPaint.setARGB(255, 0, 255, 0);
		mTargetPaint.setStyle(Paint.Style.STROKE);
		mTargetPaint.setStrokeWidth(3);
	}
	public TouchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setARGB(255,0, 255, 0);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(3);
		
		mTargetPaint = new Paint();
		mTargetPaint.setAntiAlias(false);
		mTargetPaint.setARGB(255, 0, 255, 0);
		mTargetPaint.setStyle(Paint.Style.STROKE);
		mTargetPaint.setStrokeWidth(3);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		int w = getWidth() / 5;
		int base = -mTextMetrics.ascent + 1;
		int bottom = mHeaderBottom;
		
		int lineSz = mLines.size();
		int k = 0;
		for(k=0; k<lineSz; k++)	
		{
			ArrayList<PT> m = mLines.get(k);
			
			float lastX = 0, lastY = 0;
			//mPaint.setARGB(255, 255, 0, 0);
			int sz = m.size();
			int i = 0;
			for(i=0; i<sz; i++)
			{	
				PT n = m.get(i);			
				if(i>0)
				{
				  canvas.drawLine(lastX, lastY, n.x, n.y, mTargetPaint);
				  canvas.drawPoint(lastX, lastY, mTargetPaint);
				}					
				lastX = n.x;
				lastY = n.y;
			}	
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			
			mVelocity = VelocityTracker.obtain();
			mVelocityList.add(mVelocity);
			
			curLine = new ArrayList<PT>() ;			
			mLines.add(curLine);				
		}
		mVelocity.addMovement(event);
		mVelocity.computeCurrentVelocity(1);
		final int N = event.getHistorySize();
		for (int i = 0; i < N; i++) {
			curLine.add(new PT(event.getHistoricalX(i), event.getHistoricalY(i)));
		}
		curLine.add(new PT(event.getX(), event.getY()));
		mCurDown = action == MotionEvent.ACTION_DOWN
				|| action == MotionEvent.ACTION_MOVE;
		mCurX = (int) event.getX();
		mCurY = (int) event.getY();
		mCurPressure = event.getPressure();
		mCurWidth = (int) (event.getSize() * (getWidth()/3));

		invalidate();
		return true;
	}
	public class PT
	{
		public Float x;
		public Float y;
		public PT(Float x, Float y)
		{
			this.x = x;
			this.y = y;
		}
	};
	public void Clear() {
		for(ArrayList<PT> m: mLines)
		{
			m.clear();
		}			
		mLines.clear();		
		mVelocityList.clear();
		invalidate();
	}


}
