package com.Yeic.LoginGui;


import java.util.Random;

import com.Yeic.ChefGui.ChefActivity;
import com.Yeic.CustomerGui.CustomerActivitiy;
import com.Yeic.Database.DatabaseConnection;
import com.Yeic.GuiItem.MyProgressDialog;
import com.Yeic.HTTP.SessionManager;
import com.Yeic.RestaurantOwnerGui.RestaurantOwnerActivity;
import com.Yeic.Users.Chef;
import com.Yeic.Users.Customer;
import com.Yeic.Users.RestaurantOwner;
import com.Yeic.Users.User;
import com.Yeic.cse400.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * 
 * This class is the enterance interface of the application.
 *
 */
@SuppressLint("InlinedApi")
public class LoginActivity extends Activity {
	private  final String name="username";
	public static final String USERNAME="USERNAME";
	private  final String password="password";
	private  final String usertype="usertype";
	public static final String file="Login";
	private Button loginButton;
	private Button newRegister;
	private EditText loginUsername;
	private EditText loginPassword;
	private MyProgressDialog progDialog;
	private CheckBox rememberMe;
	private SharedPreferences preference;
	private DatabaseConnection connection;
	private String email;
	private TextView forgotPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initilizeLoginPage();
	}
	/**
	 * initialize components on activity 
	 * 
	 */
	private void initilizeLoginPage(){
		forgotPassword=(TextView)findViewById(R.id.forgotPassword);
		forgotPassword.setPaintFlags(forgotPassword.getPaintFlags() |Paint.UNDERLINE_TEXT_FLAG);
		loginButton=(Button)findViewById(R.id.loginButton);
		newRegister=(Button)findViewById(R.id.signUpButton);
		loginUsername=(EditText)findViewById(R.id.loginUsername);
		loginPassword=(EditText)findViewById(R.id.loginPassword);
		rememberMe=(CheckBox)findViewById(R.id.rememberMeCheck);
		loginButton.setOnClickListener(loginButtonListener);
		newRegister.setOnClickListener(registerButtonListener);
		forgotPassword.setOnClickListener(forgotPasswordListener);
	}
	private OnClickListener forgotPasswordListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		   ForgotPasswordDialog();
		}
	};
	private void ForgotPassword(DialogInterface dialog){
		dialog.dismiss();
		if(email.length()!=0&&isEmailValid(email)){
			new SendEmail().execute(email);
		}else{
			ErrorMessage("Email is not valid", "Error");
		}
	}
	 private boolean isEmailValid(String email){
    	 String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    	 return email.matches(EMAIL_REGEX);
    }
	private void ForgotPasswordDialog(){
		AlertDialog.Builder alertDialog=new AlertDialog.Builder(LoginActivity.this);
		final EditText input=new EditText(LoginActivity.this);
		input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		alertDialog.setView(input);
		alertDialog.setMessage("Write your email.");
		alertDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				email=input.getText().toString();
				ForgotPassword(dialog);
			}
		});
		alertDialog.setCancelable(true);
		alertDialog.show();
	}
	private OnClickListener registerButtonListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(LoginActivity.this,NewRegistration.class);
			startActivityForResult(intent, 2);
		}
	};
	private class SendEmail extends AsyncTask<String, Void, Integer>{
		ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog = ProgressDialog.show(LoginActivity.this, "Loading...", "Please wait...", true);
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(String... params) {
			
			return DatabaseConnection.forgotPassword(passwordGenerator(), params[0]);
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			Log.e("ERROR", result+"Email sonuc");
			if(result==1){
				Dialog("Your new password successfully sent to you email", "Information");
			}else if(result==0){
				Dialog("Error Occured", "ERROR");
			}else if(result==-1){
				Dialog("Your email doesn't exist", "Information");
			}
			super.onPostExecute(result);
		}
	}
	private void Dialog(String message,String title){
		AlertDialog.Builder alertDialog=new AlertDialog.Builder(LoginActivity.this);
		alertDialog.setMessage(message);
		alertDialog.setTitle(title);
		alertDialog.setCancelable(true);
		alertDialog.setPositiveButton("Ok", null);
		alertDialog.show();
	}
	private String passwordGenerator(){
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();
		   StringBuilder sb = new StringBuilder( 8 );
		   for( int i = 0; i < 8; i++ ) 
		      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		   return sb.toString();
		
	}
	/**
	 *  This operation gather data from another activity 
	 *  @pre resultCode=1
	 *  @pre requestCode=2
	 *  
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==2){
			if(resultCode==RESULT_OK){
				String username=data.getExtras().getString("Username");
				loginUsername.setText(username);	
			}	
		}
		
		
	};
	private OnClickListener loginButtonListener=new OnClickListener() {
		/**This method is checking if user is valid and if user valid, it is finishing this activity and creating new activity which is main pages.
		 * @post new ChefActivity or new CustomerActivity or new RestaurantOwnerActivity
		 * 
		 */
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			/*progDialog=new ProgressDialog(LoginActivity.this);
			progDialog.setTitle("Please wait...");
			progDialog.setMessage("Logginning...");
			progDialog.setCancelable(false);
			progDialog.setProgress(ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			progDialog.show();*/
            CloseKeyboard();
			progDialog=new MyProgressDialog(LoginActivity.this);
			progDialog.show();
			Thread thread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					new ConnectionToNetwork().execute(new String[]{loginUsername.getText().toString(),loginPassword.getText().toString()});
				}
			});
			thread.start();
			/*Intent i = new Intent(LoginActivity.this, ChefActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra(USERNAME, "");
            startActivity(i);
            finish();*/
		}
			
		
	};
	private class ConnectionToNetwork extends AsyncTask<String, Void, User>{

		@Override
		protected User doInBackground(String... params) {
			String username=params[0];
			String password=params[1];
			connection=new DatabaseConnection();
			SessionManager.inilizeCookie();
			Log.e("Username", username+"name");
			Log.e("Password", password+"pass");
			return connection.Login(username, password);
		}
		@Override
		protected void onPostExecute(User result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(progDialog!=null)progDialog.dismiss();
			if(result==null){
				Toast.makeText(LoginActivity.this, "Password or Username Wrong", Toast.LENGTH_SHORT).show();
			}else{
				if(result instanceof Chef){
				SaveSharedPreference(loginUsername.getText().toString(),loginPassword.getText().toString().hashCode() , "Chef");
				Intent intent=new Intent(LoginActivity.this, ChefActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(USERNAME, result.getUsername());
				Log.e("ERROR", result.getUsername()+"");
				startActivity(intent);
				finish();
				}
				else if(result instanceof Customer){
				SaveSharedPreference(loginUsername.getText().toString(), loginPassword.getText().toString().hashCode(), "Customer");	
				Intent i = new Intent(LoginActivity.this, CustomerActivitiy.class);
	            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	            i.putExtra(USERNAME, result.getUsername());
	            startActivity(i);
	            finish();
				}
				else if(result instanceof RestaurantOwner){
				SaveSharedPreference(loginUsername.getText().toString(),loginPassword.getText().toString().hashCode() , "RestaurantOwner");
				Intent intent = new Intent(LoginActivity.this, RestaurantOwnerActivity.class);
	            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	            intent.putExtra(USERNAME, result.getUsername());
				Log.e("ERROR", result.getUsername()+"");
	            startActivity(intent);
	            finish();
				}
			}
		}
		
	}
	/**
	 * Saves user login information to shared preference to login automatically.
	 * @pre login process confirmation is not failed
	 * @post startActivity(intent)
	 * 
	 */
	public void SaveSharedPreference(String username,int password,String type){
		if(rememberMe.isChecked()){
			Editor editor=preference.edit();
			editor.putString(this.name, username);
			editor.putInt(this.password, password);
			editor.putString(this.usertype, type);
			editor.commit();	
		}
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		preference=getSharedPreferences(file, Context.MODE_PRIVATE);
		if(preference.contains(name)){
			if(preference.contains(password)){
				String type=preference.getString(usertype, null);
				String name=preference.getString(this.name, null);
				if(type.equals("Chef")){
					Intent intent=new Intent(LoginActivity.this, ChefActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra(USERNAME, name);
					Log.e("AUTO LOGIN", name);
					startActivity(intent);
					finish();
				}else if(type.equals("RestaurantOwner")){
					Intent intent = new Intent(LoginActivity.this, RestaurantOwnerActivity.class);
		            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		            intent.putExtra(USERNAME, name);
		            Log.e("AUTO LOGIN", name);
		            startActivity(intent);
		            finish();
				}else{
					Intent i = new Intent(LoginActivity.this, CustomerActivitiy.class);
		            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		            i.putExtra(USERNAME, name);
		            Log.e("AUTO LOGIN", name);
		            startActivity(i);
		            finish();
				}
			}
		}
		super.onResume();
	}
	public  void CloseKeyboard(){
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}
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
