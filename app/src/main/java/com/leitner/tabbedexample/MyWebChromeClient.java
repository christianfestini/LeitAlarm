package com.leitner.tabbedexample;

import android.os.RecoverySystem;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by i0004913 on 27.11.2017.
 */

public class MyWebChromeClient extends WebChromeClient {
    private ProgressListener mListener;

    public MyWebChromeClient(ProgressListener listener){
        mListener = listener;
    }

    @Override
    public void onProgressChanged(WebView view, int progress){
        mListener.OnUpdateProgress(progress);
        Log.d("PROGRESS", String.valueOf(progress));
        super.onProgressChanged(view, progress);
    }

    public interface ProgressListener{
        public void OnUpdateProgress(int progress);
    }
}
