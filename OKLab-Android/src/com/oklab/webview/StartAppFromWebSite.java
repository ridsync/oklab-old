package com.oklab.webview;

/**
 * Created by okc on 2015-01-19.
 * Ref : http://developer.android.com/training/basics/intents/filters.html
 * Ref : http://blog.daum.net/mailss/36
 * Ref : https://ch7895.wordpress.com/2013/01/15/android-url-scheme-%EC%A3%BC%EC%86%8C%EC%B0%BD%EC%97%90%EC%84%9C-%EC%95%B1%EC%8B%A4%ED%96%89/
 1. 실행할 앱의 scheme 정보를 알아야 한다.
 만약 만들고 있거나, 소스코드가 있는 앱이라면 Manifest파일에 다음을 추가 한다.
 <activity android:name="testLaunch" >
 <intent-filter>
 <action android:name="android.intent.action.VIEW" />
 <category android:name="android.intent.category.DEFAULT" />
 <category android:name="android.intent.category.BROWSABLE" />
 <data android:scheme="test_scheme" android:host="test_host" />
 </intent-filter>
 </activity>
 2. 웹페이지 또는 링크를 다음과 같이 만든다.. 앱실행 또는 구글 플래이 다운로드 화면으로 이동
 *
 * <a id="applink" href="intent://test_host#Intent;scheme=test_scheme;action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package=com.test.app;end">
 *     앱실행 또는 구글 플래이 다운로드 화면으로 이동</a>
 *
 */
public class StartAppFromWebSite {


}
