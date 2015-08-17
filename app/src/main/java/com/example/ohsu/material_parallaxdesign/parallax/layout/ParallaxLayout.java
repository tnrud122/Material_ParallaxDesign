package com.example.ohsu.material_parallaxdesign.parallax.layout;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

public class ParallaxLayout extends RelativeLayout implements OnGestureListener{
	private View mTopView = null;
	private View mContentView = null;
	private View mTargetView = null;
	private TimeInterpolator mInterpolator = null;
	private int mLastEventY;
	private int mLastTargetTop;
	private long mTimeBase = 0;
	private boolean overScrolled;
	private boolean onScrolled = false;
	private Animate_Parallx mParallx = new Animate_Parallx();
	private Context mContext;
	private GestureDetector gestureScanner;
	
    private float mDistance = 0;
    private Handler mTimerHandler = null;
    private MyRunnable mEventRunable = null;
    private Toolbar mToolbar = null;
    
	public ParallaxLayout(Context context) {
		super(context);
		mContext = context;
		gestureScanner = new GestureDetector(context,this);
	}

	public ParallaxLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		gestureScanner = new GestureDetector(context,this);
	}

	public void setToolbar(Toolbar toolbar) {
		if(toolbar !=null)
			mToolbar = toolbar;
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (mContentView == null && !ptInRectView(event, mContentView)) {
			return super.dispatchTouchEvent(event);
		}
		int action = event.getAction();
		if (takeTouchEvent(event)) {
		} else {
			super.dispatchTouchEvent(event);	
		}
		
		switch(action) {
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			{
				onScrolled = true;
				overScrolled = false;
				mTimeBase = 0;
				mLastEventY = 0;
				mLastTargetTop = 0;
				mTargetView = null;
			}
			break;
		case MotionEvent.ACTION_DOWN:
			{
				if(mParallx !=null)
					mParallx.cancel();

				mTargetView = getTargetView(mContentView, event);
				mTimeBase = 0;
				onScrolled = false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			{
				onScrolled = false;
				int eventOffset = (int) event.getY() - mLastEventY;
				if (!overScrolled) overScrolled = false;

				if (mTargetView != null && mTargetView.getVisibility() != View.VISIBLE) {
					mTargetView = getTargetView(mContentView, event);
					mTimeBase = 0;
					overScrolled = false;
				} else {
					int targetTop = getViewTop(mTargetView);
					int viewOffset = targetTop - mLastTargetTop;
					if (eventOffset != 0 && viewOffset == 0 && !overScrolled) {
						long currentTime = System.currentTimeMillis();
						if (mTimeBase == 0) {
							mTimeBase = currentTime;
						} else if (currentTime - mTimeBase > 50) {
							overScrolled = true;
							mTimeBase = 0;
						}
					} else if (eventOffset != 0 && viewOffset != 0) {
						mTimeBase = 0;
					}
				}
			}
			break;
		}
		mLastTargetTop = getViewTop(mTargetView);
		mLastEventY = (int) event.getY();
		
		return true;
		
	}
	
	private boolean takeTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int contentTop = mContentView.getTop();

		if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
			if (contentTop > 0) {
				if(mParallx !=null) {
					mParallx.startAnimate(true, 500, contentTop);
				}	
			} else if (contentTop < 0) {
				int offset = mContentView.getBottom() - getBottom();
				mParallx.startAnimate(true, 500, offset);
			}
		} else if (action == MotionEvent.ACTION_MOVE) {
			int offset = (int) (event.getY() - mLastEventY);
			offset = offset / 2;
			boolean handled = false;
			if (contentTop >= 0) {
				int scrollY = mContentView.getScrollY();
				int curTop = mContentView.getTop();
				if (!overScrolled || scrollY > 0 || (offset < 0 && scrollY == 0 && curTop <= 0)) {
					return false;
				}
				offsetContent(offset);
			}
			return handled;
		}
		return false;
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		mTopView =getChildAt(0);
		mContentView = getChildAt(1);
		mInterpolator = new DecelerateInterpolator();
		
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
		// TODO Auto-generated method stub
		int contentTop = 0;
		int contentBottom = 0;

		if(mTopView !=null) {
			mTopView.layout(left, top, right, bottom);
		}
		if (mContentView != null) {
			contentTop = mContentView.getTop();
			contentBottom = contentTop + this.getMeasuredHeight();
			mContentView.layout(0, contentTop, right, contentBottom);
		}
	}
	
	private int getViewTop(View view) {
		if (view == null) return 0;
		
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		
		return location[1];
	}
	
	public boolean offsetContent(int offset) {
		if (mContentView != null) {
			int contentTop = mContentView.getTop();
			if(contentTop == 0) {
				if(offset > 0) {
					mContentView.offsetTopAndBottom(offset);
				}
			} else if(contentTop > 0) {
				if(offset > 0) {
					mContentView.offsetTopAndBottom(offset);
				} else {
					int nVal = contentTop + offset;
					if(nVal < 0) {
						mContentView.offsetTopAndBottom(0);
					} else {
						mContentView.offsetTopAndBottom(offset);
					}
				}
			} else {
				int contentOffset = contentTop * -1;
				mContentView.offsetTopAndBottom(contentOffset);
			}
		}

		if(mTopView !=null) {
			int contentOffsets = mContentView.getTop();
			ViewGroup vg = (ViewGroup) mTopView;
			View imgContent = vg.getChildAt(0);
			if(imgContent !=null) {
				if(contentOffsets > 0) {
					int cwidth = imgContent.getMeasuredWidth();
					int x = cwidth/2;
					float scales = contentOffsets * 0.0008f;
					imgContent.setPivotX(x);
					imgContent.setPivotY(0);
					imgContent.setScaleY(1+scales);
					imgContent.setScaleX(1+scales);		
				}
			}
		}
		
		invalidate();
		
		return true;
	}
	private View getTargetView(View target, MotionEvent event) {
		View view = null;
		if (target == null) return view;
		if (!ptInRectView(event, target)) return view;
		
		if (!(target instanceof ViewGroup)) {
			view = target;
			return view;
		}

		if (target instanceof AdapterView) {
			AdapterView<?> parent = (AdapterView<?>) target;
			int first = parent.getFirstVisiblePosition();
			int last = parent.getLastVisiblePosition();
			for (int index = 0; index <= (last - first); ++index) {
				View child = parent.getChildAt(index);
				if (!ptInRectView(event, child)) {
					continue;
				}
				if (!(child instanceof ViewGroup)) {
					view = child;
					return view;
				}

				view = getTargetView(child, event);

				return view;
			}
		} else if (target instanceof ViewGroup) {
			ViewGroup parent = (ViewGroup) target;
			int childCount = parent.getChildCount();
			for (int index = childCount - 1; index >= 0; --index) {
				View child = parent.getChildAt(index);
				if (!ptInRectView(event, child)) continue;
				if (!(child instanceof ViewGroup)) {
					view = child;
					return view;
				}
				view = getTargetView(child, event);
				return view;
			}
		}
		view = target;
		return view;
	}
	
	private boolean ptInRectView(MotionEvent event, View view) {
		if (event == null || view == null) return false;
		
		int eventX = (int) event.getRawX();
		int eventY = (int) event.getRawY();

		int[] location = new int[2];
		view.getLocationOnScreen(location);

		int width = view.getWidth();
		int height = view.getHeight();
		int left = location[0];
		int top = location[1];
		int right = left + width;
		int bottom = top + height;

		Rect rect = new Rect(left, top, right, bottom);
		boolean contains = rect.contains(eventX, eventY);
		
		return contains;
	}
	
	private class Animate_Parallx implements AnimatorUpdateListener, AnimatorListener {
		private ValueAnimator mAnimator;
		private int mLastOffset;
		private boolean mDownType = true;
		private int mOffset =0;
		public void startAnimate(boolean type,int speed,int offset) {
			cancel();
			mDownType = type;
			mOffset = offset;
			mAnimator = new ValueAnimator();
			if(mDownType) {
				mAnimator.setIntValues(0, offset);
				mLastOffset = 0;
			} else {
				mAnimator.setIntValues(offset, 0);
				mLastOffset = offset;
			}
			
			mAnimator.setDuration(speed);
			mAnimator.setRepeatCount(0);
			if (mInterpolator == null) {
				mInterpolator = new DecelerateInterpolator();
			}
			mAnimator.setInterpolator(mInterpolator);
			mAnimator.addListener(this);
			mAnimator.addUpdateListener(this);
			mAnimator.start();
		}
		public void cancel() {
			if (mAnimator != null && mAnimator.isRunning()) {
				mAnimator.cancel();
			}
			mAnimator = null;
		}
		@Override
		public void onAnimationStart(Animator animation) {}
		@Override
		public void onAnimationEnd(Animator animation) {
			// TODO Auto-generated method stub
			mAnimator = null;
			if(!mDownType) {
				mDownType = true;
				Start_AnimateParallx_Next(mOffset);
			}
		}
		@Override
		public void onAnimationCancel(Animator animation) {}
		@Override
		public void onAnimationRepeat(Animator animation) {}
		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			// TODO Auto-generated method stub
			int currentOffset = (Integer) animation.getAnimatedValue();
			int delta = mLastOffset - currentOffset;
			offsetContent(delta);
			mLastOffset = currentOffset;
		}
	}
	
	public void Start_AnimateParallx_Next(int offset) { 
		if(mParallx !=null) {
			mParallx.startAnimate(true, 300, offset);
		}
	}
	
	public void Start_AnimateParallx_Prev(int offset) {
		if(mParallx !=null) {
			mParallx.startAnimate(false, 300, offset);
		}
	}
	public int getPixelFromDP(Context context, int DP) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int)(DP*scale);
	}
	
	/*
	 * ScrollEvent - ListView
	 * */
	public void onScrollStateChanged(AbsListView view, int scrollState) {}
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		View child = view.getChildAt(0);
		if(child !=null) {
			int viewHeight = child.getHeight();
		    int height = getPixelFromDP(mContext, viewHeight); 
		    int scrolly = -child.getTop() +view.getFirstVisiblePosition() * height;
		    if(mTopView !=null) {
		    	float y = (scrolly/2)*-1;
				float z = y;
				z *= -0.5;
				setToolbarTransparent((int)z);
		    	mTopView.setTranslationY(y);
		    	float testY = mTopView.getTranslationY();
	        	if(testY == 0) {
					Log.d("parallax", "====> startBouncer animate!");
	        		if(onScrolled)
	        			Bouncer_onChanged(mDistance);
	        	}
		    }
		}
	    
	}
	
	/*
	 * ScrollEvent - RecyclerView 
	 */
	public void onScrollStateChanged(RecyclerView recyclerView, int newState) {}
	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		View child = recyclerView.getChildAt(0);
		if(child !=null) {
			int firstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
			int viewHeight = child.getHeight();
		    int height = getPixelFromDP(mContext, viewHeight); 
		    int scrolly = -child.getTop() +firstVisiblePosition * height;
		    if(mTopView !=null) {
		    	float y = (scrolly/2)*-1;
				float z = y;
				z *= -0.5;
				setToolbarTransparent((int)z);
		    	mTopView.setTranslationY(y);
		    	float testY = mTopView.getTranslationY();
	        	if(testY == 0) {
					//Log.d("parallax", "====> startBouncer animate!");
	        		if(onScrolled)
	        			Bouncer_onChanged(mDistance);
	        	}
		    }
		}
		
	}

	public void startBouncer(float offset) {
		Log.e("parallax", "====> startBouncer animate!");
		Start_AnimateParallx_Prev((int)offset);
	}
	
	public void onTouchEvent_Gesture(MotionEvent event) {
		gestureScanner.onTouchEvent(event);
	}
	@Override
	public boolean onDown(MotionEvent e) {return false;}
	@Override
	public void onShowPress(MotionEvent e) {}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {return false;}
	@Override
	public void onLongPress(MotionEvent e) {}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {return false;}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		// TODO Auto-generated method stub
		if (e2.getY() - e1.getY() > 0 ) {
			float distanceTimeFactor = 0.4f;
			mDistance = (distanceTimeFactor * velocityY/2)* 0.1f;
		}
        return true;
	}
	
	private class MyRunnable implements Runnable {
	     private float mvalue;
	     MyRunnable(final float value) {
	       this.mvalue = value;
	     }
	     public void setValue(final float value) {
	    	 this.mvalue = value;
	     }
	     public void run() {
	    	 startBouncer(mvalue);
	     }
	  }

	private void Bouncer_onChanged(float value) {
		Log.d("parallax", "====> startBouncer animate!");
		if(mTimerHandler == null) {
			mTimerHandler = new Handler();
			if(mEventRunable == null)
				mEventRunable = new MyRunnable(value);
			else
				mEventRunable.setValue(value);
		} else {
			if(mEventRunable == null) 
				mEventRunable = new MyRunnable(value);
			else 
				mEventRunable.setValue(value);
			
			mTimerHandler.removeCallbacks(mEventRunable);
		}
		mTimerHandler.postDelayed(mEventRunable, 20);
	}

	public void setToolbarTransparent(int alpha) {
		if(alpha <= 255 && alpha >=0) {
			if(mToolbar !=null) {
				Drawable c = mToolbar.getBackground();
				c.setAlpha(alpha);
				mToolbar.setBackground(c);
				/*if(alpha == 0)
					mToolbar.setTranslationY(-mToolbar.getHeight());
				else
					mToolbar.setTranslationY(0);*/

			}
		}

	}
}
