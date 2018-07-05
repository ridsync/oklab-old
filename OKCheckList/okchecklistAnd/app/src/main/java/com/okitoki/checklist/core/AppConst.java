package com.okitoki.checklist.core;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-01.
 */
public class AppConst {

    public static final String PACKAGE_NAME = "com.okitoki.okcart";

    public static final int TOTAL_COUNT_EACH_ITEM = 30;

    public static final int REQUEST_CODE_ACT_ADD_CART = 10;
    public static final int REQUEST_CODE_ACT_FAV_ADD_CART = 11;
    public static final int REQUEST_CODE_ACT_CARTD_DETAIL = 12;
    public static final int REQUEST_CODE_ACT_MODIFY_CART = 13;
    public static final String BUNDLE_CAMERA_TYPE = "BUNDLE_CAMERA_TYPE";
    public static final String BUNDLE_PHOTOPATH = "BUNDLE_PHOTOPATH";
    public static final int IMAGE_MAX_SIZE = 1920;

    public static final String PREFERENCE_MAIN_LIST_TYPE = "PREFERENCE_MAIN_LIST_TYPE";
    public static final String INTENT_EXTRA_SELECT_MART = "INTENT_EXTRA_SELECT_MART";
    public static final String INTENT_EXTRA_PRODUCT_TITLE = "INTENT_EXTRA_PRODUCT_TITLE";
    public static final String INTENT_EXTRA_URL = "INTENT_EXTRA_URL";
    public static final String INTENT_EXTRA_IS_GPS_VIEW = "INTENT_EXTRA_IS_GPS_VIEW";
    public static final String INTENT_EXTRA_IS_FAV_VIEW = "INTENT_EXTRA_IS_FAV_VIEW";
    public static final String INTENT_EXTRA_IS_PUSH = "INTENT_EXTRA_IS_PUSH";

    public static final String SYMBOL_ICON_ID_EMART = "E";
    public static final String SYMBOL_ICON_ID_HOMEPLUS = "H";
    public static final String SYMBOL_ICON_ID_LOTTEMART = "L";
    public static final String SYMBOL_ICON_ID_GSHOME = "GS";
    public static final String SYMBOL_ICON_ID_COSTCO= "C";
    public static final String SYMBOL_ICON_ID_GENERAL_MART= "M";

    public static final String PREF_FIREBASE_UID = "PREF_FIREBASE_UID";
    public static final String PREF_DEFAULT_MAINSCREEN_SETTING = "PREF_DEFAULT_MAINSCREEN_SETTING";
    public static final String PREF_TOUR_MAIN_COMPLETED= "PREF_TOUR_MAIN_COMPLETED";
    public static final String PREF_TOUR1_COMPLETED= "PREF_TOUR1_COMPLETED";
    public static final String PREF_TOUR2_COMPLETED= "PREF_TOUR2_COMPLETED";
    public static final String PREF_RATINGAPP_COMPLETED= "PREF_RATINGAPP_COMPLETED";
    public static final String PREF_USE_TTS = "PREF_USE_TTS";
    public static final String PREF_HOLLIDAY_ALARM_IS_SET_DEFAULT = "PREF_HOLLIDAY_ALARM_IS_SET_DEFAULT";
    public static final String PREF_HOLLIDAY_ALARM_NOTIFY = "PREF_HOLLIDAY_ALARM_NOTIFY";
    public static final String PREF_HOLLIDAY_ALARM_DAY_AGO = "PREF_HOLLIDAY_ALARM_DAY_AGO";
    public static final String PREF_HOLLIDAY_ALARM_SET_HOUR = "PREF_HOLLIDAY_ALARM_SET_HOUR";
    public static final String PREF_HOLLIDAY_ALARM_SET_MINUTE = "PREF_HOLLIDAY_ALARM_SET_MINUTE";

    public static final String API_KEY = "336b5a8c40fd7c2751bddfe133e51052";

    public static final String FA_EVENT_CART_ADD = "카트추가_메인화면";
    public static final String FA_EVENT_FAV_CART_ADD = "빠른추가_메인화면";
    public static final String FA_EVENT_FAV_CART_ADD_MODIFYING = "빠른추가_수정화면";
    public static final String FA_EVENT_CART_ADD_ICON_CHOICE = "마트아이콘선택_수정화면";
    public static final String FA_EVENT_CART_ADD_TOGGLE_STT = "음성인식토글_수정화면";
    public static final String FA_EVENT_CART_ADD_STT_RESULT = "음성인식결과_수정화면";
    public static final String FA_EVENT_CART_DETAIL_SEARCH = "물품검색버튼_체크리스트";
    public static final String FA_EVENT_CART_PRODUCT_SEARCH = "물품검색버튼_검색창";
    public static final String FA_EVENT_CART_PRODUCT_SEARCH_FRAG = "물품검색버튼_메인검색";
    public static final String FA_EVENT_REST_GPS_SEARCH = "마휴_GPS_클릭";
    public static final String FA_EVENT_REST_MART_SELECT = "마휴_마트아이콘_클릭";
    public static final String FA_EVENT_REST_MART_FAV_CLICK = "마휴_등록마트_클릭";
    public static final String FA_EVENT_REST_MART_REGION_CLICK = "마휴_마트지역_클릭";
    public static final String FA_EVENT_REST_MART_ARRIVE = "푸시알림_마트휴무일_도착";
    public static final String FA_EVENT_ALARM_RECEIVE = "푸시알림_알람리시버_Handle";
    public static final String FA_EVENT_ALARM_DB_SELECT = "푸시알림_마트휴무일_RDB조회성공";
    public static final String FA_EVENT_REST_MART_CLICK = "푸시알림_마트휴무일_클릭";
    public static final String FA_EVENT_ALARM_UID_NULL = "푸시알림_UID_NULL_발생";
    public static final String FA_EVENT_ALARM_USER_NULL = "푸시알림_USER_NULL_발생";
    public static final String FA_EVENT_FIREBASE_AUTH_ERROR = "파이어베이스_인증에러";

    public static final String FA_PARAM_MART_NAME = "martname";
    public static final String FA_PARAM_NORMAL = "normal";
    public static final String FA_PARAM_BOOEAN = "isTrue";
    public static final String FA_PARAM_MART_REGION = "martRegion";

    public static final String HOLIDAY_MART_ALARM_MESSAGE = "holiday_alert_message";
    public static final String HOLIDAY_NOTICE_ALL_SHOW = "holiday_notice_all_show";
    public static final String HOLIDAY_NOTICE_EMART_SHOW = "holiday_notice_emart_show";
    public static final String HOLIDAY_NOTICE_HOMEPLUS_SHOW = "holiday_notice_homeplus_show";
    public static final String HOLIDAY_NOTICE_LOTTEMART_SHOW = "holiday_notice_lottemart_show";
    public static final String HOLIDAY_NOTICE_COSTCO_SHOW = "holiday_notice_costco_show";
    public static final String HOLIDAY_NOTICE_DESC = "holiday_notice_desc";
    public static final String HOLIDAY_NOTICE_DESC_2 = "holiday_notice_desc_2";
    public static final String HOLIDAY_NOTICE_EMART_LINK = "holiday_notice_emart_link";
    public static final String HOLIDAY_NOTICE_LOTTEMART_LINK = "holiday_notice_lottemart_link";
    public static final String HOLIDAY_NOTICE_HOMEPLUS_LINK = "holiday_notice_homeplus_link";
    public static final String HOLIDAY_NOTICE_COSTCO_LINK = "holiday_notice_costco_link";
}
