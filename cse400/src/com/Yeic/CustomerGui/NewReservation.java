package com.Yeic.CustomerGui;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.Yeic.Database.DatabaseCustomerConnection;
import com.Yeic.Equipments.Reservation;
import com.Yeic.Equipments.RestaurantDate;
import com.Yeic.GuiAdapters.TableAdaptor;
import com.Yeic.GuiItem.MyProgressDialog;
import com.Yeic.Items.Table;
import com.Yeic.LoginGui.LoginActivity;
import com.Yeic.cse400.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;

public class NewReservation extends Activity {
	 private int myear;
	    private int mmonth;
	    private int mday;
	    private TableAdaptor tableAdaptor;
	    private Button repickDate;
	    private ListView listview;
	    private ArrayList<Table> tablelist;
	    private int id;
	    public static final String ID="ID";
	    private DatabaseCustomerConnection databaseConnection;
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	// TODO Auto-generated method stub
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.activity_new_reservation);
	    	tablelist=new ArrayList<Table>();
	    	tableAdaptor=new TableAdaptor(tablelist, this);
	    	repickDate=(Button)findViewById(R.id.DatePickbutton);
	    	listview=(ListView)findViewById(R.id.TablelistView);
	    	repickDate.setOnClickListener(rePickDateListener);
	    	listview.setAdapter(tableAdaptor);
	    	databaseConnection=new DatabaseCustomerConnection(getIntent().getStringExtra(LoginActivity.USERNAME));
	    	listview.setOnItemClickListener(onitemClick);
	    	DatePicker();
	    }
	    private OnItemClickListener onitemClick=new OnItemClickListener() {
	    	@Override
	    	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	    			long arg3) {
	    		// TODO Auto-generated method stub
	    		new InsertReservation().execute(arg2);
	    	}
		};
	    private OnClickListener rePickDateListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePicker();
			}
		};
	    public void DatePicker(){
			final DatePickerDialog.OnDateSetListener datasetListener=new DatePickerDialog.OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO Auto-generated method stub
					mday=dayOfMonth;
					myear=year;
					mmonth=monthOfYear;
					Log.e("Message", " "+myear+" "+mmonth+" "+mday);
				}
			};
			final Calendar c = Calendar.getInstance();
	        myear  = c.get(Calendar.YEAR);
	        mmonth = c.get(Calendar.MONTH);
	        mday   = c.get(Calendar.DAY_OF_MONTH);
			final DatePickerDialog dpd=new DatePickerDialog(NewReservation.this	,datasetListener, myear, mmonth, mday);
			dpd.getDatePicker().setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
			dpd.setMessage("Pick date for reservation");
			dpd.setCancelable(false);
			dpd.setOnShowListener(new DialogInterface.OnShowListener() {
				
				@Override
				public void onShow(DialogInterface dialog) {
					// TODO Auto-generated method stub
					Button postivebutton=dpd.getButton(DialogInterface.BUTTON_POSITIVE);
					postivebutton.setText("Select");
					postivebutton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							GregorianCalendar today=new GregorianCalendar();
							GregorianCalendar selected=new GregorianCalendar();
							DatePicker picker=dpd.getDatePicker();
							myear=picker.getYear();
							mmonth=picker.getMonth();
							mday=picker.getDayOfMonth();
							selected.set(myear, mmonth, mday);
							if(selected.before(today)){
								Toast.makeText(NewReservation.this, "Choose after today or today", Toast.LENGTH_LONG).show();
							}else{
								new RepickTables().execute();
								dpd.dismiss();
							}
						}
					});
					Button negativeButton=dpd.getButton(DialogInterface.BUTTON_NEGATIVE);
					negativeButton.setText("cancel");
					negativeButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							finish();
							dpd.dismiss();
						}
					});
				}
			});
			dpd.setCanceledOnTouchOutside(true);
			dpd.show();
		}
	    private class InsertReservation extends AsyncTask<Integer, Void, Integer>{
	    	private MyProgressDialog dialog;
			@Override
            protected void onPreExecute() {
            	// TODO Auto-generated method stub
				dialog=new MyProgressDialog(NewReservation.this);
				dialog.show();
            	super.onPreExecute();
            }
			@Override
			protected Integer doInBackground(Integer... params) {
				// TODO Auto-generated method stub
				GregorianCalendar date=new GregorianCalendar();
				date.set(myear, mmonth, mday);
				Date datesql=new Date(date.getTimeInMillis());
				RestaurantDate reservation=new RestaurantDate(datesql);
				id=databaseConnection.createReservation(new Reservation(tablelist.get(params[0]), databaseConnection.getUser().getUsername(),reservation));
				return id;
			 
			}
			@Override
				protected void onPostExecute(Integer result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					dialog.dismiss();
					if(result>-1){
				        setResult(RESULT_OK);
						finish();
					}else if(result==-2){
						InformMessage("You have already reservation on this day.", "Yeic Restaurant");
					}else{
						InformMessage("Error occured", "ERROR");
					}
					
				}
	    	
	    }
	   /* private void MakeOrder(){
	    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
	    	builder.setTitle("Yeic Restaurant");
	    	builder.setMessage("Your reservation successfully done.Would you like to make order?");
	    	builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(NewReservation.this,MakeOrderActivity.class);
					intent.putExtra(ID, id);
					startActivity(intent);
				}
			});
	    	builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			});
	    	builder.setCancelable(false);
	    	builder.show();
	    }*/
	    private class RepickTables extends AsyncTask<Void, Void, ArrayList<Table>>{
			 private MyProgressDialog dialog;
				@Override
	            protected void onPreExecute() {
	            	// TODO Auto-generated method stub
					dialog=new MyProgressDialog(NewReservation.this);
					dialog.show();
	            	super.onPreExecute();
	            }
				@Override
				protected ArrayList<Table> doInBackground(Void... params) {
					GregorianCalendar date=new GregorianCalendar();
					date.set(myear, mmonth, mday);
					ArrayList<Table> tableList=databaseConnection.getDateTables(new Date(date.getTimeInMillis()));
					return tableList;
				}
				@Override
				protected void onPostExecute(ArrayList<Table> result) {
					
					if(result!=null){
						tableAdaptor.setTables(result);
						tablelist=result;
						tableAdaptor.notifyDataSetChanged();
					    if(result.size()==0){
								InformMessage("This day restaurant is full sorry","Yeic Restaurant");
						}
					}
	                
					dialog.dismiss();
					super.onPostExecute(result);
				}
			
		}
	    private void InformMessage(String message,String title){
			AlertDialog.Builder dialog=new AlertDialog.Builder(this);
			dialog.setTitle(title);
			dialog.setMessage(message);
			dialog.setCancelable(false);
			dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
					dialog.dismiss();
				}
			});
			dialog.show();
		}
	    


}
