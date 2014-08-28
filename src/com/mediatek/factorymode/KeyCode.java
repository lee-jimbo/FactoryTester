package com.mediatek.factorymode;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.view.Window;

public class KeyCode extends Activity {

	public static final int KEY_START = 0;
	public static final int KEY_MENU = 0;
	public static final int KEY_HOME = 1;
	public static final int KEY_BACK = 2;
	public static final int KEY_SEARCH = 3;
	public static final int KEY_VOLUME_DOWN = 4;
	public static final int KEY_VOLUME_UP = 5;
	public static final int KEY_MAX = 6;

	private GridView gridview;
	ArrayList<HashMap<String, Object>> lstImageItem;
	SimpleAdapter saImageItems;
	Button succesButton ;
	Button failButton ;

	private boolean keymenutested = false;
	private boolean keyhometested = false;
	private boolean keybacktested = false;
	private boolean keysearchtested = false;
	private boolean keyvldtested = false;
	private boolean keyvlutested = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keycode);
		gridview = (GridView) this.findViewById(R.id.keycode_grid);		
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		saImageItems = new SimpleAdapter(this,
				lstImageItem,
				R.layout.keycode_grid,
				new String[] { "imageView" },
				new int[] { R.id.imgview });
		succesButton = (Button)this.findViewById(R.id.keycode_bt_ok);
		failButton = (Button)this.findViewById(R.id.keycode_bt_failed);
		
		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_HOMEKEY_DISPATCHED);

		succesButton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(KeyCode.this, FactoryMode.class);
				setResult(RESULT_OK,intent);
				finish();
			}
			
		});
		failButton.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(KeyCode.this, FactoryMode.class);
				setResult(RESULT_CANCELED,intent);
				finish();
			}
			
		});
		// gridview.setOnItemClickListener(new ItemClickListener());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// int selectkey = 0;
		// TODO Auto-generated method stub
		HashMap<String, Object> map = new HashMap<String, Object>();
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			// selectkey = KEY_MENU;
			if (!keymenutested) {
				map.put("imageView", R.drawable.menu);
				lstImageItem.add(map);
				keymenutested = true;
			}
			break;
		case KeyEvent.KEYCODE_HOME:
			// selectkey = KEY_HOME;
			if (!keyhometested) {
				map.put("imageView", R.drawable.home);
				lstImageItem.add(map);
				keyhometested = true;
			}
			break;
		case KeyEvent.KEYCODE_BACK:
			// selectkey = KEY_BACK;
			if (!keybacktested) {
				map.put("imageView", R.drawable.back);
				lstImageItem.add(map);
				keybacktested = true;
			}
			break;
		case KeyEvent.KEYCODE_SEARCH:
			// selectkey = KEY_SEARCH;
			if (!keysearchtested) {
				map.put("imageView", R.drawable.search);
				lstImageItem.add(map);
				keysearchtested = true;
			}
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			// selectkey = KEY_VOLUME_DOWN;
			if (!keyvldtested) {
				map.put("imageView", R.drawable.vldown);
				lstImageItem.add(map);
				keyvldtested = true;
			}
			break;
		case KeyEvent.KEYCODE_VOLUME_UP:
			// selectkey = KEY_VOLUME_UP;
			if (!keyvlutested) {
				map.put("imageView", R.drawable.vlup);
				lstImageItem.add(map);
				keyvlutested = true;
			}
			break;
		}
		gridview.setAdapter(saImageItems);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
		case KeyEvent.KEYCODE_HOME:
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_SEARCH:
		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_VOLUME_UP:
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
	}

}
