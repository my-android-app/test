package com.shoushuzhitongche.app.view.doctor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ListView;
import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.utils.Utils;
import com.dixiang.framework.widget.LoadingDialog;
import com.dixiang.framework.widget.PullToRefreshView;
import com.dixiang.framework.widget.PullToRefreshView.OnFooterRefreshListener;
import com.dixiang.framework.widget.PullToRefreshView.OnHeaderRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.MYApplication;
import com.shoushuzhitongche.app.view.doctor.adpter.CityListNetAdapter;
import com.shoushuzhitongche.app.view.doctor.adpter.DoctorAdapter;
import com.shoushuzhitongche.app.view.doctor.adpter.SubDeptListAdapter;
import com.shoushuzhitongche.app.view.doctor.bean.CityEntity;
import com.shoushuzhitongche.app.view.doctor.bean.CityMapEntity;
import com.shoushuzhitongche.app.view.doctor.bean.DeptSubCatEntity;
import com.shoushuzhitongche.app.view.doctor.bean.DisNavsEntity;
import com.shoushuzhitongche.app.view.doctor.bean.DoctorsEntity;
import com.shoushuzhitongche.app.view.localdata.dao.LocalDataDao;

public class DoctorListActivity extends BaseActivity implements OnClickListener {
	private List<DisNavsEntity> disNavs;//大科室集合
	private LayoutInflater mInflater;
	private ListView listView;
	private PullToRefreshView lv_refresh;
	private List<DoctorsEntity> listDoctor;
	private RelativeLayout rl_dept, rl_area;
	private TextView tv_dept, tv_area;
	private DoctorAdapter doctorAdapter;
	private int page = 1;
	
