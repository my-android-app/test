package com.shoushuzhitongche.app.view.receive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dixiang.framework.common.BaseViewHolder;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.view.receive.bean.AppointEntity;

public class ReceiveAppointAdapter extends ArrayAdapter<AppointEntity>{

	private Context context;
    LayoutInflater inflater;
	public ReceiveAppointAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_1);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		SendAppointHolder holder = null;
		 if (convertView == null) {
	            convertView = inflater.inflate(R.layout.lv_item_receive_invite, null);
	            holder = new SendAppointHolder(convertView);
	            convertView.setTag(holder);
	        } else {
	            holder = (SendAppointHolder) convertView.getTag();
	        }
	        holder.populateView(position, getItem(position));
		return convertView;
	}

	
	class SendAppointHolder extends BaseViewHolder<AppointEntity>{
		private TextView patient_name,tv_disease_name,receive_date,tv_intent_date,tv_state;
		private View view_state;
		private ImageView ico_state;
		
		public SendAppointHolder(View view) {
			super(view);
			patient_name = (TextView) view.findViewById(R.id.patient_name);
			tv_disease_name = (TextView) view.findViewById(R.id.tv_disease_name);
			tv_intent_date = (TextView) view.findViewById(R.id.tv_intent_date);
			receive_date = (TextView) view.findViewById(R.id.receive_date);
			view_state = view.findViewById(R.id.view_state);
			ico_state = (ImageView) view.findViewById(R.id.ico_state);
			tv_state = (TextView) view.findViewById(R.id.tv_state);
		}
		
		@Override
		public void populateView(int position, AppointEntity item) {
			patient_name.setText(item.getName());
			tv_disease_name.setText(item.getDiseaseName());
		    try{
		    	tv_intent_date.setText(item.getTravelType());
		    }catch(Exception ex){
		    	ex.printStackTrace();
		    }
		    receive_date.setText(item.getDateUpdated());
		    if(item.getDoctorAccept() == null){
		    	receive_date.setVisibility(View.VISIBLE);
		    	view_state.setVisibility(View.GONE);
		    }else{
		    	receive_date.setVisibility(View.GONE);
		    	view_state.setVisibility(View.VISIBLE);
		    	if("1".equals(item.getDoctorAccept())){
		    		ico_state.setImageResource(R.drawable.ico_agree);
		    		tv_state.setTextColor(context.getResources().getColor(R.color.agree));
		    		tv_state.setText("已同意");
		    	}else{
		    		ico_state.setImageResource(R.drawable.ico_disagree);
		    		tv_state.setTextColor(context.getResources().getColor(R.color.disagree));
		    		tv_state.setText("已拒绝");
		    	}
		    }
		}
	}
}
