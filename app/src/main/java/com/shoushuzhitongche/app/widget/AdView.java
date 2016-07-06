package com.shoushuzhitongche.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;
import com.shoushuzhitongche.app.R;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

public class AdView extends FrameLayout implements ViewPager.OnPageChangeListener {

    public static final int DEFAULT_DURATION = 4000;

    private ViewPager viewPager;
    private PagerAdapter adapter;
    private LinearLayout tipLayout;
    private TextView titleView;
    private PagerIndicator indicatorView;
    private Timer mCycleTimer;
    private TimerTask mCycleTask;
    private Timer mResumingTimer;
    private TimerTask mResumingTask;
    private boolean mAutoCycle;
    private boolean mCycling;
    private boolean mAutoRecover = true;
    private long mSliderDuration = DEFAULT_DURATION;
    private boolean mCanScroll = true;

    public void setmCanScroll(boolean mCanScroll) {
        this.mCanScroll = mCanScroll;
    }

    public AdView(Context context) {
        super(context);
        init(context);
    }

    public void setIndicatorGravity(int gravity) {
        if (tipLayout != null) {
            tipLayout.setGravity(gravity);
        }
    }

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setTitleViewVisibility(int visibility) {
        if (titleView != null) titleView.setVisibility(visibility);
    }

    public void setBottomViewVisibility(int visibility) {
        if (tipLayout != null)
            tipLayout.setVisibility(visibility);
    }

    private void init(Context context) {
        viewPager = new ViewPager(context);
        viewPager.setId(R.id.lib_viewpager);
        viewPager.setOnPageChangeListener(this);
        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        recoverCycle();
                        break;
                }
                return false;
            }
        });

        titleView = new TextView(context);
        titleView.setTypeface(Typeface.SANS_SERIF);
        titleView.setTextColor(getResources().getColor(android.R.color.white));
        titleView.setSingleLine();

        indicatorView = new PagerIndicator(context);
        //indicatorView.setItemCount(4);
        int padding = (int) (getResources().getDisplayMetrics().density * 4 + 0.5f);
        tipLayout = new LinearLayout(context);
        tipLayout.setBackgroundColor(Color.parseColor("#00000000"));
        tipLayout.setGravity(Gravity.RIGHT);
        tipLayout.setPadding(padding, padding, padding, padding);
        tipLayout.addView(titleView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        tipLayout.addView(indicatorView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        int height = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220, getResources().getDisplayMetrics()) + 0.5f);
        addView(viewPager, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(tipLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM));

        setSliderTransformDuration(1100, null);
        //setAutoCycle();
    }

    public void setAdapter(PagerAdapter adapter) {
        this.adapter = adapter;
        viewPager.setAdapter(adapter);
        indicatorView.setItemCount(adapter.getCount());
    }

    public void setCurrentItem(int item) {
        viewPager.setCurrentItem(item);
        indicatorView.setItemCount(adapter.getCount());
        titleView.setText(adapter.getPageTitle(item));
    }

    public int getCurrentItem() {
        return viewPager.getCurrentItem();
    }

    public void setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(reverseDrawingOrder, transformer);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                ViewHelper.setAlpha(tipLayout, 1.0f);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 中间透明度高，两边低
        // positionOffset 1 <--> 0
        if (positionOffset >= 0f && positionOffset <= 1.0f) {
            float x = 1.0f - positionOffset;
            // y = 4 (x - 1/2)^2
            float y = (float) (4 * Math.pow((x - 0.5f), 2));
            ViewHelper.setAlpha(tipLayout, y);
        }
    }

    @Override
    public void onPageSelected(int position) {
        indicatorView.setItemAsSelected(position);
        titleView.setText(adapter.getPageTitle(position));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pauseAutoCycle();
                break;
        }
        return false;
    }


    private android.os.Handler mh = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int ci = viewPager.getCurrentItem() + 1;
            if (adapter != null) {
                if (ci >= adapter.getCount()) {
                    ci = 0;
                }
                viewPager.setCurrentItem(ci, true);
            }
        }
    };


    public void startAutoCycle() {
        startAutoCycle(DEFAULT_DURATION, mSliderDuration, mAutoRecover);
    }

    public void startAutoCycle(long delay, long duration, boolean autoRecover) {
        if (mCycleTimer != null) mCycleTimer.cancel();
        if (mCycleTask != null) mCycleTask.cancel();
        if (mResumingTask != null) mResumingTask.cancel();
        if (mResumingTimer != null) mResumingTimer.cancel();
        mSliderDuration = duration;
        mCycleTimer = new Timer();
        mAutoRecover = autoRecover;
        mCycleTask = new TimerTask() {
            @Override
            public void run() {
                mh.sendEmptyMessage(0);
            }
        };
        mCycleTimer.schedule(mCycleTask, delay, mSliderDuration);
        mCycling = true;
        mAutoCycle = true;
    }

    private void pauseAutoCycle() {
        if (mCycling) {
            mCycleTimer.cancel();
            mCycleTask.cancel();
            mCycling = false;
        } else {
            if (mResumingTimer != null && mResumingTask != null) {
                recoverCycle();
            }
        }
    }

    public void setDuration(long duration) {
        if (duration >= 500) {
            mSliderDuration = duration;
        }
    }

    public void stopAutoCycle() {
        if (mCycleTask != null) {
            mCycleTask.cancel();
        }
        if (mCycleTimer != null) {
            mCycleTimer.cancel();
        }
        if (mResumingTimer != null) {
            mResumingTimer.cancel();
        }
        if (mResumingTask != null) {
            mResumingTask.cancel();
        }
        mAutoCycle = false;
    }

    private void recoverCycle() {
        if (!mAutoRecover || !mAutoCycle) {
            return;
        }

        if (!mCycling) {
            if (mResumingTask != null && mResumingTimer != null) {
                mResumingTimer.cancel();
                mResumingTask.cancel();
            }
            mResumingTimer = new Timer();
            mResumingTask = new TimerTask() {
                @Override
                public void run() {
                    startAutoCycle();
                }
            };
            mResumingTimer.schedule(mResumingTask, DEFAULT_DURATION + 2000);
        }
    }

    public void setSliderTransformDuration(int period, Interpolator interpolator) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(), interpolator, period);
            mScroller.set(viewPager, scroller);
        } catch (Exception e) {

        }
    }

    @Override
    public void scrollTo(int x, int y) {
        if (mCanScroll) {
            super.scrollTo(x, y);
        }
    }
}
