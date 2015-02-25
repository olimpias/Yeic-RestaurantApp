package com.Yeic.Users;

import java.util.ArrayList;

import com.Yeic.Equipments.Order;
import com.Yeic.Equipments.Reservation;
import com.Yeic.Items.Comment;

public class Customer  extends User{
	/**
	 * @invariant commentList!=null
	 */
private ArrayList<Comment> commentList;
private Order order;
private ArrayList<Reservation> reservations;
	public Customer(String username, String password, String phone,
			String name, String surname, String emailAddress) {
		super(username, password, phone, name, surname, emailAddress);
		// TODO Auto-generated constructor stub
	}
public ArrayList<Comment> getCommentList() {
	return commentList;
}public void setCommentList(ArrayList<Comment> commentList) {
	this.commentList = commentList;
}
	public Customer(String username, String phone, String name, String surname,
			String emailAddress) {
		super(username, phone, name, surname, emailAddress);
		// TODO Auto-generated constructor stub
	}
	public Customer(String username) {
	 super(username);
	 commentList=new ArrayList<Comment>();
	 order=null;
	 reservations=new ArrayList<Reservation>() ;
	}
	public void setOrder(Order order) {
		this.order = order;
	}public void setReservations(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}public ArrayList<Reservation> getReservations() {
		return reservations;
	}public Order getOrder() {
		return order;
	}
	
	

}
