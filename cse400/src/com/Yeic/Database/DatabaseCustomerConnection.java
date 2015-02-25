package com.Yeic.Database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.Yeic.CustomerGui.CustomerActivitiy;
import com.Yeic.Equipments.Order;
import com.Yeic.Equipments.Reservation;
import com.Yeic.Equipments.RestaurantDate;
import com.Yeic.GuiItem.OrderItem;
import com.Yeic.HTTP.SessionManager;
import com.Yeic.Items.Comment;
import com.Yeic.Items.Drink;
import com.Yeic.Items.Food;
import com.Yeic.Items.Menu;
import com.Yeic.Items.Table;
import com.Yeic.Users.Customer;
import com.Yeic.Users.User;
/**
 * 
 * DatabaseCustomerConnection contains database connection methods.
 *
 */
public class DatabaseCustomerConnection extends DatabaseConnection {
private User user;
public DatabaseCustomerConnection(String username) {
	user=new Customer(username);
}
/**
 * refreshComments() is bringing comments from database and set comments in user as ArrayList.If it successes returns 1, if it fails returns 0
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
 * getProfil() brings profile information about user and set them on users field.If it fails returns 0, if it successes returns 1.
 * @return
 */
public boolean getProfil(){
	InputStream is;
	String line;
	String result;
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/selectProfil.php");
		HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
		HttpEntity entity=response.getEntity();
		is=entity.getContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}try
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
	        Log.e("Customer",result+" Check");
		    
		}
	    catch(Exception e)
		{
	    	return false;//Connection problem
		}try {
			JSONObject obj=new JSONObject(result);
			getUser().setEmailAddress(obj.getString("email"));
			getUser().setPassword(CustomerActivitiy.tmpPass);
			getUser().setPhone(obj.getString("phone"));
			getUser().setName(obj.getString("name"));
			getUser().setSurname(obj.getString("surname"));
			getUser().setUsername(obj.getString("username"));
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
}
/**
 * getUser() method return user as customer
 * @return
 */
public Customer getUser() {
	return (Customer)user;
}
/**
 * updateProfil() updates user profile by using database.If it fails returns 0, if it successes returns 1.
 * @pre user!=null
 * @param user
 * @return
 */
public boolean updateProfil(User user){
	InputStream is;
	String line;
	String result;
	
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/updateProfil.php");
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		Log.e("Password", user.getPass());
		if(!user.getPass().equals(CustomerActivitiy.tmpPass)){
			nameValuePairs.add(new BasicNameValuePair("password", user.getPass()+""));
			Log.e("PAssword", user.getPassword()+""+user.getPass());
	
		}
        nameValuePairs.add(new BasicNameValuePair("phone", user.getPhone()));
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
        Log.e("Customer",result+" Check");
	    
	}
    catch(Exception e)
	{
    	return false;//Connection problem
	}
	try {
		JSONObject obj=new JSONObject(result);
		if(obj.getInt("code")==1){
			this.user.setPassword(user.getPass());
			this.user.setEmailAddress(user.getEmailAddress());
			this.user.setPhone(user.getPhone());
			return true;
		}
		return false;
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
	}
}
/**
 * getDateTables() brings empty tables from database according to date.It returns ArrayList
 * @pre date!=null
 * @param date
 * @return
 */
public ArrayList<Table> getDateTables(Date date){
	InputStream is;
	String line;
	String result;
	
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/selectDateTable.php");
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("date",date.toString()));
		Log.e("Print Date", date.toString());
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
        Log.e("Customer",result+" Check");
	    
	}
    catch(Exception e)
	{
    	return null;//Connection problem
	}
	try {
		ArrayList<Table> listTables=new ArrayList<Table>();
		JSONArray jsonarray=new JSONArray(result);
		for(int i=0;i<jsonarray.length();i++){
			JSONObject jobj=(JSONObject) jsonarray.get(i);
			Table table=new Table(jobj.getInt("id"), jobj.getString("description"), jobj.getString("name"));
			listTables.add(table);
		}
		return listTables;
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
}
/**
 * createReservation create new reservation in database.If it fails returns 0, if it successes returns 1
 * @pre reservation!=null
 * @param reservation
 * @return
 */
