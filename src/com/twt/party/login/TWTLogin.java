package com.twt.party.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * 主要是对天外天登陆接口的封装，可进行登陆操作和得到用户相关的信息
 * @author Boelroy
 *
 */
public class TWTLogin {
	
	/**
	 *  天外天登陆接口
	 *  建议开新的线程来进行此操作，不然会阻塞主界面
	 * @param context 当前Activity或者Service 的上下文
	 * @param userName 登陆的用户名
	 * @param passWord 登陆密码
	 * @return true表示登陆成功 false表示登陆失败
	 */
	public static boolean loginToServer(final Context context, final String userName, final String passWord){
		
				//首先使用NameValuePair封装将要查询的年数和关键字绑定  
				ArrayList<NameValuePair> userpair = new ArrayList<NameValuePair>(2);  
		     	userpair.add(new BasicNameValuePair(TWTLoginConst.TWT_USER_NAME,userName));  
		     	userpair.add(new BasicNameValuePair(TWTLoginConst.TWT_USER_PASSWORD,passWord));
				
		     	
		     	//使用HttpPost封装整个SQL语句  
		     	//使用HttpClient发送HttpPost对象  
		     	InputStream content = null;
		     	try {
		     		HttpClient client = new DefaultHttpClient();
			     	HttpPost post = new HttpPost(TWTLoginConst.TWT_LOGIN_URL);
					post.setEntity(new UrlEncodedFormEntity(userpair));
					HttpResponse response = client.execute(post);
					
					//解析相应得到响应中的主体
					HttpEntity entity = response.getEntity();  
	     	        content = entity.getContent();
				} catch (Exception e) {
					//如果抛错，输出错误信息到日志
					Log.e(TWTLoginConst.TWT_PARTY_ERROR,"Connect to server error!"+e.toString());
					//直接登陆失败
					//return false;
				}
		     	
		     	//将InputReader中的内容转化为string
		     	String userInfo = null;
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(content,"UTF-8"),8);
					StringBuilder b = new StringBuilder();
			     	String tmp = null;
			     	try {
						while((tmp = reader.readLine()) != null){
							b.append(tmp + "\n");
						}
						userInfo = b.toString();
				} catch (UnsupportedEncodingException e1) {
					
					e1.printStackTrace();
				}
				} catch (IOException e) {
					//如果抛错，输出错误信息到日志
					Log.e(TWTLoginConst.TWT_PARTY_ERROR,"Convert InpterStream to String error!");
				}
		     	
		     	
		     	//将得到的json格式中的字符串处理，并处理得到其中的信息
		     	try {
					JSONArray userInfoJson= new JSONArray(userInfo);
					JSONObject userInfoObj = userInfoJson.getJSONObject(0);
					
					
					SharedPreferences sharedInfo = 
								context.getSharedPreferences(TWTLoginConst.TWT_SHAREDPREFENCE_NAME
										, Context.MODE_PRIVATE);
					//将用户名存储到SharedPreferences中
					sharedInfo.edit().putString(TWTLoginConst.TWT_USER_NAME
										, userInfoObj.getString(TWTLoginConst.TWT_USER_NAME)).commit();
					Log.i(TWTLoginConst.TWT_PARTY_ERROR, userInfoObj.getString(TWTLoginConst.TWT_USER_NAME));
					//将uid存储到SharedPreferences中，同时也存储是否登陆成功的标志
					if(!(userInfoObj.getString(TWTLoginConst.TWT_USER_UID).equals("1")
							||userInfoObj.getString(TWTLoginConst.TWT_USER_UID).equals("0"))){
						
						sharedInfo.edit().putBoolean(TWTLoginConst.IS_LOGIN_SUCCEED, true).commit();
						sharedInfo.edit().putString(TWTLoginConst.TWT_USER_UID
										, userInfoObj.getString(TWTLoginConst.TWT_USER_UID)).commit();
						sharedInfo.edit().putString(TWTLoginConst.TWT_USER_NAME, userName).commit();
					}
					else
						sharedInfo.edit().putBoolean(TWTLoginConst.IS_LOGIN_SUCCEED, false).commit();
						
				} catch (JSONException e) {
					//日志输出转换错误
					Log.e(TWTLoginConst.TWT_PARTY_ERROR, "Convert Json to String error");
				}
		
		return context.getSharedPreferences(TWTLoginConst.TWT_SHAREDPREFENCE_NAME,
				                                                     0).getBoolean(TWTLoginConst.IS_LOGIN_SUCCEED, false);
	}
	
	
	/**
	 * 用来得到用户的真实姓名
	 * @param context 当前Activity或者Service的上下文
	 * @return 返回用户的真实姓名
	 */
	public static String getUserRealName(Context context){
		SharedPreferences userinfo =  context.getSharedPreferences(TWTLoginConst.TWT_SHAREDPREFENCE_NAME, 0);
		String username = userinfo.getString(TWTLoginConst.TWT_USER_NAME, "");
		
		return username;
	}
	
	/**
	 * 用来得到用户在服务器上的识别码，即uid
	 * @param context 当前Activity或者Service的上下文
	 * @return 返回用户的uid
	 */
	public static String getUserUid(Context context){
		SharedPreferences userinfo = context.getSharedPreferences(TWTLoginConst.TWT_SHAREDPREFENCE_NAME, 0);
		String useruid = userinfo.getString(TWTLoginConst.TWT_USER_UID, "");
		
		return useruid;
	}
	
	/**
	 * 用来得到用户的登陆用的用户名
	 * @param context 当前Activity或者Service的上下文
	 * @return 返回用户的登陆用户名
	 */
	public static String getUserName(Context context){
		SharedPreferences userinfo = context.getSharedPreferences(TWTLoginConst.TWT_SHAREDPREFENCE_NAME, 0);
		String userName = userinfo.getString(TWTLoginConst.TWT_USER_NAME, "");
				
		return userName;
	}
}
