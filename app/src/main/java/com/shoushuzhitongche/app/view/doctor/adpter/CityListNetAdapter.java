package com.shoushuzhitongche.app.view.doctor.adpter;

import java.util.List;

import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.view.doctor.bean.CityEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityListNetAdapter extends BaseAdapter{

	private Context mContext;
	private List<CityEntity> cityList;
	private LayoutInflater inflater;
	public CityListNetAdapter(Context context,List<CityEntity> cityList) {
		this.mContext = context;
		this.cityList = cityList;
		this.inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return cityList== null?0:cityList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return cityList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.lv_item_city, parent, false);
			holder = new ViewHolder();
			holder.tv_city = (TextView) convertView.findViewById(R.id.tv_city);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.tv_city.setText( cityList.get(position).getCity() );
		return convertView;
	}

	public final class ViewHolder{

		public TextView	tv_city = null;
	}

}
