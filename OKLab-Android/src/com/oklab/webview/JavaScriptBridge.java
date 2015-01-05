package com.oklab.webview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;

public class JavaScriptBridge implements CSWInterface{
	
	private WebView[] webViews;
	private String id;
	private Activity act;
	
	public JavaScriptBridge(WebView[] webViews, String id,Activity context){
		this.webViews = webViews;
		this.id = id;
		this.act = context;
	}
		
	public String getId(){
		return id;
	}
	
	public void exit(){
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
			}
		});
	}
	
	public void execute(final String json){
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.d("CSW jsInterface","exec = execute getJSBeans start json = " + json);
				JSBean[] beans = getJSBeans(json);
				Log.d("CSW jsInterface","CSW exec = execute getJSBeans end ");
				if(beans!=null){
					
					for(int i=0; i<beans.length; i++){
						execute(beans[i]);
					}
				}
				Log.d("CSW jsInterface","CSW exec = execute script end ");
			}
		});
				
	}
	
	private void execute(JSBean bean){
		String script = getScript(bean);
		if( bean.idxs!=null && isNotEmpty(script) ){
			for(int i=0; i<bean.idxs.length; i++){
				webViews[bean.idxs[i]].loadUrl(
						CSWInterface.SCRIPT_PREFIX + script + CSWInterface.SCRIPT_SUFFIX);
				Log.d("CSW jsInterface","CSW exec = " + CSWInterface.SCRIPT_PREFIX+ script + CSWInterface.SCRIPT_SUFFIX);
			}
		}
	}
	
	private String getScript(JSBean bean){
		String script = null;
		
		if( isNotEmpty(bean.function) ){
			StringBuffer sb = new StringBuffer();
			sb.append(bean.function).append("(");
			
			if(bean.args!=null){
				for(int i=0; i<bean.args.length; i++){
					if(i>0)
						sb.append(",");
					
					String arg = bean.args[i];
					
					if(isNotEmpty(arg))
						sb.append("'").append(arg).append("'");
					else
						sb.append("''");
				}
			}
			
			sb.append(")");
			script = sb.toString();
		}
		
		return script;
	}
	
	private boolean isNotEmpty(String str){
		return ( str != null && str.length()>0 );
	}
	
	private JSBean[] getJSBeans(String json){
		
		JSBean[] beans = null;
		
		try {
			JSONArray jAry = new JSONArray(json);
			
			int length = jAry.length();
			beans = new JSBean[length];
			
			for(int i=0; i<length; i++){
				beans[i] = getJSBean( jAry.getJSONObject(i) );	
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return beans;
	}
	
	private JSBean getJSBean(JSONObject jObj){
		JSBean bean = new JSBean();
		
		bean.idxs = toIntArray( toStringArray( jObj.optJSONArray(IDXS) ) );
		bean.function = jObj.optString(FNC);
		bean.args = toStringArray( jObj.optJSONArray(ARGS) );
		
		return bean;
	}
	
	private String[] toStringArray(JSONArray jAry){
		String[] sAry = null;
		
		if(jAry!=null){
			int length = jAry.length();
			sAry = new String[length];
			
			for(int i=0; i<length; i++){
				sAry[i] = jAry.optString(i);
			}
		}
		
		return sAry;
	}
	
	private int[] toIntArray(String[] idxs){
		int[] intIdxs = null;
		
		if(idxs!=null){
			try{
				int length = idxs.length;
				
				intIdxs = new int[length];
				
				for(int i=0; i<length; i++){
					intIdxs[i] = Integer.parseInt(idxs[i]);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return intIdxs;
	}
	
	private class JSBean {
		
		public JSBean(){
			this.idxs = null;
			this.function = null;
			this.args = null;
		}
		
		public int[] idxs;
		public String function;
		public String[] args;
	}
}