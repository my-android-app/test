package com.shoushuzhitongche.app.view.localdata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dixiang.framework.common.BaseViewHolder;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.view.localdata.bean.SubCityEntity;

public class CityListAdapter extends ArrayAdapter<SubCityEntity>{

	private Context context;
    LayoutInflater inflater;
	public CityListAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_1);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		CityHolder holder = null;
		 if (convertView == null) {
	            convertView = inflater.inflate(R.layout.lv_item_city, null);
	            holder = new CityHolder(convertView);
	            convertView.setTag(holder);
	        } else {
	            holder = (CityHolder) convertView.getTag();
	        }
	        holder.populateView(position, getItem(position));
		return convertView;
	}
}
	
	
	class CityHolder extends BaseViewHolder<SubCityEntity>{
		private TextView tv_city;
		public CityHolder(View view) {
			super(view);
			tv_city = (TextView) view.findViewById(R.id.tv_city);
		}
		
		@Override
		public void populateView(int position, SubCityEntity item) {
			tv_city.setText(item.getCity());
		}
	}
