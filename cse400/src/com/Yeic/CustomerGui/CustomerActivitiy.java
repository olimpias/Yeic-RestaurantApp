package com.Yeic.CustomerGui;

import java.util.ArrayList;
import java.util.List;

import com.Yeic.Database.DatabaseConnection;
import com.Yeic.Database.DatabaseCustomerConnection;
import com.Yeic.Equipments.Reservation;
import com.Yeic.GuiAdapters.CommentAdaptor;
import com.Yeic.GuiAdapters.NavigatorListViewAdaptor;
import com.Yeic.GuiAdapters.CustomerReservationAdaptor;
import com.Yeic.GuiItem.MyProgressDialog;
import com.Yeic.GuiItem.RowNagivatorBlock;
import com.Yeic.HTTP.SessionManager;
import com.Yeic.Items.Comment;
import com.Yeic.LoginGui.LoginActivity;
import com.Yeic.Users.Customer;
import com.Yeic.Users.User;
import com.Yeic.cse400.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
@SuppressLint("ValidFragment")
/**
 * This class is customer main page.
 */
public class CustomerActivitiy extends Activity {
	private static final String [] listOfblocksTitle={"YeiC","Reservation","Profil"};
	private static final int [] listOfImage={R.drawable.yeiclogotransparentsmall,R.drawable.reservationsmall,R.drawable.adduser50};
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private List<RowNagivatorBlock> rowNagivatorBlock; 
	private ActionBarDrawerToggle actionDrawer;
	private DatabaseCustomerConnection databaseConnection;
	public static final String tmpPass="123456789";
	public static ArrayList<Object> orderList;
	protected final static int ORDERMADE=2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		databaseConnection=new DatabaseCustomerConnection(getIntent().getStringExtra(LoginActivity.USERNAME));
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
	        	fragment=new ProfilFragment();
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
	/**
	 * This class includes manifest of Yeic and Comments
	 *
	 *
	 */
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
			new RefreshComment().execute();
			return view;
		}
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		private class RefreshComment extends AsyncTask<Void, Void, Boolean>{
			private MyProgressDialog dialog;
			@Override
			protected void onPreExecute() {
				dialog=new MyProgressDialog(CustomerActivitiy.this);
				dialog.show();
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
	}
	/**
	 * This class is for making reservation or canceling it.
	 * 
	 *
	 */
	private class ReservationFragment extends Fragment{
		
		    private Button newRevervationButton;
		    private ListView listview;
		    private ArrayList<Reservation> reservation;
		    private CustomerReservationAdaptor reservationadaptor;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) { 
				View view=inflater.inflate(R.layout.reservation_layout, container, false);
				listview=(ListView)view.findViewById(R.id.reservationListView);
				newRevervationButton=(Button)view.findViewById(R.id.newReservationButton);

				//listview.setOnItemLongClickListener(longClickListener);
		        newRevervationButton.setOnClickListener(NewReservationListener);
		        new SelectReservations().execute();
		        new IsReservationDatePass().execute();
			return view;
		}
		private OnClickListener NewReservationListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(CustomerActivitiy.this, NewReservation.class);
				intent.putExtra(LoginActivity.USERNAME, databaseConnection.getUser().getUsername());
				startActivityFromFragment(ReservationFragment.this, intent, 2);
				
			}
		};
		/*private OnItemLongClickListener longClickListener=new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				//DeleteMessage();
				return false;
			}
		};*/
		private class SelectReservations extends AsyncTask<Void, Void, Boolean>{
			private MyProgressDialog dialog;
			@Override
			protected void onPreExecute() {
				dialog=new MyProgressDialog(CustomerActivitiy.this);
				dialog.show();
				super.onPreExecute();
			}   
  
			@Override
			protected Boolean doInBackground(Void... params) {
				//reservation=databaseConnection.isReservationExits()
				return databaseConnection.selectReservations();
			}
			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				
				if(result){
					reservation=databaseConnection.getUser().getReservations();
					reservationadaptor=new CustomerReservationAdaptor(reservation, getActivity(),databaseConnection);
					listview.setAdapter(reservationadaptor);
				}else{
					InformMessage("ERROR", "ERROR");
				}
				dialog.dismiss();
				super.onPostExecute(result);
			}
			
		}
        @Override
        public void onActivityResult(int requestCode, int resultCode,
        	Intent data) {
        	Log.e("OnResult", "Activity");
                if(resultCode==RESULT_OK){
                	new SelectReservations().execute();
                }

        }
		

		/**
		 * AddComment() pops up dialog which allow user to make comment
		 */
        private class IsReservationDatePass extends AsyncTask<Void, Void, Boolean>{
        	private MyProgressDialog dialog;
			@Override
			protected void onPreExecute() {
				dialog=new MyProgressDialog(CustomerActivitiy.this);
				dialog.show();
				super.onPreExecute();
			}
			@Override
			protected Boolean doInBackground(Void... params) {
				
				return databaseConnection.isReservationPass();
			}
			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(result){
					AddComment();
				}
				super.onPostExecute(result);
			}
        	
        }
		private void AddComment(){
			AlertDialog.Builder alertDialog=new AlertDialog.Builder(CustomerActivitiy.this);
			alertDialog.setMessage(R.string.COMMENT);
			final EditText input=new EditText(CustomerActivitiy.this);
			alertDialog.setView(input);
			alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					new InsertComment().execute(input.getText().toString());
					dialog.dismiss();
				}
			});
			alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					
				}
			});
			alertDialog.show();
		}
		/**
		 * 
		 * This class is searching for the user have reservation or not.It is invoking isReservationPass() method in DatabaseCustomerConnection.
		 *
		 */

		/**
		 * 
		 * This class is invoking insertComment() method in databaseConnection.
		 *@pre params[0]!=null 
		 *@pre params[0].length()>0
		 */
		private class InsertComment extends AsyncTask<String, Void, Boolean>{
			private MyProgressDialog dialog;
			@Override
			protected void onPreExecute() {
				dialog=new MyProgressDialog(CustomerActivitiy.this);
				dialog.show();
				super.onPreExecute();
			}   
			@Override
			protected Boolean doInBackground(String... params) {
				// TODO Auto-generated method stub
				return databaseConnection.InsertComment(params[0]);
			}
			@Override
			protected void onPostExecute(Boolean result) {
				dialog.dismiss();
				if(result){
					InformMessage("Your comment has added Thank you", "Yeic Restaurant");
				}else{
					InformMessage("Error Occured", "Error");
				}
				super.onPostExecute(result);
			}
		}
		
		/**
		 * ConfirmMessage() forwards to order fragment, If user clicks yes.
		 */
	}
	/**
	 *
	 * ErrorMessage() warns user about what goes wrong in background.
	 * @pre message!=null
	 * @pre title!=null
	 * @param message
	 * @param title
	 */
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
	/**
	 * This class is used for making order or editing order.
	 * 
	 *
	 */
	/**
	 * This class is used for editing profil.
	 * 
	 *
	 */
	private class ProfilFragment extends Fragment{
		private EditText usernameText;
		private EditText passwordText;
		private EditText nameText;
		private EditText surnameText;
		private EditText phoneText;
		private EditText emailText;
		private Button button;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view=inflater.inflate(R.layout.activity_add_chef, container, false);
			usernameText=(EditText)view.findViewById(R.id.chefUsername);
			passwordText=(EditText)view.findViewById(R.id.chefPassword1);
			nameText=(EditText)view.findViewById(R.id.chefName);
			surnameText=(EditText)view.findViewById(R.id.chefSurname);
			phoneText=(EditText)view.findViewById(R.id.chefPhone);
			emailText=(EditText)view.findViewById(R.id.chefEmail);
			button=(Button)view.findViewById(R.id.chefButton);
			button.setOnClickListener(editbutton);
			button.setText("Edit Profile");
			new RefreshUser().execute();
			return view;
		}
		/**
		 * updateViews() is refresh user informations on the profilFragment
		 * @pre databaseConnection.getUser().getUsername()!=null
		 * @pre databaseConnection.getUser().getName()!=null
		 * @pre databaseConnection.getUser().getSurname()!=null
		 * @pre databaseConnection.getUser().getPhone()!=null
		 * @pre databaseConnection.getUser().getEmailAddress()!=null
		 */
		public void updateViews(){
			usernameText.setText(databaseConnection.getUser().getUsername());
			usernameText.setEnabled(false);
			passwordText.setText(tmpPass);
			nameText.setText(databaseConnection.getUser().getName());
			nameText.setEnabled(false);
			surnameText.setText(databaseConnection.getUser().getSurname());
			surnameText.setEnabled(false);
			phoneText.setText(databaseConnection.getUser().getPhone());
			emailText.setText(databaseConnection.getUser().getEmailAddress());
			emailText.setEnabled(false);
		}
		/**
		 * 
		 *
		 */
		private class RefreshUser extends AsyncTask<Void, Void, Boolean>{
			private MyProgressDialog dialog;
			@Override
			protected void onPreExecute() {
				dialog=new MyProgressDialog(CustomerActivitiy.this);
				dialog.show();
				super.onPreExecute();
			}
			@Override
			protected Boolean doInBackground(Void... params) {
				
				return databaseConnection.getProfil();
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if(result){
					updateViews();
					
				}
				dialog.dismiss();
				super.onPostExecute(result);
			}
		}
		/**
		 * 
		 * 
		 *
		 */
		private class UpdateUser extends AsyncTask<Void, Void, Boolean>{
			private MyProgressDialog dialog;
            @Override
            protected void onPreExecute() {
            	dialog=new MyProgressDialog(CustomerActivitiy.this);
				dialog.show();
            	super.onPreExecute();
            }
			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub
				User user=new Customer(usernameText.getText().toString(), passwordText.getText().toString(), phoneText.getText().toString(), nameText.getText().toString(), surnameText.getText().toString(), emailText.getText().toString());
				return databaseConnection.updateProfil(user);
			}
			@Override
			protected void onPostExecute(Boolean result) {
				dialog.dismiss();
				if(result){
					updateViews();
					InformMessage("Editing your profil successfully done", "Information");
				}else{
					InformMessage("Error occured", "ERROR");
				}
				
				super.onPostExecute(result);
			}
			
		}
		private OnClickListener editbutton=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CloseKeyboard();
				String error="";
				if(passwordText.getText().toString().length()<6){
					error=error+" Password length must be longer than 6.";
				}
				if(phoneText.getText().toString().length()!=11){
					error=error+" Invalid phone number.";
				}
				if(error.equals("")){
					new UpdateUser().execute();	
				}else{
					InformMessage(error, "Inform");
				}
				
			}
		};
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
		Intent intent=new Intent(CustomerActivitiy.this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
		finish();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	public  void CloseKeyboard(){
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}

}
