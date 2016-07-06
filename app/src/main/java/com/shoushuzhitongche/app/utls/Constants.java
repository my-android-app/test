package com.shoushuzhitongche.app.utls;

import com.dixiang.framework.config.SystemConfig;

public class Constants {

//	public static String baseURL = "http://192.168.31.223/api.mingyizhudao.com/api2/";
	public static String baseURL = SystemConfig.baseURL;
//	public static String baseURL = "http://192.168.31.118/api/api/";//佳佳
	public final static String API = "9";
//	public static String baseURL = "http://api.mingyizhudao.com/api2/";
	public final static String PARM_ACTION_URL = "parm_action_url";
	public final static String PARM_PAGE_TITLE = "parm_page_title";
	public final static String PARM_ORDER_TYPE = "parm_order_type";
	public final static String API_LEVEL = "?api=2";
	//http://192.168.31.222/myzd/api/appnav1?os=android&appv=10
	//http://192.168.31.222/myzd/api/disease/1?api=4&os=android&appv=10 
	
	public final static String LOACLE_DATA_TIME = "loacl_data_time";
	
	public final static String SERVER_KEY = "server";
	
	public final static int PHOTO_MAX_SIZE = 9;
	
	public final static int HANDLER_SUCCESS = 1;
	public final static int HANDLER_FAILURE = 2;
	
	public final static String LOACLE_DATA = "loacl_data";
	
	public static final class Result_Status{
		public static final int RESULT_SCCESS = 0;
		public static final int RESULT_FAILURE = 1;
	}
	
	public static int TAKEPHOTOFROMCAMERA = 1000;//相机拍取照片
	public final static int PHOTOWIDTH = 250;
	
	public final static String APPINFO = "appInfo";
	
	public final static String MOBILE = "mobile";
	public final static String USER_NAME = "userName";
	public final static String USERTOKEN = "userToken";
	
	public final static String DB_NAME = "mingyizhudao.db";
	
	public final static class DATAYPTE{
		/**学术职称*/
		public final static String ACADEMICTITLE ="academicTitle";
		
		/**医学职称*/
		public final static String CLINICALTITLE ="clinicalTitle";
		
		/**性别*/
		public final static String GENDER = "gender";
		
		/**就诊方式*/
		public final static String BOOKINGTRAVCELTYPE = "bookingTravelType";
		
		/**支付链接*/
		public final static String PAYMENTURL = "paymentUrl";
		
		/**用户注册*/
		public final static String userregister = "userregister";
		
		/**发送验证码*/
		public final static String smsverifycode = "smsverifycode";
		
		/**登录*/
		public final static String userlogin = "userlogin";
		
		/**密码登录*/
		public final static String userpawlogin = "userpawlogin";
		
		/**创建患者基本信息*/
		public final static String savepatient = "savepatient";
		
		/**创建患者预约*/
		public final static String savepatientbooking = "savepatientbooking";
		
		/**我的患者列表*/
		public final static String patientlist = "patientlist";
		
		/**预约列表*/
		public final static String bookinglist = "bookinglist";
		
		/**收到的预约 */
		public final static String doctorbookinglist = "doctorbookinglist";
		
		/**患者详情*/
		public final static String patientinfo = "patientinfo";
		
		/**医生详情*/
		public final static String doctorinfo = "doctorinfo";
		
		/**保存个人信息*/
		public final static String saveprofile = "saveprofile";
		
		/**支付*/
		public final static String paymentUrl = "paymentUrl";
		
		/**修改密码*/
		public final static String forgetpassword = "forgetpassword";
		
		/**发现模块地址*/
		public final static String findView = "findView";
		
		/**城市列表*/
		public final static String citylist = "citylist";
		
		/**首页接口*/
		public final static String indexannouncement = "indexannouncement";
		
		/**个人中心数据总接口*/
		public final static String main = "main";
		
		/**医生回复*/
		public final static String savedoctoropinion = "savedoctoropinion";
		
		/**本地版本*/
		public final static String dataversion = "dataversion";
		
		/**签约专家列表*/
		public final static String orderlist = "orderlist";
		
		/**收到的预约详情*/
		public final static String doctorpatientinfo = "doctorpatientinfo";
		
		/**签约专家列表*/
		public final static String contractdoctorlist = "contractdoctorlist";
		
		/**科室列表*/
		public final static String deptlist = "deptlist";
		
		/**转诊会诊信息*/
		public final static String doctordr = "doctordr";
		
		/**会诊信息*/
		public final static String doctorhzview = "doctorhzview";
		
		/**转诊信息*/
		public final static String doctorzzview = "doctorzzview";
		
		/**创建或修改会诊信息*/
		public final static String savedoctorhz = "savedoctorhz";
		
		/**创建或修改转诊信息*/
		public final static String savedoctorzz = "savedoctorzz";
		
		/**不参与转诊*/
		public final static String notjoinzz = "notjoinzz";
		
		/**不参与会诊*/
		public final static String notjoinhz = "notjoinhz";
	}
	
	public final static class PARMS{
		public final static String CITY_ID = "city_id";
		public final static String CITY_NAME = "city_name";
		public final static String PROVINCE_ID = "province_id";
		
		public final static String ID = "id";
		public final static String CREATORID = "creatorId";
		public final static String PATIENTID = "patientId";
		public final static String ACTIONURL = PARM_ACTION_URL;
		public final static String PAGE_TITLE = "page_title";
		public final static String STATE = "state";
		
		public final static String REFNO = "refno";
		
		public final static String PHONE_NEED_SUM = "phone_need_sum";
		
		public final static String STATE_INFO = "state_info";
		
		public final static String PATIENT_INFO_URL = "patientinfoUrl";
		
		public final static String PATIENT_FILE_URL = "patientfileUrl";
	
		public final static String IS_COME_FROM_BOOKING = "is_come_from_booking";
		
		public final static String BACK_DATA = "return_doctor_details";
		
		public final static String RECIVER_FLG = "reciver_flg";
		
		public final static String IS_NEED_RETURN = "is_need_return";
	}
	
	
	
	public final static class ACTIONURL{
		public final static String CHECK_DATA_VERSION = baseURL + "/dataversion?api=2";
	} 
	
	public static final class ORDER{
		/** 医生id*/
		public final static String PARM_ORDER_DOCTOR_ID = "parm_doctor_id";
		/**科室*/
		public final static String PARM_ORDER_DOCTOR_DEPT = "parm_doctor_dept";
		/**医院名称*/
		public final static String PARM_ORDER_DOCTOR_HP = "parm_doctor_hp";
		/**医生名称*/
		public final static String PARM_ORDER_DOCTOR_NAME = "parm_doctor_name";
	}
	
	public final static class INTENTCODE{
		public final static int CREATE_PATIENT_ACTIVITY = 1001;
		public final static int UPLOAD_FILE_ACTIVIY = 1002;
		public final static int ADD_USER_ACTIVITY = 1003;
		public final static int WATTING_ACTIVITY = 1004;
		public final static int UPLOAD_FILE_ACTIVITY = 1005;
		public final static int INTENTION = 1006; 
		public final static int PAY_INFO_ACTIVITY = 1007;
		public final static int SIGN_ACTIVITY = 1008;
	}
	
	public static final String IMG_WH = "/imageView2/1/";
	public static final String IMG_WH_BG = "/imageView2/0/640";
	
    public static final class HTTP_INDEX{
	   public final static String First = "0";
	   public final static String Second = "1";
	   public final static String Third = "2";
	   public final static String Fourth = "3";
    }
}
          