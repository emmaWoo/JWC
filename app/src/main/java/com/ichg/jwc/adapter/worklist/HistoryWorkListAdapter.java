package com.ichg.jwc.adapter.worklist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ichg.jwc.R;
import com.ichg.jwc.adapter.RecyclerAdapterBase;
import com.ichg.service.object.WorkListInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryWorkListAdapter extends RecyclerAdapterBase {


    private OnItemAdapterListener mListener;
    private List<WorkListInfo> workListInfoList;
    private Context context;

    public interface OnItemAdapterListener {
        void onClickItem(int workListId);
    }

    public HistoryWorkListAdapter(Context context, List<WorkListInfo> workListInfoList, OnItemAdapterListener listener) {
        super(workListInfoList);
        this.context = context;
        this.mListener = listener;
        this.workListInfoList = workListInfoList;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_list_history, parent, false));
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        WorkListInfo itemInfo = workListInfoList.get(position);
        holder.itemPosition = position;
        holder.labelCity.setText(itemInfo.city);
        holder.labelPay.setText(itemInfo.isDailyWage() ? context.getString(R.string.daily_wage) : context.getString(R.string.hourly));
        holder.labelTitle.setText(itemInfo.title);
        holder.labelDate.setText(itemInfo.getWorkDate());
        holder.labelMoney.setText(itemInfo.payAmount);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        int itemPosition;
        @Bind(R.id.label_city) TextView labelCity;
        @Bind(R.id.label_pay) TextView labelPay;
        @Bind(R.id.label_money) TextView labelMoney;
        @Bind(R.id.label_title) TextView labelTitle;
        @Bind(R.id.label_date) TextView labelDate;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.button_card)
        public void onClick() {
            WorkListInfo workListInfo = workListInfoList.get(itemPosition);
            if(!workListInfo.isOneMonth()) {
                mListener.onClickItem(Integer.parseInt(workListInfo.id));
            }
        }

    }
}
