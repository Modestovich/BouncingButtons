package com.example.bouncingbuttons;

public class MyButton {

	public void setBut_click(int but_click) {
		this.but_click = but_click;
	}

	public void setBut_num(String but_num) {
		this.but_num = but_num;
	}

	private int icon_id;
	private String but_num;
	private int but_click;
	private int bg_id;

	public MyButton(int icon_id, String but_num, int but_click,int bg_id) {
		super();
		this.icon_id = icon_id;
		this.but_num = but_num;
		this.but_click = but_click;
		this.bg_id = bg_id;
	}

	public int getIcon_id() {
		return icon_id;
	}

	public String getBut_num() {
		return but_num;
	}

	public int getBut_click() {
		return but_click;
	}

	public int getBg_id() {
		return bg_id;
	}
}
