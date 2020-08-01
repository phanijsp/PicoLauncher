package org.deltaverse.picolauncher;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ThemeActivity extends AppCompatActivity {

	GridView gridView;
	ArrayList<ThemeObject> themeObjects;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theme);

		themeObjects = new ArrayList<>();
		gridView = findViewById(R.id.grid_view);


		ThemeUtils themeUtils = new ThemeUtils(getApplicationContext());
		themeObjects = themeUtils.getThemes();
		GridAdapter gridAdapter = new GridAdapter(getApplicationContext(), themeObjects, themeUtils.getTheme());
		gridView.setAdapter(gridAdapter);
		gridAdapter.notifyDataSetChanged();

		gridItemClickHandler();
	}

	public void gridItemClickHandler() {
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
	}
}