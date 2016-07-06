package com.shoushuzhitongche.app.view.patient.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.dixiang.framework.common.BaseViewHolder;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.patient.PatientDetailActivty;
import com.shoushuzhitongche.app.view.patient.bean.PatientEntity;

public class PatientAdapter extends ArrayAdapter<PatientEntity>{
    private Context context;
    private LayoutInflater inflater;
    private boolean isShow;
    private PatientEntity patientEntity;

	public PatientAdapter(Context context) {
		this(context, false);
	}
	
	public PatientAdapter(Context context , boolean isShow) {
		super(context, android.R.layout.simple_list_item_1);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.isShow = isShow;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		PatientHolder holder = null;
		 if (convertView == null) {
	            convertView = inflater.inflate(R.layout.activity_patient_item, null);
	            holder = new PatientHolder(convertView);
	            convertView.setTag(holder);
	        } else {
	            holder = (PatientHolder) convertView.getTag();
	        }
	        holder.populateView(position, getItem(position));
		return convertView;
	}
	
	public PatientEntity getPatientEntity() {
		return patientEntity;
	}

	public void setPatientEntity(PatientEntity patientEntity) {
		this.patientEntity = patientEntity;
	}

	class PatientHolder extends BaseViewHolder<PatientEntity>{
		private TextView patient_name;
		private TextView patient_dsc;
		private TextView disease_name;
		private TextView btn_detials;
		private CheckBox check;
		public PatientHolder(View view) {
			super(view);
			check = (CheckBox)view.findViewById(R.id.check);
			patient_name = (TextView)view.findViewById(R.id.patient_name);
			patient_dsc = (TextView)view.findViewById(R.id.patient_dsc);
			disease_name = (TextView)view.findViewById(R.id.disease_name);
			btn_detials = (TextView)view.findViewById(R.id.btn_detials);
			if(isShow){
				check.setVisibility(View.VISIBLE);
				btn_detials.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void populateView(final int position, final PatientEntity item) {
			patient_name.setText(item.getName());
			patient_dsc.setText(context.getResources().getString(R.string.patient_item_dsc, new String []{item.getGender(),item.getAge()+"岁",item.getCityName()}));
			disease_name.setText(item.getDiseaseName());
			btn_detials.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Bundle bundle = new Bundle();
					bundle.putString(Constants.PARMS.ID,  item.getId());
					bundle.putString(Constants.PARMS.ACTIONURL,  item.getActionUrl());
					IntentHelper.getInstance(context).gotoActivity(PatientDetailActivty.class, bundle);
				}
			});
			
			check.setChecked(item.isCheck());
			
			check.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					PatientEntity entity = item;
					entity.setCheck(true);
					setPatientEntity(entity);
					replaceData(position ,entity);
				}
			});
		}
	}
	
	private void replaceData(int position ,PatientEntity item){
		List<PatientEntity> list = new ArrayList<PatientEntity>();
		int count = getCount();
		for(int i = 0; i < count ; i ++){
			if(i == position){
				list.add(item);	
			}else{
				PatientEntity entity = getItem(i);
				entity.setCheck(false);
				list.add(entity);
				entity = null;
			}
		}
		clear();
		addAll(list);
		list = null;
		notifyDataSetChanged();
	}
}
