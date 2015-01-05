package com.oklab.menutabs;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class DOdr {

	public static DOdr createMainOdr(DPdt pdt
									, long rsvUid
									, long tblUid
									, int pdtCnt
									, long usrUid){
		DOdr odr =  new DOdr(
				//-1
                -1
                , ""
                , -1
                , -1
                , rsvUid
				, tblUid
				, pdt.getPdtUid()
				, -1
				, pdtCnt
				, pdt.getPdtAmt()
                , 0
				, 0 // TODO DC
                , 0
                , pdt.getSplyAmt()
                , pdt.getTipAmt()
                , pdt.getVatAmt()
				, usrUid
				, false
				, null
                , -1
				, ""
				, ""
				, -1
				, -1
				, 0
				, 0
				, 0
                , ""
                , ""
                , ""
                , ""
                , ""
                , pdt.getPdtTp());
		odr.setPdt_(pdt);
		return odr;
	}
	
	
    private final long m_lPosUid;
    private final String m_strStoreOpenYmd;
    private final long m_lOdrUid;
    private final long m_lSaleUid;
    private final long m_lRsvUid;
    private long m_lTblUid;
    private long m_lPdtUid;
    private final long m_lPdtSetUid;
    private int m_nPdtCnt;
    private long m_lPdtAmt;
    private long m_lAddtnAmt;
    private long m_lDcAmt;
    private long m_lDcUid;
    private long m_lSplyAmt;
    private long m_lTipAmt;
    private long m_lVatAmt;
    private long m_lUsrUid;
    private boolean m_isSvcYn;
    private String m_strOdrDttm;
    private long m_lLnkOdrUid;
    private String m_strMemoCd;
    private String m_strCmt;
    private long m_lOptnOdrUid;
    private long m_lSetOdrUid;
    
    private String m_strUsrNm;
    private String m_strMemoTxt;
    private String m_strPdtNm;
    private String m_strPdtSetNm;
    private String m_strTblNm;
    private String m_strPdtTp;
    
    private DPdt m_objPdt;
    private DPdt m_objPdtOpt = null;
    
    private String lnk_svc_rsv;
    private int m_game_num;
    private int m_game_type;
    private int m_game_grp;
    private String m_machine_code;
    
    private ArrayList<DOdr> m_arrOdrHisList;
    private ArrayList<DOdr> m_arrOdrSetList;
    private ArrayList<DOdr> m_arrOdrOptList;

    private DOdr m_setPrtOdr = null;
    
    //부모의 pdtcont 가 0이 될 경우 하위 pdt의 단위 count 를 cash 하도록 함!
    private int m_nCshChildPerCnt = 0;


    protected DOdr(
    		//long rowId
            long posUid
            , String storeOpenYmd
            , long odrUid
            , long saleUid
            , long rsvUid
            , long tblUid
            , long pdtUid
            , long pdtSetUid
            , int pdtCnt
            , long pdtAmt
            , long addtnAmt
            , long dcAmt
            , long dcUid
            , long splyAmt
            , long tipAmt
            , long vatAmt
            , long usrUid
            , boolean svcYn
            , String odrDttm
            , long lnkOdrUid
            , String memoCd
            , String cmt
            , long optnOdrUid
            , long setOdrUid
            , int mGameGrp
            , int mGameNum
            , int mGameTp
            , String usrNm
            , String memoTxt
            , String pdtNm
            , String pdtSetNm
            , String tblNm
            , String pdtTp) {
        m_lPosUid = posUid;
        m_strStoreOpenYmd = storeOpenYmd;
        m_lOdrUid = odrUid;
        m_lSaleUid = saleUid;
        m_lRsvUid = rsvUid;
        m_lTblUid = tblUid;
        m_lPdtUid = pdtUid;
        m_lPdtSetUid = pdtSetUid;
        m_nPdtCnt = pdtCnt;
        m_lPdtAmt = pdtAmt;
        m_lAddtnAmt = addtnAmt;
        m_lDcAmt = dcAmt;
        m_lDcUid = dcUid;
        m_lSplyAmt = splyAmt;
        m_lTipAmt = tipAmt;
        m_lVatAmt = vatAmt;
        m_lUsrUid = usrUid;
        m_isSvcYn = svcYn;
        m_strOdrDttm = odrDttm;
        m_lLnkOdrUid = lnkOdrUid;
        m_strMemoCd = memoCd;
        m_strCmt = cmt;
        m_lOptnOdrUid = optnOdrUid;
        m_lSetOdrUid = setOdrUid;
        
        m_strUsrNm = usrNm;
        m_strMemoTxt = memoTxt;
        m_strPdtNm = pdtNm;
        m_strPdtSetNm = pdtSetNm;
        m_strTblNm = tblNm;
        m_strPdtTp = pdtTp;
        
        m_game_grp= mGameGrp;
        m_game_num = mGameNum;
        m_game_type = mGameTp;
        
        m_arrOdrHisList = new ArrayList<DOdr>();
        m_arrOdrSetList = new ArrayList<DOdr>();
        m_arrOdrOptList = new ArrayList<DOdr>();
    }
    
    public DOdr clone() {
    	DOdr odrInstance =  new DOdr(
    			//this.getRowId()
                this.getPosUid()
                , this.getStoreOpenYmd()
                , this.getOdrUid()
                , this.getSaleUid()
                , this.getRsvUid()
                , this.getTblUid()
                , this.getPdtUid()
                , this.getPdtSetUid()
                , this.getOriginPdtCnt()
                , this.getPdtAmt()
                , this.getAddtnAmt()
                , this.getDcAmt()
                , this.getDcUid()
                , this.getSplyAmt()
                , this.getTipAmt()
                , this.getVatAmt()
                , this.getUsrUid()
                , this.isSvcYn()
                , this.getOdrDttm()
                , this.getLnkOdrUid()
                , this.getMemoCd()
                , this.getCmt()
                , this.getOptnOdrUid()
                , this.getSetOdrUid()
                , this.get_game_group()
                , this.get_game_num()
                , this.get_game_type()
                , this.getUsrNm()
                , this.getMemoTxt()
                , this.getPdtNm()
                , this.getPdtSetNm()
                , this.getTblNm()
                , this.getPdtTp());
    	
    	for(DOdr odrItem : m_arrOdrHisList) {
    		odrInstance.addOdrHis(odrItem.clone());
    	}
       	for(DOdr odrItem : m_arrOdrSetList) {
    		odrInstance.addOdrSet(odrItem.clone());
    	}
       	for(DOdr odrItem : m_arrOdrOptList) {
       		odrInstance.addOdrOpt(odrItem.clone());
       	}
       	
       	return odrInstance;
    }
    
    public boolean isAddable(DOdr odr, boolean ignoreTbl) {
    	if(ignoreTbl) {
        	return (m_lPdtSetUid == -1
        			&& m_lPdtUid == odr.getPdtUid()
        			&& m_lPdtAmt == odr.getPdtAmt()
        			&& m_lAddtnAmt == odr.getAddtnAmt()
        			&& getOptnPdtCnt() == odr.getOptnPdtCnt()
        			&& m_lDcAmt == odr.getDcAmt()
        			&& m_isSvcYn == odr.isSvcYn()
        			&& !m_strPdtTp.equals(DPdt.PDT_TP_OPEN_COURSE)
    				&& !m_strPdtTp.equals(DPdt.PDT_TP_OPEN_SET)
//        			&& (m_arrOdrSetList == null || m_arrOdrSetList.isEmpty())
        			&& ((m_strMemoCd != null && m_strMemoCd.equals(odr.getMemoCd()))
        				|| (TextUtils.isEmpty(m_strMemoCd) == TextUtils.isEmpty(odr.getMemoCd())))
        			&& ((m_strCmt != null && m_strCmt.equals(odr.getCmt()))
            				|| (TextUtils.isEmpty(m_strCmt) == TextUtils.isEmpty(odr.getCmt())))
            		&& (m_arrOdrOptList.size() == odr.getOdrOptList().size()));
    	} else {
        	return (m_lPdtSetUid == -1
        			&& m_lTblUid == odr.getTblUid()
        			&& m_lPdtUid == odr.getPdtUid()
        			&& m_lPdtAmt == odr.getPdtAmt()
        			&& m_lAddtnAmt == odr.getAddtnAmt()
        			&& getOptnPdtCnt() == odr.getOptnPdtCnt()
        			&& m_lDcAmt == odr.getDcAmt()
        			&& m_isSvcYn == odr.isSvcYn()
        			&& !m_strPdtTp.equals(DPdt.PDT_TP_OPEN_COURSE)
    				&& !m_strPdtTp.equals(DPdt.PDT_TP_OPEN_SET)
//        			&& (m_arrOdrSetList == null || m_arrOdrSetList.isEmpty())
        			&& ((m_strMemoCd != null && m_strMemoCd.equals(odr.getMemoCd()))
        				|| (TextUtils.isEmpty(m_strMemoCd) == TextUtils.isEmpty(odr.getMemoCd())))
        			&& ((m_strCmt != null && m_strCmt.equals(odr.getCmt()))
            				|| (TextUtils.isEmpty(m_strCmt) == TextUtils.isEmpty(odr.getCmt())))
            		&& (m_arrOdrOptList.size() == odr.getOdrOptList().size()));
    	}
    }

    public boolean isAddable(DOdr odr) {
    	return isAddable(odr, false);
    }
    
    public boolean isEqual(DOdr odr) {
    	if (m_lOdrUid == odr.getOdrUid()
    			&& m_lTblUid == odr.getTblUid()
    			&& m_lPdtUid == odr.getPdtUid()
				&& getPdtCnt() == odr.getPdtCnt()
    			&& m_lPdtAmt == odr.getPdtAmt()
    			&& m_lAddtnAmt == odr.getAddtnAmt()
    			&& m_lDcAmt == odr.getDcAmt()
    			&& m_isSvcYn == odr.isSvcYn()
    			&& ((m_strMemoCd != null && m_strMemoCd.equals(odr.getMemoCd()))
    				|| (TextUtils.isEmpty(m_strMemoCd) == TextUtils.isEmpty(odr.getMemoCd())))
    			&& ((m_strCmt != null && m_strCmt.equals(odr.getCmt()))
        			|| (TextUtils.isEmpty(m_strCmt) == TextUtils.isEmpty(odr.getCmt())))) {
    		if(m_arrOdrSetList.size() != odr.getOdrSetList().size() ||
    				m_arrOdrOptList.size() != odr.getOdrOptList().size()) {
    			return false;
    		}
    		boolean isFound = true;
    		if(m_arrOdrSetList.size() == odr.getOdrSetList().size()) {
	    		for(DOdr setOdr1 : m_arrOdrSetList) {
	    			for(DOdr setOdr2 : odr.getOdrSetList()) {
	    				if(setOdr1.getOdrUid() == setOdr2.getOdrUid()){
	    					isFound = true;
	    					break;
	    				}
	    			}
	    			if(!isFound) {
	    				return false;
	    			}
	    		}
    		}
    		if(m_arrOdrOptList.size() == odr.getOdrOptList().size() 
    				&& !m_arrOdrOptList.isEmpty() && !odr.getOdrOptList().isEmpty()) {
    			for(DOdr optOdr1 : m_arrOdrOptList) {
    				isFound = false;
    				for(DOdr optOdr2 : odr.getOdrOptList()) {
    					// 옵션 상품인 경우 상품 갯수까지도 동일한지 비교하도록 한다.
    					if(optOdr1.getOdrUid() == optOdr2.getOdrUid() 
    							&& optOdr1.getPdtCnt() == optOdr2.getPdtCnt()) {
    						isFound = true;
    						break;
    					}
    				}
    				if(!isFound) {
    					return false;
    				}
    			}
    		}
    		return isFound; // 항상 true 를 리턴한다.
    	}
    	return false;
    }

    /**
     * POS UID
     * @return
     */
	public long getPosUid() {
		return m_lPosUid;
	}

	/**
	 * 개점일
	 * @return
	 */
	public String getStoreOpenYmd() {
		return m_strStoreOpenYmd;
	}

	/**
	 * 주문 UID
	 * @return
	 */
	public long getOdrUid() {
		return m_lOdrUid;
	}

	/**
	 * 매출 UID
	 * @return
	 */
	public long getSaleUid() {
		return m_lSaleUid;
	}

	/**
	 * 예약 UID
	 * @return
	 */
	public long getRsvUid() {
		return m_lRsvUid;
	}

	/**
	 * 테이블 UID
	 * @return
	 */
	public long getTblUid() {
		return m_lTblUid;
	}

	/**
	 * 상품 UID
	 * @return
	 */
	public long getPdtUid() {
		return m_lPdtUid;
	}

	/**
	 * 상품 세트 UID
	 * @return
	 */
	public long getPdtSetUid() {
		return m_lPdtSetUid;
	}

//	public int getPdtCnt() {
//		return m_nPdtCnt;
//	}

	/**
	 * 상품 단가
	 * @return
	 */
	public long getPdtAmt() {
		return m_lPdtAmt;
	}

	/**
	 * 부가금액
	 * @return
	 */
	public long getAddtnAmt() {
		return m_lAddtnAmt;
	}
	
	/**
	 * 할인금액
	 * @return
	 */
	public long getDcAmt() {
		return m_lDcAmt;
	}
	
	/**
	 * 할인 UID
	 * @return
	 */
	public long getDcUid() {
		return m_lDcUid;
	}
	
	/**
	 * 공급가
	 * @return
	 */
	public long getSplyAmt() {
		return m_lSplyAmt;
	}
	
	/**
	 * 봉사료금액
	 * @return
	 */
	public long getTipAmt() {
		return m_lTipAmt;
	}
	
	/**
	 * 부가세금액
	 * @return
	 */
	public long getVatAmt() {
		return m_lVatAmt;
	}

	/**
	 * 사용자 UID
	 * @return
	 */
	public long getUsrUid() {
		return m_lUsrUid;
	}

	/**
	 * 서비스여부
	 * @return
	 */
	public boolean isSvcYn() {
		return m_isSvcYn;
	}

	/**
	 * 주문시각
	 * @return
	 */
	public String getOdrDttm() {
		return m_strOdrDttm;
	}

	/**
	 * 주문시각
	 * @return
	 */
	public void setOdrDttm(String strOdrDttm) {
		m_strOdrDttm = strOdrDttm;
	}
	
	/**
	 * 연관주문 UID
	 * @return
	 */
	public long getLnkOdrUid() {
		return m_lLnkOdrUid;
	}

	/**
	 * 메모코드
	 * @return
	 */
	public String getMemoCd() {
		return m_strMemoCd;
	}

	/**
	 * 비고
	 * @return
	 */
	public String getCmt() {
		return m_strCmt;
	}
	
	/**
	 * 옵션주문 UID
	 * @return
	 */
	public long getOptnOdrUid() {
		return m_lOptnOdrUid;
	}
	
	/**
	 * 세트주문 UID
	 * @return
	 */
	public long getSetOdrUid() {
		return m_lSetOdrUid;
	}
	
	/**
	 * 사용자 이름
	 * @return
	 */
	public String getUsrNm() {
		return m_strUsrNm;
	}
	
	/**
	 * 메모 문구
	 * @return
	 */
	public String getMemoTxt() {
		return m_strMemoTxt;
	}
	
	/**
	 * 상품 이름
	 * @return
	 */
	public String getPdtNm() {
        return m_strPdtNm;
	}
	
	/**
	 * 상품세트 이름
	 * @return
	 */
	public String getPdtSetNm() {
		return m_strPdtSetNm;
	}
	
	/**
	 * 테이블 명
	 * @return
	 */
	public String getTblNm() {
		return m_strTblNm;
	}
	
	/**
	 * 상품 구분코드
	 * @return
	 */
	public String getPdtTp() {
		return m_strPdtTp;
	}
	
	/**
	 * 상품 반환
	 * @return
	 */
	public DPdt getPdt() {
		return m_objPdt;
	}
	
	
	/**
	 * 옵션상품 반환
	 * @return
	 */
	public DPdt getPdtOpt() {
		return m_objPdtOpt;
	}
	
	public void setPdtUid(long lPdtUid) {
		m_lPdtUid = lPdtUid;
	}

	public void setPdtCnt(int pdtCnt){
		m_nPdtCnt = pdtCnt;
		calcAmt();
	}
	
	/**
	 * 예약 서비스 필드
	 * @return
	 */
	public String get_lnkSvcRsv() {
		return lnk_svc_rsv;
	}

	public void set_lnkSvcRsv(String lnkSvcRsv) {
		this.lnk_svc_rsv = lnkSvcRsv;
	}
	
	/**
	 * 게임번호 반환
	 * @return
	 */
	public int get_game_num() {
		return m_game_num;
	}

	public void set_game_num(int game_num) {
		this.m_game_num = game_num;
	}
	
	/**
	 * 게임 타입 반환 
	 * @return
	 */
	public int get_game_type() {
		return m_game_type;
	}
	
	public void set_game_type( int type ) {
		this.m_game_type = type;
	}
	
	/**
	 * 게임그룹 반환
	 * @return
	 */
	public int get_game_group() {
		return m_game_grp;
	}

	public void set_game_group(int m_game_grp) {
		this.m_game_grp = m_game_grp;
	}
	
	/**
	 * 초기 방정보(머신코드)
	 * @return
	 */
	public String get_Machine_Code() {
		return m_machine_code;
	}
	
	public void set_Machine_Code( String code ) {
		this.m_machine_code = code;
	}
	
	public long getSaleAmt() {
		long saleAmt = m_lPdtAmt  * getPdtCnt();
		//[[ bkshin_20130715_1 - 선택 갯수가 0 이 아닌 경우에만 수행하도록 한다.
		if (getPdtCnt() != 0) {
			// 세트상품인 경우 history 에 있는 상품도 가져오도록 한다.
			saleAmt += getChildSetAmt();
			
			/*if(m_arrOdrHisList != null && !m_arrOdrHisList.isEmpty()) {
				for(DOdr hisOdr : m_arrOdrHisList) {
					if(hisOdr.getPdtCnt() != 0) {
						saleAmt += hisOdr.getChildSetAmt();
					}
				}
			}*/
			
			//[[ bkshin_20130612 옵션메뉴 추가
			if(m_arrOdrOptList != null && ! m_arrOdrOptList.isEmpty()) {
				for(DOdr optOdr : m_arrOdrOptList) {
					saleAmt += optOdr.getPdtAmt() * optOdr.getPdtCnt();
				}
			}
			//]] bkshin_20130612
			
		}
		//]] bkshin_20130715_1
		return saleAmt;
	}
	
	public void setPdtAmt(long pdtAmt){
		m_lPdtAmt = pdtAmt;
		calcAmt();
	}
	
	@Deprecated
	public void setAddtnAmt(long addtnAmt) {
		m_lAddtnAmt = addtnAmt;
	}
	
	public void setDcAmt(long dcAmt) {
		m_lDcAmt = dcAmt;
		m_isSvcYn = false;
		calcAmt();
	}
	
	public void setDcUid(long dcUid) {
		m_lDcUid = dcUid;
	}
	
	public void setSvcYn(boolean svcYn) {
		m_isSvcYn = svcYn;
		if(svcYn) {
			//[[ bkshin_20130719 서비스 할인 금액은 옵션과 세트상품 하위 부가금액을 모두 더한 금액이므로 소스 수정함.
			m_lDcAmt = getSaleAmt() * -1;
			//]] bkshin_20130719
			//[[ bkshin_20130625 서비스 상품이라면 dc_uid 를 -1 로 변환하도록 한다.
			this.setDcUid(-1);
			//]] bkshin_20130625
		} else {
			m_lDcAmt = 0;
		}
		calcAmt();
	}
	
	public void setMemoCd(String memoCd) {
		m_strMemoCd = memoCd;
	}
	
	public void setCmt(String cmt) {
		m_strCmt = cmt;
	}
	
	public void setOptnOdrUid(long lOptnOdrUid) {
		m_lOptnOdrUid = lOptnOdrUid;
	}
	
	public void setSetOdrUid(long lSetOdrUid) {
		m_lSetOdrUid = lSetOdrUid;
	}
	
	protected void setPdt_(DPdt pdt) {
		m_objPdt = pdt;
		calcAmt();
	}
	
	///////////////////////////////////
	
	
	public int getOriginPdtCnt() {
		return m_nPdtCnt;
	}

	public int getPdtCnt() {
		int count = m_nPdtCnt;
		
		return count;
	}
	
	public int getOptnPdtCnt() {
		int count = 0;
		if(m_arrOdrOptList != null && !m_arrOdrOptList.isEmpty()) {
			for(DOdr odr : m_arrOdrOptList) {
				count += odr.getPdtCnt();
			}
		}
		
		return count;
	}
	
	public void addPdtCnt(int addCnt) {

		// bkshin_20130807 상품수량 변경 외에도 해당 함수를 사용하는 부분이 있어 주석처리함
		// 갯수 변경 시 할인 금액이 남아 있을 경우 Exception
//		boolean bEnable = (m_lDcAmt != 0 && !m_isSvcYn);
//		if (bEnable) {
//			CrashErrorReporter.getInstance().assertLog("DOdr::addPdtCnt Exception (addCnt=" + addCnt + ", dcAmt=" + m_lDcAmt + ")");
//			throw new DbException("addPdtCnt Exception (addCnt=" + addCnt + ", dcAmt=" + m_lDcAmt + ")", -1);
//		}

		if(m_arrOdrSetList != null && !m_arrOdrSetList.isEmpty()) {
			for(DOdr setOdr : m_arrOdrSetList) {
				setOdr.addSetChildCnt(m_nPdtCnt, addCnt);
			}
		}
		//coded by sekim 2013-07-19 option 상품 이 있는 상품 증가시 처리 되도록 수정
		if(m_arrOdrOptList != null && !m_arrOdrOptList.isEmpty()) {
			for(DOdr optionOdr :m_arrOdrOptList) {
				optionOdr.addSetChildCnt(m_nPdtCnt, addCnt);
			}
		}

		m_nPdtCnt += addCnt;
		
		if(m_isSvcYn) {
			m_lDcAmt = getSaleAmt() * -1;
		}
		calcAmt();
	}
	
	private void addSetChildCnt(int parentCnt, int parentAddCnt) {
		
		int perCnt = 0;
		if(parentCnt != 0) {
			perCnt = m_nPdtCnt/parentCnt;
			m_nCshChildPerCnt = perCnt;
		} else {
			perCnt = m_nCshChildPerCnt;
		}
		
		int increaseCnt = parentCnt + parentAddCnt;
		m_nPdtCnt = perCnt * (increaseCnt);
		calcAmt();
	}
	
	
	public ArrayList<DOdr> getOdrHisList() {
		return m_arrOdrHisList;
	}
	
	public void addOdrHis(DOdr odr) {
		m_arrOdrHisList.add(odr);
		calcAmt();
	}
	
	public void addOdrHis(int idx, DOdr odr) {
		m_arrOdrHisList.add(idx, odr);
		calcAmt();
	}
	
	public ArrayList<DOdr> getOdrSetList() {
		return m_arrOdrSetList;
	}
	
	public void addOdrSet(DOdr odr) {
		if(m_setPrtOdr == null) {
			odr.m_setPrtOdr = this;
		} else {
			odr.m_setPrtOdr = m_setPrtOdr;
		}
		m_arrOdrSetList.add(odr);
		calcAmt();
	}
	
	public void addOdrSet(int idx, DOdr odr) {
		if(m_setPrtOdr == null) {
			odr.m_setPrtOdr = this;
		} else {
			odr.m_setPrtOdr = m_setPrtOdr;
		}
		m_arrOdrSetList.add(idx, odr);
		calcAmt();
	}
	
	public ArrayList<DOdr> getOdrOptList() {
		return m_arrOdrOptList;
	}
	
	public void addOdrOpt(DOdr odr) {
		m_arrOdrOptList.add(odr);
		// 해당 옵션 상품의 가격을 계산한다.
		calcAmt();
	}
	
	public void addOdrOpt(int idx, DOdr odr) {
		m_arrOdrOptList.add(idx, odr);
		// 해당 옵션 상품의 가격을 계산한다.
		calcAmt();
	}
	
	private void calcAmt() {
		long realAmt = m_lPdtAmt * getOriginPdtCnt();

		// 세트상품
		realAmt += getChildSetAmt();

		//[[ bkshin_20130731 옵션메뉴도 일반메뉴와 같이 독립된 상품으로 인식하여 면세가 등을 계산 시 일반메뉴에 해당 값을 포함하여 계산하지 않도록 한다.
		//[[ bkshin_20130612 옵션메뉴 계산 추가
		if(m_arrOdrOptList != null && !m_arrOdrOptList.isEmpty()) {
			for(int i = 0; i < m_arrOdrOptList.size(); i++) {
				DOdr optnOdr = m_arrOdrOptList.get(i);
				optnOdr.calcAmt();
				//realAmt += (optnOdr.getPdtAmt() * optnOdr.getPdtCnt());
			}
		}
		//]] bkshin_20130612
		//]] bkshin_20130731
		
		if(m_objPdt != null) {
			m_lTipAmt = m_objPdt.calcTipAmt(realAmt + m_lDcAmt);
			m_lVatAmt = m_objPdt.calcVatAmt(realAmt + m_lDcAmt);
			m_lSplyAmt = m_objPdt.calcSplyAmt(realAmt + m_lDcAmt);
		}
	}
	
	private long getChildSetAmt() {
		long amt = 0;
		if(m_arrOdrSetList != null && !m_arrOdrSetList.isEmpty()) {
			for(DOdr odr : m_arrOdrSetList) {
				amt += (odr.getAddtnAmt() * odr.getPdtCnt());
			}
		}
		return amt;
	}
	
}
