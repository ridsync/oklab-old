package com.oklab.myshoptest;

import android.database.Cursor;
import android.net.Uri;

public class InDCshHis extends DCshHis {
    
    public static final String TABLE_NAME = "csh_his";
    public static final String _POS_UID = "pos_uid";
    public static final String _STORE_OPEN_YMD = "store_open_ymd";
    public static final String _STORE_UID = "store_uid";
    public static final String CNM_POS_UID = _POS_UID; // NUMBER NOT NULL, /* POS UID */
    public static final String CNM_CSH_UID = "csh_uid"; // NUMBER NOT NULL, /* 현금 UID */
    public static final String CNM_STORE_OPEN_YMD = _STORE_OPEN_YMD; // VARCHAR2(8) NOT NULL, /* 개점일 */
    public static final String CNM_STLMT_UID = "stlmt_uid"; // NUMBER NOT NULL, /* 결제 UID */
    public static final String CNM_STLMT_MTD_CD = "stlmt_mtd_cd"; // VARCHAR2(16) NOT NULL, /* 결제수단코드 */
    public static final String CNM_AMT = "amt"; // NUMBER DEFAULT 0, /* 금액 */
    public static final String CNM_BLC = "blc"; // NUMBER DEFAULT 0, /* 잔액 */
    public static final String CNM_USR_UID = "usr_uid"; // NUMBER NOT NULL, /* 사용자 UID */
    public static final String CNM_MEMO_UID = "memo_uid"; // NUMBER NOT NULL, /* 메모 UID */
    public static final String CNM_REG_DTTM = "reg_dttm"; // DATE DEFAULT systimestamp /* 입력일시 */

    public static final String JNM_USR_NM = "usr_nm";
    public static final String JNM_MEMO_TXT = "memo_txt";
    public static final String JNM_STORE_OPEN_HMS = "store_open_hms";
    
    InDCshHis(
    			//long rowId
				long posUid
	            , long cshUid
	            , String storeOpenYmd
	            , long stlmtUid
	            , String stlmtMtdCd
	            , long amt
	            , long blc
	            , long usrUid
	            , long memoUid
	            , String regDttm
	            , String usrName
	            , String memoTxt
	            , String storeOpenHms) {
		super(
				//rowId
				posUid
				, cshUid
				, storeOpenYmd
				, stlmtUid
				, stlmtMtdCd
				, amt
				, blc
				, usrUid
				, memoUid
				, regDttm
				, usrName
				, memoTxt
				, storeOpenHms);
	}
    
    static InDCshHis createInstance(Cursor cursor) {
        return new InDCshHis(
//        		cursor.getLong(cursor.getColumnIndex(TCshHis._ID))
                cursor.getLong(cursor.getColumnIndex(CNM_POS_UID))
                , cursor.getLong(cursor.getColumnIndex(CNM_CSH_UID))
                , cursor.getString(cursor.getColumnIndex(CNM_STORE_OPEN_YMD))
                , cursor.getLong(cursor.getColumnIndex(CNM_STLMT_UID))
                , cursor.getString(cursor.getColumnIndex(CNM_STLMT_MTD_CD))
                , cursor.getLong(cursor.getColumnIndex(CNM_AMT))
                , cursor.getLong(cursor.getColumnIndex(CNM_BLC))
                , cursor.getLong(cursor.getColumnIndex(CNM_USR_UID))
                , cursor.getLong(cursor.getColumnIndex(CNM_MEMO_UID))
                , cursor.getString(cursor.getColumnIndex(CNM_REG_DTTM))
                , cursor.getString(cursor.getColumnIndex(JNM_USR_NM))
                , cursor.getString(cursor.getColumnIndex(JNM_MEMO_TXT))
                , cursor.getString(cursor.getColumnIndex(JNM_STORE_OPEN_HMS)));
    }
}
