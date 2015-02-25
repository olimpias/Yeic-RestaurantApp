package com.Yeic.CustomerGui;

import java.util.ArrayList;
import com.Yeic.Database.DatabaseCustomerConnection;
import com.Yeic.GuiAdapters.MakeOrderAdaptor;
import com.Yeic.GuiItem.MyProgressDialog;
import com.Yeic.GuiItem.OrderItem;
import com.Yeic.cse400.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
/**
 * This class is used for listing avaliable food,drink and menus
 * 
 *
 */
public class MakeOrderActivity extends Activity {
private DatabaseCustomerConnection databaseConnection;
private Button catalogButton;
private Button confirmButton;
private ListView listview;
public static MakeOrderAdaptor adaptor;
public static ArrayList<Object> objList;
public static ArrayList<OrderItem> listorder;
protected final static String USERNAME="USERNAME";
protected final static String CONDITION="SUCCUES";
public static int orderID;
protected final static int SUCCESS=2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.makeorderlist);
		databaseConnection=new DatabaseCustomerConnection(getIntent().getStringExtra(USERNAME));
		Log.e("ORDER ID", orderID+"");
		Log.e("Restart", "RestartERROR");
		adaptor=new MakeOrderAdaptor(listorder, this);
		catalogButton=(Button)findViewById(R.id.Foodbutton);
		catalogButton.setOnClickListener(foodButtonListener);
		confirmButton=(Button)findViewById(R.id.ConfirmButton);
		confirmButton.setOnClickListener(confirmButtonListener);
		listview=(ListView)findViewById(R.id.orderslistView);
		listview.setAdapter(adaptor);
		new GetItems().execute();
	}
    private OnClickListener foodButtonListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(MakeOrderActivity.this, OrderActivity.class);
			startActivity(intent);
		}
	};
	private OnClickListener confirmButtonListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new InsertOrders().execute();
		}
	};
	private class InsertOrders extends AsyncTask<Void, Void, Boolean>{
		private MyProgressDialog dialog;
		@Override
		protected void onPreExecute() {

			dialog=new MyProgressDialog(MakeOrderActivity.this);
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return databaseConnection.InsertOrders(listorder, orderID);
		}
		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if(result){
				ErrorMessage("You orders successfully added.", "Yeic Restaurant");
			}else{
				ErrorMessage("ERROR occured", "Yeic Restaurant");
			}
			
			
			super.onPostExecute(result);
		}
		
	}
	private class GetItems extends AsyncTask<Integer, Void, Boolean>{
		private MyProgressDialog dialog;
		@Override
		protected void onPreExecute() {

			dialog=new MyProgressDialog(MakeOrderActivity.this);
			dialog.show();
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(Integer... params) {
			MakeOrderActivity.objList=databaseConnection.getSelectableOrders();
			//get selected items
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
                
			dialog.dismiss();
			super.onPostExecute(result);
		}
	}
	 
	private void ErrorMessage(String message,String title){
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setCancelable(false);
		dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	@Override
		public void onBackPressed() {
		    setResult(RESULT_OK);
			finish();
		}

}
