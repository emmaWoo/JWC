package com.ichg.jwc.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.ichg.jwc.R;
import com.ichg.jwc.listener.DialogListener;
import com.ichg.jwc.utils.DialogManager;

import java.util.ArrayList;

public class DialogFragment extends android.support.v4.app.DialogFragment {
	private CharSequence title;
	private CharSequence message;
	private CharSequence positiveButtonText;
	private CharSequence negativeButtonText;
	private boolean cancelable;
	private int dialogType;
	private DialogListener listener;
	private ArrayList<CharSequence> list;
	private OnKeyListener onKeyListener;

	private final DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int id) {
			if (listener != null) {
				listener.onPositive();
			}
		}
	};

	private final DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int id) {
			if (listener != null) {
				listener.onNegative();
			}
		}
	};

	private final DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
			if (listener != null) {
				listener.onCancel();
			}
		}
	};

	private final DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (listener != null) {
				listener.onItemClick(which);
			}
		}
	};

	public void setProgressingDialog(CharSequence message, boolean cancelable, DialogListener listener) {
		dialogType = DialogManager.DialogType.PROGRESSING_DIALOG;
		this.message = message;
		this.cancelable = cancelable;
		this.listener = listener;
	}

	public void setAlertDialog(CharSequence title, CharSequence message, CharSequence positiveButtonText, boolean cancelable,
	                           DialogListener listener, OnKeyListener onKeyListener) {
		dialogType = DialogManager.DialogType.ALERT_DIALOG;
		this.message = message;
		this.title = title;
		this.positiveButtonText = positiveButtonText;
		this.cancelable = cancelable;
		this.listener = listener;
		this.onKeyListener = onKeyListener;
	}

	public void setYesOrNoDialog(CharSequence title, CharSequence message, CharSequence positiveButtonText,
	                             CharSequence negativeButtonText, boolean cancelable, DialogListener listener) {
		dialogType = DialogManager.DialogType.YES_OR_NO_DIALOG;
		this.message = message;
		this.title = title;
		this.positiveButtonText = positiveButtonText;
		this.negativeButtonText = negativeButtonText;
		this.cancelable = cancelable;
		this.listener = listener;
	}

	public void setTextListDialog(ArrayList<CharSequence> list, String title, boolean cancelable, DialogListener listener) {
		dialogType = DialogManager.DialogType.TEXT_LIST_DIALOG;
		this.title = title;
		this.list = list;
		this.cancelable = cancelable;
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		switch (dialogType) {
			case DialogManager.DialogType.PROGRESSING_DIALOG:
				//, R.style.TransparentProgressDialog
				ProgressDialog progressDialog = new ProgressDialog(getActivity());
				progressDialog.setIndeterminate(true);
				progressDialog.setCanceledOnTouchOutside(cancelable);
				progressDialog.setCancelable(cancelable);
				progressDialog.setOnCancelListener(cancelListener);
				return progressDialog;
			case DialogManager.DialogType.ALERT_DIALOG:
				AlertDialog.Builder builder;
				AlertDialog alertDialog;
				builder = new AlertDialog.Builder(getActivity());
				builder.setMessage(message);
				builder.setTitle(title);
				builder.setPositiveButton(positiveButtonText, positiveListener);
				alertDialog = builder.create();
				alertDialog.setCancelable(cancelable);
				alertDialog.setCanceledOnTouchOutside(cancelable);
				alertDialog.setOnCancelListener(cancelListener);
				alertDialog.setOnKeyListener(onKeyListener);
				return alertDialog;
			case DialogManager.DialogType.YES_OR_NO_DIALOG:
				builder = new AlertDialog.Builder(getActivity());
				builder.setMessage(message);
				builder.setTitle(title);
				builder.setPositiveButton(positiveButtonText, positiveListener);
				builder.setNegativeButton(negativeButtonText, negativeListener);
				alertDialog = builder.create();
				alertDialog.setCancelable(cancelable);
				alertDialog.setCanceledOnTouchOutside(cancelable);
				alertDialog.setOnCancelListener(cancelListener);
				return alertDialog;
			case DialogManager.DialogType.TEXT_LIST_DIALOG:
				builder = new AlertDialog.Builder(getActivity());
				if (!TextUtils.isEmpty(title)) {
					builder.setTitle(title);
				}
				builder.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.dialog_expandable_list_item, list), clickListener);
				Dialog dialog = builder.create();
				dialog.setCancelable(cancelable);
				dialog.setCanceledOnTouchOutside(cancelable);
				return dialog;
		}
		return null;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if (listener != null) {
			listener.onCancel();
		}
	}
}
