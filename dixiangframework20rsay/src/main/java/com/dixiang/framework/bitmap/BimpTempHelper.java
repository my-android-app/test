package com.dixiang.framework.bitmap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BimpTempHelper {
    public List<Bitmap> bmp = new ArrayList<>();//选中的bitmap
    public List<ImageItem> imageItems = new ArrayList<>();//所有照片详情
    public List<ImageItem> imageItemsChoosend = new ArrayList<>();//选中的照片
    private static BimpTempHelper instance;

    //图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
    public List<String> drr = new ArrayList<>();//选中的图片地址

    public static BimpTempHelper getInstance() {
        if (instance == null) {
            instance = new BimpTempHelper();
        }
        return instance;
    }

    public void clearData() {
        if (drr != null) {
            drr.clear();
        }
        if (bmp != null) {
            bmp.clear();
        }
        if(imageItemsChoosend!=null){
            imageItemsChoosend.clear();
        }
    }

    public List<ImageItem> getImageItemsChoosend() {
        return imageItemsChoosend;
    }

    public List<ImageItem> getImageItems(Context context) {
        if (imageItems != null && imageItems.size() > 0) {
            return imageItems;
        }
        AlbumHelper helper = AlbumHelper.getHelper();
        imageItems = helper.getAllImageItems();
        return imageItems;
    }

    public Bitmap revitionImageSize(String path) throws IOException {
        Bitmap bitmap = null;

//        try {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        while (true) {
            if ((options.outWidth >> i <= 5000)
                    && (options.outHeight >> i <= 5000)) {
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
}
