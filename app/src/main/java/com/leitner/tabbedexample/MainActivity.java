package com.leitner.tabbedexample;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.images.ImageManager;
import com.google.firebase.FirebaseException;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private SharedPreferences sharedPreferences;
    private int mImageLeftCollapsed;

    private int mImageTopCollapsed;

    private int mImageLeftExpanded;

    private int mImageTopExpanded;

    SharedPreferences.Editor editor;
    public static FloatingActionButton fab;
    AlarmDB db;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {



        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

//        startService(new Intent(this, UpdateBotClientStatusService.class));

        //editor.putString("resort", "Silvretta").commit();
        //Set AppTheme based on registered resort
        if (sharedPreferences.getString("resort", "").equals("Prinoth")){
            setTheme(R.style.AppTheme_Prinoth);
        }
        else if (sharedPreferences.getString("resort", "").equals("Ropeways")){
            setTheme(R.style.AppTheme_Ropeways);
        }else{
            //Do nothing because AppTheme is already present
        }
        //setTheme(R.style.AppTheme_Prinoth);
        super.onCreate(savedInstanceState);

//        editor.clear().commit();
        if (sharedPreferences.getBoolean("app_is_registered", false)){

            //Calendar
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
//            }
//            else{
//
//            }
//

            Log.d("ARRIVED", "arrived");
            setContentView(R.layout.activity_main);


            final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
            final AppBarLayout mAppBar = (AppBarLayout) findViewById(R.id.appbar);
            final ImageView collapintoolbarLogo = (ImageView) findViewById(R.id.ctoolbar_icon);

//
//            final CoordinatorLayout mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
//            final ImageView imageView = (ImageView) findViewById(R.id.imageview_title);
//            final ImageView collapsingToolbarImageView = (ImageView) findViewById(R.id.ctoolbar_background);
//            final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_collapsing);
//

            //Set image based on registered resort
            if (sharedPreferences.getString("resort", "").equals("Prinoth")){
                collapintoolbarLogo.setImageResource(R.drawable.prinoth_logo_transparent);
            }
            else if (sharedPreferences.getString("resort", "").equals("Ropeways")){
                collapintoolbarLogo.setImageResource(R.drawable.leitner_logo_transparent);
            }else{
                //Do nothing because image is already present
            }
            //collapintoolbarLogo.setImageResource(R.drawable.prinoth_logo_transparent);


            mImageLeftCollapsed = getResources().getDimensionPixelOffset(R.dimen.image_left_margin_collapsed);
            mImageTopCollapsed = getResources().getDimensionPixelOffset(R.dimen.image_top_margin_collapsed);
            mImageLeftExpanded = collapintoolbarLogo.getLeft();
            mImageTopExpanded = collapintoolbarLogo.getTop();
            editor.putBoolean("application_foreground", true);
            editor.commit();
            final int scrollLimit = -292;
            try{

                Log.d("DEVICE language",Locale.getDefault().getLanguage());


                mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

//                        final int scrollRange = appBarLayout.getTotalScrollRange();
//                        float offsetFactor = (float) (-verticalOffset) / (float) scrollRange;
//                        int childCount = collapsingToolbarLayout.getChildCount();
//                        for (int i = 0; i < childCount; i++) {
//                            View child = collapsingToolbarLayout.getChildAt(i);
//                            final ViewOffsetHelper offsetHelper = getViewOffsetHelper(child);
//                            if (child.getId() == R.id.ctoolbar_icon){
//                                float scaleFactor = 1F - offsetFactor * .5F ;
//                                child.setScaleX(scaleFactor);
//                                child.setScaleY(scaleFactor);
//
//                                int topOffset = (int) ((mImageTopCollapsed - mImageTopExpanded) * offsetFactor) - verticalOffset;
//                                int leftOffset = (int) ((mImageLeftCollapsed - mImageLeftExpanded) * offsetFactor);
//                                child.setPivotX(0);
//                                child.setPivotY(0);
//                                offsetHelper.setTopAndBottomOffset(topOffset);
//                                offsetHelper.setLeftAndRightOffset(leftOffset);
//                            }
//                        }
                        final int scrollRange = appBarLayout.getTotalScrollRange();
                        float offsetFactor = (float) (-verticalOffset) / (float) scrollRange;
                        float scaleFactor = 1F - offsetFactor * .7F ;
                        collapintoolbarLogo.setScaleX(scaleFactor);
                        collapintoolbarLogo.setScaleY(scaleFactor);
                        int topOffset = (int) ((mImageTopCollapsed - mImageTopExpanded) * offsetFactor) - verticalOffset;
                        int leftOffset = (int) ((mImageLeftCollapsed - mImageLeftExpanded) * offsetFactor);
                        collapintoolbarLogo.setTranslationY(-20 + (topOffset - (collapintoolbarLogo.getTop() - collapintoolbarLogo.getTop())) / 3);
                        Log.d("POSITIONINGSCALEFACTOR", String.valueOf(scaleFactor));
                        Log.d("POSITIONINGTOPBOTTOM", String.valueOf(topOffset - (collapintoolbarLogo.getTop() - collapintoolbarLogo.getTop())));
                        Log.d("POSITIONINGLEFTRIGHT", String.valueOf(leftOffset - (collapintoolbarLogo.getLeft() - collapintoolbarLogo.getLeft())));
                        if (verticalOffset >= scrollLimit){
                            //tabHeaderView.setAlpha((float)verticalOffset/(float)scrollLimit);
//                            if (verticalOffset == scrollLimit){
//                                Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadein);
//                                imageView.startAnimation(animation);
//                                imageView.setVisibility(View.VISIBLE);
//                            }
//                            else{
//                                if (imageView.getVisibility() == View.VISIBLE){
//                                    Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout);
//                                    imageView.startAnimation(animation);
//                                    imageView.setVisibility(View.INVISIBLE);
//                                }
//                            }
                        }


                    }
                });
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayShowTitleEnabled(false);

                Log.d("RESORT", sharedPreferences.getString("resort", null));
