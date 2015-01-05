/**
 * The Class DPdt.
 *
 * @author kangwt4
 * 상품
 */

package com.oklab.menutabs;

import java.util.ArrayList;

public class DPdt {
    
    public static final String PDT_DSP_TP_ICON = "0";
    public static final String PDT_DSP_TP_PIC = "1";
    public static final String PDT_DSP_TP_COLOR = "2";

    public static final String TIP_USE_TP_NON = "0";
    public static final String TIP_USE_TP_RT = "1";
    public static final String TIP_USE_TP_AMT = "2";
	
	public static final String USE_TP_YES = "Y";
	public static final String USE_TP_NO = "N";
	public static final String USE_TP_DEL = "D";
	
	public static final String PDT_TP_NORMAL = "0"; // 일반
	public static final String PDT_TP_SET = "1"; // 세트
	public static final String PDT_TP_COURSE = "2"; // 코스
	public static final String PDT_TP_OPEN_SET = "3"; // 오픈세트
	public static final String PDT_TP_OPEN_COURSE = "4"; // 오픈코스
	public static final String PDT_TP_OPEN_MENU = "5"; // 오픈메뉴
	public static final String PDT_TP_GROUP = "6"; // 그룹
	public static final String PDT_TP_MARKET_PRICE = "7"; // 시세
	public static final String PDT_TP_ELECTRONIC_SCALE = "8"; // 전자저울
	public static final String PDT_TP_OPTION = "9"; // 옵션
	
	public static final String ICON_TP_PICTURE = "0"; // 그림 아이콘
	public static final String ICON_TP_COLOR = "1"; // 색상 아이콘
	
    private long m_lPdtUid;
    private long m_lStoreUid;
    private String m_strPdtCd;
    private String m_strPdtBcd;
    private String m_strPdtNm;
    private String m_strPdtDspTp;
    private long m_lPdtIconUid;
    private long m_lPdtPicUid;
    private String m_strPdtRgb;
    private long m_lPrc;
    private boolean m_isVatYn;
    private String m_strTipUseTp;
    private float m_fTipRt;
    public long m_lTip;
    private boolean m_isKchnPrintUseYn;
    private long m_lKchnPrintUid;
    private long m_lBuyPrc;
    private long m_lBuySplyAmt;
    private long m_lBuyVat;
    private boolean m_isPntInsYn;
    private boolean m_isPntUseYn;
    private boolean m_isDcYn;
    private boolean m_isDlvYn;
    private boolean m_isPkgYn;
    private String m_strUseTp;
    private String m_strPdtTp;
    private long m_lRefStoreUid;
    private long m_lRefPdtUid;
    private String m_strRegDttm;
    private long m_lRegUsrUid;
    private String m_strEdtDttm;
    private long m_lEdtUsrUid;
    
    //code by sekim 2013-06-04 ERD UPDATE
    private String m_strPdtRcptNm; // VARCHAR2(150), /* 상품영수증명 */
    private String m_strPdtOdrNm; // VARCHAR2(150), /* 상품주문서명 */
    private long m_lPdtStdUid; // NUMBER, /* 상품규격 UID */
    private long m_lStdPdtUid; // NUMBER, /* 규격상품 UID */
    private long m_lCstPrc; // NUMBER, /* 소비자가 */
    private float m_fVatRt; // NUMBER(4,2), /* 부가세율 */
    private int m_lPrintOdr; //  NUMBER(4) DEFAULT 99, /* 인쇄순서 */
    private int m_nKchnCrtHH; // NUMBER(2), /* 주방제조시간 */
    private int m_nKchnCrtMM; // NUMBER(2), /* 주방제조분 */
    private long m_lOdrCorpUid; // NUMBER, /* 발주처UID */
    private long m_lCrtCorpUid; // NUMBER, /* 제조사UID */
    private String m_strOriFrom; // VARCHAR2(200), /* 원산지 */
    private String m_strCrtFrom; // VARCHAR2(200), /* 생산지 */
    private long m_lCal; // NUMBER(6) /* 칼로리 */
    
    private boolean m_isPosPdtTipUseYn;
    
    private String m_strRegUsrNm;
    private String m_strEdtUsrNm;
    private String m_strIconFilePath;
    private String m_strIconTp;
    private String m_strPicFilePath;
    
    private String m_strStdNm;
    private boolean m_isStdUseYn;
    private String m_strStdAbv;
    private String m_strStdCmt;
    
    // 해당 상품에 포함되어 있는 주방프린터 Uid
    private ArrayList<Long> m_arrPdtKchnPrintUid = new ArrayList<Long>();
    
