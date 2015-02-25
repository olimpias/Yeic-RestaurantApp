package com.Yeic.RestaurantOwnerGui;
import java.util.ArrayList;
import java.util.List;

import com.Yeic.Database.DatabaseConnection;
import com.Yeic.Database.DatabaseRestaurantOwnerConnection;
import com.Yeic.Equipments.Reservation;
import com.Yeic.GuiAdapters.CommentAdaptor;
import com.Yeic.GuiAdapters.NavigatorListViewAdaptor;
import com.Yeic.GuiAdapters.ReservationAdaptor;
import com.Yeic.GuiAdapters.TableAdaptor;
import com.Yeic.GuiItem.RowNagivatorBlock;
import com.Yeic.HTTP.SessionManager;
import com.Yeic.Items.Comment;
import com.Yeic.Items.Table;
import com.Yeic.LoginGui.LoginActivity;
import com.Yeic.Users.Chef;
import com.Yeic.cse400.R;

import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
@SuppressLint("ValidFragment")
/**
 * 
 * This class is main interface of Restaurant Owner.
 *
 */
public class RestaurantOwnerActivity extends Activity {
	private static final String [] listOfblocksTitle={"YeiC","Reservation","Tables","Chef"};
	private static final int [] listOfImage={R.drawable.yeiclogotransparentsmall,R.drawable.reservationsmall,R.drawable.table50,R.drawable.cook50};
	protected static final int ADDTABLE=0;
	protected static final int EDITTABLE=1;
	
