package com.ichg.jwc.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.widget.Toast;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.activity.setting.SettingActivity;
import com.ichg.jwc.fragment.work.HistoryWorkFragment;
import com.ichg.jwc.fragment.work.WorkTabFragment;
import com.ichg.jwc.presenter.NavigationDrawerPresenter;
import com.ichg.jwc.presenter.NavigationItemInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends ActivityBase {

	private static final int DEFAULT_PAGE_TYPE = NavigationType.WORK_LIST;

	private static final int DURING_LEAVE_WAIT = 2000;

	public static final class NavigationType {
		public static final int WORK_LIST = 1;
		public static final int WORK_HISTORY = 2;
		public static final int SETTING = 3;
		public static final int QUESTION = 4;
		public static final int ABOUT = 5;
	}

	private SparseArray<Fragment> fragmentMap;
	private SparseArray<NavigationItemInfo> itemInfoSparseArray = new SparseArray<>();
	private DrawerLayout drawerLayout;
	private boolean isClickToLeave = false;

	private NavigationDrawerPresenter drawerPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);
		fragmentMap = new SparseArray<>();
		setContentView(R.layout.activity_main);
		initNavigationDrawer();
	}

	private void initNavigationDrawer() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerPresenter = new NavigationDrawerPresenter(drawerLayout, (NavigationView) findViewById(R.id.navigation),
				new NavigationDrawerPresenter.NavigationDrawerPresenterListener() {

					@Override
					public void onNavigationItemSelected(NavigationItemInfo itemInfo) {
						if (itemInfo.pageType == NavigationType.SETTING) {
							startSetting();
						} else if (itemInfo.pageType == NavigationType.QUESTION) {
							startQuestion();
						} else if (itemInfo.pageType == NavigationType.ABOUT) {
							startAbout();
						} else {
							onNavigationDrawerItemSelected(itemInfo);
						}
					}

					@Override
					public void onProfileClick() {
						startActivity(new Intent(MainActivity.this, ProfileViewActivity.class));
					}
				}
		);

		drawerPresenter.addDrawerItems(initDrawerItemInfoList());
		drawerPresenter.selectNavigationItem(DEFAULT_PAGE_TYPE);
	}

	private List<NavigationItemInfo> initDrawerItemInfoList() {
		List<NavigationItemInfo> drawerItemList = new ArrayList<>();
		drawerItemList.add(createNavigationItemInfo(R.string.drawer_work_list, R.drawable.icon_joblist, NavigationType.WORK_LIST));
		drawerItemList.add(createNavigationItemInfo(R.string.drawer_work_history, R.drawable.icon_track, NavigationType.WORK_HISTORY));
		drawerItemList.add(createNavigationItemInfo(R.string.drawer_setting, R.drawable.icon_proset, NavigationType.SETTING));
		drawerItemList.add(createNavigationItemInfo(R.string.drawer_question, R.drawable.icon_question, NavigationType.QUESTION));
		drawerItemList.add(createNavigationItemInfo(R.string.drawer_about, R.drawable.icon_about, NavigationType.ABOUT));
		return drawerItemList;
	}

	private NavigationItemInfo createNavigationItemInfo(int nameRes, int iconRes, int pageType) {
		NavigationItemInfo itemInfo = new NavigationItemInfo(getString(nameRes), iconRes, pageType);
		itemInfoSparseArray.put(pageType, itemInfo);
		return itemInfo;
	}

	private void startSetting() {
		startActivity(new Intent(MainActivity.this, SettingActivity.class));
	}

	private void startQuestion() {
		startActivity(new Intent(MainActivity.this, QuestionActivity.class));
	}

	private void startAbout() {
		startActivity(new Intent(MainActivity.this, AboutActivity.class));
	}

	private void onNavigationDrawerItemSelected(NavigationItemInfo itemInfo) {
		//FragmentBase.setAnimation(FragmentBase.AnimationType.FAST_FADE_OUT);
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		Fragment fragment = fragmentMap.get(itemInfo.pageType);
		if (fragment == null) {
			fragment = createPageFragment(itemInfo.pageType);
			fragmentMap.put(itemInfo.pageType, fragment);
		}
		if (itemInfo.arguments != null) {
			fragment.setArguments(itemInfo.arguments);
			itemInfo.arguments = null;
		}
		fragmentTransaction.replace(R.id.container, fragment, itemInfo.name);
		fragmentTransaction.commit();
		getSupportFragmentManager().executePendingTransactions();
	}

	private Fragment createPageFragment(int pageType) {
		Fragment pageFragment = null;
		switch (pageType) {
			case NavigationType.WORK_LIST:
				pageFragment = new WorkTabFragment();
				break;
			case NavigationType.WORK_HISTORY:
				pageFragment = new HistoryWorkFragment();
				break;
		}
		return pageFragment;
	}

	public void selectNavigationItem(int pageType) {
		drawerPresenter.selectNavigationItem(pageType);
	}

	public void setNavigationItemArguments(int pageType, Bundle arguments) {
		drawerPresenter.setNavigationItemArguments(pageType, arguments);
	}

	public void bindToolbarToDrawer(Toolbar toolbar) {
		ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
				R.string.navigation_drawer_close);
		drawerLayout.post(actionBarDrawerToggle::syncState);
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
	}

	@Override
	protected void onResume() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.title_background_orange));
		}
		super.onResume();
 		boolean isLogin = JoinWorkerApp.accountManager.isSignIn();
		boolean isLoginFlowSuccess = JoinWorkerApp.accountManager.isSignInFlowSuccess();
		if (!isLogin) {
//			JoinWorkerApp.preference.clearPrefs();
			Intent intent = new Intent(this, AccountLoginActivity.class);
			startActivity(intent);
//			overridePendingTransition(R.anim.activity_slide_in_up, android.R.anim.fade_out);
			finish();
		} else if (!isLoginFlowSuccess) {
			Intent intent = new Intent(this, ProfileActivity.class);
			startActivity(intent);
//			overridePendingTransition(R.anim.activity_slide_in_up, android.R.anim.fade_out);
			finish();
		}
		drawerPresenter.updateStatus();
	}

	@Override
	public void onBackPressed() {
		if (drawerPresenter.isOpen()) {
			drawerPresenter.closeDrawer();
		} else if (drawerPresenter.getCurrentPageType() != NavigationType.WORK_LIST) {
			//FragmentBase.setAnimation(AnimationType.NONE);
			selectNavigationItem(NavigationType.WORK_LIST);
		} else if (isClickToLeave) {
			super.onBackPressed();
		} else {
			Toast.makeText(this, R.string.exit_toast_message, Toast.LENGTH_SHORT).show();
			Timer timer = new Timer();
			isClickToLeave = true;
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					isClickToLeave = false;
				}
			}, DURING_LEAVE_WAIT);
		}
	}

}
