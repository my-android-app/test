package com.dixiang.framework.network;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.net.URLEncoder;
import java.io.ByteArrayOutputStream;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;
import com.dixiang.framework.rsa.RSAUtils;
import com.dixiang.framework.utils.Base64Utils;
import com.dixiang.framework.utils.KeyValue;
import com.dixiang.framework.utils.Utils;

public class HttpUtils {

	public static KeyValue doGet(String urlStr, String param) throws Exception {
		if(Utils.isStringEmpty(urlStr)){
			return null;
		}
		URL url = null;
		HttpURLConnection httpURLConnection = null;
//		urlStr = getFinalUrl(urlStr);
		url = new URL(urlStr);
//		Log.e("http_url", urlStr);
		// param=URLEncoder.encode(param, "UTF-8");
		httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setConnectTimeout(5 * 1000);
		httpURLConnection.setReadTimeout(5 * 1000);
		httpURLConnection.setRequestMethod("GET");
		int response = httpURLConnection.getResponseCode();
		if (response == HttpURLConnection.HTTP_OK) {
			InputStream inptStream = httpURLConnection.getInputStream();
			return new KeyValue(0, dealResponseResult(inptStream));
		} else {
			return new KeyValue(response, null);
		}
	}

	/*
	 * Function : 发送Post请求到服务器 Param : params请求体内容，encode编码格式
	 */
	public static KeyValue doPost(String strUrlPath, String param)
			throws Exception {
		// byte[] data = getRequestData(params,
		// encode).toString().getBytes();//获得请求体
		param = URLEncoder.encode(getRSAEncryptParam(param), "utf-8") ;
//		param = getRSAEncryptParam(param);
		Log.e("http_post_param", param);
		byte[] data = param.getBytes();
		// String urlPath = "http://192.168.1.9:80/JJKSms/RecSms.php";
		URL url = new URL(strUrlPath);
		Log.e("http_url", strUrlPath);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setConnectTimeout(60 * 1000); // 设置连接超时时间
		httpURLConnection.setReadTimeout(60 * 1000);
		httpURLConnection.setDoInput(true); // 打开输入流，以便从服务器获取数据
		httpURLConnection.setDoOutput(true); // 打开输出流，以便向服务器提交数据
		httpURLConnection.setRequestMethod("POST"); // 设置以Post方式提交数据
		httpURLConnection.setUseCaches(false); // 使用Post方式不能使用缓存
		// 设置请求体的类型是文本类型
		httpURLConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		// 设置请求体的长度
		httpURLConnection.setRequestProperty("Content-Length",
				String.valueOf(data.length));
		// 获得输出流，向服务器写入数据
		OutputStream outputStream = httpURLConnection.getOutputStream();
		outputStream.write(data);

		int response = httpURLConnection.getResponseCode(); // 获得服务器的响应码
		if (response == HttpURLConnection.HTTP_OK) {
			InputStream inptStream = httpURLConnection.getInputStream();
			return new KeyValue(0, dealResponseResult(inptStream)); // 处理服务器的响应结果
		} else {
			return new KeyValue(response, null);
		}
	}
	
	public static KeyValue doPut(String url, String json)
			throws Exception {
			//创建一个http客户端  
			HttpClient client=new DefaultHttpClient();  
			//创建一个PUT请求  
			HttpPut httpPut=new HttpPut(url);  
			//组装数据放到HttpEntity中发送到服务器  
			final List dataList = new ArrayList();  
			json = URLEncoder.encode(getRSAEncryptParam(json), "utf-8") ;
			StringEntity entity = new StringEntity(json);
			httpPut.setEntity(entity);  
			//向服务器发送PUT请求并获取服务器返回的结果，可能是修改成功，或者失败等信息  
			HttpResponse response=client.execute(httpPut);  
			if (response != null) {
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if(statusCode == 200 || statusCode == 403 ){
					InputStream inputStream = response.getEntity().getContent();
					return new KeyValue(0, dealResponseResult(inputStream)); // 处理服务器的响应结果
				} else {
					return new KeyValue(statusCode, null);
				}
			}
			return new KeyValue(100, null);
	}

	/*
	 * Function : 封装请求体信息 Param : params请求体内容，encode编码格式
	 */
	public static StringBuffer getRequestData(Map<String, String> params,
			String encode) throws Exception {
		StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
		for (Map.Entry<String, String> entry : params.entrySet()) {
			stringBuffer.append(entry.getKey()).append("=")
					.append(URLEncoder.encode(entry.getValue(), encode))
					.append("&");
		}
		stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"&"
		return stringBuffer;
	}

	/*
	 * Function : 处理服务器的响应结果（将输入流转化成字符串） Param : inputStream服务器的响应输入流
	 */
	public static String dealResponseResult(InputStream inputStream) throws Exception{
		String resultData = null; // 存储处理结果
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		try {
			while ((len = inputStream.read(data)) != -1) {
				byteArrayOutputStream.write(data, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		resultData = new String(byteArrayOutputStream.toByteArray());
		return getRSADecrypResult(resultData);
	}


	private static String getRSAEncryptURL(String url) throws Exception {
		return  getFinalUrl(url);
	}
	
	private static String getRSAEncryptParam(String param) throws Exception{
		double size = 117.0;
		if (!Utils.isStringEmpty(param)) {
			StringBuffer buffer = new StringBuffer("[");
			String str64 = Base64Utils.encode(param.trim().getBytes("utf-8"));
			int len = (int) Math.ceil(str64.length() / size);
			for (int i = 0; i < len; i++) {
				buffer.append("\"");
				if (i == len - 1) {
					buffer.append(Base64Utils.encode(RSAUtils.encryptData(str64
							.substring((int) (i * size)).getBytes())));
				} else {
					buffer.append(Base64Utils.encode(RSAUtils
							.encryptData(str64.substring((int) (i * size),
									(int) ((i + 1) * size)).getBytes())));
				}
				buffer.append("\"");
				buffer.append(",");
			}
			String value = buffer.substring(0, buffer.length() - 1);
			return value + "]";
		}
		return param;
	}
	
	private static String getFinalUrl(String url){
		
		try{
			String header= url;
			StringBuffer buffer = new StringBuffer("");
			int index0 = url.indexOf("?");
			if(index0 != -1){
				header = url.substring(0, index0);
				String paramStr = url.substring(index0+1);
				String [] strs = paramStr.split("&");
				buffer.append("{");
				for (String string : strs) {
					int index = string.indexOf("=");
					String key = string.substring(0,index);
					String value = string.substring(index+1);
					buffer.append("\"");
					buffer.append(key);
					buffer.append("\":\"");
					buffer.append(value);
					buffer.append("\"");
					buffer.append(",");
				}
				String param = getRSAEncryptParam(buffer.substring(0, buffer.length()-1)+"}");
				return header+"?param="+URLEncoder.encode(param, "UTF-8");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return url;
	}

	private static String getRSADecrypResult(String str) throws Exception {
		if(Utils.isStringEmpty(str)){
			return "";
		}
		if(str.contains("{")){
			Log.e("http_url", str);
			return str;
		}
		StringBuffer buffer = new StringBuffer("");
		str = str.substring(1, str.length() - 1);
		String[] resluts = str.split(",");
		for (String string : resluts) {
			String value =  string.replace("\"", "").replace("\\", "");
			buffer.append(new String(RSAUtils.decryptData(Base64Utils.decode(value)),"utf-8"));
		}
		Log.e("http_url", buffer.toString());
		return buffer.toString();
	}
	
	
}