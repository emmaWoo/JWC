package com.ichg.jwc.presenter;

import android.os.Bundle;

public class NavigationItemInfo {
    public String name;
    public int iconRes;
    public int pageType;
    public int eventCount = 0;
    public Bundle arguments;

    public NavigationItemInfo(String name, int iconRes, int pageType) {
        this.name = name;
        this.iconRes = iconRes;
        this.pageType = pageType;
    }

    public void setArguments(Bundle arguments) {
        this.arguments = arguments;
    }
}
