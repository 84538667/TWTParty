package com.twt.party;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainBehindFrament extends ListFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.sliding_behind, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter = new SampleAdapter(getActivity());
		for(int i = 0; i < 20; i++){
			adapter.add(new SampleItem("µÚ"+ (i+1) + "¿Î", R.drawable.rating_good, R.drawable.navigation_next_item));
		}
		setListAdapter(adapter);
	}
	
	private class SampleItem {
		public String tag;
		public int iconRes;
		public int navigation;
		public SampleItem(String tag, int iconRes,int navigation) {
			this.tag = tag; 
			this.iconRes = iconRes;
			this.navigation = navigation;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.lesson_item, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);
			ImageView navigation = (ImageView)convertView.findViewById(R.id.row_navigation);
			navigation.setImageResource(getItem(position).navigation);

			return convertView;
		}

	}
}
