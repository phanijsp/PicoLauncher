package org.deltaverse.picolauncher;

import android.graphics.drawable.Drawable;

public class AppData {
	private String label;
	private String name;
	private Drawable icon;
	AppData(String label, String name, Drawable icon){
		this.label = label;
		this.name = name;
		this.icon = icon;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
}
