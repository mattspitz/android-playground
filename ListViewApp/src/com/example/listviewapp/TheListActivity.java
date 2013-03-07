package com.example.listviewapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

public class TheListActivity extends ListActivity {

	private ListView mListView;
	private SheepleAdapter mSheepleAdapter;

	private static class Sheeperson {
		private final String name;
		private final String woolColor;
		private boolean isChecked = false;
		
		public Sheeperson(String name, String woolColor) {
			this.name = name;
			this.woolColor = woolColor;
		}
		
		public void toggle() { isChecked = !isChecked; }
		public String getName() { return name; }
		public String getWoolColor() { return woolColor; }
		public boolean isChecked() { return isChecked; }

		@Override
		public String toString() {
			return "Sheeperson [name=" + name + ", woolColor=" + woolColor
					+ ", isChecked=" + isChecked + "]";
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

	public class SheepleAdapter extends ArrayAdapter<Sheeperson> {
		private final List<Sheeperson> items;

		public SheepleAdapter(Context context, int resource, int textViewResourceId, List<Sheeperson> items) {
			super(context, resource, textViewResourceId, items);
			this.items = items;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position,  convertView, parent);
			Sheeperson item = items.get(position);
			if (item != null) {
				CheckedTextView itemView = (CheckedTextView) view.findViewById(R.id.sheep_name);
				itemView.setText(String.format("%s (%s)", item.getName(), item.getWoolColor()));
				itemView.setChecked(item.isChecked());
			}
			return view;
		}
	}

	public void buttonOnClick(View view) {
		SparseBooleanArray recipientsArray = mListView.getCheckedItemPositions();
		
		List<Sheeperson> selectedSheeple = new ArrayList<Sheeperson>();
		if (recipientsArray != null) {
			for (int i = 0; i<recipientsArray.size(); i++) {
				if (recipientsArray.valueAt(i))
					selectedSheeple.add(mSheepleAdapter.getItem(recipientsArray.keyAt(i)));
			}
		}
		
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
		mListView = (ListView)findViewById(android.R.id.list);
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mSheepleAdapter.getItem(position).toggle();
				mSheepleAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.the_list, menu);
		return true;
	}

}
