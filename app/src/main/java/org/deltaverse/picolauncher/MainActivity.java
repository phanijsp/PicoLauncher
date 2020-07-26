package org.deltaverse.picolauncher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphCardView;
import soup.neumorphism.ShapeType;

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
        setContentView(R.layout.activity_main);
        Log.i("Here","oncreate");

        Window w = getWindow();
        w.setStatusBarColor(ContextCompat.getColor(this, R.color.default_background));
        w.setNavigationBarColor(ContextCompat.getColor(this, R.color.default_background));
//        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        apps = getUpdatedList();
        filtered_List = new ArrayList<>();
        root_layout = (ConstraintLayout) findViewById(R.id.root_layout);
        neumorphCardView = (NeumorphCardView) findViewById(R.id.neu);
        editText = (EditText) findViewById(R.id.edit_text);
        listView = (ListView) findViewById(R.id.apps_listview);

        editTextTouchHandler();
        editTextHandler();
        listItemClickHandler();
    }
    public  void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
    public void listItemClickHandler(){
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
    public void editTextTouchHandler(){
       editText.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               apps = getUpdatedList();
               return false;
           }
       });
    }
    public ArrayList<AppData> getUpdatedList(){
        //loading_apps
        final ArrayList<AppData> appData_ArrayList = new ArrayList<>();
        new Thread(){
            @Override
            public void run() {
                packageManager = getPackageManager();
                Intent i = new Intent(Intent.ACTION_MAIN, null);
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> availableActivities = packageManager.queryIntentActivities(i, 0);
                for(ResolveInfo resolveInfo : availableActivities){
                    System.out.println(resolveInfo.loadLabel(packageManager)+" "+resolveInfo.activityInfo.packageName);
                    appData_ArrayList.add(new AppData(resolveInfo.loadLabel(packageManager).toString(),resolveInfo.activityInfo.packageName,resolveInfo.loadIcon(packageManager)));
                }
            }
        }.start();
        return appData_ArrayList;
    }
    public void editTextHandler(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("Here before",s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("Here",s.toString());
                filtered_List = new ArrayList<>();
                if(s.toString().trim().length()<1){

                }else{
                    for(AppData app : apps){
                        if(app.getLabel().toLowerCase().trim().contains(s.toString().toLowerCase().trim())){
                            filtered_List.add(app);
                            Log.i("Here",app.getLabel());
                        }
                    }
                }
                adapter = new appList_Adapter(getApplicationContext(), filtered_List);
                listView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.i("Here","backpressed");
    }

    @Override
    protected void onRestart() {
        Log.i("Here","onrestart");

        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i("Here","onresume");

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
        },100);

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("Here","onpause");

        params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        neumorphCardView.setLayoutParams(params);
        neumorphCardView.setVisibility(View.INVISIBLE);

        super.onPause();

    }

    @Override
    protected void onStart() {
        Log.i("Here","onstart");

        super.onStart();
    }

    @Override
    protected void onDestroy() {
        Log.i("Here","ondestroy");

        super.onDestroy();
    }
}