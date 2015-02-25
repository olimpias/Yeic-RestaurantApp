package com.Yeic.Database;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.Yeic.CustomerGui.CustomerActivitiy;
import com.Yeic.Equipments.Reservation;
import com.Yeic.Equipments.RestaurantDate;
import com.Yeic.HTTP.SessionManager;
import com.Yeic.Items.Comment;
import com.Yeic.Items.Table;
import com.Yeic.Users.Chef;
import com.Yeic.Users.RestaurantOwner;
import com.Yeic.Users.User;
/**
 * 
 * this class is for Restaurant OWner user type
 * 
 *
 */
public class DatabaseRestaurantOwnerConnection extends DatabaseConnection {
 private User user;
	public DatabaseRestaurantOwnerConnection(String name) {
	user=new RestaurantOwner(name);
}
	/**
	 * deleteComment deletes given comment according to its id from database.If deletion is succeed returns 1,if it fails returns 0.
	 * @pre comment!=null
	 * @param comment
	 * @return
	 */
	public boolean deleteComment(Comment comment){
		InputStream is;
		String line;
		String result;
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", comment.getCommentID()+""));
		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/deleteComment.php");
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
			HttpEntity entity=response.getEntity();
			is=entity.getContent();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return false;
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
		    	return false;//Connection problem
			}
			try {
				JSONObject json=new JSONObject(result);
				int code=json.getInt("code");
				if(code ==0)return false;
				return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	}
	/**
	 * refreshComments brings comments from database and set on array list on user.If it successes returns 1. if it fails returns 0.
	 * @return
	 */
	public boolean refreshComments(){
		InputStream is;
		String line;
		String result;

		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/selectComment.php");
			HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
			List<Cookie> cookies=SessionManager.getCookie().getCookies();
			for(int i=0;i<cookies.size();i++){
			  Log.e("COOKIE", cookies.get(i)+"");
			}
			HttpEntity entity=response.getEntity();
			is=entity.getContent();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return false;
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
		    	return false;//Connection problem
			}
			try {
				ArrayList<Comment> commentList=new ArrayList<Comment>();
				JSONArray array=new JSONArray(result);
				for(int i=0;i<array.length();i++){
					JSONObject jobj=array.getJSONObject(i);
					Comment comment=new Comment(jobj.getInt("id"), jobj.getString("review"),new RestaurantDate(jobj.getString("date")), jobj.getString("name"), jobj.getString("surname"));
					commentList.add(comment);
				}
				getUser().setCommentList(commentList);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		return true;
	}
	/**
	 * refreshReservations() brings all reservations from database and set array list to user.If selection is succeed returns 1, if it fails returns 0.
	 * @return
	 */
	public boolean refreshReservations(){
		InputStream is;
		String line;
		String result;

		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/selectReservations.php");
			HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
			HttpEntity entity=response.getEntity();
			is=entity.getContent();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return false;
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
		    	return false;//Connection problem
			}
			try {
				ArrayList<Reservation> reservationList=new ArrayList<Reservation>();
				JSONArray array=new JSONArray(result);
				for(int i=0;i<array.length();i++){
					JSONObject jobj=array.getJSONObject(i);
					Reservation reservation=new Reservation(jobj.getInt("id"), new Table(jobj.getInt("idtable"), jobj.getString("tdescription"), jobj.getString("tname")), new RestaurantDate(jobj.getString("date")), jobj.getString("name"), jobj.getString("surname"), jobj.getString("phone"));
					reservationList.add(reservation);
					
				}
				getUser().setReservationList(reservationList);
			} catch (JSONException e) {
				getUser().setReservationList(new ArrayList<Reservation>());
				e.printStackTrace();
				return false;
			}
		return true;
	}
	/**
	 * getUser() method return user as RestaurantOwner
	 * @return
	 */
	public RestaurantOwner getUser() {
		return (RestaurantOwner)user;
	}public void setUser(User user) {
		this.user = user;
	}
	/**
	 * refreshTables brings all tables from database and set as array list on user.If selection is succeed returns 1, if it fails returns 0.
	 * @return
	 */
	public boolean refreshTable(){
		InputStream is;
		String line;
		String result;

		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/selectTable.php");
			HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
			HttpEntity entity=response.getEntity();
			is=entity.getContent();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return false;
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
		    	return false;//Connection problem
			}
			try {
				ArrayList<Table> tableList=new ArrayList<Table>();
				JSONArray array=new JSONArray(result);
				for(int i=0;i<array.length();i++){
					JSONObject jobj=array.getJSONObject(i);
					Table table=new Table(jobj.getInt("id"), jobj.getString("description"), jobj.getString("name"));
					tableList.add(table);
				}
				getUser().setTableList(tableList);
			} catch (JSONException e) {
				getUser().setTableList(new ArrayList<Table>());
				e.printStackTrace();
				return false;
			}
		return true;
	}
	/**
	 * editTable() edits table according to table id.If it successes returns 1, if it fails returns 0.
	 * @pre table!=null
	 * @param table
	 * @return
	 */
	public boolean editTable(Table table){
		InputStream is;
		String line;
		String result;
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", table.getTableID()+""));
		nameValuePairs.add(new BasicNameValuePair("name", table.getName()+""));
		nameValuePairs.add(new BasicNameValuePair("description", table.getDescription()+""));
		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/updateTable.php");
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
			HttpEntity entity=response.getEntity();
			is=entity.getContent();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return false;
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
		    	return false;//Connection problem
			}
			try {
				JSONObject json=new JSONObject(result);
				int code=json.getInt("code");
				if(code ==0)return false;
				return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	}
	/**
	 * addTable insert table to database.If it successes return 1 if it fails returns 0.
	 * @pre table!=null
	 * @param table
	 * @return
	 */
	public boolean addTable(Table table){
		InputStream is;
		String line;
		String result;
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("name", table.getName()+""));
		nameValuePairs.add(new BasicNameValuePair("description", table.getDescription()+""));
		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/insertTable.php");
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
			HttpEntity entity=response.getEntity();
			is=entity.getContent();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return false;
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
		    	return false;//Connection problem
			}
			try {
				JSONObject json=new JSONObject(result);
				int code=json.getInt("code");
				if(code ==0)return false;
				return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	}
	/**
	 * deleteTable deletes table from database according to table id.If it successes returns 1, if it fails returns 0.
	 * @param table
	 * @return
	 */
	public boolean deleteTable(Table table){	
		InputStream is;
		String line;
		String result;

		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/deleteTable.php");
			ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", table.getTableID()+""));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
			HttpEntity entity=response.getEntity();
			is=entity.getContent();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return false;
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
		    	return false;//Connection problem
			}
			try {
				JSONObject json=new JSONObject(result);
				int code=json.getInt("code");
				if(code ==0)return false;
				return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	}
	/**
	 * getChef() brings user chef informations and return as chef.
	 * @return
	 */
	public Chef getChef(){
		InputStream is;
		String line;
		String result;

		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/selectChef.php");
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
		        Log.e("ResultControl",result+" Check");
			    
			}
		    catch(Exception e)
			{
		    	return null;//Connection problem
			}
			Chef chef=new Chef();
			try {
				JSONObject obj=new JSONObject(result);
				
				if(obj!=null){
					chef=new Chef(obj.getString("username"), CustomerActivitiy.tmpPass, obj.getString("phone"), obj.getString("name"), obj.getString("surname"), obj.getString("email"));	
				}
				 
			} catch (JSONException e) {
				getUser().setReservationList(new ArrayList<Reservation>());
				e.printStackTrace();
				return null;
			}
			return chef;
	}
	/**
	 * addChef() inserts chef to database and deleting old chef.If it successes return 1.If it fails returns 0.
	 * @pre chef!=null
	 * @param chef
	 * @return
	 */
	public boolean addChef(Chef chef){
		InputStream is;
		String line;
		String result;
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username",chef.getUsername()));
		if(!chef.getPass().equals(CustomerActivitiy.tmpPass))
		nameValuePairs.add(new BasicNameValuePair("password",chef.getPass()));
		nameValuePairs.add(new BasicNameValuePair("name", chef.getName()));
		nameValuePairs.add(new BasicNameValuePair("surname", chef.getSurname()));
		nameValuePairs.add(new BasicNameValuePair("phone", chef.getPhone()));
		nameValuePairs.add(new BasicNameValuePair("email", chef.getEmailAddress()));
		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/insertChef.php");
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
			HttpEntity entity=response.getEntity();
			is=entity.getContent();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return false;
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
		    	return false;//Connection problem
			}
			try {
				JSONObject json=new JSONObject(result);
				int code=json.getInt("code");
				if(code ==0)return false;
				return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		
	}
	
}
