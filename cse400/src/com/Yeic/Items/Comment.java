package com.Yeic.Items;

import com.Yeic.Equipments.RestaurantDate;
/**
 * Comment class
 */
public class Comment {
	/**
	 * Invariants:
	 * commentID must be positive.
	 * @invariant commentID>-1
	 * 
	 */
private int commentID;
private String review;
private RestaurantDate date;
private String name;
private String surname;
private String username;
public Comment(int commentID, String review, RestaurantDate date, String username) {
	super();
	this.commentID = commentID;
	this.review = review;
	this.date = date;
	this.username = username;
}
public Comment(int commentID, String review, RestaurantDate date, String name,
		String surname) {
	super();
	this.commentID = commentID;
	this.review = review;
	this.date = date;
	this.name = name;
	this.surname = surname;
}
public int getCommentID() {
	return commentID;
}public RestaurantDate getDate() {
	return date;
}public String getName() {
	return name;
}public String getReview() {
	return review;
}public String getSurname() {
	return surname;
}public String getUsername() {
	return username;
}
}
