package com.okitoki.checklist.holiday;

import com.okitoki.checklist.utils.JsonParser;
import com.okitoki.checklist.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2016-02-09.
 */
public class RestUtil {

    public static int MART_CODE_EMART = 1000;
    public static int MART_CODE_HOMEPLUS = 2000;
    public static int MART_CODE_LOTTE = 3000;
    public static int MART_CODE_COSTCO = 4000;
    private static int pointCode = 0;

// MartCode 이마트 1000 , 홈플러스 2000 , 롯데마트 3000 , 코스트코 4000

    public static String[] emartRegionList= {"서울","부산","인천","대구","광주","대전","울산","강원","경기","경상","전라","충청"
            ,"제주","세종"};

    public static String[] homeplusRegionList= {"서울","대전","광주","대구","부산","울산","경기/인천","강원","충북","충남","경북"
            ,"경남","전북","전남","제주","세종"};

    public static String[] lotteRegionList= {"서울","인천","경기","강원","충남","대전","충북","경북","대구","전북","광주","전남"
            ,"경남","울산","부산","제주"};

    public static String[] costcoRegionList= {"서울","인천","경기","강원","충남","대전","충북","경북","대구"};

    public static List<RestMartRegion> getEmartRegionList(String[] arraylist){
        List<RestMartRegion> martList = new ArrayList<>();
        for (String element : arraylist) {
            RestMartRegion region = new RestMartRegion();
            region.setRegionName(element);
            martList.add(region);
        }
        return martList;
    }

    /**
     *  이마트 파싱
     */
    public static List<RestMartInfo> jsonParsingEmart(){

        pointCode = MART_CODE_EMART; // 이마트 기본 매장코드
        List<RestMartInfo> emartList = new ArrayList<RestMartInfo>();
        try {
            Document month = Jsoup.connect("http://store.emart.com/main/holiday.do").get(); //이번달 웹에서 내용을 가져온다.
//                    month = Jsoup.connect("http://store.emart.com/main/holiday.do?view=next").get(); // 다음달 웹에서 내용을 가져온다.
//                        Elements contents = doc.select("div.tab_cont.cont02 ul.area_list li a"); //내용 중에서 원하는 부분을 가져온다.
//                    Elements contents1 = doc.select("div.dtable_list.mt20"); //내용 중에서 원하는 부분을 가져온다.
            emartList.addAll(parsingMartEachLocation(month, "#d-store-A tr")); // 서울
            emartList.addAll(parsingMartEachLocation(month, "#d-store-B tr")); // 부산
            emartList.addAll(parsingMartEachLocation(month, "#d-store-C tr")); //
            emartList.addAll(parsingMartEachLocation(month, "#d-store-D tr")); //
            emartList.addAll(parsingMartEachLocation(month, "#d-store-E tr")); //
            emartList.addAll(parsingMartEachLocation(month, "#d-store-F tr")); //
            emartList.addAll(parsingMartEachLocation(month, "#d-store-G tr")); //
            emartList.addAll(parsingMartEachLocation(month, "#d-store-H tr")); //
            emartList.addAll(parsingMartEachLocation(month, "#d-store-I tr")); //
            emartList.addAll(parsingMartEachLocation(month, "#d-store-J tr")); //
            emartList.addAll(parsingMartEachLocation(month, "#d-store-L tr")); //
            emartList.addAll(parsingMartEachLocation(month, "#d-store-N tr")); //
            emartList.addAll( parsingMartEachLocation(month, "#d-store-P tr")); //
            emartList.addAll(parsingMartEachLocation(month, "#d-store-Q tr")); //
            Logger.d("JsonParsing","[Emart] mart element size = " +  emartList.size());
        } catch (Exception e){
            e.printStackTrace();
        }
        return emartList;
    }

