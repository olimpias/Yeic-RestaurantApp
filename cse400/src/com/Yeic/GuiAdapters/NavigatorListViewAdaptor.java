package com.Yeic.GuiAdapters;

import java.util.List;

import com.Yeic.GuiItem.RowNagivatorBlock;
import com.Yeic.cse400.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * NavigatorListViewAdaptor class is used for listing components on list view.
 *
 */
public class NavigatorListViewAdaptor extends ArrayAdapter<RowNagivatorBlock> {
	/**
	 * Invariants:
	 * context list need to be assign on constructor or it will throw exception.
	 * @invariant context!=null
	 * 
	 */
private Context context;
	public NavigatorListViewAdaptor(Context context, int resource,
			List<RowNagivatorBlock> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	private class ViewHolder{
		ImageView imageView;
		TextView textTitle;
	}
	@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		 ViewHolder holder;
		 RowNagivatorBlock rowItem=getItem(position);
		 LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		 if(convertView==null){
			 convertView=inflater.inflate(R.layout.listnagivatordrawer,null);//Eklemeyi unutma
			 holder=new ViewHolder();
			 holder.imageView=(ImageView)convertView.findViewById(R.id.iconDrawerListview);
			 holder.textTitle=(TextView)convertView.findViewById(R.id.textDrawerListView);
			 convertView.setTag(holder);
		 }else{
			 holder=(ViewHolder)convertView.getTag();
		 }
		 holder.imageView.setImageResource(rowItem.getImageId());
		 holder.textTitle.setText(rowItem.getTitle());
		 return convertView;
		}

}
