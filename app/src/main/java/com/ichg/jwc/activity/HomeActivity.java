package com.ichg.jwc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.ichg.jwc.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends Activity {
    @Bind(R.id.label_version) TextView labelVersion;

    private Handler handler = new Handler();
    private static Uri uri = null;
    private Runnable mainRunnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Intent protocolIntent = getIntent();
        final String action = protocolIntent.getAction();
        if (protocolIntent.getData() != null) {
            uri = getIntent().getData();
        }
        labelVersion.setText(getVersionName(this));
        mainRunnable = () -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            if (action != null && uri != null) {
                intent.putExtra("protocol_url", uri.toString());
                uri = null;
            }
            startActivity(intent);
            finish();
        };
        handler.postDelayed(mainRunnable, 1000);
    }

    private String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return "ver " + packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        removeRunnableWhenLeave(mainRunnable);
        super.onBackPressed();
    }

    @Override
    protected void onUserLeaveHint() {
        removeRunnableWhenLeave(mainRunnable);
        super.onUserLeaveHint();
    }

    private void removeRunnableWhenLeave(Runnable runnable) {
        if(runnable != null) {
            handler.removeCallbacks(runnable);
            finish();
        }
    }

}
