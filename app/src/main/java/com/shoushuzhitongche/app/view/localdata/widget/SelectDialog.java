package com.shoushuzhitongche.app.view.localdata.widget;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dixiang.framework.common.BaseViewHolder;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.view.localdata.bean.CommonEntity;

public class SelectDialog  extends AlertDialog {
	public static SelectDialog dlg = null;
    private static Activity contexts;
    public static IOnSelectedListener selectedListener;
    private static ListAdapter adapter;
    protected SelectDialog(Activity context) {
        super(context);
        dlg = this;
   }
    
    private static SelectDialog newInitialize(Activity context){
    	contexts = context;
//    	if(dlg == null){
    	dlg = new SelectDialog(context);
//    	}
    	return dlg;
    }
    
    private static OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			CommonEntity entity = adapter.getItem(arg2);
			selectedListener.onSelectedListener(entity);
			if(dlg != null && dlg.isShowing())
				dlg.dismiss();
		}
	};
	
	
	public static void showAlertDialog(Activity context,List<CommonEntity> list, IOnSelectedListener listener){
		selectedListener = listener;
		dlg = newInitialize(context);
		if(context.isFinishing()) return;
		dlg.show();
		
		LayoutInflater l = LayoutInflater.from(contexts);
        View view = l.inflate(R.layout.dlg_selected,null);
        ListView listView = (ListView)view.findViewById(R.id.listview);
        listView.setOnItemClickListener(onItemClickListener);
        adapter = new ListAdapter(contexts);
        adapter.addAll(list);
        listView.setAdapter(adapter);
        Window window = dlg.getWindow();
        window.setContentView(view);
        
	}
    
    static class ListAdapter extends ArrayAdapter<CommonEntity>{
        LayoutInflater inflater;

    	public ListAdapter(Context context) {
    		super(context, android.R.layout.simple_list_item_1);
            this.inflater = LayoutInflater.from(context);
    	}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		
    		ListHolder holder = null;
    		 if (convertView == null) {
    	            convertView = inflater.inflate(R.layout.dlg_selected_item, null);
    	            holder = new ListHolder(convertView);
    	            convertView.setTag(holder);
    	        } else {
    	            holder = (ListHolder) convertView.getTag();
    	        }
    	        holder.populateView(position, getItem(position));
    		return convertView;
    	}
    }
    
   static class ListHolder extends BaseViewHolder<CommonEntity>{

    	private TextView tv_value;
		public ListHolder(View view) {
			super(view);
			tv_value = (TextView)view.findViewById(R.id.tv_value); 
		}

		@Override
		public void populateView(int position, CommonEntity item) {
			tv_value.setText(item.get_value());
		}
    	
    }
    
    public interface IOnSelectedListener{
    	public abstract void onSelectedListener(CommonEntity entity);
    }
}
