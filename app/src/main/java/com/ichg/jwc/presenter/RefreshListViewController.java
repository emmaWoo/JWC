package com.ichg.jwc.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ichg.jwc.R;
import com.ichg.jwc.listener.LoadMoreExecutor;
import com.ichg.service.utils.Debug;

public class RefreshListViewController {

	private final static int refreshIconColorRes = R.color.title_background_orange;

	private RecyclerView recyclerView;
	private SwipeRefreshLayout refreshLayout;

	private LoadMoreExecutor loadMoreExecutor;
	private LoadMoreListener mLoadMoreListener;

	private int preChildCont = 0;
	private int preHeight = 0;

	private RefreshListViewController() {
	}

	public static RefreshListViewController init(View view) {
		return init(view, false);
	}

	public static RefreshListViewController init(View view, boolean resizeable) {
		return init(view, refreshIconColorRes, resizeable);
	}

	public static RefreshListViewController init(View view, int refreshIconColorRes, boolean resizeable) {
		final RefreshListViewController instance = new RefreshListViewController();
		instance.recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		if (instance.recyclerView == null) {
			throw new IllegalStateException("Can't find recycler view in your layout.");
		}
		instance.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
		if (resizeable) {
			instance.recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(instance::calculateSwipeRefreshFullHeight);
		}
		instance.refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe_refresh);
		if (instance.refreshLayout == null) {
			throw new IllegalStateException("Can't find refresh view in your layout.");
		}
		instance.refreshLayout.setColorSchemeColors(ContextCompat.getColor(view.getContext(), refreshIconColorRes));
		return instance;
	}

	@SuppressLint("NewApi")
	public RefreshListViewController nestedScrollingEnabled(boolean isEnable) {
		recyclerView.setNestedScrollingEnabled(isEnable);
		return this;
	}

	private void calculateSwipeRefreshFullHeight() {
		int height = 0;
		int childCount = recyclerView.getChildCount();
		SwipeRefreshLayout.LayoutParams swipeParams = refreshLayout.getLayoutParams();
		if (childCount == preChildCont) {
			swipeParams.height = preHeight;
			refreshLayout.setLayoutParams(swipeParams);
			return;
		}
		Debug.e("calc", "child view count = " + recyclerView.getChildCount());
		for (int idx = 0; idx < childCount; idx++) {
			View v = recyclerView.getChildAt(idx);
			height += v.getHeight();
		}

		if (height == 0 || height == swipeParams.height) {
			return;
		}
		Debug.e("calc", "change height = " + height);
		swipeParams.height = height;
		refreshLayout.setLayoutParams(swipeParams);
		preChildCont = childCount;
		preHeight = height;
	}

	public View getRefreshLoadingIcon() {
		for (int i = 0, size = refreshLayout.getChildCount(); i < size; i++) {
			View childView = refreshLayout.getChildAt(i);
			if (childView instanceof ImageView) {
				return childView;
			}
		}
		return null;
	}

	public RefreshListViewController onRefresh(RefreshListener listener) {
		refreshLayout.setOnRefreshListener(listener::onRefresh);
		return this;
	}

	public RefreshListViewController onLoadMore(LoadMoreListener listener) {
		mLoadMoreListener = listener;
		return this;
	}

	public void resetListHeight() {
		SwipeRefreshLayout.LayoutParams params = refreshLayout.getLayoutParams();
		params.height = ViewGroup.LayoutParams.MATCH_PARENT;
	}

	public void setColumnNumber(Context context, int columnNumber) {
		GridLayoutManager layoutManager = new GridLayoutManager(context, columnNumber);
		recyclerView.setLayoutManager(layoutManager);
	}

	public RecyclerView getRecyclerView() {
		return recyclerView;
	}

	public SwipeRefreshLayout getSwipeRefreshLayout() {
		return refreshLayout;
	}

	public RefreshListViewController setAdapter(RecyclerView.Adapter adapter) {
		if (recyclerView == null) {
			throw new IllegalStateException("No recycler view was detected, plz init first.");
		}
		if (adapter instanceof LoadMoreExecutor) {
			loadMoreExecutor = (LoadMoreExecutor) adapter;
			loadMoreExecutor.setLoadMoreListener(this::onAdapterLoadMore);
		}
		recyclerView.setAdapter(adapter);
		return this;
	}

	private void onAdapterLoadMore() {
		if (mLoadMoreListener != null) {
			mLoadMoreListener.onLoadMore();
		}
	}

	public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
		recyclerView.addOnScrollListener(listener);
	}

	public boolean isRefreshing() {
		return refreshLayout.isRefreshing();
	}

	public void setEnablePullToRefresh(boolean isEnable) {
		refreshLayout.setEnabled(isEnable);
	}

	public void setRefreshing(boolean isLoading) {
		refreshLayout.setRefreshing(isLoading);
	}

	public void setEnableLoadMore(boolean isEnable) {
		if (loadMoreExecutor == null) {
			throw new IllegalStateException("Adapter must to implement Load More interface to active this feature.");
		}
		loadMoreExecutor.setLoadMoreEnable(isEnable);
	}

	public RefreshListViewController initScrollTopShadow(View shadowView) {
		onScrollTopChange(isAttachTop -> shadowView.setVisibility(isAttachTop ? View.GONE : View.VISIBLE));
		return this;
	}

	public RefreshListViewController onScrollTopChange(ScrollTopListener listener) {
		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			boolean isTop = true;

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				View child = recyclerView.findChildViewUnder(0, 0);
				int position = recyclerView.getChildAdapterPosition(child);
				if (position == 0 && child.getY() == 0) {
					isTop = true;
					listener.onScrollTopChange(true);
				} else if (isTop) {
					listener.onScrollTopChange(false);
					isTop = false;
				}
			}
		});
		return this;
	}

	public interface ScrollTopListener {
		void onScrollTopChange(boolean isAttachTop);
	}

	public interface RefreshListener {
		void onRefresh();
	}

	public interface LoadMoreListener {
		void onLoadMore();
	}

}
