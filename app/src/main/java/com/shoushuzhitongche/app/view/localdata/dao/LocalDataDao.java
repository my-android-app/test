package com.shoushuzhitongche.app.view.localdata.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import com.shoushuzhitongche.app.common.QJSQLiteOpenHelper;
import com.shoushuzhitongche.app.utls.Constants;
import com.shoushuzhitongche.app.view.doctor.bean.DeptSubCatEntity;
import com.shoushuzhitongche.app.view.doctor.bean.DisNavsEntity;
import com.shoushuzhitongche.app.view.localdata.bean.CommonEntity;
import com.shoushuzhitongche.app.view.localdata.bean.LocalDataEntity;
import com.shoushuzhitongche.app.view.localdata.bean.ProvinceEntity;
import com.shoushuzhitongche.app.view.localdata.bean.SubCityEntity;
import com.shoushuzhitongche.app.view.localdata.server.DataCheckService;

public class LocalDataDao extends QJSQLiteOpenHelper{

	public LocalDataDao(Context context, String dbName) {
		super(context, dbName);
	}
	
    public static LocalDataDao newInstance(Context context) {
        return new LocalDataDao(context, Constants.DB_NAME);
    }
    
    public void saveLocalData(final LocalDataEntity localDataEntity ,final Handler handler){
    	new Thread(new Runnable() {
    		@Override
			public void run() {
			    List<CommonEntity> list = null;
    			try{
    				list = new ArrayList<CommonEntity>();
    				Map<String, String> academic = localDataEntity.getAcademicTitle();
    				Map<String, String> bookingTrave = localDataEntity.getBookingTravelType();
    				Map<String, String> gender = localDataEntity.getGender();
    				Map<String, String> clinical = localDataEntity.getClinicalTitle();
    				Map<String, String> url = localDataEntity.getUrl();
    				List<ProvinceEntity> listCity = localDataEntity.getCity();
    				
    				List<DisNavsEntity> listDept = localDataEntity.getDept();
    				
    				list.addAll(getCommonList(academic , Constants.DATAYPTE.ACADEMICTITLE));
    				list.addAll(getCommonList(bookingTrave , Constants.DATAYPTE.BOOKINGTRAVCELTYPE));
    				list.addAll(getCommonList(gender , Constants.DATAYPTE.GENDER));
    				list.addAll(getCommonList(clinical , Constants.DATAYPTE.CLINICALTITLE));
    				list.addAll(getURLList(url));
    				if(list != null && list.size() > 0){
    					deleteAllCommon();
    					putList(list);
    				}
    				if(listCity != null && listCity.size() > 0){
    					deleteAllCity();
    					saveCity(listCity);
    				}
    				
    				if(listDept != null && listDept.size() > 0){
    					deleteAllDept();
    					saveDept(listDept);
    				}
    				
    				handler.sendEmptyMessage(DataCheckService.SUCCESS);
    			}catch(Exception ex){
    				ex.printStackTrace();
    			}
			}
		}).start();
    }
    
    private void deleteAllCommon(){
    	execUpdate("delete from CommonEntity");
    }
    
    private void deleteAllCity(){
    	execUpdate("delete from SubCityEntity");
    }
    
    private void deleteAllDept(){
    	execUpdate("delete from DeptSubCatEntity");
    }
    
    private List<CommonEntity> getCommonList(Map<String, String> map , String type){
    	List<CommonEntity> list = new ArrayList<CommonEntity>();
    	Set set = null;
		Iterator<String> iterator = null;
		if(map != null && !map.isEmpty()){
			set = map.keySet();
			iterator = set.iterator();
			while(iterator.hasNext()){
				String _id = iterator.next();
				String _value = map.get(_id);
				String _type = type;
				CommonEntity entity = new CommonEntity();
				entity.setKey_id(_id);
				entity.set_type(_type);
				entity.set_value(_value);
				list.add(entity);
			}
		}
		return list;
    }
    
    private List<CommonEntity> getURLList(Map<String, String> map){
    	List<CommonEntity> list = new ArrayList<CommonEntity>();
    	Set set = null;
		Iterator<String> iterator = null;
		if(map != null && !map.isEmpty()){
			set = map.keySet();
			iterator = set.iterator();
			while(iterator.hasNext()){
				String _id = iterator.next();
				String _value = map.get(_id);
				CommonEntity entity = new CommonEntity();
				entity.setKey_id(_id);
				entity.set_type(_id);
				entity.set_value(_value);
				list.add(entity);
			}
		}
		return list;
    }
    
    private List<CommonEntity> getCommenList(String type){
    	List<CommonEntity> list = null;
    	String sql = "select * from CommonEntity where _type = ?";
    	Cursor cursor = null;
    	try {
    		list = new ArrayList<CommonEntity>();
    		cursor = querySQL(sql, new String[]{type});
    		if(cursor != null){
    			while(cursor.moveToNext()){
    				list.add(fetchCommonEntity(cursor));
    			}
    		}
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			if(cursor != null) cursor.close();
		}
    	return list;
    }
    
    private CommonEntity fetchCommonEntity(Cursor cursor){
    	String _id = cursor.getString(cursor.getColumnIndex("key_id"));
    	String _value = cursor.getString(cursor.getColumnIndex("_value"));
    	String _type = cursor.getString(cursor.getColumnIndex("_type"));
    	CommonEntity commonEntity = new CommonEntity();
    	commonEntity.setKey_id(_id);
    	commonEntity.set_type(_type);
    	commonEntity.set_value(_value);
    	return commonEntity;
    }
    
