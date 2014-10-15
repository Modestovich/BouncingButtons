package com.example.bouncingbuttons;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BounceActivity extends Activity {

	// Button needs
	private List<MyButton> sButton = new ArrayList<MyButton>();
	private int[][] icons = new int[10][2];
	private int[] bg_id = { R.drawable.btn_1, R.drawable.btn_2,
			R.drawable.btn_3, R.drawable.btn_4, R.drawable.btn_5,
			R.drawable.btn_6, R.drawable.btn_7, R.drawable.btn_8,
			R.drawable.btn_9, R.drawable.btn_10 };
	private int[] icon_id = { R.drawable.ic_1, R.drawable.ic_2,
			R.drawable.ic_3, R.drawable.ic_4, R.drawable.ic_5, R.drawable.ic_6,
			R.drawable.ic_7, R.drawable.ic_8, R.drawable.ic_9, R.drawable.ic_10 };
	private ListView listView;
	// TimeView
	private TextView curTime;
	//
	private int itemHeight;
	private int itemWidth;
	//
	private AlertDialog.Builder alert;
	//
	RelativeLayout firstLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bounce);
		populateButtonListNaddListeners();
		populateListView();
		showCurTime();
	}
	
	private void populateListView() {
		ArrayAdapter<MyButton> adapter = new MyButtonAdapter();
		listView = (ListView) findViewById(R.id.myListView);
		listView.setAdapter(adapter);
	}

	private void populateButtonListNaddListeners() {
		prepareIcons();
		String prepareName = "Кнопка ";
		for (int i = 0; i < 10; i++)
			sButton.add(new MyButton(makeRandomIcon(), prepareName + (i + 1),
					0, bg_id[i]));
		MakeOnEventListener();
	}

	private int makeRandomIcon() {
		Random r = new Random();
		int num;
		while (true) {
			num = r.nextInt(10);
			if (icons[num][1] == 1) {
				icons[num][1] = 0;
				return icons[num][0];
			}
		}
	}

	private void prepareIcons() {
		for (int i = 0; i < 10; i++) {
			icons[i][0] = icon_id[i];
			icons[i][1] = 1;
		}
	}
	
	private void MakeOnEventListener() {
		listView = (ListView) findViewById(R.id.myListView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {
				TextView countClick = (TextView) viewClicked
						.findViewById(R.id.myNumClick);
				String str = (String) countClick.getText();
				int curNum = Integer.parseInt(str) + 1;
				countClick.setText(String.valueOf(curNum));
				sButton.get(position).setBut_click(curNum);
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
					View viewClicked, int position, long id) {
				CreateAlertDialog(viewClicked,position);
				alert.show();
				return false;
			}

			private void CreateAlertDialog(final View viewClicked,final int position) {
				alert = new AlertDialog.Builder(BounceActivity.this);
				alert.setTitle("New button name");
				alert.setMessage("Put new name of button");
				final EditText input = new EditText(BounceActivity.this);
				alert.setView(input);
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Editable value = input.getText();
								if(value.length()>0){
									((TextView)viewClicked.findViewById(R.id.myButNumber)).setText(value);
									sButton.get(position).setBut_num(String.valueOf(value));
								}
							}
						});

			}
		});
		listView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				
				ListView lv = (ListView) v;
				View curView = getCurView(lv, event);
				if (curView != null) {
					TextView textViewColorBut = (TextView) curView
							.findViewById(R.id.myButNumber);
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						textViewColorBut.setTextColor(Color.BLACK);
						break;
					case MotionEvent.ACTION_UP:
						textViewColorBut.setTextColor(Color.WHITE);
						break;
					case MotionEvent.ACTION_MOVE:
						textViewColorBut.setTextColor(Color.WHITE);
						break;
					case MotionEvent.ACTION_CANCEL:
						textViewColorBut.setTextColor(Color.WHITE);
						break;
					}
				}
				return false;
			}

			private View getCurView(ListView listView, MotionEvent motionEvent) {
				View forRetView = null;
				Rect rect = new Rect();
				int childCount = listView.getChildCount();
				int[] listViewCoords = new int[2];
				listView.getLocationOnScreen(listViewCoords);
				int x = (int) motionEvent.getRawX() - listViewCoords[0];
				int y = (int) motionEvent.getRawY() - listViewCoords[1];
				View child;
				for (int i = 0; i < childCount; i++) {
					child = listView.getChildAt(i);
					child.getHitRect(rect);
					if (rect.contains(x, y)) {
						forRetView = child;
						break;
					}
				}
				return forRetView;
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				itemHeight = (listView.getHeight() / 6);
				itemWidth = (listView.getWidth());
				//final float d = (float) ((float) itemWidth / (2.0 * (float) itemHeight));
				try {
					//for the first element
					RelativeLayout curView = (RelativeLayout) listView
							.getChildAt(0);
					firstLayout = curView;
					float dw = (float)curView.getBottom()/(float)itemHeight;
					curView.setPivotX(itemWidth/2);
					curView.setPivotY(itemHeight);
					curView.setScaleX(dw);
					curView.setScaleY(dw);
					for(int i=1;i<visibleItemCount-1;i++){
						RelativeLayout cView = (RelativeLayout) listView
								.getChildAt(i);
						cView.setScaleX(1.0f);
						cView.setScaleY(1.0f);
					}
					//for the last element
					RelativeLayout lastView = (RelativeLayout) listView.getChildAt(visibleItemCount-1);
					int curLastHeight = listView.getHeight()-lastView.getTop();
					dw = (float)curLastHeight/(float)itemHeight;
					lastView.setPivotX(itemWidth/2);
					lastView.setScaleX(dw);
	//				lastView.setX(((1.0f-dw)*itemWidth)/32);	
					lastView.setPivotY(0);
					lastView.setScaleY((float)curLastHeight/(float)itemHeight);
				} catch (Exception c) {
					Log.d("EXCEPTION", "Message");
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {	
			}
		});
	}

	private void showCurTime() {
		curTime = (TextView) findViewById(R.id.curTimeTextView);
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Thread() {
					@Override
					public void run() {
						curTime.setText(getTime());
					}

					private CharSequence getTime() {
						Calendar c = Calendar.getInstance();
						int hours = c.get(Calendar.HOUR_OF_DAY);
						int minutes = c.get(Calendar.MINUTE);
						int seconds = c.get(Calendar.SECOND);
						return String.format("%02d:%02d:%02d", hours, minutes,
								seconds);
					}
				});
			}
		}, 1000, 1000);
	}

	private class MyButtonAdapter extends ArrayAdapter<MyButton> {
		public MyButtonAdapter() {
			super(BounceActivity.this, R.layout.mybutton, sButton);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.mybutton,
						parent, false);
			}
			MyButton currentButton = sButton.get(position);
			// Filling TextViews & ImageView
			ImageView imageView = (ImageView) itemView
					.findViewById(R.id.myIcon);
			imageView.setImageResource(currentButton.getIcon_id());
			//
			TextView butNum = (TextView) itemView
					.findViewById(R.id.myButNumber);
			butNum.setText("" + currentButton.getBut_num());
			//
			TextView nutClick = (TextView) itemView
					.findViewById(R.id.myNumClick);
			nutClick.setText("" + currentButton.getBut_click());
			//
			RelativeLayout myLayout = (RelativeLayout) itemView
					.findViewById(R.id.myButton);
			myLayout.setBackgroundResource(currentButton.getBg_id());
			return itemView;
		}
	}
}