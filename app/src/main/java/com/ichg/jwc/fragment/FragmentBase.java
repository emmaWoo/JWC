package com.ichg.jwc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ichg.jwc.R;
import com.ichg.jwc.activity.ActivityBase;

public abstract class FragmentBase extends Fragment {

	private ActivityBase activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (ActivityBase) getActivity();
	}

	public ActivityBase getActivityBase() {
		return activity;
	}

	protected void onNavigationClick() {
		activity.onBackPressed();
	}

	protected void onBackPress() {
		hideSoftInputFromWindow();
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		if (fragmentManager.getBackStackEntryCount() > 1) {
//			if (getAnimationType() == AnimationType.PUSH) {
//				setAnimation(AnimationType.POP);
//			}
			fragmentManager.popBackStackImmediate();
		} else {
			activity.finish();
		}
	}

	protected final void showSoftInputFromWindow() {
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	protected final void hideSoftInputFromWindow() {
		View focusView = getActivityBase().getWindow().getCurrentFocus();
		if (focusView != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getActivityBase().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
		}
	}

	public void switchToChildFragment(FragmentBase fragment, Bundle arguments) {
		switchToChildFragment(fragment, arguments, true);
	}

	public void switchToChildFragment(Fragment fragment, Bundle arguments, boolean isAddToBackStack) {
		if (arguments != null) {
			fragment.setArguments(arguments);
		}
		FragmentTransaction fragmentTransaction;
		FragmentManager fragmentManager;
		fragmentManager = getChildFragmentManager();
		if (fragmentManager == null) {
			return;
		}
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.sub_fragment, fragment);
		if (isAddToBackStack) {
			fragmentTransaction.addToBackStack(null);
		}
		fragmentTransaction.commit();
		fragmentManager.executePendingTransactions();
	}

}