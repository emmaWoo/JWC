package com.ichg.jwc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.fragment.work.WorkListFragment;
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
		public static final int ACCOUNT_SETTING = 1;
		public static final int WORK_LIST = 2;
		public static final int PROFILE = 3;
		public static final int QUESTION = 4;
		public static final int LOGOUT = 5;
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
						if (itemInfo.pageType == NavigationType.LOGOUT) {
							JoinWorkerApp.logout();
							Intent intent = new Intent(MainActivity.this, AccountLoginActivity.class);
							startActivity(intent);
							//getActivityBase().overridePendingTransition(R.anim.activity_slide_in_up, android.R.anim.fade_out);
							finish();
						} else if (itemInfo.pageType == NavigationType.PROFILE) {
							Bundle bundle = new Bundle();
							Intent intent = new Intent(MainActivity.this, ProfileViewActivity.class);
							intent.putExtras(bundle);
							startActivity(intent);
							//getActivityBase().overridePendingTransition(R.anim.activity_slide_in_up, android.R.anim.fade_out);
						} else {
							onNavigationDrawerItemSelected(itemInfo);
						}
					}

					@Override
					public void onProfileClick() {
						Bundle bundle = new Bundle();
						Intent intent = new Intent(MainActivity.this, ProfileViewActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
		);

		drawerPresenter.addDrawerItems(initDrawerItemInfoList());
		drawerPresenter.selectNavigationItem(DEFAULT_PAGE_TYPE);
	}

	private List<NavigationItemInfo> initDrawerItemInfoList() {
		List<NavigationItemInfo> drawerItemList = new ArrayList<>();
		drawerItemList.add(createNavigationItemInfo(R.string.account_setting, R.drawable.icon_set, NavigationType.ACCOUNT_SETTING));
		drawerItemList.add(createNavigationItemInfo(R.string.work_list, R.drawable.icon_notice, NavigationType.WORK_LIST));
		drawerItemList.add(createNavigationItemInfo(R.string.profile, R.drawable.icon_profile, NavigationType.PROFILE));
		drawerItemList.add(createNavigationItemInfo(R.string.question, R.drawable.icon_qa, NavigationType.QUESTION));
		drawerItemList.add(createNavigationItemInfo(R.string.logout, R.drawable.icon_signout, NavigationType.LOGOUT));
		return drawerItemList;
	}

	private NavigationItemInfo createNavigationItemInfo(int nameRes, int iconRes, int pageType) {
		NavigationItemInfo itemInfo = new NavigationItemInfo(getString(nameRes), iconRes, pageType);
		itemInfoSparseArray.put(pageType, itemInfo);
		return itemInfo;
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
			case NavigationType.ACCOUNT_SETTING:
				pageFragment = new WorkTabFragment();
				break;
			case NavigationType.WORK_LIST:
				pageFragment = new WorkTabFragment();
				break;
			case NavigationType.QUESTION:
				pageFragment = new WorkTabFragment();
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
