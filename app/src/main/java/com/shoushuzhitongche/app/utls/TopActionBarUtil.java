package com.shoushuzhitongche.app.utls;


import com.shoushuzhitongche.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class TopActionBarUtil {
	public View getTopActionBar( Context  context , String rbtext1, String rbtext2,final OnTopActionBarListener onTopActionBarListener) {
		View view = LayoutInflater.from(context).inflate(R.layout.top_redio_actionbar, null);
		final RadioButton	rb1 = (RadioButton)view.findViewById(R.id.rb1);
		rb1.setText(rbtext1);
		final RadioButton	rb2 = (RadioButton)view.findViewById(R.id.rb2);
		rb2.setText(rbtext2);
		
		RadioGroup rg = ((RadioGroup)view.findViewById(R.id.grdiogroup));
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int id = group.getCheckedRadioButtonId();
				switch (id) {
					case R.id.rb1:
						onTopActionBarListener.onLeftClick(rb1);
						break;
					case R.id.rb2:
						onTopActionBarListener.onRightClick(rb2);
						break;
				}
				
			}
		});
		return view;
	}
	
	
	public interface OnTopActionBarListener {
		void onLeftClick(RadioButton radioButton);
		void onRightClick(RadioButton radioButton);
	}
}
