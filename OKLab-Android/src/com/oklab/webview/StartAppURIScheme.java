package com.oklab.webview;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.oklab.BaseActivity;
import com.oklab.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by okc on 2015-01-19.
 * URI스키마 활용한 App Activity의 외부 Launch 제공
 * 카카오, 다음네이버등에서도 제공하는 Acitivty URIScheme가 있다.
 * Ref : http://developer.android.com/training/basics/intents/filters.html
 * Ref : http://nsinc.tistory.com/77
 * Ref : http://blog.daum.net/mailss/36
 * Ref : https://ch7895.wordpress.com/2013/01/15/android-url-scheme-%EC%A3%BC%EC%86%8C%EC%B0%BD%EC%97%90%EC%84%9C-%EC%95%B1%EC%8B%A4%ED%96%89/
 1. 실행할 앱의 scheme 정보를 알아야 한다.
 만약 만들고 있거나, 소스코드가 있는 앱이라면 Manifest파일에 다음을 추가 한다.
 <activity android:name="StartAppURLIcheme" >
 <intent-filter>
 <action android:name="android.intent.action.VIEW" />
 <category android:name="android.intent.category.DEFAULT" />
 <category android:name="android.intent.category.BROWSABLE" />
 <data android:scheme="app" android:host="host.domain.com" />
 </intent-filter>
 </activity>
 2. 웹페이지 또는 링크를 다음과 같이 만든다.. 앱실행 또는 구글 플래이 다운로드 화면으로 이동
 *
 * <a href=”app://host.domain.com?param1=1&param2=2″>
 *
 * <a id="applink" href="intent://host.domain.com#Intent;scheme=app;action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package=com.domain.host;end">
 *     앱실행 또는 구글 플래이 다운로드 화면으로 이동</a>
 *
 */
public class StartAppURIScheme extends BaseActivity implements View.OnClickListener{
    private String mHtml;
    private static final String FILE="url_scheme.html";

    private static final int OPERATOR_PLUS=0, OPERATOR_MINUS=1, OPERATOR_MULTI=2, OPERATOR_DIVIDE=3, OPERATOR_MOD=4; 	//연산 종류
    private static final String KEY_OP1="param1", KEY_OP2="param2", KEY_OPERATOR="operator";	//url을 통해서 넘어오는 파라미터 키

    private EditText mOperandEdit1, mOperandEdit2;		//피연산자 입력 뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.url_scheme_activity);

        mOperandEdit1 = (EditText)findViewById(R.id.operand1);
        mOperandEdit2 = (EditText)findViewById(R.id.operand2);

        findViewById(R.id.btn_web).setOnClickListener(this);		//웹브라우저 실행 버튼
        findViewById(R.id.btn_copy).setOnClickListener(this);		//경로 복사 버튼
        findViewById(R.id.btn_move).setOnClickListener(this);		//계산기로 이동하는 버튼

        File dir = getExternalCacheDir();
        if(dir==null) {
            Log.d("UriScheme" , "dir = null");
        }
        else {
            if(!dir.exists()) dir.mkdirs();						//외장 데이터영역(cache 영역) 까지 폴더 만들기
            mHtml = dir.getAbsolutePath()+"/"+FILE;	//html 파일 이동할 경로

            File html = new File(mHtml);					//html 복사할 파일 생성
//            if(!html.exists()) {									//asset에서 html 파일을 아직 복사하지 않았으면
                try {
                    InputStream is = getAssets().open(FILE);						//asset에서 파일을 읽을 stream 객체 얻는다
                    FileOutputStream fos = new FileOutputStream(mHtml);	//외장 데이터 영역에 저장할 stream 객체

                    int len = -1;									//읽은 크기
                    byte buffer[] = new byte[2048];			//파일 복사할 버퍼
                    while( (len = is.read(buffer)) != -1 )		//버퍼에 읽어서
                        fos.write(buffer, 0, len);					//파일 복사
                    fos.flush();
                    is.close();
                    fos.close();
                }
                catch (Exception e) { e.printStackTrace(); }
//            }

        }

        // scheme를 통해 intent안에 넘어온 Data를 받는 부분.
        Uri data = getIntent().getData();
        if(data != null) {									//데이터가 있으면 url을 통해서 실행
            String op1 = data.getQueryParameter(KEY_OP1);	//피연사자1 파라미터를 가져온다
            mOperandEdit1.setText(op1);

            String op2 = data.getQueryParameter(KEY_OP2);	//피연사자2 파라미터를 가져온다
            mOperandEdit2.setText(op2);

            String operator = data.getQueryParameter(KEY_OPERATOR);	//연사자 파라미터를 가져온다

            Toast.makeText(this, "Intent data = " + data.getQuery().toString() , Toast.LENGTH_SHORT).show();

//            if(!TextUtils.isEmpty(op1) && !TextUtils.isEmpty(op2))				//피연산 1, 2의 값이 있으면
//                isUriScheme = true;											//url 통해서 실행되었다고 판단
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_web) {		//기본 웹브라우저 실행
            Intent i = new Intent();

            ComponentName comp = new ComponentName("com.android.browser", "com.android.browser.BrowserActivity");
            i.setComponent(comp);
            i.setAction(Intent.ACTION_VIEW);
            i.addCategory(Intent.CATEGORY_BROWSABLE);
            i.setData(Uri.parse("file://" + mHtml));
            startActivity(i);
        }
        else if(v.getId() == R.id.btn_copy) {	//html 파일경로 복사
//            ClipboardManager cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//                cm.setPrimaryClip(ClipData.newPlainText("", "file://" + mHtml));
//            else {
//                cm.setText("file://"+mHtml);
//            }
//            Toast.makeText(this, "URL이 복사되었습니다", Toast.LENGTH_SHORT).show();
        }
        else if(v.getId() == R.id.btn_move) {	//계산기로 이동
            //Intent delete = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, Uri.parse("package:ok.lab"));
            //startActivity(delete);
//            startActivity(new Intent(this, BkCalculatorActivity.class));

            // [앱에서 호출]
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("app://host.domain.com?param1=test&param2=test2"));
            startActivity(intent);
        }
        finish();
    }
}
