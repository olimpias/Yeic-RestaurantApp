package com.Yeic.RestaurantOwnerGui;

import java.util.ArrayList;
import com.Yeic.Database.DatabaseRestaurantOwnerConnection;
import com.Yeic.Users.Chef;
import com.Yeic.cse400.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
/**
 * 
 * This class is interface of adding chef.
 *
 */
public class AddChefActivity extends Activity {
	protected static final String CHEFARRAYLIST="CHEFARRAYLIST";
	private EditText usernameText;
	private EditText passwordText;
	private EditText nameText;
	private EditText surnameText;
	private EditText phoneText;
	private EditText emailText;
	private Button button;
	private DatabaseRestaurantOwnerConnection connection;
	ArrayList<CharSequence> array;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_chef);
		array=getIntent().getCharSequenceArrayListExtra(CHEFARRAYLIST);
		usernameText=(EditText)findViewById(R.id.chefUsername);
		passwordText=(EditText)findViewById(R.id.chefPassword1);
		nameText=(EditText)findViewById(R.id.chefName);
		surnameText=(EditText)findViewById(R.id.chefSurname);
		phoneText=(EditText)findViewById(R.id.chefPhone);
		emailText=(EditText)findViewById(R.id.chefEmail);
		button=(Button)findViewById(R.id.chefButton);
		connection=new DatabaseRestaurantOwnerConnection("");
		if(array.size()!=0){
			button.setText(R.string.edit_chef);
			usernameText.setText(array.get(0));
			passwordText.setText(array.get(1));
			nameText.setText(array.get(2));
			surnameText.setText(array.get(3));
			phoneText.setText(array.get(4));
			emailText.setText(array.get(5));
		}else{
			button.setText(R.string.create_new_chef);
		}
		button.setOnClickListener(buttonListener);
	}
	private OnClickListener buttonListener=new OnClickListener() {
		/**
		 * Button listener is calling inserting new chef method.Before calling, edit text are checked.If edit texts are not passed, errors pop up.
		 * @pre usernameText.getText().toString().length()>8
		 * @pre passwordText.getText().toString().length()!=0
		 * @pre nameText.getText().toString().length()!=0
		 * @pre surnameText.getText().toString().length()!=0
		 * @pre phoneText.getText().toString().length()!=0
		 * @pre emailText.getText().toString().length()!=0
		 * 
		 */
		@Override
		public void onClick(View v) {
		  String errorMessage="";
		  if(usernameText.getText().toString().length()<8){
			  errorMessage=errorMessage+" Username must be longer than 8 character.";
		  }
		  if(passwordText.getText().toString().length()==0){
			  errorMessage=errorMessage+" Enter password.";
		  }
		  if(nameText.getText().toString().length()==0){
			  errorMessage=errorMessage+" Fill the name field";
		  }
		  if(surnameText.getText().toString().length()==0){
			  errorMessage=errorMessage+" Fill the surname field"; 
		  }
		  if(phoneText.getText().toString().length()==0){
			  errorMessage=errorMessage+" Fill the phone field";
		  }
		  if(emailText.getText().toString().length()==0){
			  errorMessage=errorMessage+" Fill the email field";
		  }
		  if(!errorMessage.equals("")){
			  ErrorMessage(errorMessage);
		  }else{
			  Chef chef=new Chef(usernameText.getText().toString(), passwordText.getText().toString(), phoneText.getText().toString(), nameText.getText().toString(), surnameText.getText().toString(), emailText.getText().toString());
			  new InsertChef().execute(chef);
		  }
			
		}
	};
	/**
	 * ErrorMessage() warns user about error
	 * @param message
	 * @pre message!=null and !message.equals("")
	 */
	private void ErrorMessage(String message){
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setTitle("Error");
		dialog.setMessage(message);
		dialog.setCancelable(false);
		dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	/**
	 * 
	 * This class is invoking addChef() method in DatabaseRestaurantOwnerConnection.
	 * @pre params[0]!=null
	 *
	 */
	private class InsertChef extends AsyncTask<Chef, Void, Boolean>{
        private ProgressDialog dialog;
		@Override
        protected void onPreExecute() {
        	dialog=ProgressDialog.show(AddChefActivity.this, "Loading...", "Please wait...", true);
        	super.onPreExecute();
        }
		@Override
		protected Boolean doInBackground(Chef... params) {
			boolean result=false;
			result=connection.addChef(params[0]);                                                                      
			return result;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if(result){
				CloseActivity();
			}else{
				ErrorMessage("Connection Problem");
			}
			super.onPostExecute(result);
		}
		
	}
	/**
	 * CloseAcivity() is closing activity
	 */
	public void CloseActivity(){
		setResult(RESULT_OK);
		finish();
	}

	

}
