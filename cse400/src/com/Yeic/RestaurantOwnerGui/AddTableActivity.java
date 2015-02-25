package com.Yeic.RestaurantOwnerGui;


import com.Yeic.Database.DatabaseRestaurantOwnerConnection;
import com.Yeic.Items.Table;
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
 * This class is interface of Table management like insert and edit.
 *
 */
public class AddTableActivity extends Activity {
    protected static final String TABLESTATS="tableStatus";
    private EditText editNameText;
    private EditText editDescriptionText;
    private Button button;
    private int id;
    private DatabaseRestaurantOwnerConnection databaseConnection;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		editNameText=(EditText)findViewById(R.id.editTextItemName);
		editDescriptionText=(EditText)findViewById(R.id.editTextItemDescription);
		button=(Button)findViewById(R.id.buttonAddItem);
		int status=getIntent().getIntExtra(TABLESTATS, 0);
		databaseConnection=new DatabaseRestaurantOwnerConnection("");
		id=-1;
		button.setText(R.string.create_table);
		if(status==1){
			String name=getIntent().getStringExtra("name");
			id=getIntent().getIntExtra("id", -1);
			String description=getIntent().getStringExtra("description");
			editNameText.setText(name);
			editDescriptionText.setText(description);
			button.setText(R.string.edit_table);
		}
		button.setOnClickListener(buttonListener);
		
	}
	private OnClickListener buttonListener=new OnClickListener() {
		/**
		 * This method is updating or inserting new table according to id.
		 * @pre id>-2>
		 * @pre editDescriptionText.getText().toString().length!=0
		 * @pre editNameText.getText().toString().length!=0
		 */
		@Override
		public void onClick(View v) {
			Table table;
			String errorMessage="";
			String description=editDescriptionText.getText().toString();
			String name=editNameText.getText().toString();
			if(name.length()==0){
				errorMessage=errorMessage+" Fill the name part.";
			}
			if(description.length()==0){
				errorMessage=errorMessage+" Fill the description part.";
			}
			if(description.length()!=0&&name.length()!=0){
			  if(id==-1){
				  table=new Table(description, name);
				  new AddTable().execute(table);
			  }else{
				  table=new Table(id, description, name);
				  new EditTable().execute(table);
			  }
			}else{
				ErrorMessage(errorMessage);
			}
		}
	};
	/**
	 * 
	 * This class is invoking editTable() method in DatabaseRestaurantOwnerConnection class.
	 * @pre params[0]!=null
	 *
	 */
	private class EditTable extends AsyncTask<Table, Void, Boolean>{
		private ProgressDialog dialog;
		@Override
        protected void onPreExecute() {
			dialog=ProgressDialog.show(AddTableActivity.this, "Loading...", "Please wait...", true);
        	super.onPreExecute();
        }
		@Override
		protected Boolean doInBackground(Table... params) {
			boolean result=false;
			result=databaseConnection.editTable(params[0]);
			return result;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if(result)CloseActivity();
			super.onPostExecute(result);
		}
		
	}
	/**
	 * 
	 * This class is invoking addTable method in DatabaseRestaurantOwnerConnection class.
	 *@pre params[0]!=null
	 */
	private class AddTable extends AsyncTask<Table, Void, Boolean>{
        private ProgressDialog dialog;
        @Override
       protected void onPreExecute() {
       	 dialog=ProgressDialog.show(AddTableActivity.this, "Loading...", "Please wait...", true);
       	super.onPreExecute();
       }
		@Override
		protected Boolean doInBackground(Table... params) {
			boolean result=false;
			result=databaseConnection.addTable(params[0]);
			return result;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if(result)CloseActivity();
			super.onPostExecute(result);
		}
		
	}
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
	 * CloseAcivity() is closing activity
	 */
	private void CloseActivity(){
		setResult(RESULT_OK);
		finish();
	}


}
