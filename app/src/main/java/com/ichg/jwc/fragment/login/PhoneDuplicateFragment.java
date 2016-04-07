package com.ichg.jwc.fragment.login;

import com.ichg.jwc.fragment.FragmentBase;

public class PhoneDuplicateFragment extends FragmentBase {
//	private static final String[] LOCATION_PERMISSIONS = {
//			Manifest.permission.RECEIVE_SMS
//	};
//	public static final String INPUT_USER_PROFILE_BASE_ENTITY = "0";
//	public static final int REQUEST_PASSWORD_LOGIN = 0;
//
//	private VerifyPhonePresenter mPresenter;
//	private UserProfileBaseEntity userProfileBaseEntity;
//
//	@Override
//	public void onAttach(Context context) {
//		super.onAttach(context);
//		mPresenter = ((VerifyPhoneActivity) context).getPresenter();
//		Debug.i("onAttach");
//	}
//
//	@Nullable
//	@Override
//	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//		View contentView = inflater.inflate(R.layout.fragment_duplicate_binding, container, false);
//		initToolbar(contentView);
//		initDuplicateAccountInfo(contentView);
//		initMyAccountLoginButton(contentView);
//		initRegisterButton(contentView);
//		initWrongNumberButton(contentView);
//		return contentView;
//	}
//
//	private void initWrongNumberButton(View contentView) {
//		contentView.findViewById(R.id.button_wrong_number).setOnClickListener(v -> onNavigationClick());
//	}
//
//	private void initToolbar(View contentView) {
//		ToolbarManager.init((Toolbar) contentView.findViewById(R.id.toolbar))
//				.backNavigation(v -> onNavigationClick());
//	}
//
//	private void initDuplicateAccountInfo(View contentView) {
//		userProfileBaseEntity = (UserProfileBaseEntity) getArguments().getSerializable(INPUT_USER_PROFILE_BASE_ENTITY);
//		RegisterPhoneInfo registerPhoneInfo = mPresenter.getCurrentRegisterPhoneInfo();
//		ImageView userAvatarImageView = (ImageView) contentView.findViewById(R.id.view_avatar);
//		Picasso.with(getActivityBase()).load(userProfileBaseEntity.avatarUrl).transform(new CircleTransform()).placeholder(R.drawable
//				.bg_avatar_user_default).into(userAvatarImageView);
//		((TextView) contentView.findViewById(R.id.label_username)).setText(userProfileBaseEntity.nickname);
//		TextView labelTitle = (TextView) contentView.findViewById(R.id.label_title);
//		labelTitle.setText(String.format(getActivityBase().getString(R.string.duplicate_phone_number), registerPhoneInfo.countryCode, registerPhoneInfo.phoneNo));
//	}
//
//	private void initMyAccountLoginButton(View contentView) {
//		contentView.findViewById(R.id.button_login).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(getContext(), FragmentContentActivity.class);
//				Bundle arguments = new Bundle();
//				arguments.putString(PasswordLoginFragment.INPUT_STRING_USER_NAME, userProfileBaseEntity.userName);
//				intent.putExtra(FragmentContentActivity.INPUT_BUNDLE_FRAGMENT_ARGUMENTS, arguments);
//				intent.putExtra(FragmentContentActivity.INPUT_STRING_FRAGMENT_NAME, PasswordLoginFragment.class.getName());
//				startActivityForResult(intent, REQUEST_PASSWORD_LOGIN);
//				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//			}
//		});
//	}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == Activity.RESULT_OK) {
//			getActivity().finish();
//		}
//	}
//
//	private void initRegisterButton(View contentView) {
//		contentView.findViewById(R.id.button_register).setOnClickListener(
//				v -> PermissionManager.checkRequirePermissionIfDenied(getActivity(),
//						Constant.Permission.REQUEST_PERMISSION_SMS, LOCATION_PERMISSIONS,
//						PhoneDuplicateFragment.this::register, deniedPermissions -> {}));
//	}
//
//	private void register() {
//		DialogManager.with(getActivityBase()).setMessage(R.string.send_verify_code).setListener(new DialogListener() {
//			@Override
//			public void onPositive() {
//				DialogManager.with(getActivityBase()).setListener(new DialogListener() {
//					@Override
//					public void onCancel() {
//						mPresenter.cancel();
//					}
//				}).showProgressingDialog();
//				mPresenter.requestVerifyCodeFromServer(true);
//			}
//		}).showYesOrNoDialog();
//	}
}
