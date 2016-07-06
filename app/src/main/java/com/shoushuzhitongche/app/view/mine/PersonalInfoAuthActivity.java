package com.shoushuzhitongche.app.view.mine;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.dixiang.framework.utils.AlertDialogs;
import com.dixiang.framework.utils.ToastUtil;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.utls.UploadUtils;
import com.shoushuzhitongche.app.utls.UploadUtils.OnLoadListener;
import com.shoushuzhitongche.app.view.mine.bean.ImgQiNiuEntity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalInfoAuthActivity extends BaseActivity implements OnClickListener{
	private NoScrollGridView noScrollgridview;
	private List<ImageItem> dataChoosed = new ArrayList<ImageItem>();// 选中的item
	private BitmapCache cache;
	private GridAdapter adapter;
	private int sum = Constants.PHOTO_MAX_SIZE;
	private Button button;
	private boolean isAdd = false;
	private String verified;
	private Button btn_upcase;
	
	private ProgressDialog progressDialog;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			progressDialog.dismiss();
			LoadingDialog.getInstance(activity).dismiss();
			if(msg.what == 1){
				ToastUtil.showToast(activity, "有一张照片上传失败！");
			}
			if(msg.what == 0){
				sum++;
				button.setText("上传中("+(sum)+"/"+dataChoosed.size()+")");
				if(sum == dataChoosed.size()){
					ToastUtil.showToast(activity, "全部上传成功！");
					getDeptAlert();//返回主页
				}
			}
			if(msg.what == 2){
				button.setEnabled(true);
				button.setText("提交");
				getDeptAlert();//返回主页
				
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalinfo_auth);
		initView();
		
		doNetGetImg();
	}

	@Override
	public void initHeaderView() {
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialogs dlg = AlertDialogs.getAlertDialog(activity);
				dlg.setTitle("提示");
				dlg.setOk( "确定" );
				dlg.showAlertDialog(
						"您可以稍后完成实名认证，以便同意名医主刀服务协议。",
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								finish();
							}
						});
			}
		});
		setTitleViewValue(0, 0, R.color.white);
		setTitleViewValue("实名认证");
	}

	private void initView() {
		TextView tv_state = (TextView)findViewById(R.id.tv_state);
		
		verified = getIntent().getStringExtra("verified");
		if ("未认证".equals(verified)){
			tv_state.setVisibility(View.GONE);
		}else if ("认证中".equals(verified)) {
			tv_state.setText("名医助手正在审核中，请耐心等待。");
		}
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("上传中...");

		button = (Button)findViewById(R.id.btn_commit);
		btn_upcase = (Button)findViewById(R.id.btn_upcase);
		
		btn_upcase.setOnClickListener(this);
		
		
		cache = new BitmapCache(this, Constants.PHOTOWIDTH);

		noScrollgridview = (NoScrollGridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

		adapter = new GridAdapter(this);
		noScrollgridview.setAdapter(adapter);

		noScrollgridview
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 == dataChoosed.size()) {
					Intent intent = new Intent(activity,ImageGridActivity.class);
					intent.putExtra(Constants.PARMS.PHONE_NEED_SUM, sum);
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
		
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Constants.INTENTCODE.UPLOAD_FILE_ACTIVIY){
			dataChoosed = BimpTempHelper.getInstance().getImageItemsChoosend();
			noScrollgridview.setAdapter(adapter);
		}
	}

	public void onCommitFile(View view){
		if(!isAdd || dataChoosed == null || dataChoosed.size() == 0){
			ToastUtil.showToast(activity, "没有添加新的照片！");
			return;
		}

		if (dataChoosed != null && dataChoosed.size() > 0) {

			AlertDialogs.getAlertDialog(activity).showAlertDialog(
					"您已经选中了" + dataChoosed.size() + "张认证资料,确认提交！",
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
							//uploadFile();
							upQiniu();
							
						}
					});
		}

	}



	private void upQiniu() {
		progressDialog.show();
		final List<File> files = new ArrayList<File>();
		for (int i = 0; i < dataChoosed.size(); i++) {
			files.add(new File(dataChoosed.get(i).imagePath));
		}

		//TODO 
		String phone = CommonUtils.getMobile(activity);
		if(phone!=null&&phone.length()>=4){// 判断是否长度大于等于4
			phone = phone.substring(phone.length()-4, phone.length());
		}
		UploadUtils uploadUtils =new UploadUtils( "doc_"+phone);//现在随便写了一个doctor[id]
		///医生端上传患者预约病历或小结的空间权限
		uploadUtils.uploadFiles(activity, UploadUtils.PATIENT_QN_TOKEN, files, new OnLoadListener() {

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

		//map.put("appFile[patientId]", "200");//现在随便写了一个患者id
		
//		map.put("appFile[patientId]", "5501");//现在随便写了一个患者id
//		map.put("appFile[reportType]", "mr");//类型mr(病历 默认)dc(出院小结 暂无)
		
		QJNetUICallback2 callback = new QJNetUICallback2<QjResult<HashMap<String, Object>>>(activity) {
			@Override
			public void onSuccess(QjResult<HashMap<String, Object>> result) {
				if (result != null) {
					if (index+1 ==dataChoosed.size()) {
						//ToastUtil.showToast(activity, "提交自己服务器成功！",Toast.LENGTH_LONG);
						handler.sendEmptyMessage(2);
					}else {
						handler.sendEmptyMessage(0);
					} 
				}else {
					handler.sendEmptyMessage(1);
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

		NetOld.with(activity).fetch(UploadUtils.PATIENT_SAVE_APP_CER_FILE, map, new TypeToken<QjResult<HashMap<String, Object>>>() {}, callback);
		//NetOld.with(activity).fetch(UploadUtils.PATIENT_SAVE_APP_FILE, map, new TypeToken<QjResult<HashMap<String, String>>>() {}, callback);//病人病例
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
				if (position == Constants.PHOTO_MAX_SIZE|| !isAdd) {
					holder.image.setVisibility(View.GONE);
					convertView.setVisibility(View.GONE);
				}

			} else {
				if(dataChoosed.get(position).imagePath.contains("http://")){
					Net.displayImage(dataChoosed.get(position).imagePath, holder.image, R.drawable.default_doctor);
				}else
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
	protected void onDestroy() {
		super.onDestroy();
		LoadingDialog.getInstance(activity).dismiss();
		BimpTempHelper.getInstance().clearData();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AlertDialogs dlg = AlertDialogs.getAlertDialog(activity);
			dlg.setTitle("提示");
			dlg.setOk( "确定" );
			dlg.showAlertDialog(
					"您可以稍后完成实名认证，以便同意名医主刀服务协议。",
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							finish();
						}
					});
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private void getDeptAlert() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,dialog_fb_instruction.xml文件中定义view内容
       // window.setWindowAnimations(R.style.mydlgstyle);  //添加动画
        window.setContentView(R.layout.view_dialog_one);
       // window.setGravity(Gravity.BOTTOM);
        dlg.setCancelable(false);
        Button ok = (Button) window.findViewById(R.id.ok);
        ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
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
						
                        if (list.size()>0) {
                        	btn_upcase.setVisibility(View.VISIBLE);
							//btn_upcase.setVisibility(View.GONE);
						}
                        
                        if (list.size()==0) {
                        	isAdd=true;
    						noScrollgridview.setAdapter(adapter);
						}
						
						for(int i = 0; i < list.size();  i ++){
							ImageItem item = new ImageItem();
							item.imagePath = list.get(i).getThumbnailUrl();
							item.upImagePath = list.get(i).getAbsFileUrl();
							dataChoosed.add(item);
							noScrollgridview.setAdapter(adapter);
						}
						
					}else {
						isAdd=true;
						noScrollgridview.setAdapter(adapter);
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
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_upcase:
			dataChoosed.clear();
			isAdd = true;
			btn_upcase.setVisibility(View.GONE);
			Intent intent = new Intent(activity,ImageGridActivity.class);
			intent.putExtra(Constants.PARMS.PHONE_NEED_SUM, sum);
			startActivityForResult(intent,Constants.INTENTCODE.UPLOAD_FILE_ACTIVIY);
			break;
		default:
			break;
		}
	}	
}
