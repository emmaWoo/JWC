package com.ichg.jwc.presenter;

import com.ichg.jwc.listener.LoadMoreExecutor;

import java.util.List;

public class RecyclerAdapterHelper {

	public final static int VIEW_TYPE_NORMAL = Integer.MAX_VALUE;
	private final static int VIEW_TYPE_LOAD_MORE = Integer.MAX_VALUE - 10;
	private final static int VIEW_TYPE_HEADER = Integer.MAX_VALUE - 12;

	private boolean isLoadMoreEnable = false;
	private boolean hasHeaderView = false;
	private LoadMoreExecutor.LoadMoreListener listener;

	private List dataList;

	public RecyclerAdapterHelper(List dataList, LoadMoreExecutor.LoadMoreListener loadMoreListener) {
		this.dataList = dataList;
		listener = loadMoreListener;
	}

	public void setLoadMoreListener(LoadMoreExecutor.LoadMoreListener loadMoreListener) {
		listener = loadMoreListener;
	}

	public void setLoadMoreEnable(boolean isEnable) {
		this.isLoadMoreEnable = isEnable;
	}

	public void setHasHeaderView(boolean hasHeaderView) {
		this.hasHeaderView = hasHeaderView;
	}

	public int getAdapterItemCount() {
		int itemSize = dataList.size();
		return itemSize == 0 ? 0 : itemSize + (isLoadMoreEnable ? 1 : 0) + (hasHeaderView ? 1 : 0);
	}

	public int calcDataItemPosition(int adapterPosition) {
		return adapterPosition - (hasHeaderView ? 1 : 0);
	}

	public int getDataSize() {
		return dataList.size();
	}

	public int getItemType(int position) {
		int itemType;
		if (isHeaderItem(position)) {
			itemType = VIEW_TYPE_HEADER;
		} else if (isLoadMoreItem(position)) {
			itemType = VIEW_TYPE_LOAD_MORE;
		} else {
			itemType = VIEW_TYPE_NORMAL;
		}
		return itemType;
	}

	public boolean checkToBindLoadMore(int position) {
		if (isLoadMoreItem(position)) {
			listener.onLoadMore();
			return true;
		}
		return false;
	}

	public boolean hasHeaderView() {
		return hasHeaderView;
	}

	public boolean isNormalItem(int position) {
		return !isHeaderItem(position) && !isLoadMoreItem(position);
	}

	public boolean isHeaderItem(int position) {
		return hasHeaderView && position == 0;
	}

	public boolean isLoadMoreItem(int position) {
		return isLoadMoreEnable && position == getAdapterItemCount() - 1;
	}

	public boolean isLoadMoreType(int viewType) {
		return viewType == VIEW_TYPE_LOAD_MORE;
	}

	public boolean isHeaderType(int viewType) {
		return viewType == VIEW_TYPE_HEADER;
	}

	public boolean isLoadMoreEnable() {
		return isLoadMoreEnable;
	}

	public void clearData() {
		dataList.clear();
	}
}
