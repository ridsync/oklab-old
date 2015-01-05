package com.oklab.webview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.oklab.R;

public class WebViewChromeClient extends WebChromeClient { 
	
	private Context context;
	
	/**
	 * @param context
	 */
	public WebViewChromeClient(Context context){
		this.context = context;
	}
	
	
	@Override
    public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result)
    {
		
		Log.d("WebViewChromeClient", "onJsAlert call : url = " + url + " /Message = "+message);
        new AlertDialog.Builder(context)
            .setTitle("알림")
            .setMessage(message)
            .setPositiveButton(android.R.string.ok,
                    new AlertDialog.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            result.confirm();
                        }
                    })
            .setCancelable(false)
            .create()
            .show();

        return true;
    }


	@Override
	public boolean onJsConfirm(WebView view, String url, String message,
			final JsResult result) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(view.getContext())
	      .setTitle("확인")
	      .setMessage(message)
	      .setPositiveButton(android.R.string.ok,
	            new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	                  result.confirm();
	               }
	            })
	      .setNegativeButton(android.R.string.cancel,
	            new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	                  result.cancel();
	               }
	            })
	      .create()
	      .show();

	   return true;
	};
    
    
    
}