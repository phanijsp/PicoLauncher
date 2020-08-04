package org.deltaverse.picolauncher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.StyleableRes;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
	int[] attrs = {R.attr.colorPrimary,
			R.attr.colorPrimaryDark,
			R.color.colorAccent,
			android.R.attr.windowBackground,
			R.attr.neu_light_color,
			R.attr.neu_dark_color,
			R.attr.background_color,
			android.R.attr.statusBarColor,
			android.R.attr.navigationBarColor};
	private ArrayList<ThemeObject> themeObjects;
	private Context context;
	private int CurrentTheme;

	GridAdapter(Context context, ArrayList<ThemeObject> themeObjects, int CurrentTheme) {
		this.context = context;
		this.themeObjects = themeObjects;
		this.CurrentTheme = CurrentTheme;
	}

	@Override
	public int getCount() {
		return themeObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void updateTheme(int CurrentTheme){
		this.CurrentTheme = CurrentTheme;
	}

	@SuppressLint({"ViewHolder", "InflateParams"})
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		convertView = layoutInflater.inflate(R.layout.theme_item, null);
		ImageView imageView = convertView.findViewById(R.id.theme_icon);

		@SuppressLint("Recycle") TypedArray typedArray = context.obtainStyledAttributes(themeObjects.get(position).getTheme(), attrs);
		@StyleableRes int i = 3;
		imageView.setBackgroundColor(typedArray.getColor(i, Color.BLACK));
		if (themeObjects.get(position).getTheme() == CurrentTheme) {
			imageView.setImageResource(R.drawable.ic_baseline_check_circle_24);
		}


		return convertView;
	}
}
