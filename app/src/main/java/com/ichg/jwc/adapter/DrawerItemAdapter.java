package com.ichg.jwc.adapter;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ichg.jwc.R;
import com.ichg.jwc.presenter.NavigationItemInfo;

import java.util.List;

public class DrawerItemAdapter extends RecyclerAdapterBase {

	private List<NavigationItemInfo> mItemInfoList;
	private DrawerItemAdapterListener mListener;
	private SparseArray<Integer> mPageTypePositionMap;
	private int mSelectedPosition = -1;

	public int getCurrentPageType() {
		return mItemInfoList.get(mSelectedPosition).pageType;
	}

	public interface DrawerItemAdapterListener {
		void onDrawerItemClick(NavigationItemInfo itemInfo);

		void onClickSelectedItem(NavigationItemInfo itemInfo);
	}

	public DrawerItemAdapter(List<NavigationItemInfo> itemInfoList, DrawerItemAdapterListener listener) {
		super(itemInfoList);
		mItemInfoList = itemInfoList;
		mListener = listener;
		mPageTypePositionMap = new SparseArray<>();
	}

	@Override
	protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item, parent, false));
	}

	@Override
	protected void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		ViewHolder holder = (ViewHolder) viewHolder;
		NavigationItemInfo itemInfo = mItemInfoList.get(position);
		boolean isSelectPosition = mSelectedPosition == position;
		holder.itemPosition = position;
		holder.itemNameTextView.setText(itemInfo.name);
		holder.itemIconImageView.setImageResource(getIconImage(isSelectPosition, itemInfo.iconRes));
		holder.itemNameTextView.setTextColor(ContextCompat.getColorStateList(holder.itemNameTextView.getContext(), getTextColor(isSelectPosition)));
		holder.contentView.setBackgroundResource(getBackgroundDrawable(isSelectPosition));
		if (itemInfo.eventCount == 0) {
			holder.eventCountTextView.setVisibility(View.GONE);
		} else {
			holder.eventCountTextView.setText(String.valueOf(itemInfo.eventCount));
			holder.eventCountTextView.setVisibility(View.VISIBLE);
		}
	}

	private int getBackgroundDrawable(boolean isSelected) {
		return isSelected ? R.color.drawer_item_pass : 0;
	}

	private int getTextColor(boolean isSelected) {
		return R.color.white;
	}

	private int getIconImage(boolean isSelected, int iconRes) {
		return iconRes;
	}

	public void addDrawerItem(List<NavigationItemInfo> itemInfoList) {
		for (int i = 0, size = itemInfoList.size(); i < size; i++) {
			mPageTypePositionMap.put(itemInfoList.get(i).pageType, i);
		}
		mItemInfoList.addAll(itemInfoList);
	}

	public void selectItem(int pageType) {
		int itemPosition = mPageTypePositionMap.get(pageType);
		selectItemByPosition(itemPosition);
	}

	private void selectItemByPosition(int itemPosition) {
		NavigationItemInfo itemInfo = mItemInfoList.get(itemPosition);
		if (mSelectedPosition != itemPosition) {
			mSelectedPosition = itemPosition;
			notifyDataSetChanged();
			mListener.onDrawerItemClick(itemInfo);
		} else {
			mListener.onClickSelectedItem(itemInfo);
		}
	}

	public void setItemArguments(int pageType, Bundle arguments) {
		int itemPosition = mPageTypePositionMap.get(pageType);
		NavigationItemInfo itemInfo = mItemInfoList.get(itemPosition);
		itemInfo.setArguments(arguments);
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		int itemPosition;
		View contentView;
		View background;
		TextView itemNameTextView;
		TextView eventCountTextView;
		ImageView itemIconImageView;

		public ViewHolder(View itemView) {
			super(itemView);
			background = itemView.findViewById(R.id.drawerItem_bg);
			itemNameTextView = (TextView) itemView.findViewById(R.id.drawerItem_name);
			itemIconImageView = (ImageView) itemView.findViewById(R.id.drawerItem_icon);
			eventCountTextView = (TextView) itemView.findViewById(R.id.drawerItem_eventCount);
			contentView = itemView;
			contentView.setOnClickListener(v -> selectItemByPosition(itemPosition));
		}


		@Override
		public boolean equals(Object o) {
			return o instanceof ViewHolder && ((ViewHolder) o).itemPosition == itemPosition;
		}
	}
}
