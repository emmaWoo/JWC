package com.ichg.jwc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ichg.jwc.R;
import com.ichg.jwc.manager.ToolbarManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);
        initToolbar();
    }

    private void initToolbar() {
        ToolbarManager.init((Toolbar) findViewById(R.id.toolbar))
                .title(R.string.question)
                .backNavigation(v -> onBackPressed());
    }

    @OnClick(R.id.label_phone)
    public void onClick() {
        startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:xxxxxxxxxx")));
    }
}
