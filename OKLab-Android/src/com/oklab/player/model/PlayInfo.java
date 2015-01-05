package com.oklab.player.model;

import java.util.HashMap;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * 학습정보 및 동영상정보 클래스 <br>
 *  1.벨류데이사전학습(CyberLearn) <br>
 *  2.지식채널학습(KNOWCH) <br>
 *  - 학습정보에 따라 필요 데이터 처리
 */
public class PlayInfo implements Parcelable{
	
	public static final String TYPE_VIDEO = "1";
	
	public static final String TYPE_CONTENTS_CYBERLEARN = "TC";
	public static final String TYPE_CONTENTS_KNOWCH = "KC";
	
	/**
	 * 파라미터 키
	 */
	public static final String pKey_type = "type";			// 학습정보 타입
	public static final String pKey_grpCd = "grpCd";			// 회사코드
	public static final String pKey_lecCd = "lecCd"; 			// 과정코드
	public static final String pKey_lecSeq = "lecSeq";			// 차수코드
	public static final String pKey_userId = "userId";			// 유저아이디
	public static final String pKey_contentId = "contentId";	// 차시코드
	public static final String pKey_seq = "seq";			 	// 차시순번
	public static final String pKey_streamPath = "streamPath";  // 스트리밍 동영상 URL
	public static final String pKey_playTime = "playTime";  // seek time
	public static final String pKey_time = "time"; // 동영상 총 재생시간
	public static final String pKey_finishYn = "finishYn"; // 학습완료여부
	public static final String pKey_previewType = "previewType"; // 사전학습타입
	public static final String pKey_teamSeq = "teamSeq"; // 벨류팀코드
	public static final String pKey_lastTime = "lastTime"; // 학습완료 기준 최소시간
	
	public static final String pKey_sgrpCd = "sgrpCd"; // 회사코드
	public static final String pKey_cosCd = "cosCd"; // 과정코드
	public static final String pKey_idx = "idx"; // 차시코드
	public static final String pKey_contUrl = "contUrl";  // 스트리밍 동영상 URL
	public static final String pKey_stat = "stat";  // 학습완료여부
	
	public static final String pKey_continueYn = "continueYn"; // 학습가능여부
	public static final String pKey_contentpoolNo = "contentpoolNo"; // 차시코드
	public static final String pKey_attendCnt = "attendCnt"; // 학습한 시간 초단위
	public static final String pKey_mobilePlaySecond = "mobilePlaySecond"; // 플레이 시간 초단위
	public static final String pKey_seekYn = "seekYn"; //
	
	// 기본데이터 
	public String data = null;
	
	// 학습동영상 타입
	public String type = null;
	
	// 동영상정보 (사이버학습)
	public String grpCd = null;
	public String lecCd = null;
	public String lecSeq = null;
	public String userId = null;
	public String contentId = null;
	public String seq = null;
	public String streamPath = null;
	public int playTime = 0;
	public int time = -1;
	public String finishYn = null;
	public String previewType = null;
	public String teamSeq = null;
	public int lastTime = -1;
	
	// 동영상정보 (지식채널학습)
	public String sgrpCd = null;
	public String cosCd = null;
	public String idx = null;
	
	// 동영상진도 체크관련 추가param
	public String continueYn = null;
	public String contentpoolNo = null;
	public int attendCnt = -1;
	public int mobilePlaySecond = -1;
	
	// 기타
	public boolean isDown = false;
	public boolean seekEnable = false;
	
	
	public PlayInfo(String data) {
		// TODO Auto-generated constructor stub
		this.data = data;
	}
	
	public PlayInfo(boolean isDown , String data, HashMap<String, String> params) {
		// TODO Auto-generated constructor stub
		this.isDown = isDown;
		this.data = data;
		this.grpCd = params.get(pKey_grpCd);
		this.lecCd = params.get(pKey_lecCd);
		this.lecSeq = params.get(pKey_lecSeq);
		this.userId = params.get(pKey_userId);
		this.contentId = params.get(pKey_contentId);
		this.contentpoolNo = params.get(pKey_contentId);
		this.seq = params.get(pKey_seq);
		this.type = params.get(pKey_type);
		this.type = type == null ? TYPE_CONTENTS_CYBERLEARN : type;
		if (TYPE_CONTENTS_CYBERLEARN.equals(type)){
			this.streamPath = params.get(pKey_streamPath);
			try {
				this.playTime = Integer.valueOf(params.get(pKey_playTime));
			} catch (NumberFormatException e) {
				this.playTime = 0;
			}
			this.finishYn = params.get(pKey_finishYn);
			this.previewType = params.get(pKey_previewType);
			this.teamSeq = params.get(pKey_teamSeq);
			try {
				this.lastTime = Integer.valueOf(params.get(pKey_lastTime));
			} catch (NumberFormatException e) {
				this.lastTime = 0;
			}
		}else { // 지식채널학습 동영상정보
			this.sgrpCd = params.get(pKey_sgrpCd);
			this.cosCd = params.get(pKey_cosCd);
			this.streamPath = params.get(pKey_contUrl);
			this.idx = params.get(pKey_idx);
			try {
				this.playTime = Integer.valueOf(params.get(pKey_mobilePlaySecond));
			} catch (NumberFormatException e) {
				this.playTime = 0;
			}
			this.finishYn = "F".equals(params.get(pKey_stat)) ? "Y" : "N";
		}
		try {
			this.time = Integer.valueOf( params.get(pKey_time) );
		} catch (NumberFormatException e) {
			this.time = -1;
		}
		
		this.seekEnable = "Y".equals(params.get(pKey_seekYn)) ?  true : false;
	}
	
