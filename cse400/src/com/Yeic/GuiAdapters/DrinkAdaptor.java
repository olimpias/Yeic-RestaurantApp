package com.Yeic.GuiAdapters;


import java.util.List;




import com.Yeic.Items.Drink;
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
 * DrinkAdaptor class is used for listing components on list view.
 *
 */
public class DrinkAdaptor extends BaseAdapter {
	/**
	 * Invariants:
	 * drinkList need to be assign on constructor or it will throw exception.
	 * @invariant drinkList!=null
	 * 
	 */
	private List<Drink> drinkList;
	/**
	 * Invariants:
	 * context need to be assign or it will throw exception
	 * @invariant context!=null
	 * 
	 */
	private Context context;
	public DrinkAdaptor(Context context, int resource,
			List<Drink> objects) {
		super();
		// TODO Auto-generated constructor stub
		this.context=context;
		drinkList=objects;
	}
	private class ViewHolder{
		private TextView textName;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		Drink drink=drinkList.get(position);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if(convertView==null){
			convertView=inflater.inflate(R.layout.blockitemrepresenter,null);//Eklemeyi unutma
			 holder=new ViewHolder();
			 holder.textName=(TextView)convertView.findViewById(R.id.textViewItem);
			 convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		  holder.textName.setText(position+1+"-"+drink.getName());
		return convertView;
		
		
	}
	public void setDrinkList(List<Drink> drinkList) {
		this.drinkList = drinkList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return drinkList.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return drinkList.get(arg0);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}



	

}
