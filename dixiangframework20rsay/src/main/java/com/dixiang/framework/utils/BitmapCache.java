package com.dixiang.framework.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.dixiang.framework.R;
import com.dixiang.framework.utils.BitmapUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

public class BitmapCache {
    public Context context;
    public Handler h = new Handler(Looper.getMainLooper());
    public final String TAG = getClass().getSimpleName();
    private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<>();
    private int bitmapSize = 600;


    public BitmapCache() {

    }

    public BitmapCache(Context context) {
        this.context = context;
    }

    public BitmapCache(Context context ,int widthSize){
        this.context = context;
        this.bitmapSize = widthSize;
    }

    public void put(String path, Bitmap bmp) {
        if (!TextUtils.isEmpty(path) && bmp != null) {
            imageCache.put(path, new SoftReference<>(bmp));
        }
    }


    public void displayBmp(final ImageView imageView,final String path , final Handler handler) {
        imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        new Thread() {
            Bitmap bmp = null;
            public void run() {
                SoftReference<Bitmap> reference = imageCache.get(path);
                if (reference != null) {
                    bmp = reference.get();
                }
                if (bmp == null) {
                    try {
                        bmp = BitmapUtils.decodeSampleFromFile(context, new File(path), bitmapSize, 0);
                    } catch (IOException e) {

                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bmp);
                        imageCache.put(path, new SoftReference<>(bmp));
                    }
                });
            }
        }.start();
    }

    public void clearBitmap(String imagePath){
        if (imageCache == null){
            return;
        }
        SoftReference softReference = imageCache.get(imagePath);
        if (softReference == null) {
            return;
        }
        Bitmap bitmap = (Bitmap)softReference.get();
        if (bitmap != null)
            bitmap.recycle();
    }


    public Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 256)
                    && (options.outHeight >> i <= 256)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public void clearCache(){
        if (imageCache != null){
            Set<String> set = imageCache.keySet();
            Iterator iterator = set.iterator();
            SoftReference softReference = null;
            Bitmap bitmap = null;
            while (iterator.hasNext()){
                softReference = imageCache.get(iterator.next());
                if (softReference != null) {
                    bitmap = (Bitmap)softReference.get();
                    if (bitmap != null){
                        bitmap.recycle();
                    }
                }
            }
            softReference = null;
        }
        imageCache = null;
    }
}