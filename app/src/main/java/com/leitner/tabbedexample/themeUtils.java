package com.leitner.tabbedexample;

/**
 * Created by i0004913 on 11.01.2018.
 */

import android.app.Activity;

import android.content.Intent;



public class themeUtils extends Object {
    private static int cTheme;



    public final static int NORMAL = 0;

    public final static int PRINOTH = 1;

    public static void changeToTheme(Activity activity, int theme)

    {
        cTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity)

    {
        switch (cTheme)
        {
            default:
            case NORMAL:
                activity.setTheme(R.style.AppTheme);
                break;
            case PRINOTH:
                activity.setTheme(R.style.AppTheme_Prinoth);
                break;
        }
    }
}
