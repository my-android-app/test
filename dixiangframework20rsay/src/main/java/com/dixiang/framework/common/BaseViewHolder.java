package com.dixiang.framework.common;

import android.view.View;
import android.widget.ImageView;

import com.dixiang.framework.network.Net;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public abstract class BaseViewHolder<T> {
    private final ImageLoader mImageLoader;

    /**
     * @param view 已经初始化的view
     */
    public BaseViewHolder(View view) {
        mImageLoader = ImageLoader.getInstance();
    }

    /**
     * 更新AdapterView的每个item
     * @param item
     */
    abstract public void populateView(int position, T item);

    /** 加载图片 */
    public void displayImage(String url, ImageView imageView) {
        Net.displayImage(url, imageView);
    }
    public void displayImage(String url, ImageView imageView, int defaultImageRes) {
        Net.displayImage(url, imageView, defaultImageRes);
    }

    /***
     * 加载图片
     * @param url 图片地址，可以是本地，也可以是网络的
     * @param imageView 目标ImageView
     * @param listener 加载过程的回调接口
     */
    public void displayImage(String url, ImageView imageView, ImageLoadingListener listener) {
        mImageLoader.displayImage(url, imageView, listener);
    }

}
