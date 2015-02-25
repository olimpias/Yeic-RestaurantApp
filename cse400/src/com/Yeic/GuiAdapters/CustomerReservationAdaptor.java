package com.Yeic.GuiAdapters;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.Yeic.CustomerGui.MakeOrderActivity;
import com.Yeic.Database.DatabaseCustomerConnection;
import com.Yeic.Equipments.Reservation;
import com.Yeic.GuiItem.MyProgressDialog;
import com.Yeic.cse400.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * ReservationAdaptor class is used for listing components on list view.
 *
 */
public class CustomerReservationAdaptor extends BaseAdapter {
	/**
	 * Invariants:
	 *reservations list need to be assign on constructor or it will throw exception.
	 * @invariant reservations!=null
	 * 
	 */
private List<Reservation> reservations;
/**
 * Invariants:
 * context list need to be assign on constructor or it will throw exception.
 * @invariant context!=null
 * 
 */
private Context context;
public static final String ID="ID";
private DatabaseCustomerConnection connection;
public CustomerReservationAdaptor(List<Reservation> reservations,Context context,DatabaseCustomerConnection connection) {
	this.reservations=reservations;
	this.connection=connection;
	this.context=context;
}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return reservations.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return reservations.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	private class ViewHolder{
		TextView textUserSurname;
		TextView textDescription;
		TextView textDate;
		ImageView button;
		ImageView deleteButton;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		

		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.reservationrepresenter, null);
			holder.textUserSurname=(TextView)convertView.findViewById(R.id.reservationTitleTextView);
			holder.textDescription=(TextView)convertView.findViewById(R.id.reservationTextView);
			holder.textDate=(TextView)convertView.findViewById(R.id.reservationDateTextView);
			holder.button=(ImageView)convertView.findViewById(R.id.reservationimageView);
			holder.deleteButton=(ImageView)convertView.findViewById(R.id.reservationdeleteimageView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		Reservation reservation=reservations.get(position);
		String username="";
		if(reservation.getName()!=null && reservation.getSurname()!=null){
			username=reservation.getName()+" "+reservation.getSurname();
		}
		holder.textUserSurname.setText(username);
		
		holder.textDate.setText(reservation.getDate().StringToDate()+" ");
		Log.e("Date", reservation.getDate().StringToDate()+"");
		String information="Table name: "+reservation.getTable().getName()+"\nTable description:"+reservation.getTable().getDescription();
		if(reservation.getPhone()!=null){
			information=information+"\n Customer phone:"+reservation.getPhone();
		}
		holder.textDescription.setText(information);
		holder.button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					if(new GetItems().execute(reservations.get(position).getReservationID()).get()){
						Intent intent=new Intent(context, MakeOrderActivity.class);
						MakeOrderActivity.orderID=connection.getUser().getOrder().getOrderID();
						Log.e("ERROR", "WINDOWLEAK");
						context.startActivity(intent);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		holder.deleteButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DeleteMessage(position);
			}
		});
		return convertView;
	}
	private class GetItems extends AsyncTask<Integer, Void, Boolean>{
		private MyProgressDialog dialog;
		@Override
		protected void onPreExecute() {

			dialog=new MyProgressDialog(context);
			dialog.show();
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(Integer... params) {
			MakeOrderActivity.listorder=connection.getUserOrders(params[0]);
			return true;
		}
		@Override
		protected void onPostExecute(Boolean result) {

			dialog.dismiss();
			super.onPostExecute(result);
		}
	}
	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	private class Cancelreservation extends AsyncTask<Integer, Void, Boolean>{
		private MyProgressDialog dialog;
		@Override
		protected void onPreExecute() {

			dialog=new MyProgressDialog(context);
			dialog.show();
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(Integer... params) {
			
			return connection.deleteReservation(params[0]);
		}
		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			super.onPostExecute(result);
		}

	}
	public void DeleteMessage(final int position){
		AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
		alertDialog.setMessage("Are you sure to delete your reservation?");
		alertDialog.setTitle("Information Message");
		alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				new Cancelreservation().execute(reservations.get(position).getReservationID());
				reservations.remove(position);
				notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				
			}
		});
		alertDialog.setCancelable(false);
		alertDialog.show();
	}

}
