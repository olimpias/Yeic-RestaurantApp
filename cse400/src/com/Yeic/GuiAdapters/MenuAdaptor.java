package com.Yeic.GuiAdapters;

import java.util.List;




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
 * MenuAdaptor class is used for listing components on list view.
 *
 */
public class MenuAdaptor extends BaseAdapter {
	/**
	 * Invariants:
	 * menuList list need to be assign on constructor or it will throw exception.
	 * @invariant menuList!=null
	 * 
	 */
private List<Menu> menuList;
/**
 * Invariants:
 * context list need to be assign on constructor or it will throw exception.
 * @invariant context!=null
 * 
 */
private Context context;
	public MenuAdaptor(Context context, int resource, List<Menu> objects) {
		
		// TODO Auto-generated constructor stub
		menuList=objects;
		this.context=context;
	}
	private class ViewHolder{
		TextView textName;
		TextView textDescription;
	}
	@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		    ViewHolder holder;
		    Menu menu=menuList.get(position);
		    LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		    if(convertView==null){
		    	convertView=inflater.inflate(R.layout.blockmenurepresenter,null);
		    	holder=new ViewHolder();
		    	holder.textName=(TextView)convertView.findViewById(R.id.textViewMenuName);
		    	holder.textDescription=(TextView)convertView.findViewById(R.id.textViewMenuDescription);
		    	convertView.setTag(holder);
		    }else{
		    	holder=(ViewHolder)convertView.getTag();
		    }
		    holder.textName.setText(menu.getName());
		    holder.textDescription.setText(menu.getDescription());
			return convertView;
		}
	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menuList.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return menuList.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