    protected DPdt(
    		//long rowId
            long pdtUid
            , long storeUid
            , String pdtCd
            , String pdtBcd
            , String pdtNm
            , String pdtDspTp
            , long pdtIconUid
            , long pdtPicUid
            , String pdtRgb
            , long prc
            , boolean vatYn
            , String tipUseTp
            , float tipRt
            , long tip
            , boolean kchnPrintUseYn
            , long kchnPrintUid // No Used...
            , long buyPrc
            , long buySplyAmt
            , long buyVat
            , boolean pntInsYn
            , boolean pntUseYn
            , boolean dcYn
            , boolean dlvYn
            , boolean pkgYn
            , String useTp
            , String pdtTp
            , long refStoreUid
            , long refPdtUid
            , String regDttm
            , long regUsrUid
            , String edtDttm
            , long edtUsrUid
            
          //code by sekim 2013-06-04 ERD UPDATE
            , String strPdtRcptNm
            , String strPdtOdrNm
            , long lPdtStdUid
            , long lStdPdtUid
            , long lCstPrc
            , float fVatRt
            , int lPrintOdr
            , int nKchnCrtHH
            , int nKchnCrtMM
            , long lOdrCorpUid
            , long lCrtCorpUid
            , String strOriFrom
            , String strCrtFrom
            , long lCal
            // end code by sekim 2013-06-04 ERD UPDATE
            
            , boolean storePdtTipUseYn
            , String regUsrNm
            , String edtUsrNm
            , String iconFilePath
            , String iconTp
            , String picFilePath
            , String stdNm
            , boolean stdUseYn
            , String stdAbv
            , String stdCmt) {
//        super(pdtUid);

        m_lPdtUid = pdtUid;
        m_lStoreUid = storeUid;
        m_strPdtCd = pdtCd;
        m_strPdtBcd = pdtBcd;
        m_strPdtNm = pdtNm;
        m_strPdtDspTp = pdtDspTp;
        m_lPdtIconUid = pdtIconUid;
        m_lPdtPicUid = pdtPicUid;
        m_strPdtRgb = pdtRgb;
        m_lPrc = prc;
        m_isVatYn = vatYn;
        m_strTipUseTp = tipUseTp;
        m_fTipRt = tipRt;
        m_lTip = tip;
        m_isKchnPrintUseYn = kchnPrintUseYn;
        m_lKchnPrintUid = kchnPrintUid;
        m_lBuyPrc = buyPrc;
        m_lBuySplyAmt = buySplyAmt;
        m_lBuyVat = buyVat;
        m_isPntInsYn = pntInsYn;
        m_isPntUseYn = pntUseYn;
        m_isDcYn = dcYn;
        m_isDlvYn = dlvYn;
        m_isPkgYn = pkgYn;
        m_strUseTp = useTp;
        m_strPdtTp = pdtTp;
        m_lRefStoreUid = refStoreUid;
        m_lRefPdtUid = refPdtUid;
        m_strRegDttm = regDttm;
        m_lRegUsrUid = regUsrUid;
        m_strEdtDttm = edtDttm;
        m_lEdtUsrUid = edtUsrUid;
        
        m_strPdtRcptNm = strPdtRcptNm;
        m_strPdtOdrNm = strPdtOdrNm;
        m_lPdtStdUid = lPdtStdUid;
        m_lStdPdtUid = lStdPdtUid;
        m_lCstPrc = lCstPrc;
        m_fVatRt = fVatRt;
        m_lPrintOdr = lPrintOdr;
        m_nKchnCrtHH = nKchnCrtHH;
        m_nKchnCrtMM = nKchnCrtMM;
        m_lOdrCorpUid = lOdrCorpUid;
        m_lCrtCorpUid = lCrtCorpUid;
        m_strOriFrom = strOriFrom;
        m_strCrtFrom = strCrtFrom;
        m_lCal = lCal;
        
        m_isPosPdtTipUseYn = storePdtTipUseYn;
        m_strRegUsrNm = regUsrNm;
        m_strEdtUsrNm = edtUsrNm;
        m_strIconFilePath = iconFilePath;
        m_strPicFilePath = picFilePath;
        m_strIconTp = iconTp;
        
        m_strStdNm = stdNm;
        m_isStdUseYn = stdUseYn;
        m_strStdAbv = stdAbv;
        m_strStdCmt = stdCmt;
    }
    
    protected DPdt() {
//    	super(0);
    }
    
    /**
     * Empty 데이터를 만드는 함수 (UI 용)
     * @return
     */
    public static DPdt createEmptyPdt() {
    	return new DPdt();
    }
    
