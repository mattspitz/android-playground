package com.example.listviewapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TheListActivity extends ListActivity {

	private static final String TAG = "TheListActivity";

	private ListView mListView;
	private SheepleAdapter mSheepleAdapter;

	private static class Sheeperson {
		private final String name;
		private final String woolColor;
		private boolean isSelected = false;

		public Sheeperson(String name, String woolColor) {
			this.name = name;
			this.woolColor = woolColor;
		}

		public void toggle() { isSelected = !isSelected; }

		public String getName() { return name; }
		public String getWoolColor() { return woolColor; }
		public boolean isSelected() { return isSelected; }

		@Override
		public String toString() {
			return "Sheeperson [name=" + name + ", woolColor=" + woolColor
					+ ", isSelected=" + isSelected + "]";
		}
	}

	private List<Sheeperson> getSheeple() {
		Random r = new Random(123);

		String[] names = {
				"Baaabby Kennedy",
				"Herbert Hoofer",
				"Ewegene O'Neill",
				"Lambda Calculus",
				"Rambo",
				"Mutton Damon",
				"Woolly Mammal",
				"Lambchop",
				"Shari",
				"Sheary",
				"Mutton Chops",
				"Sideburns"
		};
		String[] woolColors = { "White", "Black" };

		List<Sheeperson> sheeple = new ArrayList<Sheeperson>();
		for (String name : names)
			sheeple.add(new Sheeperson(name, woolColors[r.nextInt(woolColors.length)]));
		for (String name : names)
			sheeple.add(new Sheeperson(name + " Jr.", woolColors[r.nextInt(woolColors.length)]));
		return sheeple;
	}

	private static Collection<View> getAllChildren(View parentView, boolean recursive) {
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

	public class SheepleAdapter extends ArrayAdapter<Sheeperson> {
		private final List<Sheeperson> items;

		public SheepleAdapter(Context context, int resource, int textViewResourceId, List<Sheeperson> items) {
			super(context, resource, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = super.getView(position,  convertView, parent);

			final Sheeperson item = items.get(position);
			/* set the clickable event for the parent */
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					item.toggle();
					notifyDataSetChanged();
				}
			});

			/* deactivate all children clicks to have them propagate to the parent! */
			for (View child : getAllChildren(view, true))
				child.setClickable(false);

			if (item != null) {
				TextView itemView = (TextView) view.findViewById(R.id.sheep_name);
				itemView.setText(String.format("%s (%s)", item.getName(), item.getWoolColor()));

				CheckBox checkBox = (CheckBox) view.findViewById(R.id.the_checkbox);
				checkBox.setChecked(item.isSelected());
			}
			return view;
		}

		public Collection<Sheeperson> getSelectedItems() {
			Collection<Sheeperson> selected = new ArrayList<Sheeperson>();
			for (Sheeperson item : items)
				if (item.isSelected())
					selected.add(item);
			return selected;
		}
	}

	public void buttonOnClick(View view) {
		Collection<Sheeperson> selectedSheeple = mSheepleAdapter.getSelectedItems();
		Toast.makeText(this, String.format("Selected %d sheeple!", selectedSheeple.size()), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_the_list);

		// Configure our data->view mapper
		mSheepleAdapter = new SheepleAdapter(this, R.layout.list_layout_row, R.id.sheep_name, getSheeple());
		setListAdapter(mSheepleAdapter);

		// Make it a multi-chooser thingy
		mListView = getListView();
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.the_list, menu);
		return true;
	}

}
