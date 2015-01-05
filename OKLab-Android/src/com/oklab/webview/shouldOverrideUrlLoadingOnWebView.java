package com.oklab.webview;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oklab.BaseActivity;
import com.oklab.R;

public class shouldOverrideUrlLoadingOnWebView extends BaseActivity {
	
	private static WebView webView1 = null;
	private static WebView webView2 = null;
	private WebView[] webViews = new WebView[2];
	
	public static final String[] JSI_IDS = {
		"body"
	,	"footer"
};
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_vd3_main);

		setLayoutComponents();
		setWebSettings();
	}
	
	private void setLayoutComponents() {
		
		webView1 = (WebView)findViewById(R.id.web_view1);
		webView2 = (WebView)findViewById(R.id.web_view2);
		
		webViews[0] = webView1;
		webViews[1] = webView2;
	}

	@Override
	public void onBackPressed() {
		
		if(webView1.canGoBack()){
    		webView1.goBack();
    		return;
    	}else{
    		finish();
            return;
    	}
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void setWebSettings(){
		
		WebSettings settings = null;
		
		for(int i=0; i<webViews.length; i++){
			webViews[i].clearFormData();
			webViews[i].clearHistory();
			webViews[i].clearCache(true);
			
			webViews[i].setWebViewClient(new CustomWebViewClient());
			webViews[i].setWebChromeClient(new WebViewChromeClient(this));
			webViews[i].addJavascriptInterface(new JavaScriptBridge(webViews, JSI_IDS[i], this), "CSaF");
			
			webViews[i].setVerticalScrollbarOverlay(true);
			
			settings = webViews[i].getSettings();
			
			settings.setJavaScriptEnabled(true);
			settings.setBuiltInZoomControls(false);
			
			settings.setCacheMode(WebSettings.LOAD_DEFAULT);
			settings.setDomStorageEnabled(true);
			settings.setAppCacheMaxSize(1024*1024*8);
			settings.setAllowFileAccess(true);
			settings.setAppCachePath(getCacheDir().getAbsolutePath());
			settings.setAppCacheEnabled(true);
		}

		webView1.loadUrl("http://value.hanwha.co.kr/m/auth/sso.c?autoLoginId=HSNC200201034&location=/mobile/main.m");

		webView2.loadUrl("http://value.hanwha.co.kr/m/TabBar.p");
		}
	
	class CustomWebViewClient extends WebViewClient{
		
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			Log.d("onPageStarted", url);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.d("onPageFinished", url);
		}
		
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d("shouldOverrideUrlLoading", url);
			if(url.startsWith("app://call?phone=")) {
				String uri = url.replace("app://call?phone=", "tel:");
				Intent i = new Intent(Intent.ACTION_CALL);
				i.setData(Uri.parse(uri));
				startActivity(i);
				return true;
			}else if (url.startsWith("app://sms?sms")) {
				String uri = url.replace("app://sms?sms=", "sms:");
				Intent i = new Intent(Intent.ACTION_SENDTO);
				i.addCategory(Intent.CATEGORY_DEFAULT);
				i.setType("vnd.android-dir/mms-sms");
				i.setData(Uri.parse(uri)); 
				startActivity(i);
				return true;
			}else if (url.startsWith("app://mailto?mailto=")) {
				String uri = url.replace("app://mailto?mailto=", "mailto:");
				Intent i = new Intent(Intent.ACTION_SENDTO);
				i.setData(Uri.parse(uri));
				startActivity(i);
				return true;
			}else if(url.startsWith("app://gohome")) {
				finish();
				return true;
			}else {
				String paramUrl = url + "?value_id=ok1234,value_pass=12345";
				HashMap<String, String> hInfo = new HashMap<String, String>();
				hInfo.put("value_id", "hhok1234");
				hInfo.put("value_pass", "hh12345");
				view.loadUrl(paramUrl, hInfo);
				return false;
			}
    	}
		
	}
	
}