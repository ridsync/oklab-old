package com.oklab.ionet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClientBasicTest {
	
	private static final String url = "http://www.dibisis.net/sub/subsub/file.php";
	public static void main(String args[]){
		
		try {
			System.out.println(httpPostRequest(url) );
//			System.out.println(httpGetRequest(url) );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String httpPostRequest(String uri) throws Exception {

        String result;

        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost();

        httpPost.setURI(new URI(uri));

        httpPost.setHeader("Host", "accounts.google.com");
        httpPost.setHeader("User-Agent", "Mozilla/5.0");
        httpPost.setHeader("Accept", 
                 "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpPost.setHeader("Accept-Language", "en-US,en;q=0.5");
        httpPost.setHeader("Cookie", getCookies());
        httpPost.setHeader("Connection", "keep-alive");
        httpPost.setHeader("Referer", "https://accounts.google.com/ServiceLoginAuth");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

        params.add(new BasicNameValuePair("type1", "한글"));

        params.add(new BasicNameValuePair("type2", "value2"));

        params.add(new BasicNameValuePair("type3", "value3"));

        params.add(new BasicNameValuePair("type4", "value4"));

        params.add(new BasicNameValuePair("type5", "value5"));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);

        httpPost.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPost);

        HttpEntity resEntity = response.getEntity();

        int responseCode = response.getStatusLine().getStatusCode();
        
        System.out.println("\nSending 'POST' request to URL : " + uri);
    	System.out.println("Post parameters : " + params);
    	System.out.println("Response Code : " + responseCode);
    	
//        result= EntityUtils.toString(resEntity);

    	// 1)
        BufferedReader bfr = new BufferedReader(
        		new InputStreamReader(resEntity.getContent(), "UTF-8"));
        
        StringBuffer sbf = new StringBuffer();
        
        String line = "";
        while ((line = bfr.readLine()) != null) {
			sbf.append(line + "\n");
		}
        
        // 2) Other Stream  
        
        // http 인코딩 문제 ....
        return sbf.toString();

    }

 
	private static String httpGetRequest(String string) throws Exception {

	    String result;

	    HttpClient httpClient = new DefaultHttpClient();
	    
	    HttpGet httpGet = new HttpGet();

	    httpGet.setHeader("User-Agent", "Mozilla/5.0");
	    httpGet.setHeader("Accept",
			"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	    httpGet.setHeader("Accept-Language", "en-US,en;q=0.5");

	    URI uri = new URIBuilder()
	    .setScheme("http")
	    .setHost("www.dibisis.net")
	    .setPath("/sub/subsub/file.php")
	    .setParameter("slaeId", "h10101")
	    .setParameter("test", "fdfd")
	    .build();

	    httpGet.setURI(uri);
//	    httpGet.setURI(new URI(uri));
	    
	    HttpResponse response = httpClient.execute(httpGet);

	    HttpEntity resEntity = response.getEntity();

	    result = EntityUtils.toString(resEntity);

	    return result;

	}

	private static String cookies;
	
	 public static String  getCookies() {
		return cookies;
	  }
	 
	  public void setCookies(String cookies) {
		this.cookies = cookies;
	  }
	
	
	  private HttpClient getHttpClient() {

//        try {
//
//            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//
//            trustStore.load(null, null);
//
//            // TODO Deprecated 다른거로 해야함....
//            SSLSocketFactory sf = new SSLConnectionSocketFactory(trustStore);
//
//            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//
//            HttpParams params = new BasicHttpParams();
//
//            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//
//            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//
//
//            SchemeRegistry registry = new SchemeRegistry();
//
//            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//
//            registry.register(new Scheme("https", sf, 443));
//
//
//            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
//
//
//            return new DefaultHttpClient(ccm, params);
//
//        } catch (Exception e) {
//
            return new DefaultHttpClient();
//
//        }

    }
}
