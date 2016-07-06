package com.shoushuzhitongche.app.view.localdata;

import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;

import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.utils.Utils;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.view.localdata.adapter.CityListAdapter;
import com.shoushuzhitongche.app.view.localdata.bean.SubCityEntity;
import com.shoushuzhitongche.app.view.localdata.dao.LocalDataDao;

public class CitySelectView extends BaseActivity{

	private LocalDataDao dao;
	private CityListAdapter adapter;
	private CityListAdapter adapter_province;
	private String province_name;
	private String province_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pop_province_city);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		dao = LocalDataDao.newInstance(activity);
		initView();
	}
	
	private void initView(){
		
		ListView listView_province = (ListView)findViewById(R.id.lv_province);
		adapter_province = new CityListAdapter(activity);
		List<SubCityEntity> listProvince = dao.getProvinces();
		adapter_province.addAll(listProvince);
		listView_province.setAdapter(adapter_province);
		listView_province.setOnItemClickListener(onItemClickListener);
		
		ListView listView = (ListView)findViewById(R.id.lv_city);
		adapter = new CityListAdapter(activity);
		int provinceId =  listProvince.get(0).getId();
		province_id = provinceId+"";
		List<SubCityEntity> listCity = dao.getSubCitys(provinceId);
		adapter.addAll(listCity);
		listView.setOnItemClickListener(onItemClickListener2);
		listView.setAdapter(adapter);
	}
	@Override
	public void initHeaderView() {
		setTitleViewValue( 0, 0, R.color.white );
		setTitleViewValue( "请选择地区" );
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			province_id = adapter_province.getItem(arg2).getId() +"";
			province_name = adapter_province.getItem(arg2).getCity();
			List<SubCityEntity> list = dao.getSubCitys(adapter_province.getItem(arg2).getId());
			adapter.clear();
			adapter.addAll(list);
			adapter.notifyDataSetChanged();
		}
	};
	
	private OnItemClickListener onItemClickListener2 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String city_name = adapter.getItem(arg2).getCity();
			String city_id = adapter.getItem(arg2).getId()+"";
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString(Constants.PARMS.CITY_ID, city_id);
			if(Utils.isStringEmpty(province_name) || Utils.isStringEmpty(province_id)){
				province_id = adapter_province.getItem(0).getId()+"";
				province_name = adapter_province.getItem(arg2).getCity();
			}
			bundle.putString(Constants.PARMS.PROVINCE_ID, province_id);
			bundle.putString(Constants.PARMS.CITY_NAME, province_name +" (省) / "+city_name+" (市/区) ");
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
		}
	};

}
