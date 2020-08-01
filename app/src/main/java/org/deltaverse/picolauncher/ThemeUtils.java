package org.deltaverse.picolauncher;

import android.content.Context;
import android.widget.Toast;

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
	private int[] themes = {ThemeDefault, ThemeRed, ThemeBlue,
			CustomTheme_1, CustomTheme_2, CustomTheme_3,
			CustomTheme_4, CustomTheme_5, CustomTheme_6,
			CustomTheme_7, CustomTheme_8, CustomTheme_9,
			CustomTheme_10, CustomTheme_11};
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
	}

	public ArrayList<ThemeObject> getThemes() {
		ArrayList<ThemeObject> themeObjects = new ArrayList<>();
		for (int theme : themes) {
			themeObjects.add(new ThemeObject(theme));
		}
		return themeObjects;
	}

}
