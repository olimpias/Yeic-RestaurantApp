package com.Yeic.CustomerGui;

import java.util.ArrayList;
import java.util.TreeSet;

import com.Yeic.GuiAdapters.ItemOrderAdaptor;
import com.Yeic.Items.*;
import com.Yeic.cse400.R;

import android.os.Bundle;
import android.widget.ListView;
import android.app.Activity;

public class OrderActivity extends Activity {

private ListView listview;
private ItemOrderAdaptor adaptor;
private TreeSet<Integer> headerPosition;
private ArrayList<Object> objectList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_layout);
		InitilizeHeaders();
		adaptor=new ItemOrderAdaptor(objectList, this, headerPosition);
		// Show the Up button in the action bar.
		listview=(ListView)findViewById(R.id.ListViewItem);
		listview.setAdapter(adaptor);
	}
	public void InitilizeHeaders(){
		ArrayList<Object> obj=MakeOrderActivity.objList;
		objectList=new ArrayList<Object>();
		headerPosition=new TreeSet<Integer>();
		boolean headerfood=true;
		boolean headerdrink=true;
		boolean headermenu=true;
		for(int i=0;i<obj.size();i++){
			if(obj.get(i) instanceof Menu){
				Menu menu=(Menu)obj.get(i);
				if(headermenu){
					String menuT="Menu";
					objectList.add(menuT);
					headerPosition.add(objectList.indexOf(menuT));
					headermenu=false;
				}
				objectList.add(menu);
			}
			if(obj.get(i) instanceof Food){
				Food food=(Food)obj.get(i);
				if(headerfood){
					String foodT="Food";
					objectList.add(foodT);
					
					headerPosition.add(objectList.indexOf(foodT));
					headerfood=false;
				}
				objectList.add(food);
			}
			if(obj.get(i) instanceof Drink){
				Drink drink=(Drink)obj.get(i);
				if(headerdrink){
					String drinkT="Drink";
					objectList.add(drinkT);
					headerPosition.add(objectList.indexOf(drinkT));
					headerdrink=false;
				}
				objectList.add(drink);
			}
		}
	}
	
}