	/**
	 * 전달된 파람으로 부터 필요 데이터 파싱
	 * @param urlData
	 * @return
	 */
	public static PlayInfo getPlayInfo(String urlData){
		if(urlData.startsWith("app://")){
			String urlWithoutProtocol = urlData.substring("app://".length());
			
			String[] requestInfo = urlWithoutProtocol.split("\\?");
			
			boolean isDown = false;
			if(requestInfo.length==1){
//				파람이 없는 경우 액션코드와 빈맵을 담아 리턴
				return new PlayInfo(isDown , urlData, new HashMap<String, String>());
				
			}else if(requestInfo.length==2){
				String[] strParams = requestInfo[1].split("&");
				
				HashMap<String, String> params = new HashMap<String, String>();
				
				for(int i=0;i<strParams.length;i++){
					
					String[] keyValue = strParams[i].split("=");
					
					if(keyValue.length==1){
						params.put(keyValue[0], "");
					}else if(keyValue.length==2){
						params.put(keyValue[0], keyValue[1]);
					}else{
						return null;
					}
				}
				
				return new PlayInfo(isDown , urlData, params);
				
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	
	public boolean parsing(){
		boolean result = false;
		
		if(this.data!=null && data.length() > 0 ){
			try{
				JSONObject root = new JSONObject(data);
				
				// 정보 
				grpCd = root.optString(pKey_grpCd);
				lecCd = root.optString(pKey_lecCd);
				lecSeq = root.optString(pKey_lecSeq);
				contentpoolNo = root.optString(pKey_contentpoolNo);
				userId = root.optString(pKey_userId);
				streamPath = root.optString(pKey_streamPath);
				
				result = true;
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
				result = false;
			}
			
			Log.i(getClass().getSimpleName(), "parsing="+toString());
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		if (TYPE_CONTENTS_CYBERLEARN.equals(type)){
			return "PlayInfo TEAMCAFE [grpCd=" + grpCd + ", lecCd=" + lecCd + ", lecSeq="
					+ lecSeq + ", contentpoolNo=" + contentpoolNo + ", userId="
					+ userId + ", streamPath=" + streamPath + ", playTime=" + playTime + ",  lastTime=" + lastTime
					+ ",  seekEnable=" + seekEnable + ",  finishYn=" + finishYn + ",  previewType=" + previewType + ",  isDown=" + isDown +"]";
		}else { // 지식채널학습 동영상정보
			return "PlayInfo KNOCH [sgrpCd=" + sgrpCd + ", cosCd=" + cosCd + ", idx=" + idx + ", userId="
			+ userId + ", streamPath=" + streamPath + ", playTime=" + playTime 
			+ ",  seekEnable=" + seekEnable +",  finishYn=" + finishYn +",  isDown=" + isDown+ "]";
		}
		
	}

	/**
	 * parcelable creator
	 */
	public static final Parcelable.Creator<PlayInfo> CREATOR = new Parcelable.Creator<PlayInfo>() {
        public PlayInfo createFromParcel(Parcel in) {
             return new PlayInfo(in);
       }

       public PlayInfo[] newArray(int size) {
            return new PlayInfo[size];
       }
	};
	
	public PlayInfo(Parcel in) {
		readFromParcel(in);
	}

	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(type);
		dest.writeString(data);
		dest.writeString(grpCd);
		dest.writeString(lecCd);
		dest.writeString(lecSeq);
		dest.writeString(userId);
		dest.writeString(contentId);
		dest.writeString(seq);
		dest.writeString(streamPath);
		dest.writeInt(playTime);
		dest.writeInt(time);
		dest.writeInt(lastTime);
		dest.writeString(finishYn);
		dest.writeString(previewType);
		dest.writeString(teamSeq);
		dest.writeString(continueYn);
		dest.writeString(contentpoolNo);
		dest.writeInt(attendCnt);
		dest.writeInt(mobilePlaySecond);
		dest.writeInt(isDown ? 1 : 0);
		dest.writeInt(seekEnable ? 1 : 0);
		
		dest.writeString(sgrpCd);
		dest.writeString(cosCd);
		dest.writeString(idx);
	}
	
	public void readFromParcel(Parcel in){
		type = in.readString();
		data = in.readString();
		grpCd = in.readString();
		lecCd = in.readString();
		lecSeq = in.readString();
		userId = in.readString();
		contentId = in.readString();
		seq = in.readString();
		streamPath = in.readString();
		playTime = in.readInt();
		time = in.readInt();
		lastTime = in.readInt();
		finishYn = in.readString();
		previewType = in.readString();
		teamSeq = in.readString();
		continueYn = in.readString();
		contentpoolNo = in.readString();
		attendCnt = in.readInt();
		mobilePlaySecond = in.readInt();
		isDown = in.readInt() == 1;
		seekEnable = in.readInt() == 1;
		
		sgrpCd = in.readString();
		cosCd = in.readString();
		idx = in.readString();
	}
}
