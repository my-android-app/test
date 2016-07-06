package com.dixiang.framework.network;

import android.widget.ImageView;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

/**
 * 网络访问
 */
public class Net {

    private static final String TAG = Net.class.getSimpleName();
    
    /** 
     *  context (Activity : activity  ;   Fragment : fragment)
     *  url 接口请求的url
     *  parameters 接口请求的参数  (Get : String ;  Post : Map<String,Object>)
     *  typeToken 解析数据的类型
     *  callback 接口回调处理
     *  flg  接口标记
     *  */
    public static <T extends IResult> void fetch(final String url, final Object parameters,
            final TypeToken<T> typeToken, NetUICallback  callback ,String flg){
    	new HttpAsyncTask().execute(url,parameters,typeToken,callback,flg);
	}
    
    public static void displayImage(String url, ImageView imageView) {
        displayImage(url, imageView, null, null, null);
    }

    public static void displayImage(String url, ImageView imageView, int defaultImageRes) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImageRes)
                .showImageForEmptyUri(defaultImageRes)
                .showImageOnFail(defaultImageRes)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        displayImage(url, imageView, options);
    }

    public static void displayImageWithFadeAnim(String url, ImageView imageView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(2000))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        displayImage(url, imageView, options);
    }

    public static void displayImage(String url, ImageView imageView, int defaultImageRes, ImageLoadingListener listener) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImageRes)
                .showImageForEmptyUri(defaultImageRes)
                .showImageOnFail(defaultImageRes)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        displayImage(url, imageView, options, listener);
    }


    public static void displayImage(String url, ImageView imageView, DisplayImageOptions options) {
        displayImage(url, imageView, options, null, null);
    }

    public static void displayImage(String url, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener) {
        displayImage(url, imageView, options, listener, null);
    }

    /**
     * 加载图片
     *
     * @param uri              图片地址
     * @param imageView        ImageView
     * @param options          展示图片的配置
     * @param listener         加载回调
     * @param progressListener 加载过程回调
     */
    public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options,
                                    ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        ImageLoader.getInstance().displayImage(uri, imageView, options, listener, progressListener);
    }

}