    public static List<RestMartInfo> parsingMartEachLocation(Document doc, String regionHtmlId){
        if(doc==null) return new ArrayList<>();

        Elements contentsa = doc.select(regionHtmlId); //내용 중에서 원하는 부분을 가져온다.
        String regionName = getRegionName(regionHtmlId);

        List<RestMartInfo> martList = new ArrayList<>();
        for (Element element : contentsa) {
            RestMartInfo martData = new RestMartInfo();
            int i = 0;
            for (Element td: element.children()) {
                i++;
                if(i==1){
                    martData.setPointName(td.text());
                } else if (i==2){
                    martData.setRestDateInfo(td.text());// TODO 이번달의 경우 1번에
                    martData.setRestDateInfoNext(td.text());// TODO 다음달의 경우 2번에
                } else {
                    martData.setPhone(td.text());
                }
                //                Logger.d("JsonParsing","[Parsing] td = "+td.text());
            }
            if( "점포명".equalsIgnoreCase(martData.getPointName()) ){
                continue;
            }
            martData.setMartCode(MART_CODE_EMART);
            martData.setPointCode(pointCode++); // 순서대로? 고유코드부여?  문제있으니 쿼리는 지점명으로 하자.
            martData.setRegionName(regionName); // 각 마트 지점  지역명
            martList.add(martData);
        }
        Logger.d("JsonParsing", regionName + "[Parsing] martList size = " + martList.size()+ " element = " + martList.toString());
        return martList;
    }

    public static String getRegionName(String regionHtmlId) {
        if ("#d-store-A tr".equalsIgnoreCase(regionHtmlId)) {
            return "서울";
        } else if ("#d-store-B tr".equalsIgnoreCase(regionHtmlId)) {
            return "부산";
        }else if ("#d-store-C tr".equalsIgnoreCase(regionHtmlId)) {
            return "인천";
        } else if ("#d-store-D tr".equalsIgnoreCase(regionHtmlId)) {
            return "대구";
        } else if ("#d-store-E tr".equalsIgnoreCase(regionHtmlId)) {
            return "광주";
        } else if ("#d-store-F tr".equalsIgnoreCase(regionHtmlId)) {
            return "대전";
        } else if ("#d-store-G tr".equalsIgnoreCase(regionHtmlId)) {
            return "울산";
        } else if ("#d-store-H tr".equalsIgnoreCase(regionHtmlId)) {
            return "강원";
        } else if ("#d-store-I tr".equalsIgnoreCase(regionHtmlId)) {
            return "경기";
        } else if ("#d-store-J tr".equalsIgnoreCase(regionHtmlId)) {
            return "경상";
        } else if ("#d-store-L tr".equalsIgnoreCase(regionHtmlId)) {
            return "전라";
        } else if ("#d-store-N tr".equalsIgnoreCase(regionHtmlId)) {
            return "충청";
        } else if ("#d-store-P tr".equalsIgnoreCase(regionHtmlId)) {
            return "제주";
        } else if ("#d-store-Q tr".equalsIgnoreCase(regionHtmlId)) {
            return "세종";
        } else {
            return "기타";
        }

    }

    /**
     *  홈플러스 파싱
     */
//    static String[] martCodeList= {"서울|0|1","서울|0|2","대전|1|1","광주|2|1","대구|3|1","부산|4|1","부산|4|2","울산|5|1","경기/인천|6|1","경기/인천|6|1","경기/인천|6|2","경기/인천|6|3","경기/인천|6|4","경기/인천|6|5","강원|7|1","충북|8|1","충남|9|1","경북|10|1"
//            ,"경남|11|1","전북|12|1","전남|13|1","제주|14|1","세종|15|1"};
    static String[] martCodeList= {"경기/인천|6|1","경기/인천|6|2","경기/인천|6|3","경기/인천|6|4","경기/인천|6|5"};

