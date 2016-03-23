package com.ichg.service.framework;

public interface RequestExecutor {

	class RequestError {
		public static final int NETWORK_NOT_AVAILABLE = -101;
		public static final int TIMEOUT_ERROR = -105;
		public static final int SERVER_ERROR = -102;
	}

	void request(Api api, Object tag);

	void cancel(Object tag);

	void cancel(Api api);
}
