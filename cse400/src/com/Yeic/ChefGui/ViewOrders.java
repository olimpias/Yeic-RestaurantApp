package com.Yeic.ChefGui;

import java.util.ArrayList;
import java.util.TreeSet;

import com.Yeic.Database.DatabaseChefConnection;
import com.Yeic.GuiAdapters.OrderAdaptor;
import com.Yeic.GuiItem.MyProgressDialog;
import com.Yeic.GuiItem.OrderItem;
import com.Yeic.Items.Drink;
import com.Yeic.Items.Food;
import com.Yeic.Items.Menu;
import com.Yeic.cse400.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
/**
 * 
 * This class shows orders of the customer
 *
 */
public class ViewOrders extends Activity {
private int idreservation;
private DatabaseChefConnection databaseConnection;
private ArrayList<OrderItem> listorders;
private TreeSet<Integer> header;
private OrderAdaptor adaptor;
private ListView listView;
protected static final int VIEWORDER=2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_layout);
		listView=(ListView)findViewById(R.id.ListViewItem);
		databaseConnection=new DatabaseChefConnection();
		idreservation=getIntent().getIntExtra("ID", -1);
		adaptor=new OrderAdaptor(new ArrayList<OrderItem>(), ViewOrders.this, new TreeSet<Integer>());
		listView.setAdapter(adaptor);
		new GetAllItem().execute();
	}
	private void createData(ArrayList<Object> obj){
		header=new TreeSet<Integer>();
		listorders=new ArrayList<OrderItem>();
		boolean headerFood=true;
		boolean headerDrink=true;
		boolean headerMenu=true;
		int counter=0;
		for(int i=0;i<obj.size();i++){
			if(obj.get(i) instanceof Menu){
				Menu menu=(Menu)obj.get(i);
				if(headerMenu){
					OrderItem item=new OrderItem("Menu");
					header.add(counter);
					listorders.add(item);
					headerMenu=false;
					counter++;
				}
				boolean isExits=false;
				int index=-1;
				for(int j=0;j<listorders.size();j++){
					if( listorders.get(j).getObj() instanceof Menu){
						Menu menu1=(Menu)listorders.get(j).getObj();
						if(menu1.getMenuID()==menu.getMenuID()){
							isExits=true;
							index=j;
							break;
						}
					}
				}
				if(isExits){
					listorders.get(index).add();
				}else{
					OrderItem item=new OrderItem(menu);
					listorders.add(item);
					counter++;
				}
			}
			if(obj.get(i) instanceof Food){
				Food food=(Food)obj.get(i);
				if(headerFood){
					OrderItem item=new OrderItem("Food");
					header.add(counter);
					listorders.add(item);
					headerFood=false;
					counter++;
				}
				boolean isExits=false;
				int index=-1;
				for(int j=0;j<listorders.size();j++){
					if( listorders.get(j).getObj() instanceof Food){
						Food food1=(Food)listorders.get(j).getObj();
						if(food.getFoodID()==food1.getFoodID()){
							isExits=true;
							index=j;
							break;
						}
					}
				}
				if(isExits){
					listorders.get(index).add();
				}else{
					OrderItem item=new OrderItem(food);
					listorders.add(item);
					counter++;
				}
			}
			if(obj.get(i) instanceof Drink){
				Drink drink=(Drink)obj.get(i);
				if(headerDrink){
					OrderItem item=new OrderItem("Drink");
					header.add(counter);
					listorders.add(item);
					headerDrink=false;
					counter++;
				}
				boolean isExits=false;
				int index=-1;
				for(int j=0;j<listorders.size();j++){
					if( listorders.get(j).getObj() instanceof Drink){
						Drink drink1=(Drink)listorders.get(j).getObj();
						if(drink.getDrinkID()==drink1.getDrinkID()){
							isExits=true;
							index=j;
							break;
						}
					}
				}
				if(isExits){
					listorders.get(index).add();
				}else{
					OrderItem item=new OrderItem(drink);
					listorders.add(item);
					counter++;
				}
			}
		}
		if(headerDrink){
			OrderItem item=new OrderItem("Drink");
			header.add(counter);
			listorders.add(item);
			counter++;
		}
		if(headerFood){
			OrderItem item=new OrderItem("Food");
			header.add(counter);
			listorders.add(item);
			counter++;
		}
		if(headerMenu){
			OrderItem item=new OrderItem("Menu");
			header.add(counter);
			listorders.add(item);
		}
	}
	/**
	 * 
	 * This class is invoking OrderList() method in DatabaseChefConnection class.
	 * @pre idreservation>0
	 */
	private class GetAllItem extends AsyncTask<Void, Void, Boolean>{
		private MyProgressDialog dialog;
        @Override
        protected void onPreExecute() {
        	// TODO Auto-generated method stub
        	dialog=new MyProgressDialog(ViewOrders.this);
        	dialog.show();

        	super.onPreExecute();
        }
		@Override
		protected Boolean doInBackground(Void... params) {
			ArrayList<Object> listofObj=databaseConnection.OrderList(idreservation);
			if(listofObj!=null){
				createData(listofObj);
				return true;
			}return false;
			
		}
		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if(!result){
				ErrorMessage("ERROR OCCURED", "ERROR");
			}else{
				adaptor.setHeader(header);
				adaptor.setItemList(listorders);
				adaptor.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
		
	}
	@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			setResult(RESULT_OK);
			finish();
		}
	/**
	 * 
	 * ErrorMessage() operation helps user to know what is going wrong.
	 * @pre message!=null
	 * @pre message.length()>0
	 * @param message
	 */
	private void ErrorMessage(String message,String title){
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setCancelable(false);
		dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}


}