    public static List<RestMartInfo> jsonParsingHomePlus(){

        pointCode = MART_CODE_HOMEPLUS; // 홈플러스 기본 매장코드
                    List<RestMartInfo> homeplusList = new ArrayList<RestMartInfo>();

        try{
            for (int i = 0; i < martCodeList.length; i++) {
                String martCodeRaw = martCodeList[i];
                String[] martBaseInfos = martCodeRaw.split("\\|");
                String regionName = martBaseInfos[0];
                String regionNo = martBaseInfos[1];
                String pageNo = martBaseInfos[2];
                Document region = Jsoup.connect("http://corporate.homeplus.co.kr/store/hypermarket.aspx?searchKeyword=all&searchRegionNo="+regionNo+"&pageNo="+pageNo).get();
                homeplusList.addAll(parsingMartEachLocationHomeplus(region, regionName, regionNo, pageNo)); // 서울
            }
            Logger.d("JsonParsing","[HomePlus] mart element size = " +  homeplusList.size());
        } catch (Exception e){
            e.printStackTrace();
        }
        return homeplusList;
    }

    public static List<RestMartInfo> parsingMartEachLocationHomeplus(Document doc, String regionName, String regionCd , String pageNo){
        if(doc==null) return new ArrayList<>();

        Elements contentsa = doc.select("div.formTable tr"); //내용 중에서 원하는 부분을 가져온다.

        List<RestMartInfo> martList = new ArrayList<>();
        for (Element element : contentsa) {
            RestMartInfo martData = new RestMartInfo();
            int i = 0;
            for (Element td: element.children()) {
                if( "th".equalsIgnoreCase(td.nodeName()) ){
                    if( "매장명".equalsIgnoreCase(td.text()) ){
                        martData.setPointName(td.text());
                        break;
                    } else {
                        martData.setPointName(td.text());
                    }
                } else if ( "td".equalsIgnoreCase(td.nodeName()) ){
                    if(i==1){
                        martData.setLocation(td.text());
                    } else if(i==2){
                        martData.setRestDateInfo(td.text());
                    } else {
                        martData.setPhone(td.text());
                    }
                }
                i++;
            }
            if( "매장명".equalsIgnoreCase(martData.getPointName()) ){
                continue;
            }
            martData.setMartCode(MART_CODE_HOMEPLUS); // 이마트 1000 , 홈플러스 2000 , 롯데마트 3000 , 코스트코 4000
            martData.setPointCode(pointCode++); // 순서대로? 고유코드부여?  문제있으니 쿼리는 지점명으로 하자.
            martData.setRegionName( regionName ); // 각 마트 지점  지역명

            String restInfo = martData.getRestDateInfo();
            if(restInfo.indexOf("(") != -1){
                martData.setOpenHours( restInfo.substring(0, restInfo.indexOf("(") -1 ) );
                martData.setRestDateInfo( restInfo.substring( restInfo.indexOf("단,") +3 , restInfo.indexOf(")\"")) );
            }

            martList.add(martData);
        }
        Logger.d("JsonParsing", regionName + "[Parsing] martList size = " + martList.size()+ " element = " + martList.toString());
        return martList;
    }



    /**
     *  롯데마트 파싱
     */

    static String[] lotteMartCodeList= {"서울|BC0101|1","서울|BC0101|2","인천|BC0102|1","경기|BC0103|1","경기|BC0103|2","경기|BC0103|3","강원|BC0104|1","충남|BC0105|1","대전|BC0106|1","충북|BC0107|1","경북|BC0108|1","대구|BC0109|1","전북|BC0110|1","광주|BC0111|1"
            ,"전남|BC0112|1","경남|BC0113|1","경남|BC0113|2","울산|BC0114|1","부산|BC0115|1","제주|BC0116|1"};