public int createReservation(Reservation reservation){
	InputStream is;
	String line;
	String result;
	
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/insertReservation.php");
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("date",reservation.getDate().getDate().toString()));
		nameValuePairs.add(new BasicNameValuePair("id",reservation.getTable().getTableID()+""));
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response=SessionManager.getHttpClient().execute(httpPost,SessionManager.getLocalContext());
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
        Log.e("Customer",result+" Check");
	    
	}
    catch(Exception e)
	{
    	return -1;//Connection problem
	}
	try {
		JSONObject obj=new JSONObject(result);
        return obj.getInt("code");
        
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return -1;
	}

}
public boolean selectReservations(){
	InputStream is;
	String line;
	String result;
	
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/selectUserReservation.php");
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
        Log.e("Customer",result+" Check ReservationCheck");
	    
	}
    catch(Exception e)
	{
    	return false;//Connection problem
	}
	try {
		ArrayList<Reservation> reservation=new ArrayList<Reservation>();
		JSONObject obj=new JSONObject(result);
		if(!obj.isNull("reservation")){
			JSONArray array=obj.getJSONArray("reservation");
			for(int i=0;i<array.length();i++){
				JSONObject jobj=array.getJSONObject(i);
				Log.e("Date", jobj.getString("date")+"");
				Reservation res=new Reservation( new Table(jobj.getInt("idtable"), jobj.getString("tdescription"), jobj.getString("tname")), new RestaurantDate(jobj.getString("date")),jobj.getInt("id"));
				reservation.add(res);
			
			}
		}
		getUser().setReservations(reservation);
		return true;
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return false;
}
/**
 * deleteReservation() deletes reservation of the user.If it successes returns 1, if it fails returns 0.
 * @return
 */
public boolean deleteReservation(int id){
	InputStream is;
	String line;
	String result;
	
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/deleteReservation.php");
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id",id+""));
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
        Log.e("Customer",result+" Check ReservationCheck");
	    
	}
    catch(Exception e)
	{
    	return false;//Connection problem
	}
	try {
		JSONObject object=new JSONObject(result);
		if(object.getInt("code")==1){
			return true;
		}else{
			return false;
		}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return false;
}
//add getorder id
/**
 * getSelectableOrders() brings foods, drinks and menus from database.It returns object list.If it fails returns null.
 * @return
 */
public ArrayList<Object> getSelectableOrders(){
	InputStream is;
	String line;
	String result;
	
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/selectItems.php");
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
        Log.e("Customer",result+" Check ReservationCheck");
	    
	}
    catch(Exception e)
	{
    	return null;//Connection problem
	}
	try {
		ArrayList<Object> list=new ArrayList<Object>();
		JSONObject jobj=new JSONObject(result);
		JSONArray arrayDrink=jobj.getJSONArray("drink");
		for(int i=0;i<arrayDrink.length();i++){
			JSONObject drinkobj=arrayDrink.getJSONObject(i);
			Drink drink=new Drink(drinkobj.getInt("id"),drinkobj.getString("name"));
			list.add(drink);
		}
		JSONArray arrayFood=jobj.getJSONArray("food");
		for(int i=0;i<arrayFood.length();i++){
			JSONObject foodobj=arrayFood.getJSONObject(i);
			Food food=new Food(foodobj.getInt("id"), foodobj.getString("name"));
			list.add(food);
		}
		JSONArray arrayMenu=jobj.getJSONArray("menu");
		for(int i=0;i<arrayMenu.length();i++){
			JSONObject menuobj=arrayMenu.getJSONObject(i);
			Menu menu=new Menu(menuobj.getInt("id"), menuobj.getString("description"), menuobj.getString("name"));
			list.add(menu);
		}
		return list;
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return null;
}
/**
 * getUserOrders brings user selected food, drinks and menus on order from database.It returns object list.If it fails returns null.
 * @return
 */
