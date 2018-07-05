package com.okitoki.checklist.utils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by okc on 2016-07-16.
 */
public class JsonParser {

    String charset = "UTF-8";
    HttpURLConnection conn;
    DataOutputStream wr;
    StringBuilder result;
    URL urlObj;
    JSONObject jObj = null;
    StringBuilder sbParams;
    String paramsString;

    public JSONObject getJsonData(String url){
        JSONObject responseJSON = null;

        HttpURLConnection   conn    = null;

        OutputStream os   = null;
        InputStream           is   = null;
        ByteArrayOutputStream baos = null;
        try {
            URL urlObj = new URL(url);
            conn = (HttpURLConnection)urlObj.openConnection();
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Cache-Control", "no-cache");
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("X-Naver-Client-Id", "wASKsyyrWrU72MQwqVmG");
            conn.setRequestProperty("X-Naver-Client-Secret", "rgrOi3RJcH");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);

//            JSONObject job = new JSONObject();
//            job.put("phoneNum", "01000000000");
//            job.put("name", "test name");
//            job.put("address", "test address");

//            os = conn.getOutputStream();
////            os.write(job.toString().getBytes());
//            os.flush();

            String response;

            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {

                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }
                byteData = baos.toByteArray();

                response = new String(byteData);

                responseJSON = new JSONObject(response);
    //            Boolean result = (Boolean) responseJSON.get("result");
    //            String age = (String) responseJSON.get("age");
    //            String job = (String) responseJSON.get("job");

//                Log.i("", "DATA response = " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseJSON;

    }


}