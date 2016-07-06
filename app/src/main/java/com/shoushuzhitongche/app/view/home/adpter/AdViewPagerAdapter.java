package com.shoushuzhitongche.app.view.home.adpter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.List;
import com.dixiang.framework.network.Net;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.WebViewActivity;
import com.shoushuzhitongche.app.view.home.bean.BannerEntity;

public class AdViewPagerAdapter extends PagerAdapter {
    private List<BannerEntity> datas;
    private Context context;

    public AdViewPagerAdapter(Context context, List<BannerEntity> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Bundle bundle = new Bundle();
            	bundle.putString(Constants.PARM_PAGE_TITLE, datas.get(position).getPageTitle());
            	bundle.putString(Constants.PARM_ACTION_URL, datas.get(position).getActionUrl());
            	IntentHelper.getInstance(context).gotoActivity(WebViewActivity.class, bundle);
//            	IntentHelper.getInstance(context).gotoActivityWithURL(WebViewActivity.class, bundle);
            }
        });
        Log.e("http_url", datas.get(position).getImageUrl());
        Net.displayImage(datas.get(position).getImageUrl(), imageView,R.drawable.ico_default);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
