package com.Yeic.GuiAdapters;

import java.util.List;





import com.Yeic.Items.Food;
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
 * FoodAdaptor class is used for listing components on list view.
 *
 */
public class FoodAdaptor extends BaseAdapter {
public FoodAdaptor(Context context, int resource, List<Food> objects) {
	super();
		// TODO Auto-generated constructor stub
		foodList=objects;
		this.context=context;
	}
/**
 * Invariants:
 * foodList need to be assign on constructor or it will throw exception.
 * @invariant foodList!=null
 * 
 */
private List<Food> foodList;
/**
 * Invariants:
 * context list need to be assign on constructor or it will throw exception.
 * @invariant context!=null
 * 
 */

private Context context;
	
	
	private class ViewHolder{
		private TextView textName;
	}
	@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		    ViewHolder holder;
		    
			Food food=foodList.get(position);
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if(convertView==null){
				convertView=inflater.inflate(R.layout.blockitemrepresenter,null);//Eklemeyi unutma
				 holder=new ViewHolder();
				 holder.textName=(TextView)convertView.findViewById(R.id.textViewItem);
				 convertView.setTag(holder);
			}else{
				holder=(ViewHolder)convertView.getTag();
			}
			  holder.textName.setText(position+1+"-"+food.getName());
			return convertView;
			
		}
	public void setFoodList(List<Food> foodList) {
		this.foodList = foodList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return foodList.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return foodList.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	
	

}
