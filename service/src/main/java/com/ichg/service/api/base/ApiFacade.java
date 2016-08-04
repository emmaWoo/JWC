package com.ichg.service.api.base;

import com.ichg.service.framework.Api;
import com.ichg.service.framework.RequestExecutor;

public class ApiFacade {
	private RequestExecutor mExecutor;

	public ApiFacade(RequestExecutor executor, int envir){
		mExecutor = executor;
		JoinWorkerApi.initVars(envir);
		JoinWorkerApi.initApiFacade(this);
	}

	public void changeRequestExecutor(RequestExecutor executor){
		mExecutor = executor;
	}

	public void updateEnvironment(int envirType){
		JoinWorkerApi.updateEnvironment(envirType);
	}

	public void updateUserToken(String userToken){
		JoinWorkerApi.updateUserToken(userToken);
	}

	public void request(Api api, Object tag){
		mExecutor.request(api, tag);
	}

	public void cancel(Object tag){
		mExecutor.cancel(tag);
	}

	public void cancel(Api api){
		mExecutor.cancel(api);
	}

}
