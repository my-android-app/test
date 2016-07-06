package com.shoushuzhitongche.app.view.order;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.bitmap.BimpTempHelper;
import com.dixiang.framework.bitmap.ImageGridActivity;
import com.dixiang.framework.bitmap.ImageItem;
import com.dixiang.framework.bitmap.NoScrollGridView;
import com.dixiang.framework.bitmap.PhotoActivity;
import com.dixiang.framework.common.QJNetUICallback2;
import com.dixiang.framework.common.QjResult;
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
import com.shoushuzhitongche.app.view.PayViewActivity;
import com.shoushuzhitongche.app.view.booking.UploadFileCommonActivity;
import com.shoushuzhitongche.app.view.mine.bean.ImgQiNiuEntity;
import com.shoushuzhitongche.app.view.order.bean.OrderDetails2Entity;
import com.shoushuzhitongche.app.view.order.bean.PaysEntity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class OrderDetailsActivity extends BaseActivity{
	private OrderDetails2Entity orderDetailsEntity ;//需要去掉的.......
	private LinearLayout ll_submit_date,ll_confirm_date ,ll_zhifu;
	private TextView tv_review_tag ;
	private NoScrollGridView noScrollgridview ;//照片显示
	private GridAdapter adapter;
	private BitmapCache cache;
	private List<ImageItem> dataChoosed = new ArrayList<ImageItem>();// 选中的item
	private boolean isAdd = false;
	private TextView tv_btn ,tv_chongxin;
	private LinearLayout ll_xiaojie ;
	private static final int FAIL = 500;// 更新下载进度的标记
	private static final int SUCCESS = 200;
	private ProgressDialog progressDialog;
	private String actionUrl ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		cache = new BitmapCache(this, Constants.PHOTOWIDTH);

		ll_submit_date = (LinearLayout) findViewById(R.id.ll_submit_date);
		ll_confirm_date = (LinearLayout) findViewById(R.id.ll_confirm_date);
		//ll_xiaojie_date = (LinearLayout) findViewById(R.id.ll_xiaojie_date);
		ll_zhifu = (LinearLayout) findViewById(R.id.ll_zhifu);//审核
		tv_review_tag = (TextView) findViewById(R.id.tv_review_tag);


		ll_submit_date.setVisibility(View.GONE);
		ll_confirm_date.setVisibility(View.GONE);
		//ll_xiaojie_date.setVisibility(View.GONE);
		ll_zhifu.setVisibility(View.GONE);
		tv_review_tag.setVisibility(View.GONE);
		
		actionUrl = getIntent().getStringExtra(Constants.PARM_ACTION_URL);

		progressDialog = new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setMessage("上传中...");
		
		
		//setOrderDetail();//需要去掉的.......
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getOrderDetail();
	}
	
	@Override
	public void initHeaderView() {
		setTitleViewValue("订单详情");
		setTitleViewValue(0,0,R.color.white);
		setTopViewBg(R.color.actionbar_bg_color);
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void getOrderDetail() {
		LoadingDialog.getInstance(activity).show();
		String url = actionUrl + CommonUtils.getGetParm(activity, "");
		getHttpResult(url, Constants.HTTP_INDEX.First, new TypeToken<QjResult<OrderDetails2Entity>>() {});
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		if(flg.equals(Constants.HTTP_INDEX.First)){
			orderDetailsEntity = (OrderDetails2Entity) result.getResults(); 
			if(orderDetailsEntity != null)
				setOrderDetail();
			else
				Toast.makeText(activity, "获取数据失败！", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}

	private void setOrderDetail(){
		
		TextView tv_state = (TextView) findViewById(R.id.tv_state);//顶部的状态
		TextView tv_zhuanjia_tag = (TextView) findViewById(R.id.tv_zhuanjia_tag);//期望专家，和手术专家的tag
		TextView tv_intent = (TextView) findViewById(R.id.tv_intent);
		TextView tv_expertname = (TextView) findViewById(R.id.tv_expertname);
		TextView tv_hosptal = (TextView) findViewById(R.id.tv_hosptal);
		tv_hosptal.setVisibility(View.GONE);
		TextView tv_suggestion = (TextView) findViewById(R.id.tv_suggestion);
		TextView tv_patientname = (TextView) findViewById(R.id.tv_patientname);
		TextView tv_order_no = (TextView) findViewById(R.id.tv_order_no);
		TextView tv_create_date = (TextView) findViewById(R.id.tv_create_date);


		TextView tv_submit_date = (TextView) findViewById(R.id.tv_submit_date);
		TextView tv_yizhiyuyuejin = (TextView) findViewById(R.id.tv_yizhiyuyuejin);

		TextView tv_confirm_date = (TextView) findViewById(R.id.tv_confirm_date);
		TextView tv_yizhifufuwufei = (TextView) findViewById(R.id.tv_yizhifufuwufei);

		TextView tv_shengyu = (TextView) findViewById(R.id.tv_shengyu);
		TextView tv_gozhifu = (TextView) findViewById(R.id.tv_gozhifu);//跳转支付列表

		tv_state.setText(orderDetailsEntity.getBooking().getStatusTitle());
		tv_intent.setText(orderDetailsEntity.getBooking().getTravelType());
		tv_expertname.setText(orderDetailsEntity.getBooking().getDoctorName());
		
		String aa = orderDetailsEntity.getBooking().getExpectedDoctor();
		if (aa!=null) {
			String[]bb = aa.split("&nbsp;");
//			if (bb.length>0) {
//				tv_expertname.setVisibility(View.VISIBLE);
//				tv_expertname.setText(bb[0]);
//			}
			
			if (bb.length>1) {
				tv_hosptal.setVisibility(View.VISIBLE);
				String hosptal = "";
				for (int i = 1; i < bb.length; i++) {
					hosptal+=bb[i];
					if (i==1) {
						hosptal = hosptal+"-";
					}
				}
				tv_hosptal.setText("("+ hosptal +")");
			}
		}
		//tv_hosptal.setText();
		tv_suggestion.setText(orderDetailsEntity.getBooking().getDetail());

		tv_patientname.setText( orderDetailsEntity.getBooking().getPatientName() );
		tv_order_no.setText(orderDetailsEntity.getBooking().getRefNo());
		tv_create_date.setText(orderDetailsEntity.getBooking().getDateCreated());


		if ("1".equals(orderDetailsEntity.getBooking().getStatusCode())) {
			ll_zhifu.setVisibility(View.VISIBLE);
			PaysEntity paysEntity = orderDetailsEntity.getNotPays();
			if (paysEntity!=null) {
				tv_shengyu.setText(paysEntity.getNeedPay()+"元");
			}

			tv_gozhifu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//TODO
					//进入支付列表
//					Bundle bundle = new Bundle();
//					bundle.putString( "id", orderDetailsEntity.getBooking().getId());
//					bundle.putString("orderType", orderDetailsEntity.getNotPays().getOrderType());
////					bundle.putSerializable("orderItemEntity", item);
//					IntentHelper.getInstance(activity).getInstance(activity).gotoActivity(PayListActivity.class, bundle);
					String actionUrl = CommonUtils.getPayUrl(activity, orderDetailsEntity.getNotPays().getRefNo());
					IntentHelper.getInstance(activity).gotoActivityWithURLAndTitle(PayViewActivity.class, "订单支付", actionUrl);
				}
			});
		}
		else if ("2".equals(orderDetailsEntity.getBooking().getStatusCode())) {
			//ll_submit_date.setVisibility(View.VISIBLE);
			List<PaysEntity> pays = orderDetailsEntity.getPays();
			if (pays!=null&&pays.size()>0) {
				tv_submit_date.setText(pays.get(0).getDateClosed());
				tv_yizhiyuyuejin.setText("（已支付预约金"+pays.get(0).getFinalAmount()+"元）");
			}
		}
		else if ("5".equals(orderDetailsEntity.getBooking().getStatusCode())) {
			ll_zhifu.setVisibility(View.VISIBLE);
			//ll_submit_date.setVisibility(View.VISIBLE);
			List<PaysEntity> pays = orderDetailsEntity.getPays();
			
			if (pays!=null&&pays.size()>0) {
				
				double deposit = 0 , service = 0;
				
				for (int i = 0; i < pays.size(); i++) {
					if ("deposit".equals(pays.get(i).getOrderType())) {
						tv_submit_date.setText(pays.get(i).getDateClosed());
						deposit += Double.parseDouble(pays.get(i).getFinalAmount());
					}
					
					if ("service".equals(pays.get(i).getOrderType())) {
						tv_confirm_date.setText(pays.get(i).getDateClosed());
						service += Double.parseDouble(pays.get(i).getFinalAmount());
					}
				}
				
				tv_yizhiyuyuejin.setText("（已支付预约金"+deposit+"元）");
				
				if (service>0) {
					ll_confirm_date.setVisibility(View.VISIBLE);
					tv_yizhifufuwufei.setText("（已支付服务费"+service+"元）");
				}
				
			}
			
			
			PaysEntity paysEntity = orderDetailsEntity.getNotPays();
			if (paysEntity!=null) {
				tv_shengyu.setText(paysEntity.getNeedPay());
			}

			tv_gozhifu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//TODO
					//进入支付列表
					Bundle bundle = new Bundle();
					bundle.putString( "id", orderDetailsEntity.getBooking().getId());
					bundle.putString("orderType", orderDetailsEntity.getNotPays().getOrderType());
					IntentHelper.getInstance(activity).gotoActivity(PayListActivity.class, bundle);
				}
			});


		}//"6".equals(orderDetailsEntity.getBooking().getStatusCode())
		else if ("6".equals(orderDetailsEntity.getBooking().getStatusCode())) {

			//NoScrollGridView noScrollgridview = (NoScrollGridView) findViewById(R.id.noScrollgridview);//照片显示
			Scrollgridview();
			
			doNetGetImg();
			
			//ll_submit_date.setVisibility(View.VISIBLE);
			List<PaysEntity> pays = orderDetailsEntity.getPays();
			
			if (pays!=null&&pays.size()>0) {
				
				double deposit = 0 , service = 0;
				
				for (int i = 0; i < pays.size(); i++) {
					if ("deposit".equals(pays.get(i).getOrderType())) {
						tv_submit_date.setText(pays.get(i).getDateClosed());
						deposit += Double.parseDouble(pays.get(i).getFinalAmount());
					}
					
					if ("service".equals(pays.get(i).getOrderType())) {
						tv_confirm_date.setText(pays.get(i).getDateClosed());
						service += Double.parseDouble(pays.get(i).getFinalAmount());
					}
				}
				
				tv_yizhiyuyuejin.setText("（已支付预约金"+deposit+"元）");
				
				if (service>0) {
					ll_confirm_date.setVisibility(View.VISIBLE);
					tv_yizhifufuwufei.setText("（已支付服务费"+service+"元）");
				}
			}
		}

		RelativeLayout rl_go_order_details = (RelativeLayout) findViewById(R.id.rl_go_order_details);
		rl_go_order_details.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO
				
				if (orderDetailsEntity!=null) {
					//进入详情
					IntentHelper.getInstance(activity).gotoActivityWithURL(OrderDetailsMoreActivity.class,orderDetailsEntity.getBooking().getActionUrl());
				}
			}
		});
		
		
		if(orderDetailsEntity.getNotPays() == null){
			ll_zhifu.setVisibility(View.GONE);
		}
	}


	private void Scrollgridview() {
		tv_btn = (TextView) findViewById(R.id.tv_btn);//单独的上传照片按钮
		
		ll_xiaojie = (LinearLayout) findViewById(R.id.ll_xiaojie);
		tv_chongxin = (TextView) findViewById(R.id.tv_chongxin);////重新选择照片
		TextView tv_btn_confirm = (TextView) findViewById(R.id.tv_btn_confirm);//照片提交

		tv_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO
                Intent intent = new Intent(activity,UploadFileCommonActivity.class);
				
				intent.putExtra(Constants.PARMS.ID, orderDetailsEntity.getBooking().getPatientId());
				intent.putExtra("tokenUrl", UploadUtils.PATIENT_MR_QN_TOKEN );
				intent.putExtra("upMyServerUrl", UploadUtils.PATIENT_SAVE_APP_FILE);
				intent.putExtra("reportType", "dc");
				intent.putExtra("bookingId", orderDetailsEntity.getBooking().getId());
				startActivityForResult(intent, 199);
			}
		});

		tv_chongxin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//进入选照片界面
                Intent intent = new Intent(activity,UploadFileCommonActivity.class);
				intent.putExtra(Constants.PARMS.ID, orderDetailsEntity.getBooking().getPatientId());
				intent.putExtra("tokenUrl", UploadUtils.PATIENT_MR_QN_TOKEN );
				intent.putExtra("upMyServerUrl", UploadUtils.PATIENT_SAVE_APP_FILE);
				intent.putExtra("reportType", "dc");
				intent.putExtra("bookingId", orderDetailsEntity.getBooking().getId());
				startActivityForResult(intent, 199);
			}
		});

		tv_btn_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO
				//提交照片
				if (dataChoosed != null && dataChoosed.size() > 0) {

					upQiniu();

				} else {
					ToastUtil.showToast(activity, "请选择出院小结！");
				}
			}
		});
		noScrollgridview= (NoScrollGridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

		adapter = new GridAdapter(this);
		noScrollgridview.setAdapter(adapter);

		noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 == dataChoosed.size()) {
					// IntentHelper.getInstance(activity).gotoActivity(ImageGridActivity.class,
					// null, 1011);
					Intent intent = new Intent(activity,ImageGridActivity.class);
					startActivityForResult(intent, 1011);
					// intent.putExtra("isDymic", true);
					// startActivity(intent);
				} else {
					Intent intent = new Intent(activity,PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivityForResult(intent, 1012);
				}
			}
		});

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode==1) {
			//			IntentHelper.getInstance(activity).gotoActivity(OrderListActivity.class);
			//			finish();

		}else {
			dataChoosed = BimpTempHelper.getInstance().getImageItemsChoosend();
			if(dataChoosed != null && dataChoosed.size() > 0){
				tv_btn.setVisibility(View.GONE);
				ll_xiaojie.setVisibility(View.VISIBLE);
			}else {
				ll_xiaojie.setVisibility(View.GONE);
				tv_btn.setVisibility(View.VISIBLE);
			}
			noScrollgridview.setAdapter(adapter);
		}

		if (requestCode == 199 &&data!=null) {
			String resultDate = data.getStringExtra(UploadFileCommonActivity.SUCCESS_DATA);
			if (resultDate!=null&&resultDate.length()>0) {
				dataChoosed.clear();
				doNetGetImg();
			}
		}
	}
	@Override
	protected void onDestroy() {
		BimpTempHelper.getInstance().clearData();
		super.onDestroy();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case SUCCESS:
				progressDialog.dismiss();
				Toast.makeText(activity, "上传成功！", 2000).show();
				//gotoResultView();
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

	private void upQiniu() {
		progressDialog.show();
		final List<File> files = new ArrayList<File>();
		for (int i = 0; i < dataChoosed.size(); i++) {
			files.add(new File(dataChoosed.get(i).imagePath));
		}

		//TODO orderDetailsEntity.getBooking().getPatientId()
		UploadUtils uploadUtils =new UploadUtils( orderDetailsEntity.getBooking().getPatientId());//现在随便写了一个
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

		map.put("appFile[patientId]", orderDetailsEntity.getBooking().getPatientId());//现在随便写了一个患者id
		map.put("appFile[reportType]", "dc");//类型mr(病历 默认)dc(出院小结 暂无)


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

		//String url = UploadUtils.PATIENTAPP_FILE_URL + CommonUtils.getTokenParam(activity) +"&patientId="+orderDetailsEntity.getPatientInfo().getId()+"&reportType=mr";
		String url = UploadUtils.PATIENTAPP_FILE_URL + CommonUtils.getTokenParam(activity)+"&patientId="+orderDetailsEntity.getBooking().getPatientId()+"&reportType=dc";
		QJNetUICallback2 callback = new QJNetUICallback2<QjResult<HashMap<String,List<ImgQiNiuEntity>>>>(activity) {
			@Override
			public void onSuccess(QjResult<HashMap<String,List<ImgQiNiuEntity>>> result) {
				if (result != null
						&& result.getResults() != null) {
					List<ImgQiNiuEntity> list = result.getResults().get("files");
					
					if (list != null) {
						
						if (list.size()>0) {
							tv_btn.setVisibility(View.GONE);
							ll_xiaojie.setVisibility(View.VISIBLE);
						}else {
							ll_xiaojie.setVisibility(View.GONE);
							tv_btn.setVisibility(View.VISIBLE);
						}
						if (list.size()>=9) {
							
							tv_chongxin.setVisibility(View.GONE);
						}else {
							tv_chongxin.setVisibility(View.VISIBLE);
						}
						
						dataChoosed.clear();
						for(int i = 0; i < list.size();  i ++){
							ImageItem item = new ImageItem();
							item.imagePath = list.get(i).getThumbnailUrl();
							item.upImagePath = list.get(i).getAbsFileUrl();
							dataChoosed.add(item);
							noScrollgridview.setAdapter(adapter);
						}
					}
					
					else {
						ll_xiaojie.setVisibility(View.GONE);
						tv_btn.setVisibility(View.VISIBLE);
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
	

}
