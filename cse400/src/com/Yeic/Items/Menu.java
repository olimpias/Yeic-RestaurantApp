package com.Yeic.Items;
/**
 * 
 * Menu class
 *
 */
public class Menu {
	/**
	 * Invariants:
	 * menuID must be positive.
	 * @invariant menuID>-1
	 * 
	 */
private int menuID;
private String description;
private String name;
public Menu(int menuID, String description,String name) {
	super();
	this.menuID = menuID;
	this.description = description;
	this.name=name;
}

public Menu(String description, String name) {
	super();
	this.description = description;
	this.name = name;
}

public String getDescription() {
	return description;
}public int getMenuID() {
	return menuID;
}
public String getName() {
	return name;
}
}
