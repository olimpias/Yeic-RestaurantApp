package com.Yeic.GuiAdapters;

import java.util.ArrayList;
import java.util.TreeSet;

import com.Yeic.CustomerGui.MakeOrderActivity;
import com.Yeic.GuiItem.OrderItem;
import com.Yeic.Items.Drink;
import com.Yeic.Items.Food;
import com.Yeic.Items.Menu;
import com.Yeic.cse400.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemOrderAdaptor extends BaseAdapter {
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
private ArrayList<Object> objectList;
private TreeSet<Integer> header;
private Context context;
public ItemOrderAdaptor(ArrayList<Object> foodList,Context context,TreeSet<Integer> tree) {
	// TODO Auto-generated constructor stub
	this.objectList=foodList;
	this.context=context;
	this.header=tree;
}
class ViewHolder{
	TextView title;
	ImageView image;
	TextView description;
}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return objectList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return objectList.get(position);
	}@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		int rowType = getItemViewType(position);
		if(convertView==null){
			holder=new ViewHolder();
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if(rowType==TYPE_ITEM){
				
				convertView=inflater.inflate(R.layout.addfooddrinkrepresenter,null);
				holder.title=(TextView)convertView.findViewById(R.id.ItemtextView);
				holder.image=(ImageView)convertView.findViewById(R.id.AddItemimageView);
				holder.description=(TextView)convertView.findViewById(R.id.DescriptiontextView);
				convertView.setTag(holder);	
				Log.e("mESSAGE", objectList.get(position)+"");
			}else{
				convertView=inflater.inflate(R.layout.headerlistview,null);
				holder.title=(TextView)convertView.findViewById(R.id.headertextView);
				convertView.setTag(holder);	
				Log.e("mESSAGE", objectList.get(position)+"");
			}
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		if(rowType==TYPE_ITEM){
			if(objectList.get(position) instanceof Menu){
				Menu menu=(Menu)objectList.get(position);
				holder.title.setText(menu.getName());
				holder.description.setText(menu.getDescription());
			}
            if(objectList.get(position) instanceof Food){
				Food food=(Food)objectList.get(position);
				holder.title.setText(food.getName());
				holder.description.setText("");
			}
            if(objectList.get(position) instanceof Drink){
	            Drink drink=(Drink)objectList.get(position);
	            holder.title.setText(drink.getName());
	            holder.description.setText("");
             }
            holder.image.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DialogBox(position);
				}
			});
		}else{
			holder.title.setText((String)objectList.get(position));
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
		return !header.contains(position);
	}
	private void DialogBox(final int pos){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		final EditText text=new EditText(context);
		text.setMaxLines(4);
		text.setInputType(InputType.TYPE_CLASS_NUMBER);
		builder.setView(text);
		builder.setMessage("Quantity:");
		builder.setTitle("Yeic Restaurant");
		builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				int number=Integer.parseInt(text.getText().toString());
				if(number>0&&number<100){
				    ArrayList<OrderItem> list=MakeOrderActivity.adaptor.getItems();
				    boolean notIncluded=true;
					for(int i=0;i<list.size();i++){
						if(list.get(i).getObj() instanceof Menu && objectList.get(pos) instanceof Menu){
							Menu menu=(Menu)list.get(i).getObj();
							Menu menu1=(Menu)objectList.get(pos);
							if(menu.getMenuID()==menu1.getMenuID()){
								list.get(i).setCounter(number);
								notIncluded=false;
							}
						}
						if(list.get(i).getObj() instanceof Food && objectList.get(pos) instanceof Food){
							Food food=(Food)list.get(i).getObj();
							Food food1=(Food)objectList.get(pos);
							if(food.getFoodID()==food1.getFoodID()){
								list.get(i).setCounter(number);
								notIncluded=false;
							}
						}
						if(list.get(i).getObj() instanceof Drink && objectList.get(pos) instanceof Drink){
							Drink drink=(Drink)list.get(i).getObj();
							Drink drink1=(Drink)objectList.get(pos);
							if(drink.getDrinkID()==drink1.getDrinkID()){
								list.get(i).setCounter(number);
								notIncluded=false;
							}
						}
					}
					if(notIncluded){
						OrderItem newItem=new OrderItem(objectList.get(pos));
						newItem.setCounter(number);
						list.add(newItem);
					}
					MakeOrderActivity.adaptor.setItems(list);
					MakeOrderActivity.adaptor.notifyDataSetChanged();
				}else if(number>100){
					Toast.makeText(context, "Too many order", Toast.LENGTH_LONG).show();;
				}else{
					Toast.makeText(context, "0 order not exceptable", Toast.LENGTH_LONG).show();
				}
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.show();
	}

}
