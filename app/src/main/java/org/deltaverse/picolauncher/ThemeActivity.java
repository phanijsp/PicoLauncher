package org.deltaverse.picolauncher;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;

public class ThemeActivity extends AppCompatActivity {

	GridView gridView;
	ArrayList<ThemeObject> themeObjects;
	ImageView backButton;
	GridAdapter gridAdapter;
	ThemeUtils themeUtils;
	RewardedAd rewardedAd;
	boolean ad_loaded = false;

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
		initAd();
	}

	public void initAd() {
		rewardedAd = new RewardedAd(this, "ca-app-pub-2930601886283388/1971312861");
		RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
			@Override
			public void onRewardedAdLoaded() {
				ad_loaded = true;
				super.onRewardedAdLoaded();
			}
		};
		rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
	}

	public void backButtonClickHandler() {
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
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				if (ad_loaded) {
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
								case DialogInterface.BUTTON_POSITIVE:
									rewardedAd.show(ThemeActivity.this, new RewardedAdCallback() {
										@Override
										public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
											Log.i("Here", "onUserEarnedReward: ");
											gridAdapter.updateTheme(themeObjects.get(position).getTheme());
											gridAdapter.notifyDataSetChanged();
											themeUtils.setTheme(themeObjects.get(position).getTheme());
										}
									});
									break;
								case DialogInterface.BUTTON_NEGATIVE:
									break;
							}
						}
					};
					AlertDialog.Builder builder = new AlertDialog.Builder(ThemeActivity.this);
					builder.setMessage("Watch Ad to save changes").setPositiveButton("Okay", dialogClickListener)
							.setNegativeButton("Cancel", dialogClickListener).show();
				} else {
					gridAdapter.updateTheme(themeObjects.get(position).getTheme());
					gridAdapter.notifyDataSetChanged();
					themeUtils.setTheme(themeObjects.get(position).getTheme());
				}


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