    public static List<RestMartInfo> jsonParsingLotteMart(){

        pointCode = MART_CODE_LOTTE; // 롯데마트 기본 매장코드

        final List<RestMartInfo> lottemartList = new ArrayList<RestMartInfo>();

            try {

                for (int i = 0; i < lotteMartCodeList.length; i++) {
                    String martCodeRaw = lotteMartCodeList[i];
                    String[] martBaseInfos = martCodeRaw.split("\\|");
                    String regionName = martBaseInfos[0];
                    String regionCd = martBaseInfos[1];
                    String pageNo = martBaseInfos[2];
                    Document region = Jsoup.connect("http://company.lottemart.com/bc/branchSearch/branchSearch1.do?schBrnchTypeCd=BC0701&schRegnCd="+regionCd+"&currentPageNo="+pageNo).get();
                    lottemartList.addAll(parsingMartEachLocationLotteMart(region, regionName, regionCd, pageNo)); // 서울
                }
                Logger.d("JsonParsing","[LotteMart] mart element size = " +  lottemartList.size());
            } catch (Exception e){
                e.printStackTrace();
            }
        return lottemartList;
    }

    public static List<RestMartInfo> parsingMartEachLocationLotteMart(Document doc, String regionName, String reginNo , String pageNo){
        if(doc==null) return new ArrayList<>();

        Elements contentsa = doc.select("table.wa_branch_list tr"); //내용 중에서 원하는 부분을 가져온다.

        List<RestMartInfo> martList = new ArrayList<>();
        for (Element element : contentsa) {
            RestMartInfo martData = new RestMartInfo();
            int i = 0;
            for (Element td: element.children()) {
                if ( "td".equalsIgnoreCase(td.nodeName()) ){
                    i++;
                    if(i==1){
                        martData.setPointName(td.text());
                    } else if(i==2){
                        martData.setRestDateInfo(td.text());
                    } else if(i==3){
                        martData.setOpenHours(td.text());
                    } else if(i==4){
                        martData.setLocation(td.text());
                    }else if(i==5){
                        martData.setPhone(td.text());
                    }
                }
            }
            if( martData.getPointName() == null ){
                continue;
            }
            martData.setMartCode(MART_CODE_LOTTE); // 이마트 1000 , 홈플러스 2000 , 롯데마트 3000 , 코스트코 4000
            martData.setPointCode(pointCode++); // 순서대로? 고유코드부여?  문제있으니 쿼리는 지점명으로 하자.
            martData.setRegionName( regionName ); // 각 마트 지점  지역명
            martList.add(martData);
        }
        Logger.d("JsonParsing", regionName + "[Parsing] martList size = " + martList.size()+ " element = " + martList.toString());
        return martList;
    }


        /**
         *  코스트코 파싱
         */

        static String[] costcoCodeList= {"Gwangmyeong","Yangjae","Sangbong","Ilsan","Cheonan","Gongse","Euijeongbu","Yangpyung","Busan","Daegu","Daejeon","Ulsan" };

        public static List<RestMartInfo> jsonParsingCostco(){

            pointCode = MART_CODE_COSTCO; // 코스트코 기본 매장코드

            final List<RestMartInfo> costcoList = new ArrayList<RestMartInfo>();

            try {

                for (int i = 0; i < costcoCodeList.length; i++) {
                    String regionName = costcoCodeList[i];
                    Document doc = Jsoup.connect("https://www.costco.co.kr/store-finder/store?code="+regionName).get();
                    costcoList.add(parsingMartEachLocationCostco(doc, regionName, i)); // 서울
                }
                Logger.d("JsonParsing","[COSTCO] mart element size = " +  costcoList.size());
            } catch (Exception e){
                e.printStackTrace();
            }
            return costcoList;
        }

