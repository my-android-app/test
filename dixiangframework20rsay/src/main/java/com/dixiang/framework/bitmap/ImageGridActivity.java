package com.dixiang.framework.bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.R;
import com.dixiang.framework.bitmap.AlbumHelper;
import com.dixiang.framework.bitmap.BimpTempHelper;
import com.dixiang.framework.bitmap.BitmapCache;
import com.dixiang.framework.bitmap.ImageGridAdapter.TextCallback;
import com.dixiang.framework.bitmap.ImageItem;
import com.dixiang.framework.utils.Constants;
import com.dixiang.framework.utils.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ImageGridActivity extends BaseActivity {
    GridView gridView;
    List<ImageItem> dataList;//当前页面的imageItem
    ImageGridAdapter adapter;
    Button bt;
    private static final String TAG ="ImageGridActivity";
    private Context context;
    private boolean isDymic;//是否是动态创建，默认false
    private BitmapCache bitmapCache;
    private int sum;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ImageGridActivity.this, "最多选择" + sum + "张图片", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        sum = getIntent().getIntExtra(Constants.PARMS.PHONE_NEED_SUM, Constants.PHOTO_MAX_SIZE);
        bt = (Button) findViewById(R.id.bt);
        context = ImageGridActivity.this;
        bitmapCache = new BitmapCache(this,Constants.PHOTOWIDTH);
        bt.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (BimpTempHelper.getInstance().getImageItemsChoosend().size() == 0) {
                    ToastUtil.showToast(context, "尚未选取照片。");
                    return;
                }
//                if (isDymic) {
                //从动态页面过来的
                setResult(RESULT_OK);
                finish();
//                } else {
//                    Intent intent = new Intent(ImageGridActivity.this,
//                            PublishedActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
            }

        });
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        dataList = (List<ImageItem>) getIntent().getSerializableExtra("imagelist");
        isDymic = getIntent().getBooleanExtra("isDymic", false);
        if (dataList == null || dataList.size() == 0) {
            //如果为空
            AlbumHelper helper = AlbumHelper.getHelper();
            helper.init(context);
            dataList = helper.getCameraBucket();
//            dataList = ib.imageList;
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
////        if (keyCode == KeyEvent.KEYCODE_BACK) {
//////            BimpTempHelper.getInstance().clearData();
////            finish();
////        }
//        return true;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initView() {
        adapter = new ImageGridAdapter(ImageGridActivity.this, dataList,
                mHandler,bitmapCache);
        adapter.setMaxSize(sum);
        gridView.setAdapter(adapter);
        if (null != BimpTempHelper.getInstance().getImageItemsChoosend()) {
            bt.setText("完成" + "(" + BimpTempHelper.getInstance().getImageItemsChoosend().size() + "/" + sum  + ")");//
        }

        adapter.setTextCallback(new TextCallback() {
            public void onListen(int count) {
                bt.setText("完成" + "(" + count + "/" + sum  + ")");
            }
        });

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.TAKEPHOTOFROMCAMERA) {
                String sdState = Environment.getExternalStorageState();
                if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }
                Bundle b = data.getExtras();
                Bitmap bitmap = (Bitmap) b.get("data");
                FileOutputStream fout = null;
                String path = Environment.getExternalStorageDirectory().getPath()+"mingyizhudao/";
                File file = new File(path);
                file.mkdirs();
                String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                String filename = file.getPath() + name;
                try {
                    fout = new FileOutputStream(filename);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fout.flush();
                        fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ImageItem item = new ImageItem();
                item.imagePath = filename;
                item.isSelected = true;
                BimpTempHelper.getInstance().getImageItemsChoosend().add(item);
                dataList.add(0,item);
                adapter.setDataList(dataList);
                bt.setText("完成" + "(" + BimpTempHelper.getInstance().getImageItemsChoosend().size() + "/" + sum  + ")");//
                File f = new File(filename);
                if(f != null){
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(f);
                    intent.setData(uri);
                    context.sendBroadcast(intent);
                }
            } else if (requestCode == 1001) {
                //相册页面返回
                dataList = (List<ImageItem>) data.getSerializableExtra("imagelist");
                adapter.setDataList(dataList);
            }
        }
    }

    public void onPause() {
        super.onPause();
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
		setTitleViewValue(R.string.select_photo_title, 0, R.color.white);
	}


}