	protected static final int CHEFACTIVITY=10;
	protected static final int TABLEACTIVITY=20;
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private List<RowNagivatorBlock> rowNagivatorBlock; 
	private ActionBarDrawerToggle actionDrawer;
	private DatabaseRestaurantOwnerConnection databaseConnection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		databaseConnection=new DatabaseRestaurantOwnerConnection(getIntent().getStringExtra(LoginActivity.USERNAME));
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
	/**
	 * 
	 * This class is creating fragment according to selected in list view.
	 * 
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
				fragment=new ReservationFragment();
				break;
	        case 2:
		        fragment=new TableFragment();
		        break;
	        case 3:
		        fragment=new ChefFragment();
		        break;

			default:
				fragment=null;
				break;
				
			}
			Log.e("POsition", position+"");
			FragmentManager manager=getFragmentManager();
			manager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			drawerListView.setItemChecked(position, true);
			setTitle(listOfblocksTitle[position]);
			drawerLayout.closeDrawer(drawerListView);
		}
		
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
	 * Main fragment of the system
	 * 
	 *
	 */
	@SuppressLint("ValidFragment")
	private class YeicFragment extends Fragment{
		private ListView listview;
		private List<Comment> comments;
		private CommentAdaptor commentAdaptor;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			comments=new ArrayList<Comment>();
			View view=inflater.inflate(R.layout.yeicrestaurantlist_main, container, false);
			listview=(ListView)view.findViewById(R.id.listViewComments);
			commentAdaptor=new CommentAdaptor(comments, getActivity());
			listview.setAdapter(commentAdaptor);
			listview.setOnItemLongClickListener(longClickListener);
			new RefreshComment().execute();
			return view;
		}
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		/**
		 * This method is listing options about comment
		 * @param number
		 * 
		 */
		private  void ListDialog(final int number){
			AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
			builder.setItems(R.array.dialog_restaurantOwner_comment, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						WarningDialog(number);
						break;
                   default:
                   	
						break;
					
					}
				}
			});
			builder.show();
		}
		/**
		 * Gathering comments from database. 
		 *@post commentAdaptor.notifyDataSetChanged()
		 */
		private class RefreshComment extends AsyncTask<Void, Void, Boolean>{
			private ProgressDialog dialog;
			@Override
			protected void onPreExecute() {
				dialog=ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);
				super.onPreExecute();
			}
			@Override
			protected Boolean doInBackground(Void... params) {
				boolean result=false;
				result=databaseConnection.refreshComments();
				return result;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if(!result){
					databaseConnection.getUser().setCommentList(new ArrayList<Comment>());
				}
				comments=databaseConnection.getUser().getCommentList();
				commentAdaptor.setComments(comments);
				commentAdaptor.notifyDataSetChanged();
				dialog.dismiss();
				super.onPostExecute(result);
			}
			
		}
		/**
		 * delete comment from database.
		 *@post commentAdaptor.notifyDataSetChanged()
		 */
		private class DeleteComment extends AsyncTask<Comment, Void, Boolean>{
            private ProgressDialog dialog;
			@Override
			protected void onPreExecute() {
				dialog=ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);
				super.onPreExecute();
			}
			@Override
			protected Boolean doInBackground(Comment... params) {
				boolean result=false;
				if(databaseConnection.deleteComment(params[0])){
					result=databaseConnection.refreshComments();
				}
				return result;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if(!result){
					databaseConnection.getUser().setCommentList(new ArrayList<Comment>());
				}
				commentAdaptor.setComments(databaseConnection.getUser().getCommentList());
				commentAdaptor.notifyDataSetChanged();
				dialog.dismiss();
				super.onPostExecute(result);
			}
			
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
			 * WarningDialog is warning about deleting comment
			 * @param num
			 * @pre  num>0
			 * @pre num<comments.size()
			 * @post new RefreshComment()
			 */
		private void WarningDialog(final int num){
			 AlertDialog.Builder dialog=new Builder(RestaurantOwnerActivity.this);
			 dialog.setTitle("Information");
			 dialog.setMessage("Are you sure to delete "+comments.get(num).getName()+" "+comments.get(num).getSurname()+"'s comment ?");
			 dialog.setCancelable(false);
			 dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					new DeleteComment().execute(comments.get(num));
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
	}
	/**
	 * This class is interface of reservation view.
	 *
	 */
	private class ReservationFragment extends Fragment{
		private ListView listview;
		private List<Reservation> reservationList;
		private ReservationAdaptor reservationAdaptor;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			reservationList=new ArrayList<Reservation>();
			View view=inflater.inflate(R.layout.listview_layout, container, false);
			listview=(ListView)view.findViewById(R.id.ListViewItem);
			reservationAdaptor=new ReservationAdaptor(reservationList, getActivity());
			listview.setAdapter(reservationAdaptor);
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
		 * This class is invoking refreshReservation() method in DatabaseRestaurantOwnerConnection class.
		 *
		 */
		private class RefreshReservations extends AsyncTask<Void, Void, Boolean>{
            private ProgressDialog dialog;
            
			@Override
            protected void onPreExecute() {
            	dialog=new ProgressDialog(getActivity());
            	super.onPreExecute();
            }
			
			@Override
            protected void onPostExecute(Boolean result) {
            	if(!result){
            		databaseConnection.getUser().setReservationList(new ArrayList<Reservation>());
            	}
            	reservationAdaptor.setReservations(databaseConnection.getUser().getReservationList());
            	reservationAdaptor.notifyDataSetChanged();
				dialog.dismiss();
            	super.onPostExecute(result);
            }
			@Override
			protected Boolean doInBackground(Void... params) {
				boolean result=false;
				result=databaseConnection.refreshReservations();
				return result;
			}
			
		}
	}
	/**
	 * This class is interface of table view.
	 *
	 */
	private class TableFragment extends Fragment{
		private ListView listview;
		private List<Table> tables;
		private TableAdaptor tableadaptor;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			tables=new ArrayList<Table>();
			View view=inflater.inflate(R.layout.listview_layout, container, false);
			listview=(ListView)view.findViewById(R.id.ListViewItem);
			tableadaptor=new TableAdaptor(tables, getActivity());
			listview.setAdapter(tableadaptor);
			listview.setOnItemLongClickListener(longClickListener);
			new RefreshTable().execute();
			return view;
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
				TableActivity(null);
				break;

			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}
		/**
		 * WarningDialog() operation is warning about deleting
		 * @pre num>0 
		 * @pre num<tables.length()
		 * @post tables.remove(num) or nothing
		 * @param num
		 */
		private void WarningDialog(final int num){
			 AlertDialog.Builder dialog=new Builder(RestaurantOwnerActivity.this);
			 dialog.setTitle("Information");
			 Log.e("ERRROR", tables.size()+"");
			 Log.e("ERROR",tableadaptor.getTables().size()+"");
			 Log.e("ERROR", databaseConnection.getUser().getTableList()+"");
			 dialog.setMessage("Are you sure to delete "+tables.get(num).getName()+" ?");
			 dialog.setCancelable(false);
			 dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					new DeleteTable().execute(tables.get(num));
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
			 * ListDialog() is listing selectable chooses for selected item on list view
			 * @pre num>0
			 * @pre num<tables.length()  
			 * @param number
			 */
			private  void ListDialog(final int number){
				AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
				builder.setItems(R.array.dialog_restaurantOwner_Table, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							TableActivity(null);
							break;
                      case 1:
                      	   TableActivity(tables.get(number));
							break;
                      case 2:
                    	  WarningDialog(number);
                    	  break;
						
						}
						
					}
				});
				builder.show();
			}
			/**
			 * TableActivity() is starting new activity according to table input.If table is null, creating table activity starts
			 * else edit table activity starts. 
			 * @param table
			 */
		private void TableActivity(Table table){
			Intent intent=new Intent(getActivity(), AddTableActivity.class);
			if(table==null){
				intent.putExtra(AddTableActivity.TABLESTATS, RestaurantOwnerActivity.ADDTABLE);
			}else{
				intent.putExtra(AddTableActivity.TABLESTATS, RestaurantOwnerActivity.EDITTABLE);
				intent.putExtra("id", table.getTableID());
				intent.putExtra("description", table.getDescription());
				intent.putExtra("name", table.getName());
			}
			startActivityFromFragment(this, intent, RestaurantOwnerActivity.TABLEACTIVITY);
		}
		/**
		 * This class is invoking deleteTable() method in DatabaseRestaurantOwnerConnection class.
		 * @pre params[0]!=null  
		 *
		 */
		private class DeleteTable extends AsyncTask<Table, Void, Boolean>{
            private ProgressDialog dialog;
			@Override
			protected void onPreExecute() {
				dialog=ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);
				super.onPreExecute();
			}
			@Override
			protected Boolean doInBackground(Table... params) {
				boolean result=false;
				if(databaseConnection.deleteTable(params[0])){
					result=databaseConnection.refreshTable();
				}
				return result;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if(!result){
				 databaseConnection.getUser().setTableList(new ArrayList<Table>());	
				}
				tables=databaseConnection.getUser().getTableList();
				tableadaptor.setTables(tables);
				tableadaptor.notifyDataSetChanged();
				dialog.dismiss();
				super.onPostExecute(result);
			}
			
		}
		/**
		 * This class is invoking getTableList() method in DatabaseRestaurantOwner class.
		 *@post tableadaptor.notifyDataSetChanged()  
		 */
		private class RefreshTable extends AsyncTask<Void, Void, Boolean>{
			private ProgressDialog dialog;
			@Override
			protected void onPreExecute() {
				dialog=ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);
				super.onPreExecute();
			}
			@Override
			protected Boolean doInBackground(Void... params) {
				boolean result=false;
				result=databaseConnection.refreshTable();
				return result;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if(result){
					tables=databaseConnection.getUser().getTableList();
				}else{
					databaseConnection.getUser().setTableList(new ArrayList<Table>());
				}
				tableadaptor.setTables(tables);
				tableadaptor.notifyDataSetChanged();
				dialog.dismiss();
				super.onPostExecute(result);
			}
		}
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			if(requestCode==RestaurantOwnerActivity.TABLEACTIVITY){
				if(resultCode==RESULT_OK){
					new RefreshTable().execute();
				}	
			}
			
		}
		
		}
	/**
	 * 
	 * This class is interface of Chef manage.
	 *
	 */
	private class ChefFragment extends Fragment{
		private TextView usernameText;
		private TextView passwordText;
		private TextView nameText;
		private TextView surnameText;
		private TextView phoneText;
		private TextView emailText;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view=inflater.inflate(R.layout.chef_view, container, false);
			usernameText=(TextView)view.findViewById(R.id.chefTextViewUsername);
			passwordText=(TextView)view.findViewById(R.id.chefTextViewPassword);
			nameText=(TextView)view.findViewById(R.id.chefTextViewName);
			surnameText=(TextView)view.findViewById(R.id.chefTextViewSurname);
			phoneText=(TextView)view.findViewById(R.id.chefTextViewPhone);
			emailText=(TextView)view.findViewById(R.id.chefTextViewEmail);
			new RefreshChef().execute();
			return view;
		}
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.AddItem:
				createChefActivity();
				break;

			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}
		/**
		 * 
		 * This class is invoking getChef() method in DatabaseRestaurantOwnerConnection.
		 *
		 */
		private class RefreshChef extends AsyncTask<Void, Void, Chef>{
			private ProgressDialog dialog;
            @Override
            protected void onPreExecute() {
            	dialog=ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);
            	super.onPreExecute();
            }
			@Override
			protected Chef doInBackground(Void... params) {
				return databaseConnection.getChef();
			}
			@Override
			protected void onPostExecute(Chef chef) {
				if(chef!=null){
			    	usernameText.setText(chef.getUsername().toString());
			    	usernameText.setEnabled(false);
			    	passwordText.setText(chef.getPassword()+"");
			    	nameText.setText(chef.getName().toString());
			    	surnameText.setText(chef.getSurname().toString());
			    	phoneText.setText(chef.getPhone().toString());
			    	emailText.setText(chef.getEmailAddress().toString());
			    	
			    }
				dialog.dismiss();
				super.onPostExecute(chef);
			}
			
		}
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			MenuInflater inflator=getMenuInflater();
			inflator.inflate(R.menu.addnew, menu);
		}
		/**
		 * createChefActivity() is creating new activity where user can change chef information.
		 * @post databaseConnection.addChef()
		 */
		public void createChefActivity(){
			Intent intent=new Intent(RestaurantOwnerActivity.this, AddChefActivity.class);
			ArrayList<CharSequence> array=new ArrayList<CharSequence>();
			if(usernameText.getText().toString().length()!=0){
				array.add(usernameText.getText().toString());
				array.add(passwordText.getText().toString());
				array.add(nameText.getText().toString());
				array.add(surnameText.getText().toString());
				array.add(phoneText.getText().toString());
				array.add(emailText.getText().toString());
			}
			intent.putCharSequenceArrayListExtra(AddChefActivity.CHEFARRAYLIST, array);
			startActivityFromFragment(this, intent, CHEFACTIVITY);
		}
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			 
			super.onActivityResult(requestCode, resultCode, data);
			if(requestCode==CHEFACTIVITY){
				Log.e("CHEF1", "CHEF1");
			if(resultCode==RESULT_OK){
				Log.e("CHEF2", "CHEF2");
				new RefreshChef().execute();
			}
			}
		   
		}
		
	}
	/**
	 * Logout() closes session and deleting log information about user who was log in.
	 * @pre databaseConnection.getUser().getUsername!=null
	 */
	private void LogOut(){
		SharedPreferences preference=getSharedPreferences(LoginActivity.file, Context.MODE_PRIVATE);
		Editor editor=preference.edit();
		editor.clear();
		editor.commit();
		DatabaseConnection.LogOut();
		SessionManager.logOut();
		Intent intent=new Intent(RestaurantOwnerActivity.this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		finish();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
