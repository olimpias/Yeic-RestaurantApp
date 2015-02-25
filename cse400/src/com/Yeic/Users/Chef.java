package com.Yeic.Users;

import java.util.ArrayList;

import com.Yeic.Equipments.Order;
import com.Yeic.Items.Drink;
import com.Yeic.Items.Food;
import com.Yeic.Items.Menu;

public class Chef extends User {
/**
 * @invariant order!=null
 */
private ArrayList<Order> orders;
/**
 * @invariant drinks!=null
 */
private ArrayList<Drink> drinks;
/**
 * @invariant menus!=null
 */
private ArrayList<Menu> menus;
/**
 *  @invariant foods!=null
 */
private ArrayList<Food> foods;


public ArrayList<Order> getOrders() {
	return orders;
}public ArrayList<Drink> getDrinks() {
	return drinks;
}
public ArrayList<Menu> getMenus() {
	return menus;
}
public ArrayList<Food> getFoods() {
	return foods;
}
public void setDrinks(ArrayList<Drink> drinks) {
	this.drinks = drinks;
}public void setFoods(ArrayList<Food> foods) {
	this.foods = foods;
}public void setMenus(ArrayList<Menu> menus) {
	this.menus = menus;
}public void setOrders(ArrayList<Order> orders) {
	this.orders = orders;
}
	public Chef(String username, String password, String phone, String name,
			String surname, String emailAddress) {
		super(username, password, phone, name, surname, emailAddress);
		// TODO Auto-generated constructor stub
		drinks=new ArrayList<Drink>();
		foods=new ArrayList<Food>();
		orders=new ArrayList<Order>();
		menus=new ArrayList<Menu>();
	}
public Chef() {
   super();
}
	public Chef(String username, String phone, String name, String surname,
			String emailAddress) {
		super(username, phone, name, surname, emailAddress);
		// TODO Auto-generated constructor stub
		drinks=new ArrayList<Drink>();
		foods=new ArrayList<Food>();
		orders=new ArrayList<Order>();
		menus=new ArrayList<Menu>();
	}

	public Chef(String username) {
		super(username);
		drinks=new ArrayList<Drink>();
		foods=new ArrayList<Food>();
		orders=new ArrayList<Order>();
		menus=new ArrayList<Menu>();
	}

}
