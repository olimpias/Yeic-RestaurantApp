package com.Yeic.Equipments;



import android.annotation.SuppressLint;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@SuppressLint("SimpleDateFormat")
/**
 * 
 * RestaurantDate is special class for using sql.date and util.date.
 *
 */
public class RestaurantDate {
	/**
	 * Invariants:
	 * date needs to be assign when RestaurantDate created.
	 * @invariant date!=null
	 * 
	 */
private Date date;
public RestaurantDate(String date) {
	DateToString(date);
}
public RestaurantDate(Date date) {
	this.date=date;
}
/**
 * StringToDate operation is coverting date type to String with in dd/MM/yyyy format.
 * @return
 */
public  String StringToDate(){
	java.text.DateFormat df = new SimpleDateFormat("dd/MM/yyyy");  
	String text = df.format(date); 
    return text;
}
/**
 * DateToString() operation is converting string to date and setting on this.date.
 * @pre text!=null and text need to be date
 * @param text
 */
public  void DateToString(String text) {
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date parsed;
	try {
		parsed = (java.util.Date) format.parse(text);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		parsed=new java.util.Date();
	}
	
    java.sql.Date sql = new java.sql.Date(parsed.getTime());
    this.date=sql;
}
public Date getDate() {
	return date;
}
}
