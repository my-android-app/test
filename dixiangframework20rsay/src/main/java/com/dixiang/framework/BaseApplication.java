package com.dixiang.framework;

import java.io.File;
import java.io.InputStream;

import org.apache.http.NameValuePair;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import com.dixiang.framework.config.SystemConfig;
import com.dixiang.framework.config.SystemConstants;
import com.dixiang.framework.rsa.RSAUtils;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.libcore.RawHeaders;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.loader.AsyncHttpRequestFactory;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * 配置网络加载，图片下载信息
 * @author dx
 *
 */
public class BaseApplication extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
        // Setup ImageLoader
        ImageLoaderConfiguration config = getImageLoaderConfiguration();
        ImageLoader.getInstance().init(config);

        // Setup Ion
        Ion.Config ionConfigure = Ion.getDefault(this).configure();
        onSetIonConfig(ionConfigure);
        
        initRsa();
	}
	
    public ImageLoaderConfiguration getImageLoaderConfiguration() {
        try {
        	File cacheFile = new File(SystemConfig.CACHE_FILE);
            if (!cacheFile.exists()) {
                cacheFile.mkdirs();
            }
        } catch (Exception e) {

        }
        
        final Drawable cd = getImageHolder();
        return new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .writeDebugLogs()
                .diskCache(new LruDiscCache(new File(SystemConfig.CACHE_FILE), new Md5FileNameGenerator(), 50 * 1024 * 1024){{setCompressFormat(Bitmap.CompressFormat.JPEG);}})
                .defaultDisplayImageOptions(new DisplayImageOptions.Builder()
                        .showImageOnLoading(cd)
                        .showImageForEmptyUri(cd)
                        .showImageOnFail(cd)
                        .cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .cacheOnDisk(true)
                        .imageScaleType(ImageScaleType.EXACTLY)
                        .build())
                .diskCacheSize(50 * 1024 * 1024)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCacheSize(5* 1024 * 1024)
                .memoryCache(new WeakMemoryCache())
                .build();
    }
    
    public void onSetIonConfig(Ion.Config ionConfigure) {
        String userAgent = getHttpUserAgent();
        if (!TextUtils.isEmpty(userAgent)) {
            ionConfigure.userAgent(userAgent);
        }

        final AsyncHttpRequestFactory current = ionConfigure.getAsyncHttpRequestFactory();
        ionConfigure.setAsyncHttpRequestFactory(new AsyncHttpRequestFactory() {

            @Override
            public AsyncHttpRequest createAsyncHttpRequest(Uri uri, String s, RawHeaders rawHeaders) {
                AsyncHttpRequest ret = current.createAsyncHttpRequest(uri, s, rawHeaders);

                NameValuePair[] headers = getHttpHeaders();
                if (headers != null) {
                    for (NameValuePair nameValuePair : headers) {
                        ret.addHeader(nameValuePair.getName(), nameValuePair.getValue());
                    }
                }
                return ret;
            }

        });
    }
    
    private void initRsa(){
    	try{
    		InputStream inPublic = getResources().getAssets().open("rsa_public_key.pem");
    		SystemConstants.publicKey = RSAUtils.loadPublicKey(inPublic);	
    		
    		InputStream inPrivate = getResources().getAssets().open("pkcs8_rsa_private_key.pem");
    		SystemConstants.privateKey = RSAUtils.loadPrivateKey(inPrivate);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }

    
    /**
     * 覆写该方法，可以自定义加载图片时等待的图片、错误的图片
     * 依赖getImageLoaderConfiguration方法
     */
    public Drawable getImageHolder() {
        return getResources().getDrawable(R.drawable.ic_launcher);
    }
    
    /**
     * 覆写该方法，可以自定义全局header
     * 依赖onSetIonConfig方法
     */
    public NameValuePair[] getHttpHeaders() {
        return new NameValuePair[]{};
    }

    /**
     * 覆写该方法，可以自定义全局userAgent
     * 依赖onSetIonConfig方法
     */
    public String getHttpUserAgent() {
        return null;
    }
}
