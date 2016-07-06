package com.shoushuzhitongche.app.view.order.adapter;

import com.dixiang.framework.common.BaseViewHolder;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.PayViewActivity;
import com.shoushuzhitongche.app.view.order.bean.PayListEntity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PayListAdapter extends ArrayAdapter<PayListEntity>{

	private Context context;
	LayoutInflater inflater;
	public PayListAdapter(Context context ) {
		super(context, android.R.layout.simple_list_item_1);
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.lv_item_pay, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.populateView(position, getItem(position));
		return convertView;
	}

	public final class ViewHolder extends BaseViewHolder<PayListEntity>{
		public TextView	tv_jine , go_pay;
		public ViewHolder(View view) {
			super(view);
			tv_jine = (TextView) view.findViewById(R.id.tv_jine);
			go_pay = (TextView) view.findViewById(R.id.go_pay);
		}

		@Override
		public void populateView(int position, final PayListEntity item) {
			if("1".equals(item.getIsPaid())){
				tv_jine.setText(item.getFinalAmount());
				go_pay.setText(item.getIsPaidText());
				go_pay.setBackground(context.getResources().getDrawable(R.drawable.radius_green_bg));
				go_pay.setEnabled(false);
			}else{
				go_pay.setEnabled(true);
				tv_jine.setText(item.getFinalAmount());
				go_pay.setText("去支付");
				go_pay.setBackground(context.getResources().getDrawable(R.drawable.radius_fuzhu_bg));
				go_pay.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						String actionUrl = CommonUtils.getPayUrl(context, item.getRefNo());
						IntentHelper.getInstance(context).gotoActivityWithURLAndTitle(PayViewActivity.class, "订单支付", actionUrl);
					}
				});
			}

		}
	}

}
