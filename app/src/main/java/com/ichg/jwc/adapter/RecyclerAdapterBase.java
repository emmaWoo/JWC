package com.ichg.jwc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ichg.jwc.R;
import com.ichg.jwc.listener.LoadMoreExecutor;
import com.ichg.jwc.presenter.RecyclerAdapterHelper;

import java.util.List;

public abstract class RecyclerAdapterBase extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LoadMoreExecutor {

	private final static int DEFAULT_LOAD_MORE_LAYOUT = R.layout.listview_load_more;

	private RecyclerAdapterHelper recyclerAdapterHelper;
	private View headerView;

	protected RecyclerAdapterBase(List<?> dataList) {
		recyclerAdapterHelper = new RecyclerAdapterHelper(dataList, null);
	}

	@Override
	final public int getItemViewType(int adapterPosition) {
		return recyclerAdapterHelper.isNormalItem(adapterPosition) ?
				getCustomItemViewType(recyclerAdapterHelper.calcDataItemPosition(adapterPosition)) :
				recyclerAdapterHelper.getItemType(adapterPosition);
	}

	protected int getCustomItemViewType(int position) {
		return RecyclerAdapterHelper.VIEW_TYPE_NORMAL;
	}

	@Override
	final public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (recyclerAdapterHelper.isLoadMoreType(viewType)) {
			return onCreateLoadMoreViewHolder(parent);
		}
		if (recyclerAdapterHelper.isHeaderType(viewType)) {
			return onCreateHeaderViewHolder();
		}
		return onCreateItemViewHolder(parent, viewType);
	}

	@Override
	final public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		if (recyclerAdapterHelper.isNormalItem(position)) {
			onBindItemViewHolder(viewHolder, position - (recyclerAdapterHelper.hasHeaderView() ? 1 : 0));
		} else if (recyclerAdapterHelper.checkToBindLoadMore(position)) {
			onBindLoadMoreViewHolder(viewHolder);
		} else {
			onBindHeaderViewHolder(viewHolder);
		}
	}

	@Override
	public int getItemCount() {
		return recyclerAdapterHelper.getAdapterItemCount();
	}

	public int getDataSize() {
		return recyclerAdapterHelper.getDataSize();
	}

	@Override
	final public void setLoadMoreEnable(boolean isEnable) {
		recyclerAdapterHelper.setLoadMoreEnable(isEnable);
	}

	@Override
	final public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
		recyclerAdapterHelper.setLoadMoreListener(loadMoreListener);
	}

	final public void clearItemData(){
		recyclerAdapterHelper.clearData();
	}

	final public void setHeaderView(View headerView) {
		this.headerView = headerView;
		recyclerAdapterHelper.setHasHeaderView(true);
	}

	private RecyclerView.ViewHolder onCreateHeaderViewHolder() {
		return new DefaultViewHolder(headerView);
	}

	protected void onBindLoadMoreViewHolder(RecyclerView.ViewHolder viewHolder) {
	}

	private void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder) {
		if (headerView.getParent() != null) {
			((RecyclerView) headerView.getParent()).removeView(headerView);
		}
	}

	public boolean hasHeaderView(){
		return recyclerAdapterHelper.hasHeaderView();
	}

	protected RecyclerView.ViewHolder onCreateLoadMoreViewHolder(ViewGroup parent) {
		return new DefaultViewHolder(LayoutInflater.from(parent.getContext()).inflate(DEFAULT_LOAD_MORE_LAYOUT, parent, false));
	}

	protected abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType);

	protected abstract void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position);

	class DefaultViewHolder extends RecyclerView.ViewHolder {
		public DefaultViewHolder(View itemView) {
			super(itemView);
		}
	}

}
