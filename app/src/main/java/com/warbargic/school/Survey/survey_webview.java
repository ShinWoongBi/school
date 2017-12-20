package com.warbargic.school.Survey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.warbargic.school.R;

/**
 * Created by kippe_000 on 2017-04-20.
 */

public class survey_webview extends Activity {
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_webview);
        Intent intent = getIntent();

        String title = intent.getStringExtra("title");
        String url = intent.getStringExtra("url");

        TextView title_T = (TextView)findViewById(R.id.title);
        webView = (WebView)findViewById(R.id.webview);

        title_T.setText("- "+title);

        if(title.equals("학교폭력 실태조사")){
            ((TextView)findViewById(R.id.message)).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.message)).setText("\"학교찾기\"버튼을 꼭 누르지 마십시오!\n학교 입력란에 \"태릉고등학교\"를 정자로 입력하세요.");
        }


        final ProgressDialog progressDialog = new ProgressDialog(survey_webview.this);
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
