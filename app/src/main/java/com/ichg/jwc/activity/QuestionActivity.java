package com.ichg.jwc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

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
        startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:02-2225-4066")));
    }

    @OnClick(R.id.label_email)
    public void onClickEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"jw.service@joinworker.com.tw"});
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.label_question)
    public void onClickQuestion() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.ichg.com.tw/ezcatfiles/EC-SBU008/img/img/1335/joinwork_qa.html"));
        startActivity(browserIntent);
    }
}
