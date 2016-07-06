package com.shoushuzhitongche.app.view.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dixiang.framework.common.BaseViewHolder;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.view.order.bean.OrderItemEntity;

public class OrderItemAdpter extends ArrayAdapter<OrderItemEntity>{
	private Context context;
	LayoutInflater inflater;
	public OrderItemAdpter(Context context ) {
		super(context, android.R.layout.simple_list_item_1);
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		OrderHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.lv_item_order, null);
			holder = new OrderHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (OrderHolder) convertView.getTag();
		}
		holder.populateView(position, getItem(position));
		return convertView;
	}


	class OrderHolder extends BaseViewHolder<OrderItemEntity>{

		private TextView tv_name ,tv_status ,tv_diseaseName ,tv_intent;

		public OrderHolder(View view) {
			super(view);
			tv_name = (TextView)view.findViewById(R.id.tv_name);
			tv_status = (TextView)view.findViewById(R.id.tv_status);
			tv_diseaseName = (TextView)view.findViewById(R.id.tv_diseaseName);
			tv_intent = (TextView)view.findViewById(R.id.tv_intent);

		}

		@Override
		public void populateView(int position, final OrderItemEntity item) {
			tv_name.setText(item.getName());
			tv_diseaseName.setText(item.getDiseaseName());
			tv_intent.setText(item.getTravelType());
			tv_status.setText(item.getStatusText());
		}

	}

}
