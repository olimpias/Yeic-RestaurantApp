package com.Yeic.GuiAdapters;

import java.util.List;

import com.Yeic.Items.Table;
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
 * TableAdaptor class is used for listing components on list view. 
 *
 */
public class TableAdaptor extends BaseAdapter {
	/**
	 * Invariants:
	 *table list need to be assign on constructor or it will throw exception.
	 * @invariant tables!=null
	 * 
	 */
private List<Table> tables;
/**
 * Invariants:
 * context list need to be assign on constructor or it will throw exception.
 * @invariant context!=null
 * 
 */
private Context context;

    public TableAdaptor(List<Table> tables,Context context) {
		this.tables=tables;
		this.context=context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tables.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return tables.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	private class ViewHolder{
		TextView textName;
		TextView textDescription;
	}
   
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		Table table=tables.get(position);
		if(convertView==null){
			convertView=inflater.inflate(R.layout.blockmenurepresenter,null);
	    	holder=new ViewHolder();
	    	holder.textName=(TextView)convertView.findViewById(R.id.textViewMenuName);
	    	holder.textDescription=(TextView)convertView.findViewById(R.id.textViewMenuDescription);
	    	convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.textName.setText(table.getName());
		holder.textDescription.setText(table.getDescription());
		return convertView;
	}
   public List<Table> getTables() {
	return tables;
}  public void setTables(List<Table> tables) {
	this.tables = tables;
}
}
