package com.example.listviewapp;

import java.util.ArrayList;
import java.util.Collection;

import android.view.View;
import android.view.ViewGroup;

public final class AndroidHelpers {
	private AndroidHelpers() {}
	
	public static Collection<View> getAllChildren(View parentView, boolean recursive) {
		Collection<View> allViews = new ArrayList<View>();
		if (parentView instanceof ViewGroup) {
			for(int i = 0; i < ((ViewGroup)parentView).getChildCount(); ++i) {
				View child = ((ViewGroup)parentView).getChildAt(i);
				allViews.add(child);

				if (recursive)
					allViews.addAll(getAllChildren(child, recursive));
			}
		}
		return allViews;
	}
}
