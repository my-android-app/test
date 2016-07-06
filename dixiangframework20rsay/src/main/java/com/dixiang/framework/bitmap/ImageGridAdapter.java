package com.dixiang.framework.bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dixiang.framework.R;
import com.dixiang.framework.bitmap.BimpTempHelper;
import com.dixiang.framework.bitmap.BitmapCache;
import com.dixiang.framework.bitmap.ImageItem;
import com.dixiang.framework.common.BaseViewHolder;
import com.dixiang.framework.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageGridAdapter extends BaseAdapter {

    private TextCallback textcallback = null;
    final String TAG = getClass().getSimpleName();
    Activity act;
    List<ImageItem> dataList;//当前备选项
    public Map<String, String> map = new HashMap<>();
    private BitmapCache bitmapCache;
    private Handler mHandler;
    public List<ImageItem> imageItemsChoosend = new ArrayList<>();//选中的照片
    private int maxSize = Constants.PHOTO_MAX_SIZE;

    public static interface TextCallback {
        public void onListen(int count);
    }
    
    public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public void setTextCallback(TextCallback listener) {
        textcallback = listener;
    }

    public ImageGridAdapter(Activity act, List<ImageItem> list, Handler mHandler ,BitmapCache bitmapCache) {
        this(act,list, mHandler);
        this.bitmapCache = bitmapCache;
    }

    public ImageGridAdapter(Activity act, List<ImageItem> list, Handler mHandler) {
        this.act = act;
        this.dataList = list;
        this.imageItemsChoosend = BimpTempHelper.getInstance().getImageItemsChoosend();
        this.mHandler = mHandler;
        initData();
    }

    private void initData() {
        //初始化数据，主要删选数据，进行部分记忆(过滤)
        for (ImageItem item : dataList) {
            boolean flag = false;
            for (ImageItem i : imageItemsChoosend) {
                if (i.imagePath.equals(item.imagePath)) {
                    flag = true;
                }
            }
            if (flag) {
                item.isSelected = true;
            } else {
                item.isSelected = false;
            }
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        if (dataList != null) {
            count = dataList.size();
        }
        return count + 1;
    }

    @Override
    public ImageItem getItem(int position) {
        if (position == 0) {
            return new ImageItem();
        }
        return dataList.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDataList(List<ImageItem> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || ((ViewHolder) convertView.getTag()).isFirst) {
            convertView = View.inflate(act, R.layout.item_image_grid, null);
            viewHolder = new ViewHolder(convertView);
            if (position != 0) {
                viewHolder.isFirst = false;
            } else {
                viewHolder.isFirst = true;
            }
            convertView.setTag(viewHolder);
            viewHolder.populateView(position, getItem(position));
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.populateView(position, getItem(position));
        }
        return convertView;
    }

    class ViewHolder extends BaseViewHolder<ImageItem> {
        private TextView gridText;//照片名
        private ImageView image;//图片
        private ImageView isselected;//是否被选中
        private boolean isFirst = false;


        /**
         * @param view 已经初始化的view
         */
        public ViewHolder(View view) {
            super(view);
            gridText = (TextView)view.findViewById(R.id.item_image_grid_text);
            image = (ImageView)view.findViewById(R.id.image);
            isselected = (ImageView)view.findViewById(R.id.isselected);
        }

        @Override
        public void populateView(final int position, final ImageItem item) {

            if (position == 0) {
                //添加拍照选项
                image.setImageBitmap(BitmapFactory.decodeResource(act.getResources(), R.drawable.take_picofcamera));
                isselected.setVisibility(View.GONE);
                gridText.setVisibility(View.GONE);
                image.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (BimpTempHelper.getInstance().getImageItemsChoosend().size() < maxSize) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            act.startActivityForResult(intent, Constants.TAKEPHOTOFROMCAMERA);
                        } else {
                        	Toast.makeText(act, "最多选择" + maxSize + "张图片",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {

                if (item.isSelected) {
                    isselected.setImageResource(R.drawable.photo_select);
                    gridText.setBackgroundResource(R.drawable.bgd_relatly_line);
                } else {
                    isselected.setImageResource(R.drawable.photo_unselect);
                    gridText.setBackgroundColor(0x00000000);
                }
//                isselected.setVisibility(View.VISIBLE);
//                gridText.setVisibility(View.VISIBLE);
                image.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (BimpTempHelper.getInstance().getImageItemsChoosend().contains(item)) {
                            //如果已经被选中，去除
                            BimpTempHelper.getInstance().getImageItemsChoosend().remove(item);
                            item.isSelected = false;
//                            notifyDataSetChanged();
                            textcallback.onListen(BimpTempHelper.getInstance().getImageItemsChoosend().size());
                            isselected.setImageResource(R.drawable.photo_unselect);
                            gridText.setBackgroundColor(0x00000000);
//                            notifyDataSetChanged();
                        } else {
                            //首先判断是否超过max
                            Log.e("ImageGridAdapter", maxSize + "");
                            if (BimpTempHelper.getInstance().getImageItemsChoosend().size() >= maxSize) {
                            	Toast.makeText(image.getContext(), "最多可选择" + maxSize + "张图片",Toast.LENGTH_LONG).show();
                            } else {
                                BimpTempHelper.getInstance().getImageItemsChoosend().add(item);
                                item.isSelected = true;
                                textcallback.onListen(BimpTempHelper.getInstance().getImageItemsChoosend().size());
//                                notifyDataSetChanged();
                                isselected.setImageResource(R.drawable.photo_select);
                                gridText.setBackgroundResource(R.drawable.bgd_relatly_line);
                            }
                        }
                    }

                });
                bitmapCache.displayBmp(image, item.imagePath ,mHandler);
            }
        }
    }
}
