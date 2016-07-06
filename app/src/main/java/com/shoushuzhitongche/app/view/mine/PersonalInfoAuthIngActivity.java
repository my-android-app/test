package com.shoushuzhitongche.app.view.mine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.bitmap.BimpTempHelper;
import com.dixiang.framework.bitmap.BitmapCache;
import com.dixiang.framework.bitmap.ImageGridActivity;
import com.dixiang.framework.bitmap.ImageItem;
import com.dixiang.framework.bitmap.NoScrollGridView;
import com.dixiang.framework.bitmap.PhotoActivity;
import com.dixiang.framework.common.QJNetUICallback2;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.network.Net;
import com.dixiang.framework.network2.NetOld;
import com.dixiang.framework.utils.ToastUtil;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.UploadUtils;
import com.shoushuzhitongche.app.view.mine.bean.ImgQiNiuEntity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

public class PersonalInfoAuthIngActivity extends BaseActivity {
	private GridAdapter adapter;
	private NoScrollGridView noScrollgridview;
	private List<ImageItem> dataChoosed = new ArrayList<ImageItem>();//选中的item
	private BitmapCache cache;
	private boolean isAdd = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalinfo_authing);
		initView();
	}

	@Override
	public void initHeaderView() {
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setTitleViewValue(0, 0, R.color.white);
		setTitleViewValue("实名认证");
	}

	private void initView() {
		cache = new BitmapCache(this,Constants.PHOTOWIDTH);
		
		noScrollgridview = (NoScrollGridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

		adapter = new GridAdapter(this);
		noScrollgridview.setAdapter(adapter);

		noScrollgridview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (arg2 == dataChoosed.size()) {
							Intent intent = new Intent(activity,
									ImageGridActivity.class);
							intent.putExtra(Constants.PARMS.PHONE_NEED_SUM, getIntent().getIntExtra(Constants.PARMS.PHONE_NEED_SUM, Constants.PHOTO_MAX_SIZE));
							startActivityForResult(intent,Constants.INTENTCODE.UPLOAD_FILE_ACTIVIY);
						} else {
							Intent intent = new Intent(activity,PhotoActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("dataChoosed", ( Serializable )dataChoosed);
							intent.putExtras(bundle);
							intent.putExtra("ID", arg2);
							startActivityForResult(intent, 1012);
						}
					}
				});
		
		
		doNetGetImg();
	}
	
	
    private void doNetGetImg() {
		
    	String url = UploadUtils.PATIENTAPP_FILE_CER_URL + CommonUtils.getTokenParam(activity);
    	//String url = UploadUtils.PATIENTAPP_FILE_CER_URL + "?username=13127640379"+"&token=AE39B979C3E47719F9AA4F2C2D9C275B";
		QJNetUICallback2 callback = new QJNetUICallback2<QjResult<HashMap<String,List<ImgQiNiuEntity>>>>(activity) {
			@Override
			public void onSuccess(QjResult<HashMap<String,List<ImgQiNiuEntity>>> result) {
				if (result != null
						&& result.getResults() != null) {
					List<ImgQiNiuEntity> list = result.getResults().get("files");
					if (list != null) {
						
						for(int i = 0; i < list.size();  i ++){
							ImageItem item = new ImageItem();
							item.imagePath = list.get(i).getThumbnailUrl();
							item.upImagePath = list.get(i).getAbsFileUrl();
							dataChoosed.add(item);
							noScrollgridview.setAdapter(adapter);
						}
						
					}
				}
			}

			public void onError(Exception e, QjResult<HashMap<String,List<ImgQiNiuEntity>>> result) {
				super.onError(e, result);
				  ToastUtil.showToast(activity, "获取失败！",Toast.LENGTH_LONG);
			}

			public void onCompleted(Exception e, QjResult<HashMap<String,List<ImgQiNiuEntity>>> result) {
				super.onCompleted(e, result);
			}
		};
		NetOld.with(activity).fetch( url ,null, new TypeToken<QjResult<HashMap<String,List<ImgQiNiuEntity>>>>() {}, callback);
		
	}
	
    public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			if (dataChoosed.size() == Constants.PHOTO_MAX_SIZE) {
				return Constants.PHOTO_MAX_SIZE;
			}
			return (dataChoosed.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		 public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.myablum_item,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.blum_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == dataChoosed.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == Constants.PHOTO_MAX_SIZE || !isAdd) {
					holder.image.setVisibility(View.GONE);
					convertView.setVisibility(View.GONE);
				}

			} else {
				if(dataChoosed.get(position).imagePath.contains("http://")){
					Net.displayImage(dataChoosed.get(position).imagePath, holder.image, R.drawable.photo_loading_image);
				}else{
					cache.displayBmp(holder.image,dataChoosed.get(position).imagePath,handler);	
				}

			}
			return convertView;
		 }

		 public class ViewHolder {
			 public ImageView image;
		 }

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LoadingDialog.getInstance(activity).dismiss();
		BimpTempHelper.getInstance().clearData();
	}
	
}
