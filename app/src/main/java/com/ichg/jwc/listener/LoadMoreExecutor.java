package com.ichg.jwc.listener;

public interface LoadMoreExecutor {
	void setLoadMoreEnable(boolean isEnable);
	void setLoadMoreListener(LoadMoreListener loadMoreListener);


	interface LoadMoreListener{
		void onLoadMore();
	}
}

