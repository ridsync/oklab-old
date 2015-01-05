package com.oklab.util;
 
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oklab.R;

/**
 * XML Parsing : XmlPullParser , DOM  Parser , SAX Parser
 * @author ok
 *
 */
public class XMLParsing extends Activity{

	 private static final String TAG = "XMLParsing";
	 private static final String ENCODING_UTF8 = "utf-8";
	 private static final String ENCODING_EUC_KR = "euc-kr";
	 private static String RSS_TEST_URL4 = "http://www.pressian.com/rss/rss.xml";
	 private static String RSS_TEST_URL2 = "http://www.it.co.kr/rss/mediaitRss2.0.php?nClassifications=503,521,535";
	 private static String RSS_TEST_URL = "http://rss.cbs.co.kr/nocutnews.xml";
	 private static String RSS_TEST_URL3 = "http://rss.joinsmsn.com/joins_news_list.xml";
	 private static final String XML_FILE_PATH = "res/raw/myfeed.xml";
	 
	 private EditText etUrl ;
	 private Button btnDownRss;
	 private ListView mRsslist;
	 private RssAdapter mRSSAdapter;
	 
	 private ArrayList<RSSItem> mRssItemList;
	 private Context mContext;
	 LayoutInflater mLayoutInflater = null;
	 Handler handler = new Handler();
	 
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xmlparsing);
		mContext = this;
		mLayoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRssItemList = new ArrayList<RSSItem>();
		mRssItemList = downXmlData();
		
		initView();
		
	}
	 
	 private void initView() {
		 
		 etUrl = (EditText)findViewById(R.id.et_url);
		 etUrl.setText(RSS_TEST_URL);
		 btnDownRss = (Button)findViewById(R.id.btn_rssdown);
		 btnDownRss.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RSS_TEST_URL = etUrl.getText().toString();
				// ※ HttpGet   Url에 기호들어가면 에러난다
				Runnable action = new Runnable() {
					@Override
					public void run() {
						if (RSS_TEST_URL.trim().length() > 0){
							mRssItemList = downXmlData();
							if (mRssItemList != null){
								Log.d(TAG, "mRssItemList(downXmlData) = " + mRssItemList.toString());
								
								if ( mRssItemList.size() == 0)
								Toast.makeText(mContext, "파싱데이터없음", Toast.LENGTH_SHORT).show();
								
								mRSSAdapter.notifyDataSetChanged();
							}
						}else{
							Toast.makeText(mContext, "URL이 없습니다", Toast.LENGTH_SHORT).show();
						}						
					}
				};
				runOnUiThread(action);
			}
		});
		 
		 mRsslist = (ListView)findViewById(R.id.rss_list);
		 mRSSAdapter = new RssAdapter(this, R.layout.item_xmlparsing, mRssItemList);
			mRsslist.setAdapter(mRSSAdapter);
			mRsslist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			mRsslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					String rssUrl =  mRssItemList.get(position).getUrl();
					Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(rssUrl));
					startActivity(intent);
				}
			});
	 }
	 
	private ArrayList<RSSItem> downXmlData(){
		ArrayList<RSSItem> result ;
		Log.d(TAG, "downXmlData Started()");
		
        boolean valid1 = URLUtil.isFileUrl(RSS_TEST_URL);
        boolean valid = URLUtil.isValidUrl(RSS_TEST_URL);
        Log.d( TAG , "isValidUrl = " + valid + "/ isFileUrl" + valid1);
        try {
        	HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(RSS_TEST_URL);
        	
        	HttpResponse response = httpClient.execute(httpGet);
        	int statusCode = response.getStatusLine().getStatusCode();
        	if(statusCode == HttpStatus.SC_NOT_FOUND){
        		Log.d( TAG , "SC_NOT_FOUND" );
        		return null;
        	}else if(statusCode != HttpStatus.SC_OK){
        		Log.d( TAG , "HttpStatus.SC_OK" );
        		return null;
        	}
        	
        	HttpEntity httpEntity = response.getEntity();
        	
        	// ※ XML Data의 encoding을 읽어와서 각encoding으로 String 생성하여
        	// 한글이 깨지지 않도록 처리 
        	if (httpEntity != null){
        		InputStream inputStream = httpEntity.getContent();
        		 byte[] byteArray = convertStreamToByteArray(inputStream);
        		InputStreamReader in = null;
        		BufferedReader bufferedreader;
//        		String encoding = LocalString( inputStream );
        		String encoding = getEncodingInfoXML( new String(byteArray) );
//        		Log.d( TAG , "encoding = " + encoding );
        		
        		String xmlString = new String(byteArray, encoding);
        		
//        		if ( ENCODING_EUC_KR.equals(encoding.toLowerCase())){
//        			  xmlString = new String(byteArray, ENCODING_EUC_KR);
////        			 in = new InputStreamReader(inputStream, ENCODING_EUC_KR);
////        			 bufferedreader = new BufferedReader(in);
////        			 Log.d( TAG ,  "ENCODING_EUC_KR in.getEncoding() = " +in.getEncoding() );
//        		} else {
//        			 xmlString = new String(byteArray, ENCODING_UTF8);
////       			 	in = new InputStreamReader(inputStream, ENCODING_UTF8);
////       			 bufferedreader = new BufferedReader(in);
////       			 	Log.d( TAG ,  "ENCODING_UTF8 in.getEncoding() = " +in.getEncoding() );
//        		}
        		
//        		StringBuilder stringBuilder = new StringBuilder();
//        		
//        		String stringReadLine = null;
//   
//        		while ((stringReadLine = bufferedreader.readLine()) != null) {
//        			stringBuilder.append(stringReadLine + "\n");	
//        		}
        		
//        		if(null != stringBuilder){
//        			return parseRssXml( stringBuilder.toString() );
//        		}
        		if(null != xmlString){
        			return parseRssXml( xmlString );
        		}
        		
        	}	
        }catch (IllegalArgumentException e) {
        	Log.d(TAG, "IllegalArgumentException" );
        	Log.d(TAG, e.getMessage() );
        }  catch (ClientProtocolException e) {
        	Log.d(TAG, "ClientProtocolException" );
        	Log.d(TAG, e.getMessage() );
        	return result = new ArrayList<RSSItem>();
        } catch (IOException e) {
        	Log.d(TAG, "IOException" );
        	Log.d(TAG, e.getMessage() );
        	return  result = new ArrayList<RSSItem>();
        }
		return  result = new ArrayList<RSSItem>();
	}
	
	
	public 	byte[] convertStreamToByteArray(InputStream is) throws IOException{
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();

				int nRead;
				byte[] data = new byte[16384];

					while ((nRead = is.read(data, 0, data.length)) != -1) {
					  buffer.write(data, 0, nRead);
					  buffer.flush();
					}

				return buffer.toByteArray();
	}
	
	public static String LocalString( InputStream inputStream)
	 {
	  if (inputStream == null)
	   return null;
	  else {
		   byte[] b;
	
		   try {
			  Reader in = new InputStreamReader(inputStream, ENCODING_EUC_KR);
			  BufferedReader bufferedreader = new BufferedReader(in);
		  		StringBuilder stringBuilder = new StringBuilder();
		  		String stringReadLine = null;

  		while ((stringReadLine = bufferedreader.readLine()) != null) {
  			stringBuilder.append(stringReadLine + "\n");	
  		}
			   
			    b = stringBuilder.toString().getBytes("8859_1");
			    CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
			    try {
			     CharBuffer r = decoder.decode( ByteBuffer.wrap( b));
			     return ENCODING_UTF8;
			    } catch (CharacterCodingException e) {
			     return ENCODING_EUC_KR;
			    }
		   } catch (UnsupportedEncodingException e1) {
			   e1.printStackTrace();
		   } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  } return null;
	 }
	
	
	private String getEncodingInfoXML(String xml){
		
		 try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
	        XmlPullParser xpp = factory.newPullParser();
	        xpp.setInput( new StringReader(xml ) );
	        int eventType = xpp.next();
	        String result = xpp.getInputEncoding();
	        if (result==null) result = "utf-8"; // Null일때 기본 utf-8리턴
	        return result;
		} catch (IOException e) {
			e.printStackTrace();
			 return "utf-8";
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			 return "utf-8";
		}
		
	}
	
	
	
	private ArrayList<RSSItem> parseRssXml(String xml){
		Log.d(TAG, "Parsing Start()");
		ArrayList<RSSItem> tempRssItemList = new ArrayList<RSSItem>();
		
    	try {
    		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            
            xpp.setInput(new StringReader(xml));
            int eventType = xpp.getEventType();
            String tag = null;
            RSSItem tempRssItem = null;
            boolean isStart = false;
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                	
                }else if (eventType == XmlPullParser.END_DOCUMENT) {
                	
                }else if (eventType == XmlPullParser.START_TAG) {
                	tag = xpp.getName();
//                	if(!isReload && xpp.getInputEncoding().toUpperCase().equals(NetworkInfo.ConnectInfo.RESPONSE_ENCODING)){
//                		isReload = true;
//                    	downLoadData(NetworkInfo.ConnectInfo.RESPONSE_ENCODING);
//                    	return;
//                    }
                	if(tag.equals("item")){
                		tempRssItem = new RSSItem();
                		isStart = true;
                		
                	}
                	//제목
                	if( isStart && tag.equals("title")){
                		tempRssItem.setTitle(android.text.Html.fromHtml(xpp.nextText()).toString());
                		
                	}
                	//링크
                	else if( isStart && tag.equals("link")){
                    	tempRssItem.setUrl(xpp.nextText());
                    }
                	//설명
//                	else if( isStart && tag.equals("description")){
//                    	tempRssItem.setDescription(xpp.nextText());
//                    }
                    //날짜
                	else if( isStart && (tag.equals("pubDate") || tag.equals("date")) ){
                    	tempRssItem.setDate(xpp.nextText());
                    	//파싱이 제대로 되지않는 데이터의 경우는 추가하지않는다.
//                    	if(null != tempRssItem.getTitle() && null!= tempRssItem.getDate() && null != tempRssItem.getUrl()){
//                    		
//                    	}
                    }
                
                }else if (eventType == XmlPullParser.END_TAG) {
                	tag = xpp.getName();
                	//rss item 하나의 파싱이 끝난 경우
                    if(tag.equals("item")){
                    	tempRssItemList.add(tempRssItem);
                		isStart = false;
                	}
                }else if (eventType == XmlPullParser.TEXT) {

                }

                eventType = xpp.next();
            }
        } catch (Exception e) {
        	Log.d(TAG, "Exception Message = " + e.getMessage().toString());
        }
    	Log.d(TAG, "Parsing Finished()");
		return tempRssItemList;
	}
	
	private class RssAdapter extends ArrayAdapter<RSSItem>{
		
		public RssAdapter(Context context, int textViewResourceId,
				List<RSSItem> objects) {
			super(context, textViewResourceId, objects);
		}
		@Override
	    public int getCount() {
	        return mRssItemList.size();
	    }
	 
	    @Override
	    public RSSItem getItem(int position) {
	        return mRssItemList.get(position);
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        return position;
	    }
		public View getView(final int position, View convertView, ViewGroup parent) {
		if(position < 0){
			return null;
		}
		if(position>=mRssItemList.size()){
			return new View(mContext);
		}
		
		ViewHolder holder = null;
		if(convertView==null){
			convertView = mLayoutInflater.inflate(R.layout.item_xmlparsing, null);
			
			holder = new ViewHolder();
			holder.tv_title = (TextView) convertView.findViewById(R.id.twit_title);
			holder.tv_date = (TextView) convertView.findViewById(R.id.twit_date);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_title.setText(getItem(position).getTitle());
		holder.tv_date.setText(getItem(position).getDate());
		
		return convertView;
		}
		
		class ViewHolder {
			private TextView tv_title;
			private TextView tv_date;
		}
		
	}
	
	
	
	public void domParseTest() throws Exception {  
		  
		  System.out.println("==============================");  
		  System.out.println("domParseTest()");  
		  System.out.println("==============================");  
		    
		  File xmlFile = new File(XML_FILE_PATH);  
		    
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
		  DocumentBuilder db = dbf.newDocumentBuilder();  
		  Document doc = db.parse(xmlFile);  
		    
		  doc.getDocumentElement().normalize();  
		    
		  System.out.printf("Root element:%s\n", doc.getDocumentElement().getNodeName());  
		  NodeList itemNodeList = doc.getElementsByTagName("item");  
		    
		  for (int s = 0; s < itemNodeList.getLength(); s++) {  
		  
		   Node itemNode = itemNodeList.item(s);  
		  
		   if (itemNode.getNodeType() == Node.ELEMENT_NODE) {  
		  
		    Element itemElement = (Element)itemNode;  
		      
		    NodeList titleNodeList = itemElement.getElementsByTagName("title");  
		    Element titleElement = (Element)titleNodeList.item(0);  
		    NodeList childTitleNodeList = titleElement.getChildNodes();  
		    System.out.printf("[title : %s]\n", ((Node)childTitleNodeList.item(0)).getNodeValue());  
		      
		    NodeList linkNodeList = itemElement.getElementsByTagName("link");  
		    Element linkElement = (Element) linkNodeList.item(0);  
		    NodeList childLinkNodeList = linkElement.getChildNodes();  
		    System.out.printf("[link : %s]\n", ((Node)childLinkNodeList.item(0)).getNodeValue());  
		   }  
		  
		  }  
		 }  
	
	 /** 
	  *  
	  * @throws Exception 
	  */  
	 public void saxParseTest() throws Exception {  
	    
	  System.out.println("==============================");  
	  System.out.println("saxParseTest()");  
	  System.out.println("==============================");  
	    
	  File xmlFile = new File(XML_FILE_PATH);  
	    
	  SAXParser parser = SAXParserFactory.newInstance().newSAXParser();  
	  DefaultHandler dh = new DefaultHandler() {  
	     
	   private boolean firstElement = true;  
	   private boolean inItem = false;  
	   private boolean inTitle = false;  
	   private boolean inLink = false;  
	   private StringBuilder characterSB;  
	     
	   @Override  
	   public void startDocument() throws SAXException {  
	    System.out.println("startDocument");  
	    super.startDocument();  
	   }  
	  
	   @Override  
	   public void endDocument() throws SAXException {  
	    System.out.println("endDocument");  
	    super.endDocument();  
	   }  
	  
	   @Override  
	   public void startElement(String uri, String localName,  
	     String qName, Attributes attributes) throws SAXException {  
	      
	    if (firstElement) {  
	     System.out.printf("Root element:%s\n", qName);  
	     firstElement = false;  
	    }  
	      
	    if (qName.equals("item")) {  
	     inItem = true;  
	    } else if (qName.equals("title")) {  
	     inTitle = true;  
	    } else if (qName.equals("link")) {  
	     inLink = true;  
	    }  
	      
	    if (inItem && (inTitle || inLink)) {  
	     characterSB = new StringBuilder();  
	    }  
	      
	    super.startElement(uri, localName, qName, attributes);  
	   }  
	  
	   @Override  
	   public void characters(char[] ch, int start, int length)  
	     throws SAXException {  
	      
	    if (characterSB != null) {  
	     characterSB.append(handleCharacters(ch, start, length));  
	    }  
	      
	    super.characters(ch, start, length);  
	   }  
	  
	   @Override  
	   public void endElement(String uri, String localName, String qName)  
	     throws SAXException {  
	      
	    if (characterSB != null) {  
	     if (inItem && inTitle) {  
	      System.out.printf("[title : %s]\n", characterSB.toString());  
	     } else if (inItem && inLink) {  
	      System.out.printf("[link : %s]\n", characterSB.toString());  
	     }  
	     characterSB = null;  
	    }  
	      
	    if (qName.equals("item")) {  
	     inItem = false;  
	    } else if (qName.equals("title")) {  
	     inTitle = false;  
	    } else if (qName.equals("link")) {  
	     inLink = false;  
	    }  
	      
	    super.endElement(uri, localName, qName);  
	   }  
	     
	   /** 
	    *  
	    * @param ch 
	    * @param start 
	    * @param end 
	    * @return 
	    */  
	   public String handleCharacters(char[] ch, int start, int length) {  
	      
	    StringBuilder sb = new StringBuilder();  
	    for (int i = start; i < start + length; i++) {  
	     sb.append(ch[i]);  
	    }  
	    return sb.toString();  
	   }  
	  };  
	  parser.parse(xmlFile, dh);  
	 }  
}
