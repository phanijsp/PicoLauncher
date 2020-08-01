package org.deltaverse.picolauncher;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ThemeActivity extends AppCompatActivity {

	GridView gridView;
	ArrayList<ThemeObject> themeObjects;
	ImageView backButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theme);
		getWindow().setStatusBarColor(getResources().getColor(R.color.white));
		themeObjects = new ArrayList<>();
		gridView = findViewById(R.id.grid_view);
		backButton = findViewById(R.id.back_button);

		ThemeUtils themeUtils = new ThemeUtils(getApplicationContext());
		themeObjects = themeUtils.getThemes();
		GridAdapter gridAdapter = new GridAdapter(getApplicationContext(), themeObjects, themeUtils.getTheme());
		gridView.setAdapter(gridAdapter);
		gridAdapter.notifyDataSetChanged();

		gridItemClickHandler();
		backButtonClickHandler();
	}

	public void backButtonClickHandler(){
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ThemeActivity.this.onBackPressed();
			}
		});
	}
	public void gridItemClickHandler() {
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
	}
}