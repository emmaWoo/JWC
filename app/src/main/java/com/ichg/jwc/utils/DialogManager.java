package com.ichg.jwc.utils;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;

import com.ichg.jwc.R;
import com.ichg.jwc.fragment.DialogFragment;
import com.ichg.jwc.listener.DialogListener;

import java.util.ArrayList;

public class DialogManager {
	private FragmentActivity activity;
	private String title;
	private String message;
	private String positive;
	private String negative;
	private boolean cancelable;
	private OnKeyListener onKeyListener;
	private DialogListener listener;
	private ArrayList<CharSequence> list;

	public static class DialogType {
		public static final int PROGRESSING_DIALOG = 0;
		public static final int ALERT_DIALOG = 1;
		public static final int YES_OR_NO_DIALOG = 2;
		public static final int TEXT_LIST_DIALOG = 3;
	}

	public DialogManager(FragmentActivity activity) {
		this.activity = activity;
		setTitle(R.string.join_worker);
		setMessage("");
		setPositiveText(R.string.confirm);
		setNegativeText(R.string.cancel);
		setCancelable(true);
		setListener(null);
		setArrayList(new ArrayList<>());
	}

	public static DialogManager with(FragmentActivity activity) {
		if (activity == null) {
			throw new IllegalArgumentException("Activity must not be null.");
		}
		return new DialogManager(activity);
	}

	public DialogManager setTitle(String title) {
		this.title = title;
		return this;
	}

	public DialogManager setTitle(int id) {
		this.title = getString(id);
		return this;
	}

	public DialogManager setMessage(String message) {
		this.message = message;
		return this;
	}

	public DialogManager setMessage(int id) {
		this.message = getString(id);
		return this;
	}

	public DialogManager setPositiveText(int id) {
		this.positive = getString(id);
		return this;
	}

	public DialogManager setPositiveText(String positive) {
		this.positive = positive;
		return this;
	}

	public DialogManager setNegativeText(int id) {
		this.negative = getString(id);
		return this;
	}

	public DialogManager setNegativeText(String negative) {
		this.negative = negative;
		return this;
	}

	public DialogManager setArrayList(ArrayList<CharSequence> list) {
		this.list = list;
		return this;
	}

	public DialogManager disableBack() {
		onKeyListener = new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK)) {
					return true;
				}
				return false;
			}
		};
		return this;
	}

	private String getString(int id) {
		return this.activity.getString(id);
	}

	public DialogManager setCancelable(boolean isCancelable) {
		this.cancelable = isCancelable;
		return this;
	}

	public DialogManager setListener(DialogListener listener) {
		this.listener = listener;
		return this;
	}

	public void showProgressingDialog() {
		this.showDialog(DialogType.PROGRESSING_DIALOG);
	}

	public void showAlertDialog() {
		this.showDialog(DialogType.ALERT_DIALOG);
	}

	public void showYesOrNoDialog() {
		this.showDialog(DialogType.YES_OR_NO_DIALOG);
	}

	public void showTextListDialog() {
		this.showDialog(DialogType.TEXT_LIST_DIALOG);
	}

	public void showAPIErrorDialog(int errorCode, String errorMessage) {
		setMessage(errorMessage).showAlertDialog();
	}

	public void dismissDialog() {
		Fragment attachedFragment = activity.getSupportFragmentManager().findFragmentByTag("dialog");
		if (attachedFragment != null && attachedFragment instanceof android.support.v4.app.DialogFragment && !activity.isFinishing()) {
			try {
				((android.support.v4.app.DialogFragment) attachedFragment).dismiss();
			} catch (Exception e) {
				Log.e("e-join work", "Error dismissing");
			}
		}
	}

	private void showDialog(int type) {
		FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
		Fragment attachedFragment = activity.getSupportFragmentManager().findFragmentByTag("dialog");
		if (attachedFragment != null) {
			transaction.remove(attachedFragment);
		}
		DialogFragment dialogFragment = new DialogFragment();
		if (type == DialogType.PROGRESSING_DIALOG) {
			dialogFragment.setProgressingDialog(message, cancelable, listener);
		} else if (type == DialogType.ALERT_DIALOG) {
			dialogFragment.setAlertDialog(title, message, positive, cancelable, listener, onKeyListener);
		} else if (type == DialogType.YES_OR_NO_DIALOG) {
			dialogFragment.setYesOrNoDialog(title, message, positive, negative, cancelable, listener);
		} else if (type == DialogType.TEXT_LIST_DIALOG) {
			dialogFragment.setTextListDialog(list, title, cancelable, listener);
		}
		if (!activity.isFinishing()) {
			try {
				dialogFragment.show(transaction, "dialog");
			} catch (Exception e) {
				Log.e("e-join work", "Error show dialog");
			}
		}
	}

}
