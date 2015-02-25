package com.Yeic.Users;

import java.util.ArrayList;
import java.util.List;

import com.Yeic.Equipments.Reservation;
import com.Yeic.Items.Comment;
import com.Yeic.Items.Table;

public class RestaurantOwner extends User {
	/**
	 * @invariant commentList!=null
	 */
private List<Comment> commentList;
/**
 * @invariant tableList!=null
 */
private List<Table> tableList;
/**
 * @invariant reservationList!=null
 */
private List<Reservation> reservationList;
	public RestaurantOwner(String username, String password, String phone,
			String name, String surname, String emailAddress) {
		super(username, password, phone, name, surname, emailAddress);
		commentList=new ArrayList<Comment>();
		tableList=new ArrayList<Table>();
		reservationList=new ArrayList<Reservation>();
	}

	public RestaurantOwner(String username, String phone, String name,
			String surname, String emailAddress) {
		super(username, phone, name, surname, emailAddress);
		commentList=new ArrayList<Comment>();
		tableList=new ArrayList<Table>();
		reservationList=new ArrayList<Reservation>();
	}

	public RestaurantOwner(String username) {
		super(username);
		commentList=new ArrayList<Comment>();
		tableList=new ArrayList<Table>();
		reservationList=new ArrayList<Reservation>();
	}
	public List<Comment> getCommentList() {
		return commentList;
	}public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}
	public List<Table> getTableList() {
		return tableList;
	}public void setTableList(List<Table> tableList) {
		this.tableList = tableList;
	}public List<Reservation> getReservationList() {
		return reservationList;
	}public void setReservationList(List<Reservation> reservationList) {
		this.reservationList = reservationList;
	}


}
