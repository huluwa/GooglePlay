package com.lipan.ui.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import com.lipan.ui.activity.BaseActivity;
import com.lipan.utils.UIUtils;
import com.lipan.utils.ViewUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InterceptorFrame extends FrameLayout {
	public static final int ORIENTATION_ALL = 1;
	public static final int ORIENTATION_VERTICAL = 1;
	public static final int ORIENTATION_HORIZONTAL = 2;
	private List<View> mInterceptorViews;
	private Map<View, Integer> mViewAndOrientation;
	private int mTouchSlop;
	private float mLastX;
	private float mLastY;
	private View mTarget;

	public InterceptorFrame(Context context) {
		super(context);
		init();
	}

	private void init() {
		mInterceptorViews = new LinkedList<View>();
		mViewAndOrientation = new HashMap<View, Integer>();
		final ViewConfiguration configuration = ViewConfiguration.get(getContext());
		mTouchSlop = configuration.getScaledTouchSlop();
	}

	public void addInterceptorView(View v) {
		addInterceptorView(v, ORIENTATION_ALL);
	}

	public void addInterceptorView(final View v, final int orientation) {
		UIUtils.runInMainThread(new Runnable() {
			@Override
			public void run() {
				if (!mInterceptorViews.contains(v)) {
					mInterceptorViews.add(v);
					mViewAndOrientation.put(v, orientation);
				}
			}
		});
	}

	public void removeInterceptorView(final View v) {
		UIUtils.runInMainThread(new Runnable() {
			@Override
			public void run() {
				mInterceptorViews.remove(v);
				mViewAndOrientation.remove(v);
			}
		});
	}

	private View isTouchInterceptedView(MotionEvent event, int orientation) {
		for (View v : mInterceptorViews) {
			if (ViewUtils.isTouchInView(event, v) && mViewAndOrientation.get(v) == orientation && v.dispatchTouchEvent(event)) {
				return v;
			}
		}
		return null;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();

		if (mTarget != null) {
			boolean flag = mTarget.dispatchTouchEvent(ev);
			if (flag && (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP)) {
				mTarget = null;
			}
			return flag;
		}

		final float x = ev.getX();
		final float y = ev.getY();
		View view = null;
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				mLastX = x;
				mLastY = y;
				view = isTouchInterceptedView(ev, ORIENTATION_ALL);
				break;
			case MotionEvent.ACTION_MOVE:
				final int xDiff = (int) Math.abs(x - mLastX);
				final int yDiff = (int) Math.abs(y - mLastY);
				if (xDiff > mTouchSlop && xDiff > yDiff) {
					view = isTouchInterceptedView(ev, ORIENTATION_HORIZONTAL);
				} else if (yDiff > mTouchSlop && yDiff > xDiff) {
					view = isTouchInterceptedView(ev, ORIENTATION_VERTICAL);
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				mTarget = null;
				break;
			default:
				break;
		}
		if (view != null) {
			mTarget = view;
			return true;
		} else {
			return super.dispatchTouchEvent(ev);
		}
	}
}
