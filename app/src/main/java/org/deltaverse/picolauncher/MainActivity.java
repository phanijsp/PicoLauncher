package org.deltaverse.picolauncher;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import soup.neumorphism.NeumorphCardView;

public class MainActivity extends AppCompatActivity {
	ConstraintLayout root_layout;
	NeumorphCardView neumorphCardView;
	ConstraintLayout.LayoutParams params;
	PackageManager packageManager;
	EditText editText;
	ImageView circle;
	ArrayList<AppData> apps;
	ArrayList<AppData> filtered_List;
	ListView listView;
	appList_Adapter adapter;
	ThemeUtils themeUtils;
	String TAG = "Here";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		themeUtils = new ThemeUtils(getApplicationContext());
		setTheme(themeUtils.getTheme());
		setContentView(R.layout.activity_main);

//		Window w = getWindow();
//		w.setStatusBarColor(ContextCompat.getColor(this, R.color.default_background));
//		w.setNavigationBarColor(ContextCompat.getColor(this, R.color.default_background));

		apps = getUpdatedList();
		filtered_List = new ArrayList<>();
		root_layout = findViewById(R.id.root_layout);
		neumorphCardView = findViewById(R.id.neu);
		editText = findViewById(R.id.edit_text);
		listView = findViewById(R.id.apps_listview);
		circle = findViewById(R.id.circle);

		editTextTouchHandler();
		editTextHandler();
		listItemClickHandler();
		root_layout_long_clickHandler();
		circle_icon_clickHandler();
	}

	public void circle_icon_clickHandler(){
		circle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					startActivity(new Intent(Intent.ACTION_VOICE_COMMAND).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	public void root_layout_long_clickHandler() {
		root_layout.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				startActivity(new Intent(MainActivity.this, ThemeActivity.class));
				finish();
				return false;
			}
		});
	}

	public void listItemClickHandler() {
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AppData appData = filtered_List.get(position);
				Intent i = packageManager.getLaunchIntentForPackage(appData.getName());
				filtered_List.clear();
				editText.setText("");
				hideKeyboard();
				startActivity(i);
			}
		});
	}

	@SuppressLint("ClickableViewAccessibility")
	public void editTextTouchHandler() {
		editText.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				apps = getUpdatedList();
				return false;
			}
		});
	}

	public ArrayList<AppData> getUpdatedList() {
		//loading_apps
		final ArrayList<AppData> appData_ArrayList = new ArrayList<>();
		new Thread() {
			@Override
			public void run() {
				packageManager = getPackageManager();
				Intent i = new Intent(Intent.ACTION_MAIN, null);
				i.addCategory(Intent.CATEGORY_LAUNCHER);
				List<ResolveInfo> availableActivities = packageManager.queryIntentActivities(i, 0);
				for (ResolveInfo resolveInfo : availableActivities) {
					appData_ArrayList.add(new AppData(resolveInfo.loadLabel(packageManager).toString(), resolveInfo.activityInfo.packageName, resolveInfo.loadIcon(packageManager)));
				}
			}
		}.start();
		return appData_ArrayList;
	}

	public void editTextHandler() {
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				filtered_List = new ArrayList<>();
				if (s.toString().trim().length() >= 1) {
					if (s.toString().trim().length() == 1) {
						for (AppData app : apps) {
							if (app.getLabel().toLowerCase().trim().startsWith(s.toString().toLowerCase().trim())) {
								filtered_List.add(app);
							}
						}
					} else {
						for (AppData app : apps) {
							if (app.getLabel().toLowerCase().trim().contains(s.toString().toLowerCase().trim())) {
								filtered_List.add(app);
							}
						}
					}

				}
				sort_filtered_List();
				adapter = new appList_Adapter(getApplicationContext(), filtered_List);
				listView.setAdapter(adapter);
			}
		});
	}

	public void sort_filtered_List() {
		if (filtered_List != null && filtered_List.size() > 0) {
			for (int i = 0; i < filtered_List.size(); i++) {
				for (int j = i + 1; j < filtered_List.size(); j++) {
					if (filtered_List.get(i).getLabel().compareTo(filtered_List.get(j).getLabel()) > 0) {
						AppData temp = filtered_List.get(i);
						filtered_List.set(i, filtered_List.get(j));
						filtered_List.set(j, temp);
					}
				}
			}
			Collections.reverse(filtered_List);
		}


	}

	public void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		}
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		params = (ConstraintLayout.LayoutParams) neumorphCardView.getLayoutParams();
		TransitionManager.beginDelayedTransition(root_layout);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				params.width = ConstraintLayout.LayoutParams.MATCH_PARENT;
				neumorphCardView.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
				neumorphCardView.setVisibility(View.VISIBLE);
				neumorphCardView.setLayoutParams(params);
			}
		}, 100);
		super.onResume();
		Log.i(TAG, "onResume: ");
	}

	@Override
	protected void onPause() {
		params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
		neumorphCardView.setLayoutParams(params);
		neumorphCardView.setVisibility(View.INVISIBLE);
		super.onPause();
		Log.i(TAG, "onPause: ");
	}

	@Override
	protected void onStart() {
		Log.i(TAG, "onStart: ");
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy: ");
		super.onDestroy();
	}
}