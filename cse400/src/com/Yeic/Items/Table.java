package com.Yeic.Items;
/**
 * 
 * Table class
 *
 */
public class Table {
	/**
	 * Invariants:
	 * tableID must be positive.
	 * @invariant tableID>-1
	 * 
	 */
	private int tableID;
	private String description;
	private String name;
	private boolean isEmpty;
public Table(int tableID,String description,String name) {
	this.tableID=tableID;
	this.description=description;
	this.name=name;
}
public String getName() {
	return name;
}
public boolean isEmpty() {
	return isEmpty;
}
public String getDescription() {
	return description;
}public int getTableID() {
	return tableID;
}
public void setDescription(String description) {
	this.description = description;
}public void setEmpty(boolean isEmpty) {
	this.isEmpty = isEmpty;
}
public Table(String description, String name) {
	super();
	this.description = description;
	this.name = name;
}

}
