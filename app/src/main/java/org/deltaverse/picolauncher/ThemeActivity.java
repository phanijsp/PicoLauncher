package org.deltaverse.picolauncher;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import soup.neumorphism.NeumorphCardView;

public class ThemeActivity extends AppCompatActivity {

	GridView gridView;
	ArrayList<ThemeObject> themeObjects;
	ImageView backButton;
	GridAdapter gridAdapter;
	ThemeUtils themeUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theme);
		getWindow().setStatusBarColor(getResources().getColor(R.color.white));
		themeObjects = new ArrayList<>();
		gridView = findViewById(R.id.grid_view);
		backButton = findViewById(R.id.back_button);

		themeUtils = new ThemeUtils(getApplicationContext());
		themeObjects = themeUtils.getThemes();
		gridAdapter = new GridAdapter(getApplicationContext(), themeObjects, themeUtils.getTheme());
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
				gridAdapter.updateTheme(themeObjects.get(position).getTheme());
				gridAdapter.notifyDataSetChanged();
				themeUtils.setTheme(themeObjects.get(position).getTheme());
			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(ThemeActivity.this, MainActivity.class);
		startActivity(i);
		finish();
	}
}