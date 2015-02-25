package com.Yeic.Equipments;

import java.util.ArrayList;
import com.Yeic.Items.Drink;
import com.Yeic.Items.Food;
import com.Yeic.Items.Menu;
/**
 * 
 * Order is used for keeping user's orders.
 *
 */
public class Order {
/**
 * Invariants:
 * OrderID is positive all the time.
 * @invariant OrderID>-1
 * 
 */
private int OrderID;
private ArrayList<Drink> drinks;
private ArrayList<Menu> menus;
private ArrayList<Food> foods;


public Order(int orderID, ArrayList<Drink> drinks, ArrayList<Menu> menus,
		ArrayList<Food> foods) {
	super();
	OrderID = orderID;
	this.drinks = drinks;
	this.menus = menus;
	this.foods = foods;
}
public Order(int orderid) {
	this.OrderID=orderid;
}
public ArrayList<Drink> getDrinks() {
	return drinks;
}public ArrayList<Food> getFoods() {
	return foods;
}public ArrayList<Menu> getMenus() {
	return menus;
}public int getOrderID() {
	return OrderID;
}
public void setDrinks(ArrayList<Drink> drinks) {
	this.drinks = drinks;
}public void setFoods(ArrayList<Food> foods) {
	this.foods = foods;
}public void setMenus(ArrayList<Menu> menus) {
	this.menus = menus;
}public void setOrderID(int orderID) {
	OrderID = orderID;
}

}