    public DPdt clone() {
    	DPdt pdtInstance = new DPdt(
    			this.getPdtUid()
    			, this.getStoreUid()
    			, this.getPdtCd()
    			, this.getPdtBcd()
    			, this.getPdtNm()
    			, this.getPdtDspTp()
    			, this.getPdtIconUid()
    			, this.getPdtPicUid()
    			, this.getPdtRgb()
    			, this.getPrc()
    			, this.isVatYn()
    			, this.getTipUseTp()
    			, this.getTipRt()
    			, this.getTip()
    			, this.isKchnPrintUseYn()
    			, this.getKchnPrintUid()
    			, this.getBuyPrc()
    			, this.getBuySplyAmt()
    			, this.getBuyVat()
    			, this.isPntInsYn()
    			, this.isPntUseYn()
    			, this.isDcYn()
    			, this.isDlvYn()
    			, this.isPkgYn()
    			, this.getUseTp()
    			, this.getPdtTp()
    			, this.getRefStoreUid()
    			, this.getRefPdtUid()
    			, this.getRegDttm()
    			, this.getRegUsrUid()
    			, this.getEdtDttm()
    			, this.getEdtUsrUid()
    			, this.getPdtRcptNm()
    			, this.getPdtOdrNm()
    			, this.getPdtStdUid()
    			, this.getStdPdtUid()
    			, this.getCstPrc()
    			, this.getVatRt()
    			, this.getPrintOdr()
    			, this.getKchnCrtHH()
    			, this.getKchnCrtMM()
    			, this.getOdrCorpUid()
    			, this.getCrtCorpUid()
    			, this.getOriFrom()
    			, this.getCrtFrom()
    			, this.getCal()
    			, this.getPosPdtTipUseYn()
    			, this.getRegUsrNm()
    			, this.getEdtUsrNm()
    			, this.getIconFilePath()
    			, this.getIconTp()
    			, this.getPicFilePath()
    			, this.getStdNm()
    			, this.isStdUseYn()
    			, this.getStdAbv()
    			, this.getStdCmt());
    	
    	for(Long pdtKchnPrintUid : m_arrPdtKchnPrintUid) {
    		pdtInstance.addPdtKchnPrintUid(pdtKchnPrintUid);
    	}
    	
    	return pdtInstance;
    }
    
    /**
     * 상품의 가격 반환.
     * @return
     */
    public long getPdtAmt() {
    	return m_lPrc;
    }
    
    /**
     * 상품의 봉사료 반환
     * @return
     */
    public long getTipAmt() {
    	return calcTipAmt(m_lPrc);
    }
    
    /**
     * 상품의 부가세 반환
     * @return
     */
    public long getVatAmt() {
    	return calcVatAmt(m_lPrc);
    }
    
    /**
     * 상품의 공급가 반환
     * @return
     */
    public long getSplyAmt() {
    	return calcSplyAmt(m_lPrc);
    }
    
    /**
     * 상품 봉사료 계산
     * @param pdtAmt
     * @return
     */
    public long calcTipAmt(long pdtAmt) {
    	if(m_isPosPdtTipUseYn) {
    		if(m_strTipUseTp != null && m_strTipUseTp.equals(TIP_USE_TP_AMT)) {
    			return m_lTip;
    		} else if(m_strTipUseTp != null && m_strTipUseTp.equals(TIP_USE_TP_RT)) {
//            	return DbUtils.calcTipAmt(pdtAmt, m_fTipRt / 100);
    			return 0;
    		}
    	}
//    	else {
//    		if(m_strStoreTipUseTp != null && m_strStoreTipUseTp.equals(DStore.TIP_USE_TP_RT)) {
//            	return DbUtils.calcTipAmt(pdtAmt, m_fTipRt / 100);
//    		}
//    	}
    	return 0;
    }
    
    /**
     * 상붐 부가세 계산
     * @param pdtAmt
     * @return
     */
    public long calcVatAmt(long pdtAmt) {
    	if(m_isVatYn) {
//        	return DbUtils.calcVatAmt(pdtAmt, calcTipAmt(pdtAmt), m_fVatRt / 100);
        	return 0;
    	}
    	return 0;
    }
    
    /**
     * 상품 공급가 계산
     * @param pdtAmt
     * @return
     */
    public long calcSplyAmt(long pdtAmt) {
    	return pdtAmt - calcTipAmt(pdtAmt) - calcVatAmt(pdtAmt);
    }

    ////////////////////////////////////
    /**
     * 상품 UID 반환
     * @return
     */
    public long getPdtUid() {
        return m_lPdtUid;
    }

