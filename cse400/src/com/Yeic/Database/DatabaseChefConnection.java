package com.Yeic.Database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import com.Yeic.Equipments.Reservation;
import com.Yeic.Equipments.RestaurantDate;
import com.Yeic.HTTP.SessionManager;
import com.Yeic.Items.*;
import com.Yeic.Users.Chef;
import com.Yeic.Users.User;


/**
 * This class is used for chef type of users
 * The authorization is restricted according to user types
 * 
 *
 */
public class DatabaseChefConnection extends DatabaseConnection {
private User user;

	public DatabaseChefConnection(String username) {
		user=new Chef(username);
}
	/**
	 * getUser() method return user as chef
	 * @return
	 */
	public Chef getUser() {
		return (Chef)user;
	}
	public DatabaseChefConnection() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * addMenu() is adding new menu to database.If method returns 0, error occurs If method returns 1,menu is successfully inserted 
	 * @pre menu!=null
	 * @param menu
	 * @return
	 */
	public int addMenu(Menu menu){
		InputStream is;
		String line;
		String result;
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("name", menu.getName()+""));
		nameValuePairs.add(new BasicNameValuePair("description", menu.getDescription()+""));
		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/insertMenu.php");
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
			HttpEntity entity=response.getEntity();
			is=entity.getContent();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return 0;
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
		    	return 0;//Connection problem
			}
			try {
				JSONObject json=new JSONObject(result);
				int code=json.getInt("code");
				return code;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
	}
	/**
	 * 
	 * addDrink() is adding new drink to database.If method returns 0, error occurs If method returns 1,drink is successfully inserted 
	 * @pre drink!=null
	 * @param drink
	 * @return
	 */
	public int addDrink(Drink drink){
		InputStream is;
		String line;
		String result;
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("name", drink.getName()+""));
		Log.e("DrinkName", drink.getName());
		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/insertDrink.php");
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
			HttpEntity entity=response.getEntity();
			is=entity.getContent();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return 0;
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
		    	return 0;//Connection problem
			}
			try {
				List<Cookie> cookies=SessionManager.getCookie().getCookies();
				for(int i=0;i<cookies.size();i++){
				  Log.e("COOKIE", cookies.get(i)+"");
				}
				CookieStore cookiess=SessionManager.getHttpClient().getCookieStore();
				cookies=cookiess.getCookies();
				for(int i=0;i<cookies.size();i++){
					  Log.e("COOKIE2", cookies.get(i)+"");
					}
				Log.e("ERROR", result);
				JSONObject json=new JSONObject(result);
				int code=json.getInt("code");
				return code;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
	}
	/**
	 *  addFood() is adding new drink to database.If method returns 0, error occurs If method returns 1,drink is successfully inserted 
	 * @pre drink!=null
	 * @param food
	 * @return
	 */
	public int addFood(Food food){
		InputStream is;
		String line;
		String result;
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("name", food.getName()+""));
		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/insertFood.php");
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
			HttpEntity entity=response.getEntity();
			is=entity.getContent();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return 0;
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
		    	return 0;//Connection problem
			}
			try {
				JSONObject json=new JSONObject(result);
				int code=json.getInt("code");
				return code;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
	}
	/**
	 * RefreshFood() is collect food data from database and set on chef.If method fails, returns 0.Else returns 1. 
	 * @return
	 */
	public boolean refreshFood(){
		InputStream is;
		String line;
		String result;

		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/selectFood.php");
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
				ArrayList<Food> foodLis=new ArrayList<Food>();
				JSONArray array=new JSONArray(result);
				for(int i=0;i<array.length();i++){
					JSONObject jobj=array.getJSONObject(i);
					Food food=new Food(jobj.getInt("id"), jobj.getString("name"));
					foodLis.add(food);
				}
				getUser().setFoods(foodLis);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		return true;
	}
	/**
	 * RefreshDrink() is collect drink data from database and set on chef.If method fails, returns 0.Else returns 1. 
	 * @return
	 */
	public boolean refreshDrink(){
		InputStream is;
		String line;
		String result;

		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/selectDrink.php");
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
				ArrayList<Drink> drinkList=new ArrayList<Drink>();
				JSONArray array=new JSONArray(result);
				for(int i=0;i<array.length();i++){
					JSONObject jobj=array.getJSONObject(i);
					Drink drink=new Drink(jobj.getInt("id"), jobj.getString("name"));
					drinkList.add(drink);
				}
				getUser().setDrinks(drinkList);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		return true;
	}
	/**
	 * RefreshMenu() is collect menu data from database and set on chef.If method fails, returns 0.Else returns 1. 
	 * @return
	 */
	public boolean refreshMenu(){
		InputStream is;
		String line;
		String result;

		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/selectMenu.php");
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
				ArrayList<Menu> menuList=new ArrayList<Menu>();
				JSONArray array=new JSONArray(result);
				for(int i=0;i<array.length();i++){
					JSONObject jobj=array.getJSONObject(i);
					Menu menu=new Menu(jobj.getInt("id"), jobj.getString("description"),jobj.getString("name"));
					menuList.add(menu);
				}
				getUser().setMenus(menuList);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		return true;
	}
	/**
	 * deleteFood() is deleting food according to inside of food id.If it successes, returns 1 if it fails, returns 0
	 * @pre food!=null
	 * @param food
	 * @return
	 */
	public boolean deleteFood(Food food){
		InputStream is;
		String line;
		String result;
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", food.getFoodID()+""));
		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/deleteFood.php");
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
	 * deleteMenu() is deleting menu according to inside of menu id.If it successes, returns 1 if it fails, returns 0
	 * @pre menu!=null
	 * @param menu
	 * @return
	 */
	public boolean deleteMenu(Menu menu){
		InputStream is;
		String line;
		String result;
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", menu.getMenuID()+""));
		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/deleteMenu.php");
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
	 * deleteDrink() is deleting drink according to inside of drink id.If it successes, returns 1 if it fails, returns 0
	 * @pre drink!=null
	 * @param drink
	 * @return
	 */
	public boolean deleteDrink(Drink drink){
		InputStream is;
		String line;
		String result;
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", drink.getDrinkID()+""));
		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/deleteDrink.php");
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
	 * This method returns reservations which are stored in database
	 * @return
	 */
	public ArrayList<Reservation> getOrderList(){
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
			try {
				ArrayList<Reservation> reservationList=new ArrayList<Reservation>();
				JSONArray array=new JSONArray(result);
				for(int i=0;i<array.length();i++){
					JSONObject jobj=array.getJSONObject(i);
					Reservation reservation=new Reservation(jobj.getInt("id"), new Table(jobj.getInt("idtable"), jobj.getString("tdescription"), jobj.getString("tname")), new RestaurantDate(jobj.getString("date")), jobj.getString("name"), jobj.getString("surname"), jobj.getString("phone"));
					reservationList.add(reservation);
					
				}
				return reservationList;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
	}
	/**
	 * OrderList() is gathering orders according to reservation id.Also return values are object.
	 * @pre id>0
	 * @param id
	 * @return obj
	 */
	public ArrayList<Object> OrderList(int id){
		InputStream is;
		String line;
		String result;
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", id+""));
		try {
			HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/selectOrdersChef.php");
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
	        Log.e("ResultControl",result+" Check");
		    
		}
	    catch(Exception e)
		{
	    	return null;//Connection problem
		}try {
			ArrayList<Object> list=new ArrayList<Object>();
			JSONObject jobj=new JSONObject(result);
			if(!jobj.isNull("drink")){
				JSONArray arrayDrink=jobj.getJSONArray("drink");
				for(int i=0;i<arrayDrink.length();i++){
					JSONObject drinkobj=arrayDrink.getJSONObject(i);
					Drink drink=new Drink(drinkobj.getInt("id"),drinkobj.getString("name"));
					list.add(drink);
				}	
			}
			if(!jobj.isNull("food")){
				JSONArray arrayFood=jobj.getJSONArray("food");
				for(int i=0;i<arrayFood.length();i++){
					JSONObject foodobj=arrayFood.getJSONObject(i);
					Food food=new Food(foodobj.getInt("id"), foodobj.getString("name"));
					list.add(food);
				}	
			}
			if(!jobj.isNull("menu")){
				JSONArray arrayMenu=jobj.getJSONArray("menu");
				for(int i=0;i<arrayMenu.length();i++){
					JSONObject menuobj=arrayMenu.getJSONObject(i);
					Menu menu=new Menu(menuobj.getInt("id"), menuobj.getString("description"), menuobj.getString("name"));
					list.add(menu);
				}
			}
			
			return list;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
