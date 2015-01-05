package com.oklab.framework.intents;

import android.os.Parcel;
import android.os.Parcelable;


public class Download implements Parcelable {

	private static final long serialVersionUID = -3826078475571311370L;
	
	private String grpCd;
	private String lecCd;
	private String lecSeq;
	private String contentpoolNo;
	private String urlPath;
	private String fileNm;
	private String fnInfo;
	private int fnTotal;
	
	public Download(){
		
	}
	
	public Download(String grpCd, String lecCd, String lecSeq, String contentpoolNo, String urlPath, String fileNm){
		this.grpCd = grpCd;
		this.lecCd = lecCd;
		this.lecSeq = lecSeq;
		this.contentpoolNo = contentpoolNo;
		this.urlPath = urlPath;
		this.fileNm = fileNm;
	}
	
	public Download(String lecSeq, String contentpoolNo, String urlPath, String fileNm, int fnTotal){
		this.lecSeq = lecSeq;
		this.contentpoolNo = contentpoolNo;
		this.urlPath = urlPath;
		this.fileNm = fileNm;
		this.fnTotal = fnTotal;
	}

	public String getGrpCd() {
		return grpCd;
	}

	public void setGrpCd(String grpCd) {
		this.grpCd = grpCd;
	}

	public String getLecCd() {
		return lecCd;
	}

	public void setLecCd(String lecCd) {
		this.lecCd = lecCd;
	}

	public String getLecSeq() {
		return lecSeq;
	}

	public void setLecSeq(String lecSeq) {
		this.lecSeq = lecSeq;
	}

	public String getContentpoolNo() {
		return contentpoolNo;
	}

	public void setContentpoolNo(String contentpoolNo) {
		this.contentpoolNo = contentpoolNo;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public String getFileNm() {
		return fileNm;
	}

	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}

	public String getFnInfo() {
		return fnInfo;
	}

	public void setFnInfo(String fnInfo) {
		this.fnInfo = fnInfo;
	}

	public int getFnTotal() {
		return fnTotal;
	}

	public void setFnTotal(int fnTotal) {
		this.fnTotal = fnTotal;
	}

	@Override
	public String toString() {
		return "Download [grpCd=" + grpCd + ", lecCd=" + lecCd + ", lecSeq="
				+ lecSeq + ", contentpoolNo=" + contentpoolNo + ", rsmlNm="
				+ urlPath + ", fileNm=" + fileNm + ", fnInfo=" + fnInfo
				+ ", getGrpCd()=" + getGrpCd() + ", getLecCd()=" + getLecCd()
				+ ", getLecSeq()=" + getLecSeq() + ", getContentpoolNo()="
				+ getContentpoolNo() + ", getRsmlNm()=" + getUrlPath()
				+ ", getFileNm()=" + getFileNm() + ", getFnInfo()="
				+ getFnInfo() + "]";
	}
	
	
	public String insert() {
		return "insert into EM_DOWNLOAD (GRP_CD, LEC_CD, LEC_SEQ, CONTENTPOOL_NO, RSML_NM, FILE_NM, TOTAL_FILE) " +
				"values(" + grpCd + ", " + lecCd + ", "+ lecSeq + ", " + contentpoolNo + ", "+ urlPath + ", " + fileNm + ", " + fnTotal + ")";
	}

    protected Download(Parcel in) {
        grpCd = in.readString();
        lecCd = in.readString();
        lecSeq = in.readString();
        contentpoolNo = in.readString();
        urlPath = in.readString();
        fileNm = in.readString();
        fnInfo = in.readString();
        fnTotal = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(grpCd);
        dest.writeString(lecCd);
        dest.writeString(lecSeq);
        dest.writeString(contentpoolNo);
        dest.writeString(urlPath);
        dest.writeString(fileNm);
        dest.writeString(fnInfo);
        dest.writeInt(fnTotal);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Download> CREATOR = new Parcelable.Creator<Download>() {
        @Override
        public Download createFromParcel(Parcel in) {
            return new Download(in);
        }

        @Override
        public Download[] newArray(int size) {
            return new Download[size];
        }
    };
}