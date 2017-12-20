package com.warbargic.school.left_infor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.warbargic.school.R;

/**
 * Created by kippe_000 on 2017-05-14.
 */

public class BaseWebView extends Activity {
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basewebview);

        webView = (WebView)findViewById(R.id.webview);
        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");

        ((TextView)findViewById(R.id.title)).setText(title);
        ((ImageButton)findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        final ProgressDialog progressDialog = new ProgressDialog(BaseWebView.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("로딩중입니다..");
        progressDialog.show();

        webView.getSettings().setUserAgentString("1");
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
        webView.loadUrl(url );
    }
}
