package com.Yeic.ChefGui;

import java.util.ArrayList;
import java.util.List;

import com.Yeic.Database.DatabaseChefConnection;
import com.Yeic.Database.DatabaseConnection;
import com.Yeic.Equipments.Reservation;
import com.Yeic.GuiAdapters.DrinkAdaptor;
import com.Yeic.GuiAdapters.FoodAdaptor;
import com.Yeic.GuiAdapters.MenuAdaptor;
import com.Yeic.GuiAdapters.NavigatorListViewAdaptor;
import com.Yeic.GuiAdapters.ReservationAdaptor;
import com.Yeic.GuiItem.MyProgressDialog;
import com.Yeic.GuiItem.RowNagivatorBlock;
import com.Yeic.HTTP.SessionManager;
import com.Yeic.cse400.R;
import com.Yeic.Items.*;
import com.Yeic.LoginGui.LoginActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

@SuppressLint("ValidFragment")
/**
 * 
 * This class is Chef main page controller
 *
 */
public class ChefActivity extends Activity {
private static final String [] listOfblocksTitle={"YeiC","Food","Drink","Menu","Orders"};
private static final int [] listOfImage={R.drawable.yeiclogotransparentsmall,R.drawable.humburger,R.drawable.wine_bottle,R.drawable.food5,R.drawable.purchaseorder50};
private DrawerLayout drawerLayout;
private ListView drawerListView;
private List<RowNagivatorBlock> rowNagivatorBlock; 
private ActionBarDrawerToggle actionDrawer;
private DatabaseChefConnection databaseConnection;
private SharedPreferences preference;
private static final int REQUESTDRINKCODE=1;
private static final int REQUESTFOODCODE=2;
private static final int REQUESTMENUCODE=3;
	@SuppressLint("CommitTransaction")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		databaseConnection=new DatabaseChefConnection(getIntent().getStringExtra(LoginActivity.USERNAME));
		setContentView(R.layout.activity_mainpage);
		drawerLayout=(DrawerLayout)findViewById(R.id.layout);
		drawerListView=(ListView)findViewById(R.id.left_drawer);
		rowNagivatorBlock=new ArrayList<RowNagivatorBlock>();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		for(int i=0;i<listOfblocksTitle.length;i++){
			RowNagivatorBlock block=new RowNagivatorBlock(listOfImage[i], listOfblocksTitle[i]);
			rowNagivatorBlock.add(block);
		}
		NavigatorListViewAdaptor adaptor=new NavigatorListViewAdaptor(this,0,rowNagivatorBlock);
		drawerListView.setAdapter(adaptor);
		actionDrawer=new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){
			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);
			    invalidateOptionsMenu();
			}
			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(drawerView);
				invalidateOptionsMenu();
			}
			
		};
		drawerLayout.setDrawerListener(actionDrawer);
		drawerListView.setOnItemClickListener(new DrawerItemListener());
		FragmentTransaction tx=getFragmentManager().beginTransaction();
		tx.replace(R.id.content_frame, new YeicFragment()).commit();
		setTitle(listOfblocksTitle[0]);
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
	if(actionDrawer.onOptionsItemSelected(item))return true;
	if(item.getItemId()==R.id.action_logOut){
		LogOut();
		return true;
	}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
		protected void onPostCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onPostCreate(savedInstanceState);
		actionDrawer.syncState();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		actionDrawer.onConfigurationChanged(newConfig);
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		boolean drawerOpen=drawerLayout.isDrawerOpen(drawerListView);
		return drawerOpen;
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logout, menu);
		return true;
	}
	/**
	 * 
	 * this class is creating controller for fragments
	 *
	 */
	private class DrawerItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Selection(arg2);
		}
		 void Selection(int position){
			Fragment fragment;
			switch (position) {
			case 0:
				fragment=new YeicFragment();
				break;
	        case 1:
				fragment=new FoodFragment();
				break;
	        case 2:
		        fragment=new DrinkFragment();
		        break;
	        case 3:
		        fragment=new MenuFragment();
		        break;
	        case 4:
				fragment=new OrderFragment();
				break;

			default:
				fragment=null;
				break;
				
			}
			FragmentManager manager=getFragmentManager();
			manager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			drawerListView.setItemChecked(position, true);
			setTitle(listOfblocksTitle[position]);
			drawerLayout.closeDrawer(drawerListView);
		}
		
	}
	/**
	 * 
	 * This class is Menu fragment class where chef can view and changes about menu.
	 *
	 */
	private class MenuFragment extends Fragment{
		private List<com.Yeic.Items.Menu> menuList;
		private MenuAdaptor menuAdaptor;
		private ListView listView;
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			Log.e("LoadingNewITem", "wall1");
			if(requestCode==REQUESTMENUCODE){
				Log.e("LoadingNewITem", "wall2");
				if(resultCode==RESULT_OK){
					Log.e("LoadingNewITem", "wall3");
					new RefreshMenu().execute();	
				}
			}
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view=inflater.inflate(R.layout.listview_layout, container, false);
     	   menuList=new ArrayList<com.Yeic.Items.Menu>();//database;   
		    listView=(ListView)view.findViewById(R.id.ListViewItem);
			menuAdaptor=new MenuAdaptor(getActivity(), R.id.ListViewItem, menuList);
			listView.setAdapter(menuAdaptor);
			listView.setOnItemLongClickListener(longClickListener);
			new RefreshMenu().execute();
			return view;
		}
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			MenuInflater inflator=getMenuInflater();
			inflator.inflate(R.menu.addnew, menu);
		}
		/**
		 * WarningDialog() warns about deleting object.
		 * @pre num>-1 
		 * @pre num<menuList.size()
		 * @post menuList.remove(num)
		 * @post menuadaptor.notifiyChangedData()
		 * @param num
		 */
		private void WarningDialog(final int num){
			 AlertDialog.Builder dialog=new Builder(ChefActivity.this);
			 dialog.setTitle("Information");
			 dialog.setMessage("Are you sure to delete "+menuList.get(num).getName()+" ?");
			 dialog.setCancelable(false);
			 dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					new DeleteMenu().execute(num);
				}
			});
			 dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			});
			 dialog.show();
		 }
		 private OnItemLongClickListener longClickListener=new AdapterView.OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					ListDialog(arg2);
					return false;
				}
			};
			/**
			 * ListDialog() lists selectable chooses for selected row in list view.
			 * @pre number>-1
			 * @pre number<menuList.size()
			 * @param number
			 */
			private  void ListDialog(final int number){
				AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
				builder.setCancelable(true);
				builder.setItems(R.array.dialog_ChefItem, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							CreateItem(AddItemActivity.ITEMADDMENU, REQUESTMENUCODE,MenuFragment.this);
							break;
                       case 1:
                       	WarningDialog(number);
							break;
						
						}
						
					}
				});
				AlertDialog dialog=builder.create();
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
				
			}
			/**
			 * This class is invoking deleteMenu() method in DatabaseChefConnection class, refreshing arraylist and adaptor in the fragment class.
			 * @pre params[0]>0
			 */
		private class DeleteMenu extends AsyncTask<Integer, Void, Boolean>{
			MyProgressDialog dialog;
			@Override
           protected void onPreExecute() {
				dialog=new MyProgressDialog(ChefActivity.this);
	        	dialog.show();
           	super.onPreExecute();
           }
			@Override
			protected Boolean doInBackground(Integer... params) {
				boolean result=false;
				if(databaseConnection.deleteMenu(menuList.get(params[0])))
				result=databaseConnection.refreshMenu();
				return result;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if(!result){
                    databaseConnection.getUser().setMenus(new ArrayList<com.Yeic.Items.Menu>());
				}
				menuAdaptor.setMenuList(databaseConnection.getUser().getMenus());
		    	menuAdaptor.notifyDataSetChanged();	
				
				dialog.dismiss();
				super.onPostExecute(result);
			}
			
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.AddItem:
				CreateItem(AddItemActivity.ITEMADDMENU, REQUESTMENUCODE,MenuFragment.this);
				break;
			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}
		/**
		 * 
		 * This class is invoking refreshMenu method in DatabaseChefConnection after that it is refreshing arraylist and adaptor in the fragment class
		 *
		 */
		private class RefreshMenu extends AsyncTask<Void, Void, Boolean>{
	        private MyProgressDialog dialog;
	        @Override
	        protected void onPreExecute() {
	        	dialog=new MyProgressDialog(ChefActivity.this);
	        	dialog.show();

	        	super.onPreExecute();
	        }
			@Override
			protected Boolean doInBackground(Void... params) {
				
				  boolean	result=databaseConnection.refreshMenu();
					return result;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if(result){
			    	menuList=databaseConnection.getUser().getMenus();
			    	Log.e("DrinkList size", menuList.size()+" ");
			    	menuAdaptor.setMenuList(menuList);
			    	menuAdaptor.notifyDataSetChanged();
			    	
			    }
				dialog.dismiss();
				super.onPostExecute(result);
			}
			
			
		}
		//asyntask
	}
	/**
	 * 
	 * This class is the main page of the fragment.
	 *
	 */
	private class YeicFragment extends  Fragment{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View view=inflater.inflate(R.layout.yeicrestaurant_main, container, false);
			return view;
		}
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		
	}
	/**
	 * 
	 * This class is representing Food page.
	 *
	 */
	private class FoodFragment extends Fragment{
		private List<Food> foodList;
		private FoodAdaptor foodAdaptor;
		private ListView listView;
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			Log.e("LoadingNewITem", "wall1");
			if(requestCode==REQUESTFOODCODE){
				Log.e("LoadingNewITem", "wall2");
				if(resultCode==RESULT_OK){
					Log.e("LoadingNewITem", "wall3");
					new RefreshFood().execute();	
					
				}
			}
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View view=inflater.inflate(R.layout.listview_layout, container, false);
     	   foodList=new ArrayList<Food>();//database;   
		    listView=(ListView)view.findViewById(R.id.ListViewItem);
			foodAdaptor=new FoodAdaptor(getActivity(), R.id.ListViewItem, foodList);
			listView.setAdapter(foodAdaptor);
			listView.setOnItemLongClickListener(longClickListener);
			new RefreshFood().execute();
			return view;
		}
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			MenuInflater inflator=getMenuInflater();
			inflator.inflate(R.menu.addnew, menu);
		}
		/**
		 * WarningDialog() warns about deleting object.
		 * @pre num>-1
		 * @pre num<foodList.size()
		 * @post foodList.remove(num) 
		 * @post foodadaptor.notifiyChangedData()
		 * @param num
		 */
		private void WarningDialog(final int num){
			 AlertDialog.Builder dialog=new Builder(ChefActivity.this);
			 dialog.setTitle("Information");
			 dialog.setMessage("Are you sure to delete "+foodList.get(num).getName()+" ?");
			 dialog.setCancelable(false);
			 dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					new DeleteFood().execute(num);
				}
			});
			 dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			});
			 
			 dialog.show();
		 }
		 private OnItemLongClickListener longClickListener=new AdapterView.OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					ListDialog(arg2);
					return false;
				}
			};
			/**
			 * ListDialog() lists selectable chooses for selected row in list view.
			 * @pre number>-1 
			 * @pre number<foodList.size()
			 * @param number
			 */
			private  void ListDialog(final int number){
				AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
				builder.setCancelable(true);
				builder.setItems(R.array.dialog_ChefItem, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							CreateItem(AddItemActivity.ITEMADDFOOD, REQUESTFOODCODE,FoodFragment.this);
							break;
                       case 1:
                       	WarningDialog(number);
							break;
						
						}
						
					}
				});
				AlertDialog dialog=builder.create();
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
			/** 
			 * This class is invoking deleteFood method in DatabaseChefConnection
			 * @pre params[0]>0
			 */
		private class DeleteFood extends AsyncTask<Integer, Void, Boolean>{
			private MyProgressDialog dialog;
			@Override
            protected void onPreExecute() {
				dialog=new MyProgressDialog(ChefActivity.this);
	        	dialog.show();
            	super.onPreExecute();
            }
			@Override
			protected Boolean doInBackground(Integer... params) {
				boolean result=false;
				if(databaseConnection.deleteFood(foodList.get(params[0])))
				result=databaseConnection.refreshFood();
				return result;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if(!result){
					databaseConnection.getUser().setFoods(new ArrayList<Food>());
				}
				foodAdaptor.setFoodList(databaseConnection.getUser().getFoods());
		    	foodAdaptor.notifyDataSetChanged();	
				dialog.dismiss();
				super.onPostExecute(result);
			}
			
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.AddItem:
				CreateItem(AddItemActivity.ITEMADDFOOD, REQUESTFOODCODE,FoodFragment.this);
				break;
			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}
		/**
		 * 
		 * This class is invoking refreshFood method in DatabaseChefConnection class, refreshing arraylist and adaptor in the fragment class
		 * 
		 */
		private class RefreshFood extends AsyncTask<Void, Void, Boolean>{
	        private MyProgressDialog dialog;
	        @Override
	        protected void onPreExecute() {
	        	dialog=new MyProgressDialog(ChefActivity.this);
	        	dialog.show();
	        	super.onPreExecute();
	        }
			@Override
			protected Boolean doInBackground(Void... params) {
				  boolean	result=databaseConnection.refreshFood();
					return result;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if(result){
			    	foodList=databaseConnection.getUser().getFoods();
			    	Log.e("DrinkList size", foodList.size()+" ");
			    	foodAdaptor.setFoodList(foodList);
			    	foodAdaptor.notifyDataSetChanged();
			    	
			    }
				dialog.dismiss();
				super.onPostExecute(result);
			}
			
			
		}
		//asyntask
	}
	/**
	 * 
	 * This class is representing drink page.
	 *
	 */
	private class DrinkFragment extends  Fragment{
		private List<Drink> drinkList;
		private DrinkAdaptor drinkAdaptor;
		private ListView listView;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			Log.e("LoadingNewITem", "wall1");
			if(requestCode==REQUESTDRINKCODE){
				Log.e("LoadingNewITem", "wall2");
				if(resultCode==RESULT_OK){
					Log.e("LoadingNewITem", "wall3");
					new RefreshDrink().execute();	
					
				}
			}
		}
		 @Override
		 public View onCreateView(LayoutInflater inflater, ViewGroup container,
		   Bundle savedInstanceState) {
             View view=inflater.inflate(R.layout.listview_layout, container, false);
            	   drinkList=new ArrayList<Drink>();//database;   
			    listView=(ListView)view.findViewById(R.id.ListViewItem);
				drinkAdaptor=new DrinkAdaptor(getActivity(), R.id.ListViewItem, drinkList);
				listView.setAdapter(drinkAdaptor);
				listView.setOnItemLongClickListener(longClickListener);
				new RefreshDrink().execute();
		  return view;
		 }
		 /**
		  * @pre num >=0
		  *@pre num< drinkList.size()
		 * @post drinkList.remove(num)
		 * @post drinkadaptor.notifiyChangedData()
		  * @param num
		  */
		 private void WarningDialog(final int num){
			 AlertDialog.Builder dialog=new Builder(ChefActivity.this);
			 dialog.setTitle("Information");
			 dialog.setMessage("Are you sure to delete "+drinkList.get(num).getName()+" ?");
			 dialog.setCancelable(false);
			 dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					new DeleteDrink().execute(num);
				}
			});
			 dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			});
			 dialog.show();
		 }
		 private OnItemLongClickListener longClickListener=new AdapterView.OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					ListDialog(arg2);
					return false;
				}
			};
			/**
			 * ListDialog() lists selectable chooses for selected row in list view.
			 * @pre number>-1 
			 * @pre number<drinkList.size()
			 * @param number
			 */
			private  void ListDialog(final int number){
				AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
				builder.setCancelable(true);
				builder.setItems(R.array.dialog_ChefItem, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							CreateItem(AddItemActivity.ITEMADDDRINK, REQUESTDRINKCODE,DrinkFragment.this);
							break;
                        case 1:
                        	WarningDialog(number);
							break;
						
						}
						
					}
				});
				AlertDialog dialog=builder.create();
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
			/**
			 * ListDialog() lists selectable chooses for selected row in list view.
			 * @pre number>-1
			 * @pre number<foodList.size()
			 */
		private class DeleteDrink extends AsyncTask<Integer, Void, Boolean>{
			private MyProgressDialog dialog;
			@Override
            protected void onPreExecute() {
				dialog=new MyProgressDialog(ChefActivity.this);
	        	dialog.show();
            	super.onPreExecute();
            }
			@Override
			protected Boolean doInBackground(Integer... params) {
				boolean result=false;
				if(databaseConnection.deleteDrink(drinkList.get(params[0])))
				result=databaseConnection.refreshDrink();
				return result;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if(!result){				
					databaseConnection.getUser().setDrinks(new ArrayList<Drink>());	
				}
				drinkAdaptor.setDrinkList(databaseConnection.getUser().getDrinks());
		    	drinkAdaptor.notifyDataSetChanged();
				
				dialog.dismiss();
				super.onPostExecute(result);
			}
			
		}
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			MenuInflater inflator=getMenuInflater();
			inflator.inflate(R.menu.addnew, menu);
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.AddItem:
				CreateItem(AddItemActivity.ITEMADDDRINK, REQUESTDRINKCODE,DrinkFragment.this);
				break;
			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}
		/**
		 * 
		 * This class is invoking refreshDrink() method in DatabaseChefConnection class, , refreshing arraylist and adaptor in the fragment class.
		 *
		 */
		private class RefreshDrink extends AsyncTask<Void, Void, Boolean>{
	        private MyProgressDialog dialog;
	        @Override
	        protected void onPreExecute() {
	        	dialog=new MyProgressDialog(ChefActivity.this);
	        	dialog.show();

	        	super.onPreExecute();
	        }
			@Override
			protected Boolean doInBackground(Void... params) {
				  boolean	result=databaseConnection.refreshDrink();
					return result;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if(result){
			    	drinkList=databaseConnection.getUser().getDrinks();
			    	Log.e("DrinkList size", drinkList.size()+" ");
			    	drinkAdaptor.setDrinkList(drinkList);
			    	drinkAdaptor.notifyDataSetChanged();
			    	
			    }
				dialog.dismiss();
				super.onPostExecute(result);
			}
			
			
		}
	}
	/**
	 * 
	 * This class is representing Order page.
	 *
	 */
	@SuppressLint("ValidFragment")
	private class OrderFragment extends  Fragment{
		private List<Reservation> orderList;
		private ListView listview;
		private ReservationAdaptor reservationAdaptor;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			orderList=new ArrayList<Reservation>();
			View view=inflater.inflate(R.layout.listview_layout, container, false);
			listview=(ListView)view.findViewById(R.id.ListViewItem);
			reservationAdaptor=new ReservationAdaptor(orderList, getActivity());
			listview.setAdapter(reservationAdaptor);
			listview.setOnItemClickListener(listener);
			new RefreshReservations().execute();
			return view;
		}
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		/**
		 * 
		 * This class is invoking getOrderList() in DatabaseChefConnection class,, refreshing arraylist and adaptor in the fragment class
		 *
		 */
		private class RefreshReservations extends AsyncTask<Void, Void, Boolean>{
            private MyProgressDialog dialog;
			@Override
            protected void onPreExecute() {
				dialog=new MyProgressDialog(ChefActivity.this);
	        	dialog.show();
            	super.onPreExecute();
            }
			@Override
            protected void onPostExecute(Boolean result) {
            	if(!result){
            		orderList=new ArrayList<Reservation>();
            	}
            	reservationAdaptor.setReservations(orderList);
            	reservationAdaptor.notifyDataSetChanged();
				dialog.dismiss();
            	super.onPostExecute(result);
            }
			@Override
			protected Boolean doInBackground(Void... params) {
				
				orderList=databaseConnection.getOrderList();
				if(orderList!=null)return true;
				orderList=new ArrayList<Reservation>();
				return false;
			}
			
		}
		OnItemClickListener listener=new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ChefActivity.this, ViewOrders.class);
				intent.putExtra("ID", orderList.get(arg2).getReservationID());
				startActivityFromFragment(OrderFragment.this, intent, ViewOrders.VIEWORDER);
			}
		};
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			Log.e("ViewORder finish", "Finish");
			if(resultCode==RESULT_OK){
				FragmentTransaction tx=getFragmentManager().beginTransaction();
				tx.replace(R.id.content_frame, new OrderFragment()).commit();
				setTitle(listOfblocksTitle[4]);
				
			}
		};
	}
	/**
	 * CreateItem starts new activity to insert new food, drink or menu according to input type.
	 * @pre frag!=null
	 * @pre type!=null
	 * @param type
	 * @param requestCode
	 * @param frag
	 */
	private void CreateItem(String type,int requestCode,Fragment frag){
		Intent intent=new Intent(this, AddItemActivity.class);
		intent.putExtra(AddItemActivity.ITEMTYPE, type);
		startActivityFromFragment(frag, intent, requestCode);
	}
	/**
	 * Logout() closes session and deleting log information about user who was log in.
	 * @pre databaseConnection.getUser().getUsername!=null
	 */
	private void LogOut(){
		preference=getSharedPreferences(LoginActivity.file, Context.MODE_PRIVATE);
		Editor editor=preference.edit();
		editor.clear();
		editor.commit();
		DatabaseConnection.LogOut();
		SessionManager.logOut();
		Intent intent=new Intent(ChefActivity.this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		finish();
	}//logoutdatabase eklee....
	@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
		}
}
