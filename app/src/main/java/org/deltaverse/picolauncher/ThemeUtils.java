package org.deltaverse.picolauncher;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.StyleableRes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ThemeUtils {
	private int ThemeDefault = R.style.DefaultTheme;
	private int ThemeRed = R.style.ThemeRed;
	private int ThemeBlue = R.style.ThemeBlue;
	private int CustomTheme_1 = R.style.customTheme_1;
	private int CustomTheme_2 = R.style.customTheme_2;
	private int CustomTheme_3 = R.style.customTheme_3;
	private int CustomTheme_4 = R.style.customTheme_4;
	private int CustomTheme_5 = R.style.customTheme_5;
	private int CustomTheme_6 = R.style.customTheme_6;
	private int CustomTheme_7 = R.style.customTheme_7;
	private int CustomTheme_8 = R.style.customTheme_8;
	private int CustomTheme_9 = R.style.customTheme_9;
	private int CustomTheme_10 = R.style.customTheme_10;
	private int CustomTheme_11 = R.style.customTheme_11;
	private int CustomTheme_12 = R.style.customTheme_12;
	private int CustomTheme_13 = R.style.customTheme_13;
	private int CustomTheme_14 = R.style.customTheme_14;
	private int CustomTheme_15 = R.style.customTheme_15;
	private int CustomTheme_16 = R.style.customTheme_16;
	private int CustomTheme_17 = R.style.customTheme_17;
	private int CustomTheme_18 = R.style.customTheme_18;
	private int[] themes = {ThemeDefault, ThemeRed, ThemeBlue,
			CustomTheme_1, CustomTheme_2, CustomTheme_3,
			CustomTheme_4, CustomTheme_5, CustomTheme_6,
			CustomTheme_7, CustomTheme_8, CustomTheme_9,
			CustomTheme_10, CustomTheme_11, CustomTheme_12,
			CustomTheme_13, CustomTheme_14, CustomTheme_15,
			CustomTheme_16, CustomTheme_17, CustomTheme_18};

	int[] attrs = {R.attr.colorPrimary,
			R.attr.colorPrimaryDark,
			R.color.colorAccent,
			android.R.attr.windowBackground,
			R.attr.neu_light_color,
			R.attr.neu_dark_color,
			R.attr.background_color,
			android.R.attr.statusBarColor,
			android.R.attr.navigationBarColor};

	private File theme_data;
	private Context context;

	ThemeUtils(Context context) {
		this.context = context;
		theme_data = new File(context.getFilesDir(), "theme_data");
	}

	public int getTheme() {
		ThemeObject theme = null;
		if (theme_data.exists()) {
			try {
				FileInputStream fileInputStream = new FileInputStream(theme_data);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				theme = (ThemeObject) objectInputStream.readObject();
				objectInputStream.close();
				fileInputStream.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (theme_data.createNewFile()) {
					FileOutputStream fileOutputStream = new FileOutputStream(theme_data);
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
					theme = new ThemeObject(ThemeDefault);
					objectOutputStream.writeObject(theme);
					objectOutputStream.close();
					fileOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (theme != null) {
			return theme.getTheme();
		} else {
			return ThemeDefault;
		}
	}

	public void setTheme(int theme) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(theme_data);
			ThemeObject themeObject = new ThemeObject(theme);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(themeObject);
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(context, "Unable to save changes", Toast.LENGTH_SHORT).show();
		}
		updateWallpaper(theme);
	}

	public void updateWallpaper(int theme){
		WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
		Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		@SuppressLint("Recycle") TypedArray typedArray = context.obtainStyledAttributes(theme, attrs);
		@StyleableRes int i = 3;
		canvas.drawColor(typedArray.getColor(i, Color.BLACK));

		try {
			wallpaperManager.setBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ThemeObject> getThemes() {
		ArrayList<ThemeObject> themeObjects = new ArrayList<>();
		for (int theme : themes) {
			themeObjects.add(new ThemeObject(theme));
		}
		return themeObjects;
	}

}
