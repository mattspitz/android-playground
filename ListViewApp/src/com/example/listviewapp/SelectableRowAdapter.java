package com.example.listviewapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public abstract class SelectableRowAdapter<T extends SelectableRowItem> extends ArrayAdapter<T> {
	private final List<T> items;
	
	public SelectableRowAdapter(Context context, int resource,
			int textViewResourceId, List<T> items) {
		super(context, resource, textViewResourceId, items);
		this.items = Collections.unmodifiableList(items);
	}

	public Collection<T> getSelectedItems() {
		Collection<T> selected = new ArrayList<T>();
		for (T item : items)
			if (item.isSelected()) selected.add(item);
		return selected;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = super.getView(position,  convertView, parent);

		final T item = items.get(position);
		/* set the clickable event for the parent */
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				item.toggle();
				notifyDataSetChanged();
			}
		});

		/* deactivate all children clicks to have them propagate to the parent! */
		for (View child : AndroidHelpers.getAllChildren(view, true))
			child.setClickable(false);
		
		/* handle the drawing however our subclass specifies it */
		drawItemInView(view, item);
		
		return view;
	}
	
	protected abstract void drawItemInView(View view, T item);
}
