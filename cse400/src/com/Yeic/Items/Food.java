package com.Yeic.Items;
/**
 * 
 * Food class
 *
 */
public class Food {
	/**
	 * Invariants:
	 * foodID must be positive.
	 * @invariant foodID>-1
	 * 
	 */
private int foodID;
private String name;
public Food(int foodID, String name) {
	super();
	this.foodID = foodID;
	this.name = name;
}

public Food(String name) {
	super();
	this.name = name;
}

public int getFoodID() {
	return foodID;
}
public String getName() {
	return name;
}
@Override
public boolean equals(Object o) {
	if(o instanceof Food){
		Food f=(Food)o;
		if(f.getFoodID()==this.getFoodID())return true;
	}
	return true;
}
}
