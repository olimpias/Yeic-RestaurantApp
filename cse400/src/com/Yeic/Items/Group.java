package com.Yeic.Items;

import java.util.ArrayList;
import java.util.List;

public class Group {
private String title;
private  List<String> children=new ArrayList<String>();
public Group(String title,List<String> child) {
	// TODO Auto-generated constructor stub
	this.title=title;
	this.children=child;
}
public List<String> getChildren() {
	return children;
}public String getTitle() {
	return title;
}

}