//                if (sharedPreferences.getString("resort", null).equals("Prinoth")){
//                    toolbar.setBackgroundColor(getResources().getColor(R.color.prinothPrimary));
//                    tabLayout.setBackgroundColor(getResources().getColor(R.color.prinothPrimary));
//                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.prinothRed));
//                    collapsingToolbarLayout.setBackgroundColor(getResources().getColor(R.color.prinothPrimary));
//                    //themeUtils.changeToTheme(this, themeUtils.PRINOTH);
////                    if ( == R.style.AppTheme_Prinoth)
////                    setTheme(R.style.AppTheme_Prinoth);
//                    Log.d("THEME", getTheme().toString());
//                    Log.d("Theme", String.valueOf(R.style.AppTheme_Prinoth));
//                    Log.d("Theme", String.valueOf(R.style.AppTheme));
//                }
//                else
//                    //themeUtils.changeToTheme(this, themeUtils.NORMAL);
                Log.d("FIREBASE TOKEN", FirebaseInstanceId.getInstance().getToken());
            }catch (Exception e){
                Log.d("FIREBASE ERROR", "Seems that Firebase is not instantiated");
            }
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setOffscreenPageLimit(2);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == 2){
                        if ((mAppBar.getHeight() - mAppBar.getBottom()) != 292)
                            mAppBar.setExpanded(false);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //            @Override
            //            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //                Log.d("VP SCROLLED", String.valueOf(position));
            //            }
            //
            //            @Override
            //            public void onPageSelected(int position) {
            //                Log.d("VP SELECT", String.valueOf(position));
            //                if (position != 1){
            ////                    getFragmentManager().findFragmentById(1).onStop();
            //                }
            //            }
            //
            //            @Override
            //            public void onPageScrollStateChanged(int state) {
            //                Log.d("VP PSSC", String.valueOf(state));
            //            }
            //        });
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
        }
        else{
            Intent registerFirst = new Intent(this, RegisterFirstActivity.class);
            startActivity(registerFirst);
            finish();
        }

//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        if (sharedPreferences.getBoolean("server_online", false))
//            fab.setBackgroundTintList(getResources().getColorStateList(R.color.green));
//        else
//            fab.setBackgroundTintList(getResources().getColorStateList(R.color.red));
//        fab.setOnClickListener(new View.OnClickListener() {
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onClick(View view) {
//                if (sharedPreferences.getBoolean("server_online", false)) {
//                    Snackbar.make(view, "BotClient is online", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//                else
//                    Snackbar.make(view, "BotClient is offline", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//            }
//        });

    }

    @Override
    public void onResume(){
        super.onResume();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        Log.d("APPLICATION_FOREGROUND", "true");
        editor.putBoolean("application_foreground", true);
        editor.commit();
    }

    @Override
    public void onPause(){
        Log.d("APPLICATION_FOREGROUND", "false");
        editor.putBoolean("application_foreground", false);
        editor.commit();
        super.onPause();
    }
    @Override
    public void onStop(){
        Log.d("APPLICATION_FOREGROUND", "false");
        editor.putBoolean("application_foreground", false);
        editor.commit();
       super.onStop();
    }

//    @Override
//    public void onDestroy(){
//        AlarmDB db = new AlarmDB(getApplication().getApplicationContext());
//        db.deleteTable();
//        super.onDestroy();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
//                    Tab1Actual tab1 = new Tab1Actual();
                    Tab1ActualRecyclerView tab1 = new Tab1ActualRecyclerView();
                    return tab1;
                case 1:
//                    Tab2History tab2 = new Tab2History();
                    Tab2HistoryRecyclerView tab2 = new Tab2HistoryRecyclerView();
//                    tab2.setArguments(getIntent().getExtras());
//                    getFragmentManager()
//                            .beginTransaction()
//                            .add(android.R.id., (android.app.Fragment) tab2, "TAB2HISTORY")
//                    .commit();
                    return tab2;
                case 2:
                    Tab3Settings tab3 = new Tab3Settings();
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.actual_alarm);
                case 1:
                    return getResources().getString(R.string.historic_alarm);
                case 2:
                    return getResources().getString(R.string.settings);
            }
            return null;
        }
    }
}