	private List<CityEntity> cityList = new ArrayList<CityEntity>();
	private String cityId = "";
	private String cityName = "地区";
	private String deptName = "科室";
	private String disease_sub_category = "";
	@Override
	public void initHeaderView() {
		setTopViewBg(R.color.actionbar_bg_color);
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setTitleViewValue(0, 0, R.color.white);
		setTitleViewValue("找名医");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_doctor);
		initView();
		initData();
	}

	private void initView() {

		mInflater = LayoutInflater.from(this);
		listView = (ListView) findViewById(R.id.doctor_listview);
		lv_refresh = (PullToRefreshView) findViewById(R.id.lv_refresh);

		rl_dept = (RelativeLayout) findViewById(R.id.rl_dept);
		rl_area = (RelativeLayout) findViewById(R.id.rl_area);
		rl_dept.setOnClickListener(this);
		rl_area.setOnClickListener(this);

		tv_dept = (TextView) findViewById(R.id.tv_dept);
		tv_area = (TextView) findViewById(R.id.tv_area);

		lv_refresh.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

			@Override
			public void onHeaderRefresh(PullToRefreshView refreshView) {
				// 刷新不加载更多
				page = 1;
				getDoctorDate(true);
			}
		});

		lv_refresh.setOnFooterRefreshListener(new OnFooterRefreshListener() {

			@Override
			public void onFooterRefresh(PullToRefreshView refreshView) {
				// 加载更多
				page++;
				getDoctorDate(false);
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("doctorListEntity",
						(Serializable) listDoctor.get(position));
				bundle.putBoolean(Constants.PARMS.IS_COME_FROM_BOOKING, getIntent().getBooleanExtra(Constants.PARMS.IS_COME_FROM_BOOKING, false));
				IntentHelper.getInstance(activity).gotoActivity(
						DoctorDetailsActivity.class, bundle);
			}

		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_area:
			if (cityList != null && cityList.size() > 0) {
				initCityPopupWindow2(v, activity);
			} else {
				Toast.makeText(activity, "数据加载未完成！", 0).show();
			}
			break;
		case R.id.rl_dept:
			if (disNavs != null && disNavs.size() > 0) {
				initDeptPopupWindow(v, activity);
			} else {
				Toast.makeText(activity, "数据加载未完成！", 0).show();
			}
			break;
		default:
			break;
		}

	}

	private void initData() {
		// url = getIntent().getStringExtra(Constants.PARM_ACTION_URL);

		// disNavs = (List<DisNavsEntity>)
		// getIntent().getSerializableExtra("disNavs");

		if (doctorAdapter == null || doctorAdapter.isEmpty()) {
			listDoctor = new ArrayList<DoctorsEntity>();
			doctorAdapter = new DoctorAdapter(this);
			getDoctorDate(true);
		}

		if (cityList == null || cityList.size() == 0)
			getCityDate();
		doctorAdapter.addAll(listDoctor);
		listView.setAdapter(doctorAdapter);

		getDiseaseCategory();
	}

	public void getDoctorDate(final boolean isFresh) {

		LoadingDialog.getInstance(activity).show();
		String deptIdPram = "";
		if (Utils.isStringEmpty(disease_sub_category)) {
			deptIdPram = "";
		} else {
			deptIdPram = "&disease_sub_category=" + disease_sub_category;
		}

		String cityIdPram = "";
		if (Utils.isStringEmpty(cityId)) {
			cityIdPram = "";
		} else {
			cityIdPram = "&state=" + cityId;
		}

		String actionUrl = CommonUtils.getContractdoctorlist(activity)+CommonUtils.getTokenParam(activity)
				+ "&page=" + page + deptIdPram + cityIdPram;
		getHttpResult(actionUrl, Constants.HTTP_INDEX.First, new TypeToken<QjResult<List<DoctorsEntity>>>() {});
	}
	
	private void getCityDate() {
		String url = CommonUtils.getCitylist(activity); // +
		getHttpResult(url, Constants.HTTP_INDEX.Second, new TypeToken<QjResult<CityMapEntity>>() {});
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		if(flg.equals(Constants.HTTP_INDEX.First)){
			List<DoctorsEntity> list = (List<DoctorsEntity>) result.getResults();
			if (list == null || (list != null && list.size() <= 0)) {
				Toast.makeText(activity, "没有更多数据！", 0).show();
				if (page == 1) {
					if (listDoctor != null)
						listDoctor.clear();
					doctorAdapter.clear();
					doctorAdapter.addAll(listDoctor);
					doctorAdapter.notifyDataSetChanged();
				}
			} else {
				if (page == 1) {
					listDoctor.clear();
				}
				listDoctor.addAll(list);
				doctorAdapter.clear();
				doctorAdapter.addAll(listDoctor);
				doctorAdapter.notifyDataSetChanged();
			}
		}else if(flg.equals(Constants.HTTP_INDEX.Second)){
			cityList.clear();
			
			CityMapEntity cityMap =  (CityMapEntity) result.getResults();
			if(cityMap != null){
				Map stateList = cityMap.getStateList();
				if(stateList != null && !stateList.isEmpty()){
					Iterator<Entry<String, String>> it = stateList.entrySet().iterator(); 
					while (it.hasNext()) { 
						Entry<String, String> entry = it.next();
						int key =  Integer.parseInt(entry.getKey());
						String value = entry.getValue(); 
						
						CityEntity cityEntity = new CityEntity();
						cityEntity.setId(key);
						cityEntity.setCity(value);;
						
						cityList.add(cityEntity);
					} 
				}
			}
			
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		if(flg.equals(Constants.HTTP_INDEX.First)){
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					lv_refresh.onHeaderRefreshComplete();
					lv_refresh.onFooterRefreshComplete();
				}
			});
			
			LoadingDialog.getInstance(activity).dismiss();
		}
	}


	
	private void initCityPopupWindow2(View v, Context context) {
		// 设置pop窗口的视图，用布局转换器将xml转换成view
		View contentView = mInflater.inflate(R.layout.pop_city, null);
		// 获得pop窗口，设置大小(宽高设置为填充)，ViewGroup.LayoutParams.MATCH_PARENT填充父类
		final PopupWindow pop = new PopupWindow(
						contentView,
						LayoutParams.MATCH_PARENT,
						(int) (MYApplication.heightPixels - (MYApplication.startheight + ((43+50) *
								MYApplication.density))));
		
		ListView lv_city = (ListView) contentView.findViewById(R.id.lv_city_only);

		
		CityListNetAdapter cityAdapter = new CityListNetAdapter(context,cityList);
		lv_city.setAdapter(cityAdapter);
		// 获得焦点
		pop.setFocusable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());// 点击完按钮后另其消失（方法：设置一个空的bitmap）
		// 设置pop窗口显示的位置
		// 将按钮和pop窗口关联上，v表示一个显示的基点(按钮的基点为左下点)
		pop.showAtLocation(v,Gravity.BOTTOM,0,0);
		pop.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {

			}
		});

		lv_city.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				page = 1;
				cityName = cityList.get(position).getCity();

				tv_area.setText(cityName);

				cityId = "" + cityList.get(position).getId();
				getDoctorDate(true);
				if (pop.isShowing()) {
					pop.dismiss();
				}
			}

		});

	}


	@Override
	protected void onStop() {
		super.onStop();
	}

	private void getDiseaseCategory() {
		disNavs = LocalDataDao.newInstance(activity).getNavsDept();
	}


	private void initDeptPopupWindow(View v, Context context) {
		// 设置pop窗口的视图，用布局转换器将xml转换成view
		View contentView = mInflater.inflate(R.layout.pop_radio_province_city, null);
		// 获得pop窗口，设置大小(宽高设置为填充)，ViewGroup.LayoutParams.MATCH_PARENT填充父类
		PopupWindow pop = new
				PopupWindow(contentView,LayoutParams.MATCH_PARENT,(int)
						(MYApplication.heightPixels - (MYApplication.startheight + ((43+50) *
								MYApplication.density))));
		RadioGroup rg_province = (RadioGroup)contentView.findViewById(R.id.rg_province);
		ListView lv_city = (ListView) contentView.findViewById(R.id.lv_city);

		List<DeptSubCatEntity> dpetList = new ArrayList<DeptSubCatEntity>();
		SubDeptListAdapter septListAdapter = new SubDeptListAdapter(context,dpetList);
		lv_city.setAdapter(septListAdapter);
		setDeptDate(pop, rg_province, septListAdapter, lv_city, dpetList);

		// 获得焦点
		pop.setFocusable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());//
		//点击完按钮后另其消失（方法：设置一个空的bitmap）
		// 设置pop窗口显示的位置
		// 将按钮和pop窗口关联上，v表示一个显示的基点(按钮的基点为左下点)
		pop.showAtLocation(v,Gravity.BOTTOM,0,0);
		pop.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {

			}
		});

	}

	private void setDeptDate(final PopupWindow pop,
			final RadioGroup rg_province,
			final SubDeptListAdapter cityListAdapter, final ListView lv_city,
			final List<DeptSubCatEntity> dpetList) {

		if (disNavs != null && disNavs.size() > 0) {
			for (int i = 0; i < disNavs.size(); i++) {
				RadioButton rb = (RadioButton) mInflater.inflate(
						R.layout.province_radio_btn, null);
				rb.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						(int) (45 * MYApplication.density)));
				rb.setId(i);
				rb.setText(disNavs.get(i).getName());
				if (i == 0) {
					rb.setChecked(true);
					dpetList.clear();
					dpetList.addAll(disNavs.get(i).getSubCat());
					cityListAdapter.notifyDataSetChanged();
				}
				rg_province.addView(rb);
			}

			rg_province
			.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group,
						int checkedId) {
					dpetList.clear();
					dpetList.addAll(disNavs.get(checkedId).getSubCat());
					cityListAdapter.notifyDataSetChanged();
				}
			});

			lv_city.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					page = 1;
					deptName = dpetList.get(position).getName();
					tv_dept.setText(deptName);
					disease_sub_category = "" + dpetList.get(position).getId();
					getDoctorDate(true);
					if (pop.isShowing()) {
						pop.dismiss();
					}
				}

			});

		} else {
			Toast.makeText(activity, "数据加载失败！", 0).show();
		}
	}



}
