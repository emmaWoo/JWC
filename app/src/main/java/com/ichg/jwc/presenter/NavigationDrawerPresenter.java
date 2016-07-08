package com.ichg.jwc.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.adapter.DrawerItemAdapter;
import com.ichg.jwc.manager.AccountManager;
import com.ichg.jwc.transform.CircleTransform;
import com.ichg.service.api.base.JoinWorkerApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerPresenter {

	private Context context;

	private DrawerLayout drawerLayout;

	private ImageView viewProfile;

	private TextView labelName;

	private NavigationDrawerPresenterListener mListener;

	private DrawerItemAdapter mDrawerItemAdapter;
	private AccountManager mAccountManager;

	public interface NavigationDrawerPresenterListener {
		void onNavigationItemSelected(NavigationItemInfo itemInfo);

		void onProfileClick();
	}

	public NavigationDrawerPresenter(DrawerLayout drawerLayout, NavigationView navigationView, NavigationDrawerPresenterListener listener) {
		mAccountManager = JoinWorkerApp.accountManager;
		this.mListener = listener;
		this.context = navigationView.getContext();
		this.drawerLayout = drawerLayout;
		RelativeLayout drawerView = (RelativeLayout) navigationView.findViewById(R.id.navigation_drawer_content_layout);

		initProfileArea(drawerView);
		initNavigationItems(drawerView);
	}

	public void selectNavigationItem(int pageType) {
		mDrawerItemAdapter.selectItem(pageType);
	}

	public void setNavigationItemArguments(int pageType, Bundle arguments) {
		mDrawerItemAdapter.setItemArguments(pageType, arguments);
	}

	private void initProfileArea(View drawerView) {
		viewProfile = (ImageView) drawerView.findViewById(R.id.icon_avatar);
		labelName = (TextView) drawerView.findViewById(R.id.label_name);
		viewProfile.setOnClickListener(v -> mListener.onProfileClick());
	}

	private void initNavigationItems(View drawerView) {
		RecyclerView itemRecyclerView = (RecyclerView) drawerView.findViewById(R.id.navigation_drawer_recycler);
		itemRecyclerView.setLayoutManager(new LinearLayoutManager(drawerView.getContext()));
		mDrawerItemAdapter = new DrawerItemAdapter(new ArrayList<>(), new DrawerItemAdapter.DrawerItemAdapterListener() {
			@Override
			public void onDrawerItemClick(NavigationItemInfo itemInfo) {
				mListener.onNavigationItemSelected(itemInfo);
				closeDrawer();
			}

			@Override
			public void onClickSelectedItem(NavigationItemInfo itemInfo) {
				closeDrawer();
			}
		});
		itemRecyclerView.setAdapter(mDrawerItemAdapter);
	}

	public void addDrawerItems(List<NavigationItemInfo> itemInfoList) {
		mDrawerItemAdapter.addDrawerItem(itemInfoList);
	}

	public void updateStatus() {
		if (!TextUtils.isEmpty(JoinWorkerApp.preference.getUserToken())) {
			mDrawerItemAdapter.notifyDataSetChanged();
			updateUserProfile();
		}
	}

	public int getCurrentPageType() {
		return mDrawerItemAdapter.getCurrentPageType();
	}


	public void updateUserProfile() {
		Picasso.with(context).load(JoinWorkerApi.getBaseUrl() + mAccountManager.userAvatarUrl)
				.transform(new CircleTransform()).placeholder(R.drawable.ic_person_default).into(viewProfile);
		labelName.setText(mAccountManager.userName);
	}

	public boolean isOpen() {
		return drawerLayout.isDrawerOpen(GravityCompat.START);
	}

	public void closeDrawer() {
		drawerLayout.closeDrawers();
	}

}