        public static RestMartInfo parsingMartEachLocationCostco(Document doc, String regionName, int index){
            if(doc==null) return new RestMartInfo();

            RestMartInfo martData = new RestMartInfo();
            try {
                JSONObject json = new JSONObject(doc.body().text());
                JSONArray arrayData = (JSONArray)json.get("data");

                if (arrayData.length() > 0 ){
                    JSONObject data = (JSONObject) arrayData.get(0);
                    martData.setPointCode(pointCode+index);
                    martData.setMartCode(MART_CODE_COSTCO); // 이마트 1000 , 홈플러스 2000 , 롯데마트 3000 , 코스트코 4000
                    martData.setRegionName( regionName ); // 각 마트 지점  지역명
                    martData.setPointName(data.getString("displayName"));
                    martData.setPhone(data.getString("phone"));
                    martData.setLocation(data.getString("line1") + " " +data.getString("line2"));
                    martData.setLatitude(data.getDouble("latitude"));
                    martData.setLongitude(data.getDouble("longitude"));
                    martData.setRestDateInfo(data.getString("storeContent"));
                    martData.setOpenHours(data.getString("openings"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Logger.d("JsonParsing", regionName + "[Parsing] martList martData = " + martData);
            return martData;
        }


    public static HashMap<String,RestMartInfo> jsonParsingmartAddress(){

        final HashMap<String,RestMartInfo> costcoList = new HashMap<>();

        try {
                Document doc = Jsoup.connect("https://namu.wiki/w/%EC%9D%B4%EB%A7%88%ED%8A%B8/%EC%A7%80%EC%A0%90").get();
            Elements element = doc.select("table.wiki-table tr"); //내용 중에서 원하는 부분을 가져온다.

            for (Element td: element) {
                    RestMartInfo martData = new RestMartInfo();
                    String data = td.text();
                    if(data.contains("지점명") || ! data.contains(")") || ! data.contains("요일")) continue;

                    String pointName = data.substring(0,data.indexOf("점") +1);
                    String location = data.substring(data.indexOf("점") +1,data.indexOf(")") +1);
                    String restInfo = data.substring(data.indexOf(")") +1,data.lastIndexOf("요일") +2);
                    martData.setPointName(pointName);
                    martData.setLocation(location.trim());
                    martData.setRestDateInfo(restInfo.trim());
                    costcoList.put(pointName, martData);
            }

            Logger.d("JsonParsing","[EmartAddress] mart element size = " +  costcoList.size());
        } catch (Exception e){
            e.printStackTrace();
        }
        return costcoList;
    }


    // https://zonblog.com/%EC%9D%B4%EB%A7%88%ED%8A%B8-%EC%98%81%EC%97%85%EC%8B%9C%EA%B0%84-e%EB%A7%88%ED%8A%B8-%EA%B0%9C%EC%A0%90-%ED%8F%90%EC%A0%90-%EC%9A%B4%EC%98%81%EC%8B%9C%EA%B0%84/
    // EMart 영업시간 정보
    public static HashMap<String,String> getTimeInfoMaunalr(){

        // 10 : 23
        String[] array1 = {"묵동점","수색점","수서점","신월점","여의도점","장안점","송림점","경기광주점","구성점","김포한강점","안산점","안성점","양주점","여주점","진접점","평택점","포천점","화성봉담점","월평점","서산점","세종점","천안아산점","청주점","충주점","펜타포트점","비산점","김천점","마산점","상주점","안동점","양산점","양산점","영천점","진주점","통영점","서면점","서부산점","남원점","순천점","여수점","익산점","강릉점","동해점","속초점","원주점","서귀포점","신제주점","제주점"};

        // 10:00 ~ 22:00
        String[] array2 = {"이수점","인천공항점","광명점","보령점","제천점","천안터미널점","사천점","학성점","동광주점","태백점"};

        // 9:00 ~ 24:00
        String[] array3 = {"양재점","왕십리점"};

        // 10:30 ~ 23:00 – 인천점

        // 10 : 24
        // 나머진
        final HashMap<String,String> emartTimeList = new HashMap<>();

        for (String td: array1) {
            emartTimeList.put(td,"10:00 ~ 23:00");
        }
        for (String td: array2) {
            emartTimeList.put(td,"10:00 ~ 22:00");
        }
        for (String td: array3) {
            emartTimeList.put(td,"9:00 ~ 24:00");
        }
        emartTimeList.put("인천점","10:30 ~ 23:00");
            Logger.d("JsonParsing","[EmartAddress] mart emartTimeList size = " +  emartTimeList.size());
        return emartTimeList;
    }

    /**
     * Geo 정보
     */
    public static boolean setGeoCodeEachmartAddressDaum(RestMartInfo martInfo, String mart){

        boolean result = false;

        if(martInfo.getLatitude() > 0 && martInfo.getLongitude()>0){
          return true;
        }

        try {
            String martAddress = martInfo.getLocation();
            if("emart".equalsIgnoreCase(mart) || "costco".equalsIgnoreCase(mart) ){
                martAddress = martInfo.getLocation().substring(0, martAddress.indexOf("("));
            } else if ("lottemart".equalsIgnoreCase(mart)){
                String region = martAddress.substring(1,3) ;
                martAddress = martInfo.getLocation().substring(1, martAddress.indexOf("구"+ region));
            }

            String address = URLEncoder.encode(martAddress, "UTF-8");
            String url = "https://apis.daum.net/local/geo/addr2coord?apikey=2217d2ee691daad1e0c13f303635dcdf&q="+ address + "&output=json";
//            String url = "https://openapi.naver.com/v1/map/geocode?query="+ address;
            Logger.d("JsonParsing","[martGeocoding] url = " +  martAddress);
            JsonParser parser = new JsonParser();
            JSONObject json = parser.getJsonData(url);
            JSONObject channel = json.optJSONObject("channel");
            int jsonResult = channel.optInt("result");
            if(jsonResult > 0){
                JSONArray jsonArray =  channel.optJSONArray("item");
                if(jsonArray !=null && jsonArray.length() > 0){
                    JSONObject data = (JSONObject)jsonArray.get(0);
                    Double lat = (Double)data.opt("lat");
                    Double lng = (Double)data.opt("lng");

                    martInfo.setLatitude(lat);
                    martInfo.setLongitude(lng);
                }
                result = true;
            } else {
                result = false;
            }

            Logger.d("JsonParsing","[martGeocoding] result = " +  martInfo.getLatitude() + " / "+ martInfo.getLongitude());
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static boolean setGeoCodeEachmartAddress(RestMartInfo martInfo, String mart){

        boolean result = false;
        try {
            String martAddress = martInfo.getLocation();
            if("emart".equalsIgnoreCase(mart) || "costco".equalsIgnoreCase(mart) ){
                martAddress = martInfo.getLocation().substring(0, martAddress.indexOf("("));
            } else if ("lottemart".equalsIgnoreCase(martAddress)){
                String region = martAddress.substring(1,3) ;
                martAddress = martInfo.getLocation().substring(1, martAddress.indexOf("구"+ region));
            }

            String address = URLEncoder.encode(martAddress, "UTF-8");
            String url = "http://maps.googleapis.com/maps/api/geocode/json?address="+ address +"&language=ko&sensor=false";
            Logger.d("JsonParsing","[martGeocoding] url = " +  martAddress);
            JsonParser parser = new JsonParser();
            JSONObject json = parser.getJsonData(url);
//            JSONObject json = parser.makeHttpRequest(url,"GET",null);
            String status = json.optString("status");
            if("OK".equalsIgnoreCase(status) ){
                JSONArray jsonArray =  json.optJSONArray("results");
                if(jsonArray !=null && jsonArray.length() > 0){
                    JSONObject data = (JSONObject)jsonArray.get(0);
                    JSONObject geoMetry =  data.getJSONObject("geometry");
                    JSONObject location = geoMetry.getJSONObject("location");
                    Double lat = (Double)location.opt("lat");
                    Double lng = (Double)location.opt("lng");

                    martInfo.setLatitude(lat);
                    martInfo.setLongitude(lng);
                }
                result = true;
            } else {
                result = false;
            }

            Logger.d("JsonParsing","[martGeocoding] result = " +  martInfo.getLatitude() + " / "+ martInfo.getLongitude());
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


}
