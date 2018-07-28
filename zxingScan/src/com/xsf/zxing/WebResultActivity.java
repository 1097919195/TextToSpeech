package com.xsf.zxing;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static com.xsf.zxing.R.id.webview;

/**
 * Created by asus-pc on 2017/10/10.
 */

public class WebResultActivity extends Activity{
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_result);

        webView = (WebView) findViewById(webview);
        Bundle extras = getIntent().getExtras();

        if (null != extras) {
            String result = extras.getString("result");

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setSupportMultipleWindows(true);
            webView.setWebViewClient(new WebViewClient());//防止有的网页重定向跳转到浏览器(重定向就是，在网页上设置一个约束条件，条件满足，就自动转入到其它网页、网址)
            webView.setWebChromeClient(new WebChromeClient());

            webView.loadUrl(result);
        }
    }
}
