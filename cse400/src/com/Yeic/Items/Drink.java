package com.Yeic.Items;
/**
 * 
 * Drink class
 *
 */
public class Drink {
	/**
	 * Invariants:
	 * drinkID must be positive.
	 * @invariant drinkID>-1
	 * 
	 */
private int drinkID;
private String name;
public Drink(int drinkID, String name) {
	super();
	this.drinkID = drinkID;
	this.name = name;
}

public Drink(String name) {
	super();
	this.name = name;
}

public int getDrinkID() {
	return drinkID;
}public String getName() {
	return name;
}
@Override
public boolean equals(Object o) {
	if(o instanceof Drink){
		Drink d=(Drink)o;
		if(d.getDrinkID()==this.drinkID)return true;
	}
	return false;
}
}