    /**
     * 매장 UID 반환
     * @return
     */
    public long getStoreUid() {
        return m_lStoreUid;
    }

    /**
     * 상품 코드 값 반환
     * @return
     */
    public String getPdtCd() {
        return m_strPdtCd;
    }

    /**
     * 상품 바코드 반환
     * @return
     */
    public String getPdtBcd() {
        return m_strPdtBcd;
    }

    /**
     * 상품 이름 반환
     * @return
     */
    public String getPdtNm() {
        return m_strPdtNm;
    }
    
    /**
     * 상품 표시 타입 반환
     * @return
     */
    public String getPdtDspTp() {
    	return m_strPdtDspTp;
    }

    /**
     * 상품 아이콘 UID 반환
     * @return
     */
    public long getPdtIconUid() {
        return m_lPdtIconUid;
    }
    
    /**
     * 상품 이미지 UID 반환
     * @return
     */
    public long getPdtPicUid() {
    	return m_lPdtPicUid;
    }
    
    /**
     * 상품 색상 반환
     * @return RGB
     */
    public String getPdtRgb() {
    	return m_strPdtRgb;
    }

    /**
     * 상품 금액 반환
     * @return
     */
    public long getPrc() {
        return m_lPrc;
    }

    /**
     * 부가세 사용 여부 반환
     * @return
     */
    public boolean isVatYn() {
        return m_isVatYn;
    }
    
    /**
     * 봉사룔 사용 타입 반환
     * @return (0: 미사용, 1:요율사용, 2: 금액사용)
     */
    public String getTipUseTp() {
    	return m_strTipUseTp;
    }
    
    /**
     * 봉사료 율 반환
     * @return
     */
    public float getTipRt() {
    	return m_fTipRt;
    }

    /**
     * 봉사료 반환
     * @return
     */
    public long getTip() {
        return m_lTip;
    }

    /**
     * 주방프린트 사용 여부 반환
     * @return
     */
    public boolean isKchnPrintUseYn() {
        return m_isKchnPrintUseYn;
    }

    /**
     * 주방 프린트 UID 반환
     * @return
     */
    public long getKchnPrintUid() {
        return m_lKchnPrintUid;
    }

    /**
     * 상품 매입가 반환
     * @return
     */
    public long getBuyPrc() {
        return m_lBuyPrc;
    }

    /**
     * 상품 매입 공급가 반환
     * @return
     */
    public long getBuySplyAmt() {
        return m_lBuySplyAmt;
    }

    /**
     * 상품 매입 부가세 반환
     * @return
     */
    public long getBuyVat() {
        return m_lBuyVat;
    }

    /**
     * 상품 판매시 포인트 적립 여부 반환
     * @return
     */
    public boolean isPntInsYn() {
        return m_isPntInsYn;
    }

    /**
     * 상품 판매시 포인트로 결제 가능 여부
     * @return
     */
    public boolean isPntUseYn() {
        return m_isPntUseYn;
    }

    /**
     * 할인 여부 반환
     * @return
     */
    public boolean isDcYn() {
        return m_isDcYn;
    }

    /**
     * 배달 가능 여부 반환
     * @return
     */
    public boolean isDlvYn() {
        return m_isDlvYn;
    }

    /**
     * 포장 여부 반환
     * @return
     */
    public boolean isPkgYn() {
        return m_isPkgYn;
    }

    /**
     * 사용 타입 반환
     * @return 상품사용타입( Y:사용, N:미사용, D:삭제됨)
     */
    public String getUseTp() {
        return m_strUseTp;
    }

    /**
     * 상품 구분 여부 반환
     * @return 상품구분(0:일반, 1:세트, 2:코스, 3:오픈세트, 4:오픈코스, 5:오픈메뉴, 6:그룹, 7:시세, 8:전자저울, 9:옵션)
     */
    public String getPdtTp() {
		return m_strPdtTp;
	}

    /**
     * 해당 상품의 원본상품정보를 소유한 매장의 UID
     * @return
     */
	public long getRefStoreUid() {
		return m_lRefStoreUid;
	}

	/**
	 * 해당 상품의 원본상품 UID (프랜차이즈의 마스터 상품UID)
	 * @return
	 */
	public long getRefPdtUid() {
		return m_lRefPdtUid;
	}

	/**
	 * 상품 등록일 반환
	 * @return
	 */
    public String getRegDttm() {
        return m_strRegDttm;
    }

    /**
     * 상품 등록 사용자 UID 반환
     * @return
     */
	public long getRegUsrUid() {
        return m_lRegUsrUid;
    }

