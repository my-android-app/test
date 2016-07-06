package com.shoushuzhitongche.app.view.order;

import java.util.List;
import java.util.Map;

import com.dixiang.framework.BaseActivity;
import com.dixiang.framework.common.QJNetUICallback;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.network.Net;
import com.dixiang.framework.widget.LoadingDialog;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.view.order.adapter.PayListAdapter;
import com.shoushuzhitongche.app.view.order.bean.PayListEntity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class PayListActivity extends BaseActivity{
	private PayListAdapter payListAdapter;
	private ListView lv_pay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_list);
		payListAdapter = new PayListAdapter(activity);
		lv_pay = (ListView) findViewById(R.id.lv_pay);
		lv_pay.setAdapter(payListAdapter);
	}
	
	@Override
	public void initHeaderView() {
		
		setTitleViewValue("支付详情");
		setTitleViewValue(0,0,R.color.white);
		setTopViewBg(R.color.actionbar_bg_color);
		initBackView(R.drawable.left_back_white_icon, 0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getPayDetail();
	}
	
    public void getPayDetail() {
    	LoadingDialog.getInstance(activity).show();
    	String id = getIntent().getStringExtra(Constants.PARMS.ID);//orderview
    	String orderType = getIntent().getStringExtra("orderType");
    	String url = CommonUtils.getOrderlist(activity) +"?bookingid="+ id +CommonUtils.getGetParm(activity, "ordertype="+orderType);
		
    	getHttpResult(url, Constants.HTTP_INDEX.First, new TypeToken<QjResult<Map<String,List<PayListEntity>>>>() {});
    }
    
    @Override
    public void onSuccess(QjResult result, String flg) {
    	super.onSuccess(result, flg);
    	Map<String,List<PayListEntity>> map = (Map<String, List<PayListEntity>>) result.getResults();
    	if(map != null && !map.isEmpty()){
    		setOrderDetail(map.get("orders"));
    	}else {
			Toast.makeText(activity, "获取数据失败！", Toast.LENGTH_SHORT).show();
		}
    }
    
    @Override
    public void onCompleted(Exception e, String flg) {
    	super.onCompleted(e, flg);
    	LoadingDialog.getInstance(activity).dismiss();
    }
	
    private void setOrderDetail(List<PayListEntity> list){
    		if(list != null && list.size() > 0){
    			payListAdapter.clear();
    			payListAdapter.addAll(list);
    			payListAdapter.notifyDataSetChanged();
    		}
    		
    		double hasPay = 0;
    		double noPay = 0;
    		for (PayListEntity payListEntity2 : list) {
				if("1".equals(payListEntity2.getIsPaid())){
					hasPay += (Double.parseDouble(payListEntity2.getFinalAmount()));
				}else{
					noPay += (Double.parseDouble(payListEntity2.getFinalAmount()));
				}
			}
    		((TextView)findViewById(R.id.tv_total)).setText((hasPay+noPay)+"");
    		((TextView)findViewById(R.id.tv_yizhifu)).setText(hasPay+"");
    		((TextView)findViewById(R.id.tv_shengyu)).setText(noPay+"");
    }
	
}
