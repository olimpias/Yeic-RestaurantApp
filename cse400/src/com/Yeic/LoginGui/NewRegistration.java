package com.Yeic.LoginGui;

import com.Yeic.Database.DatabaseConnection;
import com.Yeic.Users.Customer;
import com.Yeic.Users.User;
import com.Yeic.cse400.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("InlinedApi")
public class NewRegistration extends Activity {
	
    private EditText registrationUsername;
    private EditText registrationPassword1;
    private EditText registrationPassword2;
    private EditText registrationName;
    private EditText registrationSurname;
    private EditText registrationEmail;
    private EditText registrationPhone;
    private Button registrationButton;
    private ProgressDialog progDialog;
    private Thread thread;
    private Handler hand;
    private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_registeration);
		// Show the Up button in the action bar.
		registrationUsername=(EditText)findViewById(R.id.registerUsername);
		registrationPassword1=(EditText)findViewById(R.id.registerPassword1);
		registrationPassword2=(EditText)findViewById(R.id.registerPassword2);
		registrationEmail=(EditText)findViewById(R.id.registerEmail);
		registrationPhone=(EditText)findViewById(R.id.registerPhone);
		registrationName=(EditText)findViewById(R.id.registerName);
		registrationSurname=(EditText)findViewById(R.id.registerSurname);
		registrationButton=(Button)findViewById(R.id.NewRegisterButton);
		registrationButton.setOnClickListener(registerListener);
		hand=new Handler();
	}
	private OnClickListener registerListener=new OnClickListener() {
		/**
		 * This method is checking user inputs after that sending data to database or complaining about error
		 * @pre registrationUsername.getText().toString().length()!=0 
		 * @pre registrationPassword1.getText().toString().length()!=0 
		 * @pre registrationPassword2.getText().toString().length()!=0
		 * @pre registrationName.getText().toString().length()!=0
		 * @pre registrationSurname.getText().toString().length()!=0 
		 * @pre registrationEmail.getText().toString().length()!=0 
		 * @pre registrationPhone.getText().toString().length()!=0 
		 * 
		 */
		@Override
		public void onClick(View v) {
			String errorMessage="";
			String username="",password1="",password2="1",email="",phone="",name="",surname="";
			if(registrationUsername.getText().toString().length()!=0){
				username=registrationUsername.getText().toString();
				if(username.length()<5){
					errorMessage=errorMessage+" Username length must be longer than 5.";
				}
			}else{
				errorMessage=errorMessage+" Please fill the username.";
			}
			if(registrationPassword1.getText().toString().length()!=0){
				password1=registrationPassword1.getText().toString();
				
				if(registrationPassword2.getText().toString().length()!=0){
					password2=registrationPassword2.getText().toString();
					if(!password1.equals(password2)){
						errorMessage=errorMessage+" Passwords are not matching.";
					}else{
						if(password1.length()<6)
							errorMessage=errorMessage+" Password length must be longer than 6.";
					}
				}else{
					errorMessage=errorMessage+" Check your password fields again.";
				}
				
			}else{
				errorMessage=errorMessage+" Check your password fields again.";
			}
			
			if(registrationName.getText().toString().length()!=0){
				name=registrationName.getText().toString();
			}else{
				errorMessage=errorMessage+" Fill the name.";
			}
			if(registrationSurname.getText().toString().length()!=0){
				surname=registrationSurname.getText().toString();
			}else{
				errorMessage=errorMessage+" Fill the surname.";
			}
			if(registrationEmail.getText().toString().length()!=0){
				if(!isEmailValid(registrationEmail.getText().toString())){
					 errorMessage=errorMessage+" Invalid email type.";
				}else{
					email=registrationEmail.getText().toString();	
				}
				
			}else{
				errorMessage=errorMessage+" Fill the email.";
			}
			if(registrationPhone.getText().toString().length()!=0){
				phone=registrationPhone.getText().toString();
				if(phone.length()!=11){
					errorMessage=errorMessage+" Your phone number is not valid.";
				}
			}else{
				errorMessage=errorMessage+" Fill the phone.";
			}
			if(errorMessage.equals("")){
				user=new Customer(username, password1, phone, name, surname, email);
				progDialog=new ProgressDialog(NewRegistration.this);
				progDialog.setTitle("Please Wait...");
				progDialog.setMessage("Creating Your user Profil");
				progDialog.setCancelable(false);
				progDialog.setProgress(ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
				progDialog.show();
				thread=new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						new ConnectionToNetWork().execute(user);
					}
				});
				thread.start();
				
			}else{
				errorMessageDialog(errorMessage);
			}
			
		}
	};
	/** 
	 * This class is creating new user and closing newRegistration activity
	 * @pre user!=null
	 * 
	 * 
	 *
	 */
    private class ConnectionToNetWork extends AsyncTask<User, Object, Integer>{
    User user;
		@Override
		protected Integer doInBackground(User... params) {
			user=params[0];
			int data=DatabaseConnection.newRegister(params[0],registrationPassword1.getText().toString());
			return data;
		}
		@Override
    	protected void onPostExecute(Integer result) {
    		// TODO Auto-generated method stub
    		
    		
    		if(result==0){
    			errorMessageDialog("Username already exits!");
    			if(progDialog!=null)progDialog.dismiss();
    		}else if(result==-1){
    			errorMessageDialog("Internet Connection Problem");
    			if(progDialog!=null)progDialog.dismiss();
    		}else if(result==2){
    			errorMessageDialog("Email already exits!");
    			if(progDialog!=null)progDialog.dismiss();
    		}else{
    			hand.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						progDialog.setMessage("User has created.");
					}
				}, 10000);
    			progDialog.dismiss();
    			Intent intent=new Intent();
    			intent.putExtra("Username", user.getUsername());
    			setResult(RESULT_OK, intent);
    			finish();
    		}
    		
    	}
    }
    /**
     * This method is checking if email valid.
     * @pre email!=null
     * @param email
     * @return
     */
    private boolean isEmailValid(String email){
    	 String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    	 return email.matches(EMAIL_REGEX);
    }
	/**
	 * Inform user about error
	 * @pre Message!=null
	 * 
	 * 
	 */
	private void errorMessageDialog(String Message){
		AlertDialog.Builder dialog=new AlertDialog.Builder(NewRegistration.this);
		dialog.setTitle("Error Message");
		dialog.setMessage(Message);
		dialog.setCancelable(false);
		dialog.setPositiveButton("Okey", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		dialog.show();
	}

}
