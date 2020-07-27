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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

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
	ArrayList<AppData> apps;
	ArrayList<AppData> filtered_List;
	ListView listView;
	appList_Adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.DefaultTheme);
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

		editTextTouchHandler();
		editTextHandler();
		listItemClickHandler();
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
	}

	@Override
	protected void onPause() {
		params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
		neumorphCardView.setLayoutParams(params);
		neumorphCardView.setVisibility(View.INVISIBLE);

		super.onPause();

	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}
}