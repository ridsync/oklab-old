package com.okitoki.checklist.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.okitoki.checklist.R;
import com.okitoki.checklist.core.AppConst;
import com.okitoki.checklist.ui.base.CoreActivity;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2016-02-14.
 */
public class HolidayWebViewActivity extends CoreActivity {
    private WebView webView;
    private ProgressBar mPBar;

    Toolbar toolbar_actionbar;

    boolean isFistLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview);

        toolbar_actionbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        String title = getIntent().getStringExtra(AppConst.INTENT_EXTRA_PRODUCT_TITLE);

        String realTitle = getString(R.string.nav_menu_name_6).replace("$1",title);
        toolbar_actionbar.setTitle(realTitle);
        setSupportActionBar(toolbar_actionbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        String strUrl = getIntent().getStringExtra(AppConst.INTENT_EXTRA_URL);
        webView = (WebView) findViewById(R.id.WEBVIEW);
        mPBar = (ProgressBar) findViewById(R.id.PROGRESSBAR_WEBVIEW);
        WebSettings set = webView.getSettings();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

//        webView.getSettings().setUserAgentString("Android");
//        Logger.d("webView", webView.getSettings().getUserAgentString());
        webView.loadUrl(strUrl); // 보여주고자 하는 주소
        isFistLoading = true;

        webView.setWebViewClient(new WebClient());
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    mPBar.setVisibility(ProgressBar.VISIBLE);
                } else if (progress == 100) {
                    mPBar.setVisibility(ProgressBar.GONE);
                    if(isFistLoading){
                        webView.clearHistory();
                        isFistLoading = false;
                    }
                }
                mPBar.setProgress(progress);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("OptionsMenu", "onOptionsItemSelected");
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                overridePendingTransition(0,R.anim.anim_webview_scale_out);
                return true;
            case R.id.menu_mode_close:
                finish();
                overridePendingTransition(0,R.anim.anim_webview_scale_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                webView.clearCache(false);
                finish();
                overridePendingTransition(0,R.anim.anim_webview_scale_out);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http://shopping.naver.com")) {
//                String mobileUrl = url.replace("shopping.naver.com", "m.shopping.naver.com");
                view.loadUrl(url);
            }else if (url.startsWith("sms:")) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                startActivity(i);
            }else if (url.startsWith("kakaolink:")) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            }else if (url.startsWith("tel")) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse(url));
                startActivity(i);
            } else {
                view.loadUrl(url);
            }
            return true;
        }
    }
}