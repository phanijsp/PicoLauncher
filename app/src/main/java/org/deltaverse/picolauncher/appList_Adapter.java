package org.deltaverse.picolauncher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class appList_Adapter extends BaseAdapter {
	private Context context;
	private ArrayList<AppData> appDataArrayList;
	appList_Adapter(Context context, ArrayList<AppData> appDataArrayList){
		this.context = context;
		this.appDataArrayList = appDataArrayList;
	}



	@Override
	public int getCount() {
		return appDataArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.app_layout, null);

		TextView titleText = (TextView) view.findViewById(R.id.app_label);
		ImageView imageView = (ImageView) view.findViewById(R.id.app_icon);

		titleText.setText(appDataArrayList.get(position).getLabel());
		imageView.setImageDrawable(appDataArrayList.get(position).getIcon());

		return view;
	}
}
