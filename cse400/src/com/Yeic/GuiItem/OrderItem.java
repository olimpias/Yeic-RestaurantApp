package com.Yeic.GuiItem;

public class OrderItem {

 private 	int counter;
 private Object obj;
 public OrderItem(Object obj) {
	// TODO Auto-generated constructor stub
	 this.counter=1;
	 this.obj=obj;
}
 public int getCounter() {
	return counter;
}public Object getObj() {
	return obj;
}
public void add(){
	counter++;
}
public void setCounter(int counter) {
	this.counter = counter;
}
}
