package com.ichg.jwc.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ichg.jwc.JoinWorkerApp;
import com.ichg.jwc.R;
import com.ichg.jwc.fragment.work.WorkListFragment;
import com.ichg.jwc.listener.WorkDetailListener;
import com.ichg.jwc.manager.ToolbarManager;
import com.ichg.jwc.presenter.WorkDetailPresenter;
import com.ichg.jwc.utils.DialogManager;
import com.ichg.service.object.WorkDetailInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkDetailActivity extends ActivityBase implements WorkDetailListener {

    @Bind(R.id.label_work_title) TextView labelWorkTitle;
    @Bind(R.id.label_work_city) TextView labelWorkCity;
    @Bind(R.id.label_work_area) TextView labelWorkArea;
    @Bind(R.id.label_work_pay_type) TextView labelWorkPayType;
    @Bind(R.id.label_work_menory) TextView labelWorkMenory;
    @Bind(R.id.label_work_typ) TextView labelWorkTyp;
    @Bind(R.id.label_work_time) TextView labelWorkTime;
    @Bind(R.id.label_work_place) TextView labelWorkPlace;
    @Bind(R.id.label_work_context) TextView labelWorkContext;
    @Bind(R.id.label_work_need_number) TextView labelWorkNeedNumber;
    @Bind(R.id.label_work_have_meals) TextView labelWorkHaveMeals;
    @Bind(R.id.label_work_payroll_day) TextView labelWorkPayrollDay;
    @Bind(R.id.label_work_company_name) TextView labelWorkCompanyName;
    @Bind(R.id.label_work_contact_person) TextView labelWorkContactPerson;
    @Bind(R.id.label_work_contact_person_phone) TextView labelWorkContactPersonPhone;
    @Bind(R.id.button_status) Button buttonStatus;
    @Bind(R.id.label_follow) TextView labelFollow;

    private WorkDetailPresenter mPresenter;
    private int detailId;
    private WorkDetailInfo workDetailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_detail);
        ButterKnife.bind(this);
        initToolbar();
        initPresenter();
        DialogManager.with(this).showProgressingDialog();
        detailId = getIntent().getIntExtra(WorkListFragment.WORK_DETAIL_ID, -1);
        mPresenter.getWorkDetail(detailId);
    }

    private void initToolbar() {
        ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
                .title(R.string.work_title)
                .backNavigation(v -> onBackPressed());
    }

    private void initPresenter() {
        if (mPresenter == null) {
            mPresenter = new WorkDetailPresenter(JoinWorkerApp.apiFacade, this);
        }
    }

    @OnClick({R.id.button_status, R.id.label_follow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_status:
                String status = workDetailInfo.status;
                if (WorkDetailInfo.RESPONSE_WORK.equals(status)) {
                    return;
                }
                DialogManager.with(this).showProgressingDialog();
                if (WorkDetailInfo.DETERMINE_WORK.equals(status)) {

                } else if (WorkDetailInfo.DONE_WORK.equals(status)) {

                } else {
                    mPresenter.sendWorkResponse(detailId);
                }
                break;
            case R.id.label_follow:
                if (workDetailInfo.isFollow()) {
                    mPresenter.removeWorkFollow(detailId);
                } else {
                    mPresenter.addWorkFollow(detailId);
                }
                break;
        }
    }

    @Override
    public void onFail(int errorType, String message) {
        DialogManager.with(this).dismissDialog();
        DialogManager.with(this).showAPIErrorDialog(errorType, message);
    }

    @Override
    public void onSuccess(WorkDetailInfo workDetailInfo) {
        this.workDetailInfo = workDetailInfo;
        labelWorkTitle.setText(workDetailInfo.title);
        labelWorkCity.setText(workDetailInfo.city);
        labelWorkArea.setText(workDetailInfo.district);
        labelWorkPayType.setText(getString(workDetailInfo.isDailyWage() ? R.string.daily_wage : R.string.hourly));
        labelWorkMenory.setText(getString(R.string.work_money, workDetailInfo.payAmount));
        labelWorkTyp.setText(workDetailInfo.typeName);
        String workTime = getString(R.string.work_date, workDetailInfo.getWorkDate(), workDetailInfo.workingTimeFrom, workDetailInfo.workingTimeTo);
        labelWorkTime.setText(workTime);
        labelWorkPlace.setText(workDetailInfo.address);
        labelWorkContext.setText(workDetailInfo.content);
        labelWorkNeedNumber.setText(getString(R.string.work_need_people, workDetailInfo.needPeople, workDetailInfo.matchedPeople));
        labelWorkHaveMeals.setText(getString(workDetailInfo.hasMeals() ? R.string.yes : R.string.no));
        labelWorkPayrollDay.setText(workDetailInfo.payDate);
        labelWorkCompanyName.setText(workDetailInfo.companyName);
        labelWorkContactPerson.setText(workDetailInfo.companyContactName);
        labelWorkContactPersonPhone.setText(workDetailInfo.getCompanyContactPhone());
        buttonStatus.setBackgroundResource(getButtonBackground(workDetailInfo.status));
        updateFollow();
        DialogManager.with(this).dismissDialog();
    }

    private void updateFollow() {
        int rightDrawable = workDetailInfo.isFollow() ? R.drawable.button_fa_on : R.drawable.button_fa_off;
        labelFollow.setCompoundDrawablesWithIntrinsicBounds(0, 0, rightDrawable, 0);
    }

    private int getButtonBackground(String status) {
        if (WorkDetailInfo.RESPONSE_WORK.equals(status)) {
            return R.drawable.button_do_2;
        } else if (WorkDetailInfo.DETERMINE_WORK.equals(status)) {
            return R.drawable.button_do_3;
        } else if (WorkDetailInfo.DONE_WORK.equals(status)) {
            return R.drawable.button_do_4;
        } else {
            return R.drawable.button_do_1;
        }
    }

    @Override
    public void onSuccessResponse(String status) {
        mPresenter.getWorkDetail(detailId);
    }

    @Override
    public void onSuccessFollowStatusChange(String status) {
        workDetailInfo.updateFollow();
        updateFollow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.cancel();
    }
}
