package com.shoushuzhitongche.app.view.order;

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
import com.dixiang.framework.common.QJNetUICallback;
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
import com.shoushuzhitongche.app.view.booking.UploadFileCommonActivity;
import com.shoushuzhitongche.app.view.mine.bean.ImgQiNiuEntity;
import com.shoushuzhitongche.app.view.order.bean.OrderMoreDetails;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderDetailsMoreActivity extends BaseActivity{

	private GridAdapter adapter;
	private NoScrollGridView noScrollgridview;
	private List<ImageItem> dataChoosed = new ArrayList<ImageItem>();//选中的item
	private BitmapCache cache;
	private boolean isAdd = false;
	private OrderMoreDetails orderMoreDetails;
	
	private Button btn_upcase;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail_more);
		initView();
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

	private void initView(){
		btn_upcase = (Button)findViewById(R.id.btn_upcase);
		btn_upcase.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity,UploadFileCommonActivity.class);
				
				intent.putExtra(Constants.PARMS.ID, orderMoreDetails.getPatientId());
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
	
	public void getOrderDetail() {
		LoadingDialog.getInstance(activity).show();
		String url = getIntent().getStringExtra(Constants.PARM_ACTION_URL);//orderview
		url = url + CommonUtils.getGetParm(activity, "api=3");

		getHttpResult(url, Constants.HTTP_INDEX.First, new TypeToken<QjResult<Map<String,OrderMoreDetails>>>() {});
	}

	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		Map<String,OrderMoreDetails> map = (Map<String, OrderMoreDetails>) result.getResults(); 
		if(map != null && !map.isEmpty()){
			orderMoreDetails = map.get("booking");
			setOrderDetail();
			doNetGetImg();
		}else{
			Toast.makeText(activity, "获取数据失败！", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		LoadingDialog.getInstance(activity).dismiss();
	}
	
	private void setOrderDetail(){
		((TextView)findViewById(R.id.tv_travelType)).setText(orderMoreDetails.getTravelType());
		
		TextView tv_expertname = ((TextView)findViewById(R.id.tv_expertname));
		TextView tv_hosptal =  ((TextView)findViewById(R.id.tv_hosptal));
		tv_expertname.setVisibility(View.GONE);
		tv_hosptal.setVisibility(View.GONE);
		
		String aa = orderMoreDetails.getExpectedDoctor();
		if (aa!=null) {
			String[]bb = aa.split("&nbsp;");
			if (bb.length>0) {
				tv_expertname.setVisibility(View.VISIBLE);
				tv_expertname.setText(bb[0]);
			}
			
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
		
		((TextView)findViewById(R.id.tv_suggestion)).setText(orderMoreDetails.getDetail());
		((TextView)findViewById(R.id.tv_p_name)).setText(orderMoreDetails.getPatientName());
		((TextView)findViewById(R.id.tv_gender)).setText(orderMoreDetails.getGender());
		((TextView)findViewById(R.id.tv_age)).setText(orderMoreDetails.getAge()+"岁");
		((TextView)findViewById(R.id.tv_cityName)).setText(orderMoreDetails.getCityName());
		((TextView)findViewById(R.id.tv_describe)).setText(orderMoreDetails.getDiseaseDetail());
		((TextView)findViewById(R.id.tv_zhenduan)).setText(orderMoreDetails.getDiseaseName());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 199 &&data!=null) {
			String resultDate = data.getStringExtra(UploadFileCommonActivity.SUCCESS_DATA);
			if (resultDate!=null&&resultDate.length()>0) {
				dataChoosed.clear();
				doNetGetImg();
			}
		}
	}
	
	
	private void doNetGetImg() {

		//String url = UploadUtils.PATIENTAPP_FILE_URL + CommonUtils.getTokenParam(activity) +"&patientId="+orderDetailsEntity.getPatientInfo().getId()+"&reportType=mr";
		String url = UploadUtils.PATIENTAPP_FILE_URL + CommonUtils.getTokenParam(activity) +"&patientId="+orderMoreDetails.getPatientId()+"&reportType=mr";
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
		LoadingDialog.getInstance(activity).dismiss();
		BimpTempHelper.getInstance().clearData();
	}
	
}
