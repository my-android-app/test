/**
 * 
 */
package com.shoushuzhitongche.app.view.doctor.adpter;

import com.dixiang.framework.common.BaseViewHolder;
import com.dixiang.framework.network.Net;
import com.dixiang.framework.utils.Utils;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.view.doctor.bean.DoctorsEntity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class DoctorAdapter extends ArrayAdapter<DoctorsEntity>{

    private Context context;
    private LayoutInflater inflater;
	public DoctorAdapter(Context context){
		super(context, android.R.layout.simple_list_item_1);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		DoctorHolder holder = null;
		 if (convertView == null) {
	            convertView = inflater.inflate(R.layout.list_item_doctor, null);
	            holder = new DoctorHolder(convertView);
	            convertView.setTag(holder);
	        } else {
	            holder = (DoctorHolder) convertView.getTag();
	        }
	        holder.populateView(position, getItem(position));
		return convertView;
	}
	
	class DoctorHolder extends BaseViewHolder<DoctorsEntity>{

		private ImageView ico_av;
		private TextView doctor_name ,doctor_title ,doctor_medical ,hp_name ,hp_dpt ,goodat,is_qianyue;
		public DoctorHolder(View view) {
			super(view);
			ico_av = (ImageView) view.findViewById(R.id.ico_av);
			doctor_name = (TextView) view.findViewById(R.id.doctor_name);
			doctor_title = (TextView) view.findViewById(R.id.doctor_title);
			doctor_medical = (TextView) view.findViewById(R.id.doctor_medical);
			hp_name = (TextView) view.findViewById(R.id.hp_name);
			hp_dpt = (TextView) view.findViewById(R.id.hp_dpt);
			goodat = (TextView) view.findViewById(R.id.goodat);
			is_qianyue = (TextView) view.findViewById(R.id.is_qianyue);
		}

		@Override
		public void populateView(int position, DoctorsEntity item) {
			Net.displayImage(item.getImageUrl(), ico_av, R.drawable.default_doctor);
			doctor_name.setText(item.getName());
			doctor_title.setText(item.getaTitle());
			doctor_medical.setText(item.getmTitle());
			hp_name.setText(item.getHpName());
			hp_dpt.setText(item.getHpDeptName());
			String desc = item.getDesc();
			
			if(Utils.isStringEmpty(desc)){
				goodat.setVisibility(View.GONE);
			}else{
				goodat.setText(String.format(context.getString(R.string.good_at), item.getDesc()));	
			}
			
//			if( "1".equals( item.getIsContracted() ) )
//			{
//				is_qianyue.setVisibility( View.VISIBLE );
//			}else {
//				is_qianyue.setVisibility( View.GONE );
//			}
			
		}
	}
}
