package com.ichg.joinworker.presenter;

import com.ichg.joinworker.JoinWorkerApp;
import com.ichg.joinworker.activity.VerifyPhoneActivity;
import com.ichg.joinworker.listener.AccountLoginListener;
import com.ichg.joinworker.manager.AccountManager;

public class PresenterFactory {

//	public static boolean mock = false;
//	private static Map<String, MockPresenterDelegate> mockDelegateMap = new ArrayMap<>();

//	public static void registerDelegate(String className, MockPresenterDelegate delegate) {
//		mockDelegateMap.put(className, delegate);
//	}

//	public static LoginPresenter createLoginPresenter(AccountManager accountManager, ApiFacade apiFacade, TourLoginView tourLoginView) {
//		return mock ?
//				(LoginPresenter) mockDelegateMap.get(LoginPresenter.class.getName()).createMock() :
//				new LoginPresenter(accountManager, new FacebookUtils(), tourLoginView);
//	}

	public static AccountLoginPresenter createAccountLoginPresenter(AccountManager accountManager, AccountLoginListener accountLoginListener) {
//		return mock ?
//				(AccountLoginPresenter) mockDelegateMap.get(AccountLoginPresenter.class.getName()).createMock() :
				return new AccountLoginPresenter(accountManager, accountLoginListener);
	}

	public static VerifyPhonePresenter createVerifyPhonePresenter(VerifyPhoneActivity activity) {
//		VerifyPhonePresenter presenter = JoinWorkerApp.accountManager.isSignIn() ?
//				new BindingPhonePresenter(JoinWorkerApp.apiFacade, JoinWorkerApp.accountManager, activity) :
//				new RegisterPhonePresenter(JoinWorkerApp.apiFacade, JoinWorkerApp.accountManager, activity);
//		return mock ?
//				(VerifyPhonePresenter) mockDelegateMap.get(VerifyPhonePresenter.class.getName()).createMock() :
//				presenter;
		VerifyPhonePresenter presenter = new RegisterPhonePresenter(JoinWorkerApp.apiFacade, JoinWorkerApp.accountManager, activity);
		return presenter;
	}

//	public static ChoiceEventMenuPresenter createChoiceEventMenuPresenter(int menuType, ChoiceEventMenuView choiceEventMenuView) {
//		ChoiceEventMenuPresenter presenter;
//		switch (menuType) {
//			case MenuType.MATE_CHOICE:
//				presenter = new MateChoiceEventMenuPresenter(choiceEventMenuView);
//				break;
//			case MenuType.EDITOR_CHOICE:
//			default:
//				presenter = new EditorChoiceEventMenuPresenter(choiceEventMenuView);
//				break;
//		}
//		return mock ?
//				(ChoiceEventMenuPresenter) mockDelegateMap.get(ChoiceEventMenuPresenter.class.getName()).createMock() :
//				presenter;
//	}

//	public static SearchPresenter createSearchPresenter(SearchEventView view) {
//		return mock?
//				(SearchPresenter) mockDelegateMap.get(SearchPresenter.class.getName()).createMock() :
//				new SearchPresenter(view);
//	}

//	public interface MockPresenterDelegate<Presenter> {
//		Presenter createMock();
//	}

}
