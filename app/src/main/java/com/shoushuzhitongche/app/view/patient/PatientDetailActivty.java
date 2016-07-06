package com.shoushuzhitongche.app.view.patient;

import java.io.File;
import java.io.Serializable;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.bitmap.BimpTempHelper;
import com.dixiang.framework.bitmap.ImageGridActivity;
import com.dixiang.framework.bitmap.ImageItem;
import com.dixiang.framework.bitmap.NoScrollGridView;
import com.dixiang.framework.bitmap.PhotoActivity;
import com.dixiang.framework.common.QJNetUICallback;
import com.dixiang.framework.common.QJNetUICallback2;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.config.SystemConfig;
import com.dixiang.framework.network.Net;
import com.dixiang.framework.network2.NetOld;
import com.dixiang.framework.utils.BitmapCache;
import com.dixiang.framework.utils.ToastUtil;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.utls.UploadUtils;
import com.shoushuzhitongche.app.utls.UploadUtils.OnLoadListener;
import com.shoushuzhitongche.app.view.booking.BookingActivity;
import com.shoushuzhitongche.app.view.booking.UploadFileCommonActivity;
import com.shoushuzhitongche.app.view.mine.bean.ImgQiNiuEntity;
import com.shoushuzhitongche.app.view.patient.bean.PatientDetailEntity;

public class PatientDetailActivty extends BaseActivity{
	private String patientId;
	private String actionUrl;
	private GridAdapter adapter;
	private NoScrollGridView noScrollgridview;
	private List<ImageItem> dataChoosed = new ArrayList<ImageItem>();//选中的item
	private Handler handler = new Handler();
	private BitmapCache cache;
	private boolean isAdd = false;
	
