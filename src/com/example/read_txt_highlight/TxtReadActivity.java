package com.example.read_txt_highlight;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.read_txt_highlight.PinnedSectionListView.PinnedSectionListAdapter;

public class TxtReadActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		InputStream inputStream = getResources().openRawResource(R.raw.new4);
		ArrayList<UnitData> unitDatas = TxtReadMethod.getString(inputStream);
		setListAdapter(new SimpleAdapter(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				unitDatas));

	}

	static class SimpleAdapter extends ArrayAdapter<Item> implements
			PinnedSectionListAdapter {

		ArrayList<UnitData> unitDatas = new ArrayList<UnitData>();
		private static final int[] COLORS = new int[] { R.color.orange_light,
				R.color.blue_light, R.color.red_light };

		public SimpleAdapter(Context context, int resource,
				int textViewResourceId, ArrayList<UnitData> unitDatas) {
			super(context, resource, textViewResourceId);
			this.unitDatas = unitDatas;
			generateDataset('1', '3', false);

		}

		public void generateDataset(char from, char to, boolean clear) {

			if (clear)
				clear();

			final int sectionsNumber = to - from + 1;
			prepareSections(sectionsNumber);

			int sectionPosition = 0, listPosition = 0;
			for (char i = 0; i < sectionsNumber; i++) {
				UnitData unitData = unitDatas.get(i);
				Item section = new Item(Item.SECTION, "Unit "
						+ String.valueOf(i + 1), "");
				section.sectionPosition = sectionPosition;
				section.listPosition = listPosition++;
				onSectionAdded(section, sectionPosition);
				add(section);

				ArrayList<Article> articles = unitData.getArticle();
				int itemsNumber = articles.size();
				for (int j = 0; j < itemsNumber; j++) {
					Article article = articles.get(j);
					Item item = new Item(Item.ITEM, article.getTitle(),
							article.getContent());
					item.sectionPosition = sectionPosition;
					item.listPosition = listPosition++;
					add(item);
				}

				sectionPosition++;
			}
		}

		protected void prepareSections(int sectionsNumber) {
		}

		protected void onSectionAdded(Item section, int sectionPosition) {
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = (TextView) super.getView(position, convertView,
					parent);
			view.setTextColor(Color.DKGRAY);
			view.setTag("" + position);
			Item item = getItem(position);
			if (item.type == Item.SECTION) {
				view.setBackgroundColor(parent.getResources().getColor(
						COLORS[item.sectionPosition % COLORS.length]));
			} else {
				view.setTextSize(14);
			}
			return view;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			return getItem(position).type;
		}

		@Override
		public boolean isItemViewTypePinned(int viewType) {
			return viewType == Item.SECTION;
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Item item = (Item) getListView().getAdapter().getItem(position);
		if (item != null && !(item.content).equals("")) {
			Intent contentIntent = new Intent(TxtReadActivity.this,
					ArticleContentActivity.class);
			contentIntent.putExtra("content", item.content);
			contentIntent.putExtra("title", item.title);
			startActivity(contentIntent);
		}
	}
}