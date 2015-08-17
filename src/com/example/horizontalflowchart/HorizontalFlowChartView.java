package com.example.horizontalflowchart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class HorizontalFlowChartView extends View {

	// 默认外线段高度，dp
	private final int DEFAULT_BIG_LINE_HEIGHT = 12;
	// 默认内线段高度，dp
	private final int DEFAULT_SMALL_LINE_HEIGHT = 5;

	// 默认圆的数量
	private final int DEFAULT_CIRCLE_SUM = 3;

	// 默认外圆（外线）颜色
	private final int DEFAULT_BIG_COLOR = Color.parseColor("#c0c0c0");
	// 默认内圆（内线）颜色
	private final int DEFAULT_SMALL_COLOR = Color.parseColor("#789262");

	// 默认圆内有文本
	private final boolean DEFAULT_HAS_TEXT = true;
	// 默认文本大小，sp
	private final int DEFAULT_TEXT_SIZE = 12;
	// 默认文本颜色
	private final int DEFAULT_TEXT_COLOR = Color.parseColor("#ffffff");

	// 默认触摸时该圆的颜色
	private final int DEFAULT_TOUCH_CIRCLE_COLOR = Color.parseColor("#555555");
	
	//默认动画速率
	private final int DEFAULT_LOADING_RATE = 0;
	
	//默认文本布局
	private final int DEFAULT_TEXT_GRAVITY = 0;
	
	//文本布局，文本在圆内
	private final int TEXT_GRAVITY_CENTER = 0;
	//文本布局，文本在圆下
	private final int TEXT_GRAVITY_BOTTOM = 1;
	//文本布局，文本在圆上
	private final int TEXT_GRAVITY_TOP = 2;
	
	//默认文本偏移
	private final int DEFAULT_TEXT_OFFSET = 5;
	

	// 外圆半径
	private int mBigCircleRadius;
	// 内圆半径
	private int mSmallCircleRadius;
	// 外线段长度
	private int mBigLineWidth;
	// 内线段长度
	private int mSmallLineWidth;
	// 外线段高度
	private int mBigLineHeight;
	// 内线段高度
	private int mSmallLineHeight;

	// 圆的数量
	private int mCircleSum;
	
	//圆心的y轴坐标
	private float mCircleY;

	// 外圆（外线）的颜色
	private int mBigColor;
	// 内圆（内线）的颜色
	private int mSmallColor;

	// 该view真实的宽度和高度
	private int mRealWidth, mRealHeight;

	// 画笔
	private Paint mPaint = new Paint();

	// 当前进度，默认为0
	private int mProgress = 0;
	// 当前进度的最大值
	private int mMaxProgress;
	//当前进度的目标值
	private int mToProgress = mProgress;
	
	//设置当前进度
	public void setNowCircle(int  num)
	{
		mProgress = 180*num + mSmallLineWidth*(num-1);
		mToProgress = mProgress;
	}

	// 圆内是否有文本
	private boolean mHasText;
	// 文本字体大小
	private int mTextSize;
	// 文本字体颜色
	private int mTextColor;
	//文本布局
	private int mTextGravity;
	//文本偏移
	private int mTextOffset;
	//文本的Y轴坐标
	private float mTextY;;

	// 触摸事件中，圆的顺序
	private int mTouchCircle = -1;
	// 触摸时圆改变后的颜色
	private int mTouchCircleColor;

	// 文本
	private String[] mText;

	// 设置文本
	public void setText(String[] mText) {
		this.mText = mText;
		invalidate();
	}

	// 点击圆圈事件监听器
	private OnTouchCircleListener onTouchCircleListener;

	// 设置点击圆圈时间监听器
	public OnTouchCircleListener setOnTouchCircleListener(
			OnTouchCircleListener onTouchCircleListener) {
		this.onTouchCircleListener = onTouchCircleListener;
		return this.onTouchCircleListener;
	}
	
	//动画速率
	private int mLoadingRate;
	//设置阶段性动画持续时间
	public void setLoadingRate(int mLoadingRate)
	{
		this.mLoadingRate = mLoadingRate;
	}
	

	
	public HorizontalFlowChartView(Context context) {
		this(context, null);
	}

	public HorizontalFlowChartView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HorizontalFlowChartView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		obtainStyledAttributes(attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// 获得该view真实的宽度和高度
		mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
		mRealHeight = getMeasuredHeight() - getPaddingTop()
				- getPaddingBottom();

		mPaint.setTextSize(mTextSize);
		
		
		//圆心的Y轴坐标
		switch(mTextGravity)
		{
		case TEXT_GRAVITY_CENTER:
			// 以该view的大小以及圆的数量获得默认的外圆半径
			if(mBigCircleRadius == -1)
			mBigCircleRadius = Math.min(mRealWidth / (3 * mCircleSum-1),
					mRealHeight / 2);
			//获得圆心Y轴
			mCircleY = getPaddingTop() + mBigCircleRadius;
			mTextY = mCircleY-(mPaint.descent() + mPaint.ascent())/2;
			break;
		case TEXT_GRAVITY_BOTTOM:
			//以该view的大小以及圆的数量和文本偏移获得默认的外圆半径
			if(mBigCircleRadius == -1)
				mBigCircleRadius = (int) Math.min(mRealWidth/(3*mCircleSum-1), (mRealHeight-(mPaint.descent() - mPaint.ascent())-mTextOffset)/2);
			//获得圆心坐标
			mCircleY = getPaddingTop() + mBigCircleRadius;
			mTextY = mCircleY + mBigCircleRadius +mTextOffset +(mPaint.descent() - mPaint.ascent()) ;
			break;
		case TEXT_GRAVITY_TOP:
			//以该view的大小以及圆的数量和文本偏移获得默认的外圆半径
			if(mBigCircleRadius == -1)
				mBigCircleRadius = (int) Math.min(mRealWidth/(3*mCircleSum-1), (mRealHeight-(mPaint.descent() - mPaint.ascent())-mTextOffset)/2);
			//获得圆心坐标
			mCircleY = getPaddingTop() + mTextOffset + (mPaint.descent() - mPaint.ascent())+mBigCircleRadius;
			mTextY = getPaddingTop() + (mPaint.descent()-mPaint.ascent());
			break;
		}
		
		
		//外线长度
		if(mBigLineWidth == -1)
			mBigLineWidth = (mRealWidth-mCircleSum*2*mBigCircleRadius)/(mCircleSum-1);
		
		// 以外圆半径的4/5作为默认内圆半径
				if(mSmallCircleRadius == -1)
				mSmallCircleRadius = mBigCircleRadius * 4 / 5;

				// 获得默认内线长度
				mSmallLineWidth = mBigLineWidth + 2
						* (mBigCircleRadius - mSmallCircleRadius);

				// 当前进度的最大值
				mMaxProgress = 180 * mCircleSum + mSmallLineWidth * (mCircleSum - 1);
		
	}
	
	//从自定义属性中获得属性值
	private void obtainStyledAttributes(AttributeSet attrs)
	{
		final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalFlowChartView);
		
		//获取各属性
		mCircleSum = typedArray.getInteger(R.styleable.HorizontalFlowChartView_circle_sum, DEFAULT_CIRCLE_SUM);
		
		mBigCircleRadius = (int) typedArray.getDimension(R.styleable.HorizontalFlowChartView_big_circle_radius, -1);
		mSmallCircleRadius = (int) typedArray.getDimension(R.styleable.HorizontalFlowChartView_small_circle_radius, -1);
		
		mBigLineWidth = (int) typedArray.getDimension(R.styleable.HorizontalFlowChartView_big_line_width, -1);
		
		mBigLineHeight = (int) typedArray.getDimension(R.styleable.HorizontalFlowChartView_big_line_height, dp2px(DEFAULT_BIG_LINE_HEIGHT));
		mSmallLineHeight = (int) typedArray.getDimension(R.styleable.HorizontalFlowChartView_small_line_height, dp2px(DEFAULT_SMALL_LINE_HEIGHT));
		
		mBigColor = typedArray.getColor(R.styleable.HorizontalFlowChartView_big_color, DEFAULT_BIG_COLOR);
		mSmallColor = typedArray.getColor(R.styleable.HorizontalFlowChartView_small_color, DEFAULT_SMALL_COLOR);
		
		mHasText = typedArray.getBoolean(R.styleable.HorizontalFlowChartView_has_text, DEFAULT_HAS_TEXT);
		mTextColor = typedArray.getColor(R.styleable.HorizontalFlowChartView_text_color, DEFAULT_TEXT_COLOR);
		mTextSize = (int) typedArray.getDimension(R.styleable.HorizontalFlowChartView_text_size, sp2px(DEFAULT_TEXT_SIZE));	
		
		mTouchCircleColor = typedArray.getColor(R.styleable.HorizontalFlowChartView_touch_circle_color, DEFAULT_TOUCH_CIRCLE_COLOR);
		mLoadingRate = typedArray.getInteger(R.styleable.HorizontalFlowChartView_loading_rate, DEFAULT_LOADING_RATE);
		
		mTextGravity = typedArray.getInteger(R.styleable.HorizontalFlowChartView_text_gravity, DEFAULT_TEXT_GRAVITY);
		mTextOffset = (int) typedArray.getDimension(R.styleable.HorizontalFlowChartView_text_offset, dp2px(DEFAULT_TEXT_OFFSET));
		
		typedArray.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 设置抗锯齿
		mPaint.setAntiAlias(true);
		// 绘制外围
		drawBig(canvas);
		// 绘制内围
		drawSmall(canvas);
		// 绘制文本
		if (mHasText) {
			drawText(canvas);
		}
	}

	// 绘制外围函数
	protected void drawBig(Canvas canvas) {
		// 设置画笔属性
		mPaint.setColor(mBigColor);
		mPaint.setStyle(Style.FILL);
		mPaint.setStrokeWidth(mBigLineHeight);

		// 画圆
		for (int i = 1; i <= mCircleSum; i++) {
			if (mTouchCircle >= 0 && mTouchCircle == i - 1) {
				mPaint.setColor(mTouchCircleColor);
			} else {
				mPaint.setColor(mBigColor);
			}

			canvas.drawCircle(getPaddingLeft() + i * 2 * mBigCircleRadius
					- mBigCircleRadius + (i - 1) * mBigLineWidth,
					mCircleY, mBigCircleRadius,
					mPaint);
		}
		mPaint.setColor(mBigColor);
		// 画线
		for (int i = 1; i < mCircleSum; i++)
			canvas.drawLine(getPaddingLeft() + i * 2 * mBigCircleRadius
					+ (i - 1) * mBigLineWidth-1, mCircleY, getPaddingLeft() + i * 2
					* mBigCircleRadius + i * mBigLineWidth+1, mCircleY, mPaint);
	}

	// 绘制内围函数
	protected void drawSmall(Canvas canvas) {
		// 判断当前进度，若大于最大值，则赋予最大值
		if (mProgress > mMaxProgress) {
			mProgress = mMaxProgress;
		}
		// 判断当前进度，若小于0，则赋予0
		if (mProgress < 0) {
			mProgress = 0;
		}
		// 设置画笔属性
		mPaint.setColor(mSmallColor);
		mPaint.setStyle(Style.FILL);
		mPaint.setStrokeWidth(mSmallLineHeight);

		// 画已完成的圆+线组合
		int mFinish = mProgress / (180 + mSmallLineWidth);
		if (mFinish > 0) {
			for (int i = 0; i < mFinish; i++) {
				// 画圆
				canvas.drawCircle(getPaddingLeft() + (i * 2 + 1)
						* mBigCircleRadius + i * mBigLineWidth, mCircleY, mSmallCircleRadius, mPaint);
				// 画线
				canvas.drawLine(getPaddingLeft() + (i * 2 + 1)
						* mBigCircleRadius + i * mBigLineWidth
						+ mSmallCircleRadius, mCircleY, getPaddingLeft() + (i * 2 + 1)
						* mBigCircleRadius + i * mBigLineWidth
						+ mSmallCircleRadius + mSmallLineWidth, mCircleY, mPaint);
			}
		}
		// 除去已完成的圆+线组合，还剩部分
		int mUnFinish = mProgress % (180 + mSmallLineWidth);
		// 已完成至圆
		if (mUnFinish <= 180) {
			// 画扇形
			canvas.drawArc(new RectF(getPaddingLeft() + mFinish
					* (2 * mBigCircleRadius + mBigLineWidth) + mBigCircleRadius
					- mSmallCircleRadius, mCircleY
					- mSmallCircleRadius, getPaddingLeft() + mFinish
					* (2 * mBigCircleRadius + mBigLineWidth) + mBigCircleRadius
					+ mSmallCircleRadius, mCircleY
					+ mSmallCircleRadius), 180 - mUnFinish, 2 * mUnFinish,
					false, mPaint);
		}
		// 已完成至线
		else {
			// 画圆
			canvas.drawCircle(
					getPaddingLeft() + mFinish
							* (2 * mBigCircleRadius + mBigLineWidth)
							+ mBigCircleRadius, mCircleY, mSmallCircleRadius, mPaint);
			// 画线
			canvas.drawLine(getPaddingLeft() + mFinish
					* (2 * mBigCircleRadius + mBigLineWidth) + mBigCircleRadius
					+ mSmallCircleRadius, mCircleY,
					getPaddingLeft() + mFinish
							* (2 * mBigCircleRadius + mBigLineWidth)
							+ mBigCircleRadius + mSmallCircleRadius + mUnFinish
							- 180, mCircleY, mPaint);
		}
	}

	// 绘制文本函数
	protected void drawText(Canvas canvas) {
		// 设置文本属性
		mPaint.setColor(mTextColor);
		mPaint.setTextSize(mTextSize);
		if (mText != null) {
			for (int i = 1; i <= mText.length; i++) {
				float startWidth = getPaddingLeft() + i * 2 * mBigCircleRadius
						- mBigCircleRadius + (i - 1) * mBigLineWidth
						- mPaint.measureText(mText[i - 1]) / 2;
				float startHeight = mTextY;
				canvas.drawText(mText[i - 1], startWidth, startHeight, mPaint);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getY() > getPaddingTop()
				&& event.getY() < getMeasuredHeight() - getPaddingBottom()) {
			
			switch (event.getAction()) {
			// 触摸事件以及移动事件
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_DOWN:

				onActionDown(event);

				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				onActionUp(event);
				invalidate();
				break;
			}
			
		}
		else
		{
			mTouchCircle = -1;
			invalidate();
		}
		return true;
	}

	// ACTION_DOWN触摸函数
	private void onActionDown(MotionEvent event) {
		float actionX = event.getX();
		float cache = (actionX - getPaddingLeft())
				% (2 * mBigCircleRadius + mBigLineWidth);
		if (cache <= 2 * mBigCircleRadius) {
			mTouchCircle = (int) (actionX - getPaddingLeft())
					/ (2 * mBigCircleRadius + mBigLineWidth);
		} else {
			mTouchCircle = -1;
		}
	}

	// ACTION_UP触摸函数
	private void onActionUp(MotionEvent event) {
		float actionX = event.getX();
		float cache = (actionX - getPaddingLeft())
				% (2 * mBigCircleRadius + mBigLineWidth);
		int num = (int) (actionX - getPaddingLeft())
				/ (2 * mBigCircleRadius + mBigLineWidth);
		if (cache <= 2 * mBigCircleRadius) {
			if(onTouchCircleListener != null)
			onTouchCircleListener
					.onTouchCircle(num);
			mToProgress = (num+1)*180+num*mSmallLineWidth;
			
			for(int i = 0 ; i <= mLoadingRate ; i++)
			startLoading();
		}
		mTouchCircle = -1;
	}

	// dp to px
	protected int dp2px(int dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, getResources().getDisplayMetrics());
	}

	// sp to px
	protected int sp2px(int spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, getResources().getDisplayMetrics());
	}

	public interface OnTouchCircleListener {
		public void onTouchCircle(int mCircleNum);
	}
	
	
	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			//动画加载完成
			if(mProgress == mToProgress)
			{
				return;
			}
			switch(msg.what)
			{
			case 0:
			case 1:
				mProgress ++;
				invalidate();
				startLoading();
				break;
			case 2:
			case 3:
				mProgress--;
				invalidate();
				startLoading();
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	private void startLoading()
	{
		//向右加载动画
		if(mProgress < mToProgress)
		{
			//圆圈内
			if(mProgress%(180+ mSmallLineWidth)<=180)
			{
				mHandler.sendEmptyMessageDelayed(0, 0);
			}
			//线段内
			else
			{
				mHandler.sendEmptyMessageDelayed(1, 0);
			}
		}
		//向左加载
		else if (mProgress > mToProgress)
		{
			//圆圈内
			if(mProgress%(180+ mSmallLineWidth)<=180)
			{
				mHandler.sendEmptyMessageDelayed(2, 0);
			}
			//线段内
			else
			{
				mHandler.sendEmptyMessageDelayed(3, 0);
			}
		}
	}
	

}