public ArrayList<OrderItem> getUserOrders(int id){
	InputStream is;
	String line;
	String result;
	
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/selectOrders.php");
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		Log.e("Customer",id+" reservation id");
		nameValuePairs.add(new BasicNameValuePair("reservationid",id+""));
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
        Log.e("Customer",result+" Check ReservationCheck");
	    
	}
    catch(Exception e)
	{
    	return null;//Connection problem
	}
	try {
		ArrayList<OrderItem> list=new ArrayList<OrderItem>();
		JSONObject jobj=new JSONObject(result);
		getUser().setOrder(new Order(jobj.getInt("orders")));
		Log.e("ORDERID", jobj.getInt("orders")+"");
		if(!jobj.isNull("drink")){
			JSONArray arrayDrink=jobj.getJSONArray("drink");
			for(int i=0;i<arrayDrink.length();i++){
				JSONObject obj=arrayDrink.getJSONObject(i);
				boolean check=true;
				for(int j=0;j<list.size();j++){
					if(list.get(j).getObj() instanceof Drink){
						Drink drink=(Drink)list.get(j).getObj();
						if(drink.getDrinkID()==obj.getInt("id")){
							list.get(j).add();
							check=false;
						}
					}
				}
				if(check){
					Drink drink=new Drink(obj.getInt("id"), obj.getString("name"));
					OrderItem item=new OrderItem(drink);
					list.add(item);
				}
			}
		}
		if(!jobj.isNull("food")){
			JSONArray arrayFood=jobj.getJSONArray("food");
			for(int i=0;i<arrayFood.length();i++){
				JSONObject obj=arrayFood.getJSONObject(i);
				boolean check=true;
				for(int j=0;j<list.size();j++){
					if(list.get(j).getObj() instanceof Food){
						Food food=(Food)list.get(j).getObj();
						if(food.getFoodID()==obj.getInt("id")){
							list.get(j).add();
							check=false;
						}
					}
				}
				if(check){
					Food food=new Food(obj.getInt("id"), obj.getString("name"));
					OrderItem item=new OrderItem(food);
					list.add(item);
				}
			}
		}
		if(!jobj.isNull("menu")){
			JSONArray arrayMenu=jobj.getJSONArray("menu");
			for(int i=0;i<arrayMenu.length();i++){
				JSONObject obj=arrayMenu.getJSONObject(i);
				boolean check=true;
				for(int j=0;j<list.size();j++){
					if(list.get(j).getObj() instanceof Menu){
						Menu menu=(Menu)list.get(j).getObj();
						if(menu.getMenuID()==obj.getInt("id")){
							list.get(j).add();
							check=false;
						}
					}
				}
				if(check){
					Menu menu=new Menu(obj.getInt("id"), obj.getString("description"), obj.getString("name"));
					OrderItem item=new OrderItem(menu);
					list.add(item);
				}
			}
		}
		return list;
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return null;
}
public boolean InsertOrders(ArrayList<OrderItem> list,int id){
	InputStream is;
	String line;
	String result;
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/insertItems.php");
		JSONObject json=new JSONObject();
		JSONArray arrayMenu=new JSONArray();
		for(int i=0;i<list.size();i++){
			if(list.get(i).getObj() instanceof Menu){
			  Menu menu =(Menu)list.get(i).getObj();
			  for(int j=0;j<list.get(i).getCounter();j++){
				  arrayMenu.put(new JSONObject().put("id", menu.getMenuID()));  
			  }
			  
			}
		}
		json.put("menu", arrayMenu);
		JSONArray arrayDrink=new JSONArray();
		for(int i=0;i<list.size();i++){
			if(list.get(i).getObj() instanceof Drink){
				Drink drink=(Drink)list.get(i).getObj();
				for(int j=0;j<list.get(i).getCounter();j++){
					arrayDrink.put(new JSONObject().put("id", drink.getDrinkID()));	
				}
				
			}
		}
		json.put("drink", arrayDrink);
		JSONArray arrayFood=new JSONArray();
		for(int i=0;i<list.size();i++){
			if(list.get(i).getObj() instanceof Food){
				Food food=(Food)list.get(i).getObj();
				for(int j=0;j<list.get(i).getCounter();j++){
					arrayFood.put(new JSONObject().put("id", food.getFoodID()));
				}
				
			}
		}
		json.put("food", arrayFood);
		json.put("id", id);
		httpPost.setHeader("json", json.toString());
		StringEntity se =new StringEntity(json.toString());
		se.setContentEncoding((Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		Log.e("Print", json.toString());
		httpPost.setEntity(se);
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
        Log.e("Customer",result+" Check ReservationCheck");
	    
	}
    catch(Exception e)
	{
    	return false;//Connection problem
	}
	try {
		JSONObject object=new JSONObject(result);
		if(object.getInt("code")==1){
			return true;
		}else{
			return false;
		}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return false;
}
/**
 * InsertReservation inserts selected foods,drinks and menus to database.If all items are inserted returns 1 if it fails return 0.
 * @pre obj!=null
 * @param obj
 * @return
 */
public boolean InsertReservation(ArrayList<Object> obj){
	InputStream is;
	String line;
	String result;
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/insertItems.php");
		JSONObject json=new JSONObject();
		json.put("id", getUser().getOrder().getOrderID());
		JSONArray arrayMenu=new JSONArray();
		for(int i=0;i<obj.size();i++){
			if(obj.get(i) instanceof Menu){
			  Menu menu =(Menu)obj.get(i);
			  arrayMenu.put(new JSONObject().put("id", menu.getMenuID()));
			}
		}
		json.put("menu", arrayMenu);
		JSONArray arrayDrink=new JSONArray();
		for(int i=0;i<obj.size();i++){
			if(obj.get(i) instanceof Drink){
				Drink drink=(Drink)obj.get(i);
				arrayDrink.put(new JSONObject().put("id", drink.getDrinkID()));
			}
		}
		json.put("drink", arrayDrink);
		JSONArray arrayFood=new JSONArray();
		for(int i=0;i<obj.size();i++){
			if(obj.get(i) instanceof Food){
				Food food=(Food)obj.get(i);
				arrayFood.put(new JSONObject().put("id", food.getFoodID()));
			}
		}
		json.put("food", arrayFood);
		httpPost.setHeader("json", json.toString());
		StringEntity se =new StringEntity(json.toString());
		se.setContentEncoding((Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		Log.e("Print", json.toString());
		Log.e("Size", obj.size()+"");
		httpPost.setEntity(se);
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
        Log.e("Customer",result+" Check ReservationCheck");
	    
	}
    catch(Exception e)
	{
    	return false;//Connection problem
	}
	try {
		JSONObject object=new JSONObject(result);
		if(object.getInt("code")==1){
			return true;
		}else{
			return false;
		}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return false;
}
public void setUser(User user) {
	this.user = user;
}
/**
 * InsertComment inserts review of the user to database.If insertion is succeed returns 1, if it fails returns 0.
 * @pre review!=null
 * @pre review.length()>0
 * @param review
 * @return booleanStatement
 */
public boolean InsertComment(String review){
	InputStream is;
	String line;
	String result;
	
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/insertComment.php");
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("comment",review));
		nameValuePairs.add(new BasicNameValuePair("date", new Date(new java.util.Date().getTime()).toString()));
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
        Log.e("Customer",result+" Check ReservationCheck");
	    
	}
    catch(Exception e)
	{
    	return false;//Connection problem
	}
	try {
		JSONObject object=new JSONObject(result);
		if(object.getInt("code")==1){
			return true;
		}else{
			return false;
		}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return false;
}
/**
 * isReservationPass checks if the given reservation's date is passed.If it is true returns 1 if it is not true returns false.
 * @return
 */
public boolean isReservationPass(){
	InputStream is;
	String line;
	String result;
	
	try {
		HttpPost httpPost=new HttpPost("http://yeicmobil.com/android/isReservationDatePass.php");
		ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("date", new Date(new java.util.Date().getTime()).toString()));
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
        Log.e("Customer",result+" Check ReservationCheck");
	    
	}
    catch(Exception e)
	{
    	return false;//Connection problem
	}
	try {
		JSONObject object=new JSONObject(result);
		if(object.getInt("code")==1){
			return true;
		}else{
			return false;
		}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return false;
}

}
