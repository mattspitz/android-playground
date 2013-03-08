package com.example.listviewapp;

public abstract class SelectableRowItem {
	protected boolean isSelected = false;
	
	public final void toggle() { isSelected = !isSelected; }
	public final boolean isSelected() { return isSelected; }
}
