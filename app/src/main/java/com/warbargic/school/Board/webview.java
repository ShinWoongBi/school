package com.warbargic.school.Board;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.warbargic.school.R;

/**
 * Created by kippe_000 on 2017-04-21.
 */

public class webview extends Activity {
    WebView webView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_webview);

        webView = (WebView)findViewById(R.id.webview);

        final ProgressDialog progressDialog = new ProgressDialog(webview.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("로딩중입니다..");
        progressDialog.show();

        webView.getSettings().setUserAgentString("0");
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                progressDialog.dismiss();
            }
        });
        webView.loadUrl("http://www.taereung.hs.kr/14208/subMenu.do");
    }
}
