package com.Yeic.GuiAdapters;

import java.util.ArrayList;

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
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 
 * MakeOrderAdaptor class is used for listing components on list view.
 *
 */
public class MakeOrderAdaptor extends BaseAdapter {
	/**
	 * Invariants:
	 * items list need to be assign on constructor or it will throw exception.
	 * @invariant items!=null
	 * 
	 */
	ArrayList<OrderItem> items;
 /**
	 * Invariants:
	 * context list need to be assign on constructor or it will throw exception.
	 * @invariant context!=null
	 * 
	 */
 private Context context;
 /**
	 * Invariants:
	 * icons list need to be assign on constructor or it will throw exception.
	 * @invariant icons!=null
	 * 
	 */
	public MakeOrderAdaptor(ArrayList<OrderItem> items,Context context) {
	this.items=items;
	this.context=context;
} 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}
	@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}
			

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return items.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
    class ViewHolder{
    	ImageButton button;
    	TextView title;
    	TextView description;
    }
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		Object obj=items.get(position).getObj();
		int count=items.get(position).getCounter();
		if(convertView==null){
			holder=new ViewHolder();
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);	
			convertView=inflater.inflate(R.layout.makeorder, parent,false);
			holder.title=(TextView)convertView.findViewById(R.id.TitleItemtextView);
			holder.button=(ImageButton)convertView.findViewById(R.id.RemoveButton);
			holder.description=(TextView)convertView.findViewById(R.id.DescriptionItemtextView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		if(obj instanceof Menu){
			Menu menu=(Menu)obj;
			holder.title.setText(menu.getName()+"(x"+count+")");

			holder.description.setText(menu.getDescription());
		}else if(obj instanceof Drink){
			Drink drink=(Drink)obj;
			holder.title.setText(drink.getName()+"(x"+count+")");
		}else{
			Food food=(Food)obj;
			holder.title.setText(food.getName()+"(x"+count+")");
		}
		holder.button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				items.remove(position);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}
	public ArrayList<OrderItem> getItems() {
		return items;
	}
	public void setItems(ArrayList<OrderItem> items) {
		this.items = items;
	}
	}
