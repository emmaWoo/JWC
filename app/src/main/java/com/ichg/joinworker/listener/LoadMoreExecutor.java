package com.ichg.joinworker.listener;

public interface LoadMoreExecutor {
	void setLoadMoreEnable(boolean isEnable);
	void setLoadMoreListener(LoadMoreListener loadMoreListener);


	interface LoadMoreListener{
		void onLoadMore();
	}
}

