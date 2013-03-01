package com.twt.party;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * @author boelroy
 *这个Acitvity是继承的SlidingFramentActivity。主要是此Activity封装了
 *侧边栏的布局，是基于开源项目slidingMenu的开
 */
public class MainActivity extends SlidingFragmentActivity {

	SlidingMenu sm;
	protected MainBehindFrament mFrag;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_main);
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		mFrag = new MainBehindFrament();
		t.replace(R.id.menu_frame, mFrag);
		t.commit();

		// customize the SlidingMenu
		sm = getSlidingMenu();
			sm.setShadowWidthRes(R.dimen.shadow_width);
			//sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		sm.showMenu();
		return super.onOptionsItemSelected(item);
	}
}
