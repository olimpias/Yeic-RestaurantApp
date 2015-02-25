package com.Yeic.Users;

public abstract class  User {
private String username;
private String password;
private String phone;
private String name;
private String surname;
private String emailAddress;

public User() {
	// TODO Auto-generated constructor stub
}
public User(String username, String password, String phone, String name,
		String surname, String emailAddress) {
	super();
	this.username = username;
	this.password = password;
	this.phone = phone;
	this.name = name;
	this.surname = surname;
	this.emailAddress = emailAddress;
}
public User(String username) {
	this.username=username;
}
public User(String username, String phone, String name, String surname,
		String emailAddress) {
	super();
	this.username = username;
	this.phone = phone;
	this.name = name;
	this.surname = surname;
	this.emailAddress = emailAddress;
}

public String getUsername() {
	return username;
}
public String getPhone() {
	return phone;
}
public String getName() {
	return name;
}
public String getSurname() {
	return surname;
}
public String getEmailAddress() {
	return emailAddress;
}
public int getPassword() {
	return password.hashCode();
}
public void setEmailAddress(String emailAddress) {
	this.emailAddress = emailAddress;
}public void setPassword(String password) {
	this.password = password;
}public void setPhone(String phone) {
	this.phone = phone;
}public void setName(String name) {
	this.name = name;
}public void setSurname(String surname) {
	this.surname = surname;
}
public String getPass(){
	return password;
}public void setUsername(String username) {
	this.username = username;
}
}
