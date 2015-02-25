package com.Yeic.ChefGui;

import com.Yeic.Database.DatabaseChefConnection;
import com.Yeic.GuiItem.MyProgressDialog;
import com.Yeic.Items.Drink;
import com.Yeic.Items.Food;
import com.Yeic.Items.Menu;
import com.Yeic.cse400.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
/**
 * 
 * AddItemActivity is interface of adding item
 *Deneme
 */
public class AddItemActivity extends Activity {
public static final String ITEMADDMENU="MENU";
public static final String ITEMADDFOOD="FOOD";
public static final String ITEMADDDRINK="DRINK";
public static final String ITEMTYPE="itemType";
private EditText itemNameEditText;
private EditText itemDescriptionEditText;
private Button addNewItemButton;
private String type;
private String ButtonText;
private DatabaseChefConnection databaseConnection;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		databaseConnection=new DatabaseChefConnection();
		ButtonText="Create ";
		Intent intent=getIntent();
		type=intent.getStringExtra(ITEMTYPE);
		if(type.equals(ITEMADDDRINK)){
			setContentView(R.layout.activity_add_fooddrink);
			itemNameEditText=(EditText)findViewById(R.id.editTextItemName);
		  ButtonText=ButtonText+"Drink";
		}else if(type.equals(ITEMADDFOOD)){
			setContentView(R.layout.activity_add_fooddrink);
			itemNameEditText=(EditText)findViewById(R.id.editTextItemName);
			ButtonText=ButtonText+"Food";
		}else{
			setContentView(R.layout.activity_add_item);
			itemNameEditText=(EditText)findViewById(R.id.editTextItemName);
			itemDescriptionEditText=(EditText)findViewById(R.id.editTextItemDescription);
			ButtonText=ButtonText+"Menu";
			ErrorMessage("Description should include only exact food and drink names", "Warning"); 
		}
		addNewItemButton=(Button)findViewById(R.id.buttonAddItem);
		addNewItemButton.setText(ButtonText); 
		addNewItemButton.setOnClickListener(AddItemButtonListener);
	}
	private OnClickListener AddItemButtonListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String name=itemNameEditText.getText().toString();
			String description;
			String errorMessage="";
			if(type.equals(ITEMADDMENU)){
				description=itemDescriptionEditText.getText().toString();
				if(description.length()==0){
					errorMessage="Enter Description ";
					if(name.length()==0){
						errorMessage=errorMessage+" Enter Name";
					}
					ErrorMessage(errorMessage,"ERROR");
				}else{
					new insertItem().execute(name,description);
				}
			}else if(type.equals(ITEMADDFOOD)){
				if(name.length()==0){
					errorMessage=errorMessage+" Enter Name";
					ErrorMessage(errorMessage,"ERROR");
				}else{
					new insertItem().execute(name);	
				}
			}else if(type.equals(ITEMADDDRINK)){
				if(name.length()==0){
					errorMessage=errorMessage+" Enter Name";
					ErrorMessage(errorMessage,"ERROR");
				}else{
					new insertItem().execute(name);	
				}
			}
		}
	};
	/**
	 * 
	 * ErrorMessage() operation helps user to know what is going wrong
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
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	/**
	 * 
	 * This class is invoking insert methods according the item type.
	 *@pre type==MENU || type==FOOD || type==DRINK
	 */
	private class insertItem extends AsyncTask<String, Void, Integer>{
        private MyProgressDialog dialog;
        @Override
        protected void onPreExecute() {
        	dialog=new MyProgressDialog(AddItemActivity.this);
        	dialog.show();
        	super.onPreExecute();
        }
		@Override
		protected Integer doInBackground(String... params) {
              int result=0;
			if(type.equals(ITEMADDDRINK)){
				Log.e("DRINK", "DRINKARRIVED");
		    	result=databaseConnection.addDrink(new Drink(params[0]));
		    }else if(type.equals(ITEMADDFOOD)){
		    	result=databaseConnection.addFood(new Food(params[0]));
		    }else if(type.equals(ITEMADDMENU)){
		    	result=databaseConnection.addMenu(new Menu(params[0], params[1]));
		    }
			return result;
		}
		@Override
		protected void onPostExecute(Integer result) {
			dialog.dismiss();
			if(result==1){
				CloseActivity();
			}else if(result==-1){
				ErrorMessage("That is already exists", "Yeic Restaurant");
			}
			else{
				ErrorMessage("Connection Problem","ERROR");
			}
			super.onPostExecute(result);
		}
		
	}
	/**
	 * CloseActivity operation is for losing activity
	 */
	private void CloseActivity(){
		Intent resultIntent=new Intent();
		resultIntent.putExtra(ITEMTYPE, type);
		setResult(RESULT_OK, resultIntent);
		finish();
	}

}
