package com.dixiang.framework.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {
	
	public static byte[] bitmap2byteArray(String pathName) {
		Bitmap bm = BitmapFactory.decodeFile(pathName);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
	
	public static boolean compress(Context context, Uri uri, String path, int reqWidth, int reqHeight) throws IOException {
        //压缩图片
		Bitmap b = decodeSampleFromUri(context, uri, reqWidth, reqHeight);
		return b != null && save(context, b, new File(path)) ;
	}
	
	public static boolean save(Context context, Bitmap bmp, File file) throws IOException {
        if(file.exists()){
            file.delete();
        }
		if (!file.exists()) {
			File p = file.getParentFile();
			if (p != null) {
				p.mkdirs();
			}
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file);
		// 100 means no compression
		boolean isCompress = bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
		out.flush();
		out.close();
		
		context.getContentResolver().notifyChange(Uri.fromFile(file), null);
		
		return isCompress;
	}
	public static Bitmap decodeSampleFromUri(Context context, Uri uri, int reqWidth, int reqHeight) throws IOException {
		if (reqWidth == 0 && reqHeight == 0) {
			return null;
		}
		InputStream input = context.getContentResolver().openInputStream(uri);
		
		final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(input, null, options);
		input.close();
		
		computeSize(options, reqWidth, reqHeight);
	    
	    input = context.getContentResolver().openInputStream(uri);
	    options.inJustDecodeBounds = false;
	    Bitmap sampleBimap = BitmapFactory.decodeStream(input, null, options);
	    input.close();
	    return sampleBimap;
	}

    public static Bitmap decodeSampleFromFile(Context context, File uri, int reqWidth, int reqHeight) throws IOException {
        if (reqWidth == 0 && reqHeight == 0) {
            return null;
        }
        InputStream input = context.getContentResolver().openInputStream(Uri.fromFile(uri));

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, options);
        input.close();

        computeSize(options, reqWidth, reqHeight);

        input = context.getContentResolver().openInputStream(Uri.fromFile(uri));
        options.inJustDecodeBounds = false;
        Bitmap sampleBimap = BitmapFactory.decodeStream(input, null, options);
        input.close();
        return sampleBimap;
    }

    public static Bitmap compressImage(Context context, File uri, int reqWidth, int reqHeight) throws IOException{
        Bitmap bitmap = decodeSampleFromFile(context,  uri,  reqWidth,  reqHeight);
        if (bitmap != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while ( baos.toByteArray().length / 1024 > 60) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();//重置baos即清空baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;//每次都减少10
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
            //把ByteArrayInputStream数据生成图片
            return BitmapFactory.decodeStream(isBm, null, null);
        }
        return null;
    }
	
	private static void computeSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int newWidth = reqWidth;
		int newHeight = reqHeight;
		if (reqWidth != reqHeight) {
			final int bitmapHeight = options.outHeight;
		    final int bitmapWidth = options.outWidth;
			
			if (reqHeight == 0) {
				float scaleWidht = ((float) reqWidth / bitmapWidth);
				newHeight = (int) (bitmapHeight * scaleWidht);
			}
			
			if (reqWidth == 0) {
				float scaleHeight = ((float) reqHeight / bitmapHeight);
				newWidth = (int) (bitmapWidth * scaleHeight);
			}
		}
		options.inSampleSize = calculateInSampleSize(options, newWidth, newHeight);
	}
	
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	        if (width > height) {
	            inSampleSize = Math.round((float)height / (float)reqHeight);
	        } else {
	            inSampleSize = Math.round((float)width / (float)reqWidth);
	        }
	    }
	    return inSampleSize;
	}
	
}
