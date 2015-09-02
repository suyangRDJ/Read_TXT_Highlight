package com.example.read_txt_highlight;

public class Item{

	public static final int ITEM = 0;
	public static final int SECTION = 1;

	public final int type;
	public final String title;
	public final String content;

	public int sectionPosition;
	public int listPosition;

	
	public Item(int type, String title,String content) {
	    this.type = type;
	    this.title = title;
	    this.content = content;
	}
	
	@Override 
	public String toString() {
		return title;
	}
}


