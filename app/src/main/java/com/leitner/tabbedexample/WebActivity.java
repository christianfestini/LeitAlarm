package com.leitner.tabbedexample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebActivity extends AppCompatActivity implements MyWebChromeClient.ProgressListener {

    private String url;
    private WebView mWebView;
    private ProgressBar mProgressBar;
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        ((AppCompatActivity)this).getSupportActionBar().setTitle(String.valueOf(getIntent().getExtras().getString("action_bar_title")));
        ((AppCompatActivity)this).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        url = getIntent().getExtras().getString("url");
        setContentView(R.layout.activity_web);
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
        settings.setSupportMultipleWindows(true);



//mTextMessage = (TextView) findViewById(R.id.message);
        //final ProgressDialog pd = ProgressDialog.show(this, "", "Loading...",true);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mWebView.setWebChromeClient(new MyWebChromeClient(this));

        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url){
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }
        });




        mWebView.loadUrl(url);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()){
            Log.d("NAVIGATION", "back");
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void OnUpdateProgress(int progress){
        mProgressBar.setProgress(progress);
        if (progress == 100)
            mProgressBar.setVisibility(View.INVISIBLE);
    }

}
