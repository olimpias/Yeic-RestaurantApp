package com.Yeic.Equipments;



import com.Yeic.Items.Table;
/**
 * 
 * Reservation class is used for keeping user's reservation
 *
 */
public class Reservation {
	/**
	 * Invariants:
	 * reservationID is positive all the time.
	 * @invariant reservationID>-1
	 * 
	 */
private int reservationID;
/**
 * Invariants:
 * table needs to be assign when reservation object created.
 * @invariant table!=null
 * 
 */
private Table table;
/**
 * Invariants:
 * date needs to be assign when reservation object created.
 * @invariant date!=null
 * 
 */
private RestaurantDate date;
private String name;
private String surname;
private String phone;
private String username;

public Reservation(int reservationID, Table table, RestaurantDate date,String name,String surname,String phone) {
	super();
	this.reservationID = reservationID;
	this.table = table;
	this.date = date;
	this.name=name;
	this.surname=surname;
	this.phone=phone;
}
public Reservation(Table table,String username,RestaurantDate date) {
	this.table=table;
	this.username=username;
	this.date=date;
}
public Reservation(Table table,RestaurantDate date,int id) {
	this.table=table;
	this.date=date;
	this.reservationID=id;
}
public String getName() {
	return name;
}public String getPhone() {
	return phone;
}public String getSurname() {
	return surname;
}
public String getUsername() {
	return username;
}
public void setName(String name) {
	this.name = name;
}public void setPhone(String phone) {
	this.phone = phone;
}public void setSurname(String surname) {
	this.surname = surname;
}
public void setDate(RestaurantDate date) {
	this.date = date;
}public void setReservationID(int reservationID) {
	this.reservationID = reservationID;
}public void setTable(Table table) {
	this.table = table;
}
public RestaurantDate getDate() {
	return date;
}public int getReservationID() {
	return reservationID;
}public Table getTable() {
	return table;
}
}