    public List<CommonEntity> getAcademic(){
    	return getCommenList(Constants.DATAYPTE.ACADEMICTITLE);
    }
    
    public List<CommonEntity> getBookingTrave(){
    	return getCommenList(Constants.DATAYPTE.BOOKINGTRAVCELTYPE);
    }
    
    public List<CommonEntity> getGender(){
    	return getCommenList(Constants.DATAYPTE.GENDER);
    }
    
    public List<CommonEntity> getClinical(){
    	return getCommenList(Constants.DATAYPTE.CLINICALTITLE);
    }
    
    public String getActionUrl(String type){
    	String sql = "select * from CommonEntity where _type = ?";
    	Cursor cursor = null;
    	try{
    		cursor = querySQL(sql, new String[]{type});
    		if(cursor != null && cursor.moveToFirst()){
    			CommonEntity entity = fetchCommonEntity(cursor);
    			return entity.get_value();
    		}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return "";
    }
    
    
    //############################################### 城市 #################################################################//
    
    public void saveCity(final List<ProvinceEntity> list){
		for (int i = 0;i < list.size(); i ++) {
			ProvinceEntity provinceEntity = list.get(i);
			List<SubCityEntity> citys = provinceEntity.getSubCity();
			if(citys == null || citys.size() == 0){
				citys = new ArrayList<SubCityEntity>();
			}
			for (SubCityEntity subCity : citys) {
				subCity.setProvinceId(provinceEntity.getId());
			}
			SubCityEntity subCityEntity = new SubCityEntity();
			subCityEntity.setCity(provinceEntity.getState());
			subCityEntity.setId(provinceEntity.getId());
			subCityEntity.setProvinceId(0);
			citys.add(subCityEntity);
			putList(citys);
		}
    }
    
    
    public List<SubCityEntity> getProvinces(){
    	return getSubCitys(0);
    }
    
    public List<SubCityEntity> getSubCitys(int provinceId){
    	List<SubCityEntity> list = null;
    	String sql = "select * from SubCityEntity where provinceId = "+provinceId;
    	Cursor cursor = null;
    	try{
    		list = new ArrayList<SubCityEntity>();
    		cursor = querySQL(sql);
    		if(cursor != null){
    			while(cursor.moveToNext()){
    				list.add(fetchSubCity(cursor));
    			}
    		}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}finally{
    		if(cursor == null) cursor.close();
    	}
    	return list;
    }
    
    private SubCityEntity fetchSubCity(Cursor cursor){
    	SubCityEntity subCityEntity = new SubCityEntity();
    	int id =  cursor.getInt(cursor.getColumnIndex("id"));
    	int provinceId = cursor.getInt(cursor.getColumnIndex("provinceId"));
    	String city = cursor.getString(cursor.getColumnIndex("city"));
    	subCityEntity.setId(id);
    	subCityEntity.setProvinceId(provinceId);
    	subCityEntity.setCity(city);
    	return subCityEntity;
    }
    
  //############################################### 科室   #################################################################//
    public void saveDept(final List<DisNavsEntity> list){
		for (int i = 0;i < list.size(); i ++) {
			DisNavsEntity disNavsEntity = list.get(i);
			List<DeptSubCatEntity> cats = disNavsEntity.getSubCat();
			if(cats == null || cats.size() == 0){
				cats = new ArrayList<DeptSubCatEntity>();
			}
			for (DeptSubCatEntity subCat : cats) {
				subCat.setNasId(disNavsEntity.getId());
			}
			DeptSubCatEntity subCat = new DeptSubCatEntity();
			subCat.setName(disNavsEntity.getName());
			subCat.setId(disNavsEntity.getId());
			subCat.setNasId("0");
			cats.add(subCat);
			putList(cats);
		}
    }
    
    public List<DisNavsEntity> getNavsDept(){
    	List<DeptSubCatEntity> listNavs = getNavsDept("0");
    	List<DisNavsEntity> list = new ArrayList<DisNavsEntity>();
    	for(int i = 0; i < listNavs.size()  ; i ++){
    		DisNavsEntity disNavsEntity = new DisNavsEntity();
    		disNavsEntity.setId(listNavs.get(i).getId());
    		disNavsEntity.setName(listNavs.get(i).getName());
    		List<DeptSubCatEntity> listSubDept = getNavsDept(listNavs.get(i).getId());
    		disNavsEntity.setSubCat(listSubDept);
    		list.add(disNavsEntity);
    	}
    	return list;
    }
    
    
    public List<DeptSubCatEntity> getNavsDept(String navsId){
    	List<DeptSubCatEntity> list = null;
    	String sql = "select * from DeptSubCatEntity where nasId = "+navsId;
    	Cursor cursor = null;
    	try{
    		list = new ArrayList<DeptSubCatEntity>();
    		cursor = querySQL(sql);
    		if(cursor != null){
    			while(cursor.moveToNext()){
    				list.add(fetchSubCat(cursor));
    			}
    		}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}finally{
    		if(cursor == null) cursor.close();
    	}
    	return list;
    }
    
    private DeptSubCatEntity fetchSubCat(Cursor cursor){
    	DeptSubCatEntity subCat = new DeptSubCatEntity();
    	String id =  cursor.getString(cursor.getColumnIndex("id"));
    	String nasId = cursor.getString(cursor.getColumnIndex("nasId"));
    	String name = cursor.getString(cursor.getColumnIndex("name"));
    	subCat.setId(id);
    	subCat.setNasId(nasId);
    	subCat.setName(name);
    	return subCat;
    }
    
    
}
