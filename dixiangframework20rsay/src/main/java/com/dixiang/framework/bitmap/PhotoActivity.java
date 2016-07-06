package com.dixiang.framework.bitmap;

import java.util.ArrayList;
import java.util.List;

import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.R;
import com.dixiang.framework.bitmap.BimpTempHelper;
import com.dixiang.framework.bitmap.BitmapCache;
import com.dixiang.framework.bitmap.ImageItem;
import com.dixiang.framework.network.Net;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoActivity extends BaseActivity {

    private ArrayList<View> listViews = null;
    private ViewPager pager;
    private MyPageAdapter adapter;
    private int count;
    public int max;
    BitmapCache cache;
    private Handler handler = new Handler();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        cache = new BitmapCache(this,640);
        TextView photo_bt_exit = (TextView) findViewById(R.id.photo_bt_exit);
        photo_bt_exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	setResult(RESULT_OK);
                finish();
            }
        });

        photo_bt_exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listViews.size() == 1) {
                    BimpTempHelper.getInstance().imageItemsChoosend.clear();
                    finish();
                } else {
                    BimpTempHelper.getInstance().imageItemsChoosend.remove(count);
                    pager.removeAllViews();
                    listViews.remove(count);
                    adapter.setListViews(listViews);
                    adapter.notifyDataSetChanged();
                    count = count > BimpTempHelper.getInstance().getImageItemsChoosend().size() - 1 ? count - 2 : count;
                    pageChangeListener.onPageSelected(count);
                }
            }
        });
        TextView photo_bt_enter = (TextView) findViewById(R.id.photo_bt_enter);
        photo_bt_enter.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            	setResult(RESULT_OK);
                finish();
            }
        });

        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setOnPageChangeListener(pageChangeListener);
        
        if( BimpTempHelper.getInstance().getImageItemsChoosend().size()>0 )
		{
        	 for (int i = 0; i < BimpTempHelper.getInstance().getImageItemsChoosend().size(); i++) {
                 initListViews(BimpTempHelper.getInstance().getImageItemsChoosend().get(i));//
             }
		}else {
			List<ImageItem> dataChoosed = ( List<ImageItem> )getIntent().getSerializableExtra( "dataChoosed" );
			if( dataChoosed!=null&&dataChoosed.size()>0 )
			{
				 for (int i = 0; i < dataChoosed.size(); i++) {
	                 initListViews(dataChoosed.get(i));//
	             }
			}
		}
       
        
        
        adapter = new MyPageAdapter(listViews);// 构造adapter
        pager.setAdapter(adapter);// 设置适配器
        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);
        pager.setCurrentItem(id);
    }

    private void initListViews(ImageItem bm) {
        if (listViews == null)
            listViews = new ArrayList<>();
        ImageView img = new ImageView(this);// 构造textView对象
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        img.setBackgroundColor(0xff000000);
        img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));
        if( bm.upImagePath!=null&&bm.upImagePath.contains("http://") )
		{
        	img.setTag(bm.upImagePath);
        	Net.displayImage(bm.upImagePath, img, R.drawable.default_loading);
		}else {
			img.setTag(bm.imagePath);
			 cache.displayBmp(img,bm.imagePath,handler);
		}
       
        listViews.add(img);// 添加view
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {// 页面选择响应函数
            count = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

        }

        public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

        }
    };

    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;// content

        private int size;// 页数

        public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
            // 初始化viewpager的时候给的一个页面
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {// 返回数量
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
            ((ViewPager) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {// 返回view对象
            try {
                ((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cache.clearCache();
    }

    @Override
  	public void initHeaderView() {
  		setTopViewBg(R.color.actionbar_bg_color);
  		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
  			@Override
  			public void onClick(View v) {
  				finish();
  			}
  		});
  	}
}
