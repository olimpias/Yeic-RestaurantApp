package com.Yeic.Database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.Yeic.HTTP.SessionManager;
import com.Yeic.Users.Chef;
import com.Yeic.Users.Customer;
import com.Yeic.Users.RestaurantOwner;
import com.Yeic.Users.User;
/**
 * Without user type actions are made in this class
 * 
 *
 */
public  class DatabaseConnection {	
public DatabaseConnection() {
	// TODO Auto-generated constructor stub
}
/**
 * Login() method is checking that username and password exists on database.If exists return user information otherwise
 * null
 * @pre username!=null
 * @pre password!=null
 * 
 * @param username
 * @param password
 * @return
 */
public User Login(String username,String password){
	InputStream is;
	String line;
	String result;
	ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("username", username));
	nameValuePairs.add(new BasicNameValuePair("password",password));
	try {
	HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/login.php");
	httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
	HttpEntity entity=response.getEntity();
	is=entity.getContent();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		return null;
	}
	try
    {
        BufferedReader reader = new BufferedReader
			(new InputStreamReader(is,"iso-8859-1"),8);
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null)
	    {
            sb.append(line + "\n");
        }
        is.close();
        result = sb.toString();
	    Log.e("Result", result);
	}
    catch(Exception e)
	{
       return null;//Connection problem
	}
	try {
		List<Cookie> cookies=SessionManager.getCookie().getCookies();
		for(int i=0;i<cookies.size();i++){
		  Log.e("COOKIE", cookies.get(i)+"");
		}
		JSONObject json=new JSONObject(result);
		String userName=json.getString("username");
		String usertype=json.getString("usertype");
		User user;
		if(usertype.equals("chef")){
			user=new Chef(userName);
		}else if(usertype.equals("res")){
			user=new RestaurantOwner(userName);
		}else{
			user=new Customer(userName);
		}
		return user;
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	
	
}
/**
 * Logout() is closing session on server side for user.
 */
public static void LogOut(){
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/logout.php");
		HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
		response.getEntity();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		}
}
/**
 * NewRegister() is creating new customer for the system.
 * @pre user!=null
 * @param user
 * @return
 */
public static int newRegister(User user,String password){
	InputStream is;
	String line;
	String result;
	HttpClient httpClient=new DefaultHttpClient();
	ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("username", user.getUsername()));
	nameValuePairs.add(new BasicNameValuePair("password",password));
	nameValuePairs.add(new BasicNameValuePair("phone", user.getPhone()));
	nameValuePairs.add(new BasicNameValuePair("fname",user.getName()));
	nameValuePairs.add(new BasicNameValuePair("lname",user.getSurname()));
	nameValuePairs.add(new BasicNameValuePair("email",user.getEmailAddress()));
	if(user instanceof Customer){
		nameValuePairs.add(new BasicNameValuePair("usertype","cus"));
	}else if(user instanceof Chef){
		nameValuePairs.add(new BasicNameValuePair("usertype","chef"));
	}else{
		nameValuePairs.add(new BasicNameValuePair("usertype","res"));
	}
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/newRegistration.php");
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response=httpClient.execute(httpPost);
		HttpEntity entity=response.getEntity();
		is=entity.getContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return -1;
		}
		try
	    {
	        BufferedReader reader = new BufferedReader
				(new InputStreamReader(is,"iso-8859-1"),8);
	        StringBuilder sb = new StringBuilder();
	        while ((line = reader.readLine()) != null)
		    {
	            sb.append(line + "\n");
	        }
	        is.close();
	        result = sb.toString();
	        Log.e("ResultControl",result+" Check");
		    
		}
	    catch(Exception e)
		{
	       return -1;//Connection problem
		}
		try {
			JSONObject json=new JSONObject(result);
			int code=json.getInt("code");
			return code;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
}
public static int forgotPassword(String password,String email){
	InputStream is;
	String line;
	String result;
	HttpClient httpClient=new DefaultHttpClient();
	ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("password", password));
	nameValuePairs.add(new BasicNameValuePair("email",email ));
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/forgotPassword.php");
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response=httpClient.execute(httpPost);
		HttpEntity entity=response.getEntity();
		is=entity.getContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return -1;
		}
	try
    {
        BufferedReader reader = new BufferedReader
			(new InputStreamReader(is,"iso-8859-1"),8);
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null)
	    {
            sb.append(line + "\n");
        }
        is.close();
        result = sb.toString();
        Log.e("ResultControl Email",result+" Check");
	    
	}
    catch(Exception e)
	{
       return -1;//Connection problem
	}
	try {
		JSONObject json=new JSONObject(result);
		int code=json.getInt("code");
		return code;
	}catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return -1;
	}
}
}
