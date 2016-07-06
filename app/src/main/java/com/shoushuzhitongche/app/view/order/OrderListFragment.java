package com.shoushuzhitongche.app.view.order;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.dixiang.framework.BaseFragment;
import com.dixiang.framework.common.QjResult;
import com.dixiang.framework.widget.LoadingDialog;
import com.dixiang.framework.widget.PullToRefreshView;
import com.dixiang.framework.widget.PullToRefreshView.OnFooterRefreshListener;
import com.dixiang.framework.widget.PullToRefreshView.OnHeaderRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.shoushuzhitongche.app.R;
import com.shoushuzhitongche.app.utls.CommonUtils;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.utls.IntentHelper;
import com.shoushuzhitongche.app.view.order.adapter.OrderItemAdpter;
import com.shoushuzhitongche.app.view.order.bean.OrderItemEntity;

public class OrderListFragment extends BaseFragment{
	private PullToRefreshView lv_refresh;
	private ListView listview;
	private LinearLayout ll_data_null;
	private RadioGroup radiogroup;
	private int page = 1;
	private int status = 0;//全部
	private List<OrderItemEntity> listOrder;
	private OrderItemAdpter orderItemAdpter;
	private Handler handler = new Handler();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_order, container, false); 
		initView( view );
		return view;
	}
	
	@Override
	public void initHeaderView() {
		clearTitle();
		setTitleViewValue("我的订单");
		setTitleViewValue( 0, 0, R.color.white );
	}

	private void initView(View view ) {
		lv_refresh = (PullToRefreshView) view.findViewById(R.id.lv_refresh);
		listview = (ListView) view.findViewById(R.id.listview);
		
		ll_data_null = (LinearLayout) view.findViewById(R.id.ll_data_null);
		listOrder = new ArrayList<OrderItemEntity>();
		orderItemAdpter = new OrderItemAdpter(activity);
		orderItemAdpter.addAll(listOrder);
		listview.setAdapter(orderItemAdpter);
		doNet(true);
		
		lv_refresh.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView refreshView) {
				// 刷新不加载更多
				page = 1;
				doNet(true);
			}
		});

		lv_refresh.setOnFooterRefreshListener(new OnFooterRefreshListener() {
			@Override
			public void onFooterRefresh(PullToRefreshView refreshView) {
				// 加载更多
				page++;
				doNet(false);
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putString(Constants.PARMS.STATE, orderItemAdpter.getItem(position).getStatus());
				bundle.putString(Constants.PARMS.ACTIONURL, orderItemAdpter.getItem(position).getActionUrl());
				IntentHelper.getInstance(activity).gotoActivity(OrderDetailsActivity.class, bundle);
			}
		});
		
		radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				page = 1;
				
				orderItemAdpter.clear();
				listOrder.clear();
				orderItemAdpter.notifyDataSetChanged();
				
				if (checkedId ==R.id.rb1) {
					
					status = 0;
					doNet(true);
					
				}else if (checkedId ==R.id.rb2) {
					status = 1;
					doNet(true);
					
				}else if (checkedId ==R.id.rb3) {
					status = 2;
					doNet(true);
					
				}else if (checkedId ==R.id.rb4) {
					status = 5;
					doNet(true);
					
				}else if (checkedId ==R.id.rb5) {
					status = 6;
					doNet(true);
//					Bundle bundle = new Bundle();
					//bundle.putSerializable( "id", item.getId());
					//bundle.putSerializable("orderItemEntity", item);
//					IntentHelper.getInstance(activity).gotoActivity(OrderDetailsActivity.class, bundle);
					//TODO
					
				}
				
			}
		});
		
	}
	
	public void doNet(final boolean isFresh) {
		
		String url = CommonUtils.getBookinglist(activity, "")+CommonUtils.getGetParm(activity,"")+ "&status=" + status + "&page=" + page;
		LoadingDialog.getInstance(activity).show();
		
		getHttpResult(url, Constants.HTTP_INDEX.First, new TypeToken<QjResult<List<OrderItemEntity>>>() {});
	}
	
	@Override
	public void onSuccess(QjResult result, String flg) {
		super.onSuccess(result, flg);
		List<OrderItemEntity> list =(List<OrderItemEntity>) result.getResults();
		if(list != null && !list.isEmpty()){
			listOrder.clear();
			orderItemAdpter.clear();
			
			if (list == null|| (list != null && list.size() <= 0)) {
				
				ll_data_null.setVisibility(View.VISIBLE);
				ll_data_null.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						doNet(true);
					}
				});
				
				Toast.makeText(activity, "没有更多数据！", 0).show();
				if (page == 1) {
					if(listOrder != null){ 
					listOrder.clear();
					orderItemAdpter.clear();
					}
					orderItemAdpter.notifyDataSetChanged();
				}
			} else {
				ll_data_null.setVisibility(View.GONE);
				if (page == 1) {
					listOrder.clear();
					orderItemAdpter.clear();
				}
				listOrder.addAll(list);
				orderItemAdpter.addAll(listOrder);
				orderItemAdpter.notifyDataSetChanged();
			}
		}
	}
	
	@Override
	public void onCompleted(Exception e, String flg) {
		super.onCompleted(e, flg);
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				lv_refresh.onHeaderRefreshComplete();
				lv_refresh.onFooterRefreshComplete();						
			}
		});
		LoadingDialog.getInstance(activity).dismiss();
	}
}
