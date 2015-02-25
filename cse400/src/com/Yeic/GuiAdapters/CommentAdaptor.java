package com.Yeic.GuiAdapters;

import java.util.List;
import com.Yeic.Items.Comment;
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
 * CommentAdaptor class is used for listing components on list view
 *
 */
public class CommentAdaptor extends BaseAdapter {
	/**
	 * Invariants:
	 * comments list need to be assign on constructor or it will throw exception.
	 * @invariant comments!=null
	 * 
	 */
private List<Comment> comments;
/**
 * Invariants:
 * context need to be assign or it will throw exception
 * @invariant context!=null
 * 
 */
private Context context;
public CommentAdaptor(List<Comment> comments,Context context) {
	this.comments=comments;
	this.context=context;
}
private class ViewHolder{
	TextView textUserSurname;
	TextView textDescription;
	TextView textDate;
}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comments.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return comments.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		Comment comment=comments.get(position);
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if(convertView==null){
			convertView=inflater.inflate(R.layout.commentrepresenter, null);
			holder=new ViewHolder();
			holder.textUserSurname=(TextView)convertView.findViewById(R.id.commentTitleTextView);
			holder.textDescription=(TextView)convertView.findViewById(R.id.commentTextView);
			holder.textDate=(TextView)convertView.findViewById(R.id.commentDateTextView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.textUserSurname.setText(comment.getName()+" "+comment.getSurname());
		holder.textDescription.setText(comment.getReview());
		holder.textDate.setText(comment.getDate().StringToDate()+"");//düzenlenecek...
		return convertView;
	}
	
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}
