package org.deltaverse.picolauncher;

import java.io.Serializable;

public class ThemeObject implements Serializable {
	private static final long serialVersionUID = 1L;
	private int theme;

	public ThemeObject(int theme) {
		this.theme = theme;
	}

	public int getTheme() {
		return theme;
	}

	public void setTheme(int theme) {
		this.theme = theme;
	}
}
