package com.shoushuzhitongche.app.view.booking;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.bitmap.BimpTempHelper;
import com.dixiang.framework.bitmap.BitmapCache;
import com.dixiang.framework.bitmap.ImageGridActivity;
import com.dixiang.framework.bitmap.ImageItem;
import com.dixiang.framework.bitmap.NoScrollGridView;
import com.dixiang.framework.bitmap.PhotoActivity;
import com.dixiang.framework.common.QJNetUICallback2;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.network2.NetOld;
import com.dixiang.framework.utils.AlertDialogs;
import com.dixiang.framework.utils.ToastUtil;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.UploadUtils;
import com.shoushuzhitongche.app.utls.UploadUtils.OnLoadListener;

public class UploadFileCommonActivity extends BaseActivity {
	
	public final static int SUCCESS_PAGE = 200;
	public final static String SUCCESS_DATA = "success_data";
	private NoScrollGridView noScrollgridview;
	private List<ImageItem> dataChoosed = new ArrayList<ImageItem>();// 选中的item
	private int sum = 0;
	private String bookingId;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			
			
			if(msg.what == 1){
				progressDialog.dismiss();
				button.setEnabled(true);
			}
			if(msg.what == 0){
				sum++;
				button.setText("上传中("+sum+"/"+dataChoosed.size()+")");
			}
			if (msg.what == 2) {
				 progressDialog.dismiss();
				 Intent intent=new Intent();
				 intent.putExtra(SUCCESS_DATA, "success");
				 setResult( SUCCESS_PAGE, intent);
				 finish();
			}
		}
		
	};
	private BitmapCache cache;
	private GridAdapter adapter;
	
	private Button button;

	
	
	private String patientId;
	private String tokenUrl;
	private String upMyServerUrl;
	private String reportType;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_file_common);
		initView();
		initData();
	}

	private void initData() {
		cache = new BitmapCache(this, Constants.PHOTOWIDTH);
		patientId = getIntent().getStringExtra(Constants.PARMS.ID);
		tokenUrl = getIntent().getStringExtra("tokenUrl");
		upMyServerUrl = getIntent().getStringExtra("upMyServerUrl");
		reportType = getIntent().getStringExtra("reportType");
		bookingId = getIntent().getStringExtra("bookingId");
	}

	private void initView() {
		button = (Button)findViewById(R.id.btn_commit);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("上传中...");
		
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
							startActivityForResult(intent,
									Constants.INTENTCODE.UPLOAD_FILE_ACTIVIY);
						} else {
							Intent intent = new Intent(activity,
									PhotoActivity.class);
							intent.putExtra("ID", arg2);
							startActivityForResult(intent, 1012);
						}
					}
				});
	}

	@Override
	public void initHeaderView() {
		setTopViewBg(R.color.actionbar_bg_color);
		setTitleViewValue(R.string.upload_patient_file, 0, R.color.white);
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	
	}

	public void onDataCommit(View view) {
		
		if (dataChoosed != null && dataChoosed.size() > 0) {

			AlertDialogs.getAlertDialog(activity).showAlertDialog(
					"您已经选中了" + dataChoosed.size() + "张影像资料,确认提交！",
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									button.setText("上传中(0/"+dataChoosed.size()+")");
									button.setEnabled(false);
									LoadingDialog.getInstance(activity).show("图片上传中...");
								}
							});
							upQiniu();
						}
					});
		}else{
			ToastUtil.showToast(activity, "您尚未选择任何照片！", 3000);
		}
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		dataChoosed = BimpTempHelper.getInstance().getImageItemsChoosend();
		noScrollgridview.setAdapter(adapter);
	}

	
	private void upQiniu() {
		progressDialog.show();
		final List<File> files = new ArrayList<File>();
		for (int i = 0; i < dataChoosed.size(); i++) {
			files.add(new File(dataChoosed.get(i).imagePath));
		}

		//TODO orderDetailsEntity.getBooking().getPatientId()
		UploadUtils uploadUtils =new UploadUtils( patientId );//现在随便写了一个
		///医生端上传患者预约病历或小结的空间权限
		uploadUtils.uploadFiles(activity, tokenUrl, files, new OnLoadListener() {

			@Override
			public void onLoadSuccess(String remoteDomain ,String key, String str, int index) {
				doTestNet(remoteDomain ,files.get(index), index  ,key);
			}

			@Override
			public void onLoadSuccess(String remoteDomain ,String key, String str) {
				LoadingDialog.getInstance(activity).dismiss();
			}

			@Override
			public void onLoadFail() {
				progressDialog.dismiss();
				LoadingDialog.getInstance(activity).dismiss();
				Toast.makeText(activity, "上传失败！", Toast.LENGTH_LONG).show();

			}
		});

	}
	
	   //上传七牛成功数据保存至自己服务器
		@SuppressWarnings("unchecked")
		private void doTestNet(String remoteDomain ,File file ,final int index, String key) {

			Map map = new HashMap<String, Object>();

			map.put("appFile[token]", CommonUtils.getToken(activity));
			map.put("appFile[username]", CommonUtils.getMobile(activity));
			
//			map.put("appFile[token]", "AE39B979C3E47719F9AA4F2C2D9C275B");
//			map.put("appFile[username]", "13127640379");

			map.put("appFile[size]", file.length()/1024+"");
			if (file.getName().endsWith(".png")) {
				map.put("appFile[type]", "png");
			}else {
				map.put("appFile[type]", "jpg");
			}

			map.put("appFile[remoteDomain]", remoteDomain);
			map.put("appFile[remoteKey]", key);

			map.put("appFile[patientId]", patientId);//现在随便写了一个患者id
			map.put("appFile[reportType]", reportType);//类型mr(病历 默认)dc(出院小结 暂无)


			QJNetUICallback2 callback = new QJNetUICallback2<QjResult<HashMap<String, Object>>>(activity) {
				@Override
				public void onSuccess(QjResult<HashMap<String, Object>> result) {
					if (result != null) {
						if (index+1 ==dataChoosed.size()) {
							//ToastUtil.showToast(activity, "提交自己服务器成功！",Toast.LENGTH_LONG);
							handler.sendEmptyMessage(2);//全部成功
							if(reportType.equals("dc")){
								String url = Constants.baseURL +"taskpatientda/"+bookingId;
								getHttpResult(url, Constants.HTTP_INDEX.Fourth, new TypeToken<QjResult<Object>>() {});
							}
						}else {
							handler.sendEmptyMessage(0);//成功了某一张
						} 
						
					}else {
						handler.sendEmptyMessage(1);//某一张
					}
				}

				public void onError(Exception e, QjResult<HashMap<String, Object>> result) {
					super.onError(e, result);
					ToastUtil.showToast(activity, "提交自己服务器失败！",Toast.LENGTH_LONG);
					handler.sendEmptyMessage(1);
				}

				public void onCompleted(Exception e, QjResult<HashMap<String, Object>> result) {
					super.onCompleted(e, result);
				}
			};

			NetOld.with(activity).fetch(upMyServerUrl, map, new TypeToken<QjResult<HashMap<String, String>>>() {}, callback);
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
				convertView = inflater.inflate(R.layout.myablum_item, parent,
						false);
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
				if (position == Constants.PHOTO_MAX_SIZE) {
					holder.image.setVisibility(View.GONE);
					convertView.setVisibility(View.GONE);
				}

			} else {
				cache.displayBmp(holder.image,
						dataChoosed.get(position).imagePath, handler);
			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

	}
	
	@Override
	public void onBackPressed() {
		AlertDialogs.getAlertDialog(this).showAlertDialog(
				R.string.isCancell, new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
		return;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LoadingDialog.getInstance(activity).dismiss();
		BimpTempHelper.getInstance().clearData();
	}

}
