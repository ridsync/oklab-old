package com.oklab.myshoptest;

public class DCshHis  {

    private long m_lPosUid;
    private long m_lCshUid;
    private String m_strStoreOpenYmd;
    private long m_lStlmtUid;
    private String m_strStlmtMtdCd;
    private long m_lAmt;
    private long m_lBlc;
    private long m_lUsrUid;
    private long m_lMemoUid;
    private String m_strRegDttm;
    
    private String m_strUsrName;
    private String m_strMemoTxt;
    private String m_strStoreOpenHms;
    
    
    public DCshHis(long posUid
                            , long cshUid
                            , String storeOpenYmd
                            , long stlmtUid
                            , String stlmtMtdCd
                            , long amt
                            , long blc
                            , long usrUid
                            , long memoUid
                            , String regDttm) {
        this(
        		//-1
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
                , ""
                , ""
                , "");
    }

    protected DCshHis(
    		//long rowId
            long posUid
            , long cshUid
            , String storeOpenYmd
            , long stlmtUid
            , String stlmtMtdCd
            , long amt
            , long blc
            , long usrUid
            , long momoUid
            , String regDttm
            , String userName
            , String memoTxt
            , String storeOpenHms) {
        m_lPosUid = posUid;
        m_lCshUid = cshUid;
        m_strStoreOpenYmd = storeOpenYmd;
        m_lStlmtUid = stlmtUid;
        m_strStlmtMtdCd = stlmtMtdCd;
        m_lAmt = amt;
        m_lBlc = blc;
        m_lUsrUid = usrUid;
        m_lMemoUid = momoUid;
        m_strRegDttm = regDttm;
        
        m_strUsrName = userName;
        m_strMemoTxt = memoTxt;
        m_strStoreOpenHms = storeOpenHms;
    }

    /**
     * POS Uid 반환
     * @return
     */
    public long getPosUid() {
        return m_lPosUid;
    }

    /**
     * 현금내역 UID 반환
     * @return
     */
    public long getCshUid() {
        return m_lCshUid;
    }

    /**
     * 개점 일자 반환
     * @return YYYYMMDD
     */
    public String getStoreOpenYmd() {
        return m_strStoreOpenYmd;
    }

    /**
     * 결제 UID 반환
     * @return
     */
    public long getStlmtUid() {
        return m_lStlmtUid;
    }

    /**
     * 결제 코드값 반환 DbCode.java Code 상수 확인
     * @return
     */
    public String getStlmtMtdCd() {
        return m_strStlmtMtdCd;
    }

    /**
     * 현금 내역의 입출금 금액 반환
     * @return
     */
    public long getAmt() {
        return m_lAmt;
    }

    /**
     * 현금 내역의 잔액 반환
     * @return
     */
    public long getBlc() {
        return m_lBlc;
    }
    
    /**
     * 현금 내역 입출금 담당자 UID 반환
     * @return
     */
    public long getUsrUid() {
    	return m_lUsrUid;
    }

    /**
     * 현금내역 입출금 시 메모 UID 반환
     * @return
     */
	public long getMemoUid() {
		return m_lMemoUid;
	}
    
	/**
	 * 현금내역 입출금 입력 일시 반환
	 * @return YYYYMMDDhhmmss
	 */
    public String getRegDttm() {
    	return m_strRegDttm;
    }

    /**
     * 입출금 시 담당자 이름 반환
     * @return
     */
	public String getUserName() {
		return m_strUsrName;
	}

	/**
	 * 입출금시 메모 내용
	 * @return
	 */
	public String getMemoTxt() {
		return m_strMemoTxt;
	}
	
	/**
	 * 개점 시간 반환
	 * @return hhmmss
	 */
	public String getStoreOpenHms() {
		return m_strStoreOpenHms;
	}
	
	/**
     * 입출금 시 현재 내역과 동일한 내역 의 결제 수단 코드인 지 반환
     * @param DCshHis
     * @return 동일한 결제 수단이면 true 아니면 false
     */
    public boolean isSameSettlementMtdCd(DCshHis another) {
    	return m_strStlmtMtdCd.equals(another.getStlmtMtdCd());
    }

	int m_nCount = 1;
	/**
	 * 현금 입출금 내역을 add 한다.
	 * @param another
	 */
	public void add(DCshHis another) {
        m_lAmt += another.getAmt();
        m_lBlc += another.getBlc();
        
       	m_nCount += another.getCount();
 	}
	
	/**
	 * 입출금 내역 갯수를 반환 한다.
	 * @return
	 */
	public int getCount() {
		return m_nCount;
	}

}
