package com.ichg.joinworker.manager;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.ichg.joinworker.R;

public class ToolbarManager {

	private Toolbar mToolbar;

	private ToolbarManager(Toolbar toolbar) {
		mToolbar = toolbar;
	}

	public static ToolbarManager init(Toolbar toolbar) {
		return new ToolbarManager(toolbar);
	}

	public ToolbarManager title(int resId) {
		mToolbar.setTitle(resId);
		return this;
	}

	public ToolbarManager title(String title) {
		mToolbar.setTitle(title);
		return this;
	}

	public ToolbarManager bgResource(int resId) {
		mToolbar.setBackgroundResource(resId);
		return this;
	}

	public ToolbarManager menu(int resId, OnMenuItemClickListener listener) {
		mToolbar.inflateMenu(resId);
		mToolbar.setOnMenuItemClickListener(listener);
		return this;
	}

	public boolean hasItem(int itemId) {
		return mToolbar.getMenu().findItem(itemId) != null;
	}

	public Toolbar getToolbar() {
		return mToolbar;
	}

	public Menu getMenu() {
		return mToolbar.getMenu();
	}

	public MenuItem getMenuItem(int id) {
		return mToolbar.getMenu().findItem(id);
	}

	public ToolbarManager backNavigation(OnClickListener onNavigationClickListener) {
		return navigation(R.drawable.ic_arrow_back, onNavigationClickListener);
	}

	public ToolbarManager navigation(int iconResId, OnClickListener onNavigationClickListener) {
		mToolbar.setNavigationIcon(iconResId);
		mToolbar.setNavigationOnClickListener(onNavigationClickListener);
		return this;
	}

	public ToolbarManager visible(boolean isVisible) {
		mToolbar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
		return this;
	}

	public ToolbarManager bgColor(int colorRes) {
		mToolbar.setBackgroundColor(ContextCompat.getColor(mToolbar.getContext(), colorRes));
		return this;
	}
}