	private Button btn_upcase;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_detail);
		patientId = getIntent().getStringExtra(Constants.PARMS.ID);
		actionUrl = getIntent().getStringExtra(Constants.PARMS.ACTIONURL);
		initView();
		doNet();
		doNetGetImg();
	}
	
	
	private void initView(){
		progressDialog = new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("上传中...");
		
		btn_upcase = (Button)findViewById(R.id.btn_upcase);
		btn_upcase.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity,UploadFileCommonActivity.class);
				
				intent.putExtra(Constants.PARMS.ID, patientId);
				intent.putExtra("tokenUrl", UploadUtils.PATIENT_MR_QN_TOKEN );
				intent.putExtra("upMyServerUrl", UploadUtils.PATIENT_SAVE_APP_FILE);
				intent.putExtra("reportType", "mr");
				
				startActivityForResult(intent, 199);
				
			}
		});
		
		
		noScrollgridview = (NoScrollGridView)findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

		adapter = new GridAdapter(this);
		noScrollgridview.setAdapter(adapter);

		noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == dataChoosed.size()) {

					Intent intent = new Intent(activity,
							ImageGridActivity.class);
					startActivityForResult(intent, 1011);
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
	}
	

	@Override
	public void initHeaderView() {
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		setTitleViewValue(0,0,R.color.white);
		setTitleViewValue("患者详情");
	}

	public void goBooking(View view){
		Bundle bundle = new Bundle();
		bundle.putString(Constants.PARMS.ID, patientId);
		IntentHelper.getInstance(activity).gotoActivity(BookingActivity.class, bundle);
	}
	
	private void doNet(){
		LoadingDialog.getInstance(activity).show();
		String url = actionUrl+CommonUtils.getGetParm(activity,"api="+SystemConfig.API);
		
		getHttpResult(url, Constants.HTTP_INDEX.First, new TypeToken<QjResult<Map<String,PatientDetailEntity>>>() {});
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		Map<String, PatientDetailEntity> map = (Map<String, PatientDetailEntity>) result.getResults();
		if(map != null && !map.isEmpty()){
			PatientDetailEntity patientDetailEntity = map.get("patientInfo");
			loadView(patientDetailEntity);
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}
	
	private void loadView(final PatientDetailEntity patientDetailEntity){
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				if(patientDetailEntity != null){
					((TextView)findViewById(R.id.name)).setText(patientDetailEntity.getName());
					((TextView)findViewById(R.id.phone)).setText(patientDetailEntity.getMobile());	
					((TextView)findViewById(R.id.age)).setText(patientDetailEntity.getAge()+"岁");	
					((TextView)findViewById(R.id.city)).setText(patientDetailEntity.getCityName());	
					((TextView)findViewById(R.id.disease_name)).setText(patientDetailEntity.getDiseaseName());	
					((TextView)findViewById(R.id.dsc)).setText(patientDetailEntity.getDiseaseDetail());	
				}				
			}
		});
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if( requestCode == 1011 )
		{
			dataChoosed = BimpTempHelper.getInstance().getImageItemsChoosend();
			//			if(dataChoosed != null && dataChoosed.size() > 0){
			noScrollgridview.setAdapter(adapter);
			//			}
		}
		
		if (requestCode == 199 &&data!=null) {
			String resultDate = data.getStringExtra(UploadFileCommonActivity.SUCCESS_DATA);
			if (resultDate!=null&&resultDate.length()>0) {
				dataChoosed.clear();
				doNetGetImg();
			}
		}

	}
	private static final int FAIL = 500;// 更新下载进度的标记
	private static final int SUCCESS = 200;
	private ProgressDialog progressDialog;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case SUCCESS:
				progressDialog.dismiss();
				Toast.makeText(activity, "上传成功！", 2000).show();
				finish();
				break;

			case FAIL:
				progressDialog.dismiss();
				Toast.makeText(activity, "上传失败！", 2000).show();
				break;

			default:
				break;
			}
		}
	};
	
	/**
	 * 待用
	 */
	private void upQiniu() {
		progressDialog.show();
		final List<File> files = new ArrayList<File>();
		for (int i = 0; i < dataChoosed.size(); i++) {
			files.add(new File(dataChoosed.get(i).imagePath));
		}

		//TODO orderDetailsEntity.getBooking().getPatientId()
		UploadUtils uploadUtils =new UploadUtils( patientId);//现在随便写了一个
		///医生端上传患者预约病历或小结的空间权限
		uploadUtils.uploadFiles(activity, UploadUtils.PATIENT_MR_QN_TOKEN, files, new OnLoadListener() {

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

			map.put("appFile[size]", file.length()/1024+"");
			if (file.getName().endsWith(".png")) {
				map.put("appFile[type]", "png");
			}else {
				map.put("appFile[type]", "jpg");
			}

			map.put("appFile[remoteDomain]", remoteDomain);
			map.put("appFile[remoteKey]", key);

			map.put("appFile[patientId]", patientId);//现在随便写了一个患者id
			map.put("appFile[reportType]", "mr");//类型mr(病历 默认)dc(出院小结 暂无)


			QJNetUICallback2 callback = new QJNetUICallback2<QjResult<HashMap<String, Object>>>(activity) {
				@Override
				public void onSuccess(QjResult<HashMap<String, Object>> result) {
					if (result != null) {
						if (index+1 ==dataChoosed.size()) {
							//ToastUtil.showToast(activity, "提交自己服务器成功！",Toast.LENGTH_LONG);
							mHandler.sendEmptyMessage(SUCCESS);
						} 
					}else {
						mHandler.sendEmptyMessage(FAIL);
					}
				}

				public void onError(Exception e, QjResult<HashMap<String, Object>> result) {
					super.onError(e, result);
					ToastUtil.showToast(activity, "提交自己服务器失败！",Toast.LENGTH_LONG);
					mHandler.sendEmptyMessage(FAIL);
				}

				public void onCompleted(Exception e, QjResult<HashMap<String, Object>> result) {
					super.onCompleted(e, result);
				}
			};

			//String path = new DataHandler().postDataObject(activity, SystemConfig.SAVE_APP_FILE ,map, new TypeToken<QjResult<HashMap<String, Object>>>() {}, callback);
			NetOld.with(activity).fetch(UploadUtils.PATIENT_SAVE_APP_FILE, map, new TypeToken<QjResult<HashMap<String, String>>>() {}, callback);
		}
	
	
		private void doNetGetImg() {

		//	String url = UploadUtils.PATIENTAPP_FILE_URL + CommonUtils.getTokenParam(activity) +"&patientId="+5501+"&reportType=mr";

			String url = UploadUtils.PATIENTAPP_FILE_URL + CommonUtils.getTokenParam(activity)+"&patientId="+patientId+"&reportType=mr";
			
			
			QJNetUICallback2 callback = new QJNetUICallback2<QjResult<HashMap<String,List<ImgQiNiuEntity>>>>(activity) {
				@Override
				public void onSuccess(QjResult<HashMap<String,List<ImgQiNiuEntity>>> result) {
					if (result != null
							&& result.getResults() != null) {
						List<ImgQiNiuEntity> list = result.getResults().get("files");
						if (list != null) {

							if (list.size()>=9) {
								
								btn_upcase.setVisibility(View.GONE);
							}else {
								btn_upcase.setVisibility(View.VISIBLE);
							}
							
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
		BimpTempHelper.getInstance().clearData();
	}

}
