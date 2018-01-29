package com.leitner.tabbedexample;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by i0004913 on 27.11.2017.
 */

public class MyWebViewClient extends WebViewClient {
    boolean timeout;

    public MyWebViewClient(){
        timeout = true;
    }

    @Override
    public void onPageStarted(final WebView view, String url, Bitmap favicon) {
        Runnable run = new Runnable() {
            public void run() {
                if(timeout) {
                    // do what you want
                    //showAlert("Connection Timed out", "Whoops! Something went wrong. Please try again later.");
                    Log.d("TIMEOUT", "timed out");
                    Snackbar.make(view, "Whoops! Something went wrong. Please try again later.", Snackbar.LENGTH_LONG ).show();

                }
            }
        };
        Log.d("TIMEOUT", "timing");
        Handler myHandler = new Handler(Looper.myLooper());
        myHandler.postDelayed(run, 5000);

    }


    @Override
    public void onPageFinished(WebView view, String url){
        timeout = false;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url){
        view.loadUrl(url);
        return true;
    }
}