	/**
	 * 상품 수정 일시 반환
	 * @return
	 */
    public String getEdtDttm() {
        return m_strEdtDttm;
    }

    /**
     * 상품 수정 사용자 UID 반환
     * @return
     */
    public long getEdtUsrUid() {
        return m_lEdtUsrUid;
    }
    
    /**
     * 상품영수증 출력 시 별도 명칭 반환
     * @return
     */
    //code by sekim 2013-06-04 ERD UPDATE
    public String getPdtRcptNm() {
    	return m_strPdtRcptNm;
    }
    /**
     * 상품주문서 인쇄 시 별도명칭 반환
     * @return
     */
    public String getPdtOdrNm() {
    	return m_strPdtOdrNm;
    }
    /**
     * 상품 규격 UID 반환
     * @return
     */
    public long getPdtStdUid() {
    	return m_lPdtStdUid;
    }
    /**
     * 규격상품의 경우 대표 그룹상품의 UID 반환
     * @return
     */
    public long getStdPdtUid() {
    	return m_lStdPdtUid;
    }
    
    /**
     * 소비자가 바환
     * @return
     */
    public long getCstPrc() {
    	return m_lCstPrc;
    }
    
    /**
     * 부가세 율 반환
     * @return
     */
    public float getVatRt() {
    	return m_fVatRt;
    }
    
    /**
     * 인쇄순서 반환
     * @return
     */
    public int getPrintOdr() {
    	return m_lPrintOdr;
    }
    
    /**
     * 주방제조(조리)시간 반환
     * @return hh
     */
    public int getKchnCrtHH() {
    	return m_nKchnCrtHH;
    }
    
    /**
     * 주방제조(조리)분 반환
     * @return mm
     */
    public int getKchnCrtMM() {
    	return m_nKchnCrtMM;
    }
    
    /**
     * 발주처UID 반환
     * @return
     */
    public long getOdrCorpUid() {
    	return m_lOdrCorpUid;
    }
    
    /**
     * 제조사UID 반환
     * @return 
     */
    public long getCrtCorpUid() {
    	return m_lCrtCorpUid;
    }
    
    /**
     * 원산지 반환
     * @return
     */
    public String getOriFrom() {
    	return m_strOriFrom;
    }
    
    /**
     * 생산지 반환
     */
    public String getCrtFrom() {
    	return m_strCrtFrom;
    }
    /**
     * 칼로리 반환
     * @return
     */
    public long getCal() {
    	return m_lCal;
    }
    
    
    ////////////////////////////////////
    /**
     * 매장 상픔 봉사료 사용 여부 반환
     * @return
     */
    public boolean getPosPdtTipUseYn() {
    	return m_isPosPdtTipUseYn;
    }

    ////////////////////////////////////
    /**
     * 상품 등록 사용자 이름 반환
     * @return
     */
    public String getRegUsrNm() {
    	return m_strRegUsrNm;
    }
    
    /**
     * 상품 정보 편집 사용자 이름 반환
     * @return
     */
    public String getEdtUsrNm() {
    	return m_strEdtUsrNm;
    }
    
    /**
     * 상품 아이콘 파일 path 반환
     * @return
     */
    public String getIconFilePath() {
    	return m_strIconFilePath;
    }
    
    /**
     * 상품 아이콘 타입 반환
     * @return
     */
    public String getIconTp() {
    	return m_strIconTp;
    }
    
    /**
     * 상품 이미지 파일 path 반환
     * @return
     */
    public String getPicFilePath() {
    	return m_strPicFilePath;
    }
    
    /**
     * 상품 규격 이름 반환
     * @return
     */
    public String getStdNm() {
    	return m_strStdNm;
    }
    
    /**
     * 상품 규격 사용 여부 반환
     * @return
     */
    public boolean isStdUseYn() {
    	return m_isStdUseYn;
    }
    
    /**
     * 상품규격약지 반환
     * @return
     */
    public String getStdAbv() {
    	return m_strStdAbv;
    }
    
    /**
     * 상품 규격 설명 반환
     * @return
     */
    public String getStdCmt() {
    	return m_strStdCmt;
    }
    
    /**
     * 상품 주방프린터 UID 반환
     * @return
     */
    public ArrayList<Long> getPdtKchnPrintUid() {
    	return m_arrPdtKchnPrintUid;
    }
    
    /**
     * 상품 주방프린터 UID 추가
     * @param lUid
     */
    public void addPdtKchnPrintUid(long lUid) {
    	m_arrPdtKchnPrintUid.add(lUid);
    }
}
