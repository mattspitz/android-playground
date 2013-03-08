package com.example.listviewapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TheListActivity extends ListActivity {

	private static final String TAG = "TheListActivity";

	private ListView mListView;
	private SheepleAdapter mSheepleAdapter;

	private static class Sheeperson extends SelectableRowItem {
		private final String name;
		private final String woolColor;

		public Sheeperson(String name, String woolColor) {
			this.name = name;
			this.woolColor = woolColor;
		}

		public String getName() { return name; }
		public String getWoolColor() { return woolColor; }

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

	public class SheepleAdapter extends SelectableRowAdapter<Sheeperson> {
		/* required because a default constructor isn't defined in ArrayAdapter! */
		public SheepleAdapter(Context context, int resource,
				int textViewResourceId, List<Sheeperson> items) {
			super(context, resource, textViewResourceId, items);
		}

		@Override
		protected void drawItemInView(View view, Sheeperson item) {
			TextView itemView = (TextView) view.findViewById(R.id.sheep_name);
			itemView.setText(String.format("%s (%s)", item.getName(), item.getWoolColor()));

			CheckBox checkBox = (CheckBox) view.findViewById(R.id.the_checkbox);
			checkBox.setChecked(item.isSelected());
		}

		@Override
		protected ItemClickListener<Sheeperson> getItemClickListenerForItem(final Sheeperson item) {
			return new ItemClickListener<Sheeperson>() {
				@Override
				public void onClick(View view, SelectableRowAdapter<Sheeperson> adapter) {
					Toast.makeText(TheListActivity.this, String.format("Selected %s!", item.getName()), Toast.LENGTH_SHORT).show();
					startActivity(new Intent(TheListActivity.this, MainActivity.class));
				}
			};
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
