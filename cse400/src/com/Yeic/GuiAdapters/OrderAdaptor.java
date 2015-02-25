package com.Yeic.GuiAdapters;

import java.util.ArrayList;
import java.util.TreeSet;

import com.Yeic.GuiItem.OrderItem;
import com.Yeic.Items.Drink;
import com.Yeic.Items.Food;
import com.Yeic.Items.Menu;
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
 * OrderAdaptor class is used for listing components on list view.
 *
 */
public class OrderAdaptor extends BaseAdapter {
	/**
	 * Invariants:
	 *obj list need to be assign on constructor or it will throw exception.
	 * @invariant obj!=null
	 * 
	 */
private ArrayList<OrderItem> itemList;
private TreeSet<Integer> header;
/**
 * Invariants:
 * context list need to be assign on constructor or it will throw exception.
 * @invariant context!=null
 * 
 */
private Context context;
private static final int TYPE_ITEM = 0;
private static final int TYPE_SEPARATOR = 1;
public OrderAdaptor(ArrayList<OrderItem> obj, Context context,TreeSet<Integer> headers) {
	this.itemList=obj;
	this.context=context;
	this.header=headers;
	// TODO Auto-generated constructor stub
}
	 class ViewHolder{
	    	TextView title;
	    	TextView description;
	    }
	 @Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemList.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return itemList.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	   @Override
	   public int getItemViewType(int position) {
	   	// TODO Auto-generated method stub
	   	return header.contains(position)? TYPE_SEPARATOR:TYPE_ITEM;
	   }
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		int type=getItemViewType(position);
		if(convertView==null){
			holder=new ViewHolder();
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if(type==TYPE_ITEM){
				convertView=inflater.inflate(R.layout.orderrepresenter,null);
				holder.title=(TextView)convertView.findViewById(R.id.orderRepresentertextView);
				holder.description=(TextView)convertView.findViewById(R.id.orderRepresenterDescriptiontextView);
				convertView.setTag(holder);
			}else{
				convertView=inflater.inflate(R.layout.headerlistview, null);
				holder.title=(TextView)convertView.findViewById(R.id.headertextView);
				convertView.setTag(holder);
			}
		}else{
			holder=(ViewHolder)convertView.getTag();	
		}
		
		if(type==TYPE_ITEM){
			if(itemList.get(position).getObj() instanceof Menu){
				Menu menu=(Menu)itemList.get(position).getObj();
				holder.title.setText(menu.getName()+" (x"+itemList.get(position).getCounter()+")");
				holder.description.setText(menu.getDescription());
			}
			if(itemList.get(position).getObj() instanceof Food){
				Food food=(Food)itemList.get(position).getObj();
				holder.title.setText(food.getName()+" (x"+itemList.get(position).getCounter()+")");
				holder.description.setText("");
			}
			if(itemList.get(position).getObj() instanceof Drink){
				Drink drink=(Drink)itemList.get(position).getObj();
				holder.title.setText(drink.getName()+" (x"+itemList.get(position).getCounter()+")");
				holder.description.setText("");
			}
		}else{
			holder.title.setText((String)itemList.get(position).getObj());
		}
		return convertView;
	}
	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}public void setHeader(TreeSet<Integer> header) {
		this.header = header;
	}public void setItemList(ArrayList<OrderItem> itemList) {
		this.itemList = itemList;
	}

}
