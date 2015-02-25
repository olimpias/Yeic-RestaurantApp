package com.Yeic.GuiItem;
/**
 * This class is used for navigator.Title and imageId is kept in this class
 * 
 *
 */
public class RowNagivatorBlock {
private int imageId;
private String title;
public int getImageId() {
	return imageId;
}public String getTitle() {
	return title;
}
public RowNagivatorBlock(int imageId, String title) {
	super();
	this.imageId = imageId;
	this.title = title;
}

}
