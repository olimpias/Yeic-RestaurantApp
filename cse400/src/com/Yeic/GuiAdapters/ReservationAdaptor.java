package com.Yeic.GuiAdapters;

import java.util.List;

import com.Yeic.Equipments.Reservation;
import com.Yeic.cse400.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * ReservationAdaptor class is used for listing components on list view.
 *
 */
public class ReservationAdaptor extends BaseAdapter {
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

public ReservationAdaptor(List<Reservation> reservations,Context context) {
	this.reservations=reservations;
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
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		Reservation reservation=reservations.get(position);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.commentrepresenter, null);
			holder.textUserSurname=(TextView)convertView.findViewById(R.id.commentTitleTextView);
			holder.textDescription=(TextView)convertView.findViewById(R.id.commentTextView);
			holder.textDate=(TextView)convertView.findViewById(R.id.commentDateTextView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		String username="";
		if(reservation.getName()!=null && reservation.getSurname()!=null){
			username=reservation.getName()+" "+reservation.getSurname();
		}
		holder.textUserSurname.setText(username);
		holder.textDate.setText(reservation.getDate().StringToDate()+" ");
		String information="Table name: "+reservation.getTable().getName()+"\nTable description:"+reservation.getTable().getDescription();
		if(reservation.getPhone()!=null){
			information=information+"\n Customer phone:"+reservation.getPhone();
		}
		holder.textDescription.setText(information);
		return convertView;
	}
	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

}
