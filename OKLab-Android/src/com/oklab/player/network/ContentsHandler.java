package com.oklab.player.network;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.oklab.player.model.Download;
import com.oklab.player.model.PlayInfo;
import com.oklab.util.Logger;


public class ContentsHandler {
	
	public static String def_path = "/.vd3media/";
	
	private static final Object sync_obj = new Object(); 
	
	private static PlayInfo play_info = null;
	
	public static void setCurrPlayInfo(PlayInfo info){
		if(info!=null){
			synchronized (sync_obj) {
				play_info = info;
			}
		}
	}
	
	public static PlayInfo getCurrPlayinInfo(){
		synchronized (sync_obj) {
			return play_info;
		}
	}
	public static boolean isVideoContentsValidate(String path, String keys){
		
		@SuppressWarnings("unused")
		boolean IS_VAL = false;

		String mSdPath = getPath();
        String filename = path.substring(path.lastIndexOf("/"), path.length());
        
        File base = new File(mSdPath);
        if(!base.exists()) base.mkdir();
        
        File dir = new File(mSdPath + keys);
        if(!dir.exists()) dir.mkdir();
        
        File resource = new File(mSdPath + keys + "/resource");
        if(!resource.exists()) resource.mkdir();
        
        File file = new File(mSdPath + keys + "/resource", filename);
        Logger.d("VieoPlayer = Check Path ("+ file.exists() +") : " + mSdPath + keys + filename);
        Logger.d("VieoPlayer = Check Path ("+ resource.exists() +") : " + mSdPath + keys + "/resource");

        Logger.d("VieoPlayer = Check Path ("+ file +") file.exists() : " + file.exists());
        Logger.d("VieoPlayer = Check Path ("+ file +") file.isFile() : " + file.isFile());
        
		return file.exists() && file.isFile();
	}
	
	/**
	 * bywoong
	 * @param path
	 * @param keys
	 * @return
	 * 2013. 2. 23.
	 */
	public static boolean isFilesValidate(String pathResource, String fname){
        File file = new File(pathResource, fname);
        
        boolean result = file.exists();
        
		return result; 
	}
	
	public static boolean isFilesValidate(Context context, Download dn){
		
        File file = new File(dn.getFileNm());
        
        boolean result = file.exists();
        
//        if(result){
//        	boolean isdb = VieoPlayerDBHelper.checkDownInfo(context, dn);
//        	
//        	if(!isdb){
//        		VieoPlayerDBHelper.regDownInfo(context, dn);
//        	}
//        }
        
		return result; 
	}
	
	public static long getExternalAvailableSpaceInBytes() {
        long availableSpace = -1L;
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return availableSpace;
    }
	
	public static boolean isFreeSpaceExternal(int file_byte){
		long free_byte = getExternalAvailableSpaceInBytes();
		
		Logger.d("VieoPlayer >> isFreeSpaceExternal free/file = "+free_byte+"/"+file_byte);
		return free_byte>file_byte;
	}
	
	/**
	 * bywoong
	 * @return
	 * 2013. 2. 23.
	 */
	public static String getPath(){
		String ext = Environment.getExternalStorageState();
		if (ext.equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath()+def_path;
        } else {
            return Environment.MEDIA_UNMOUNTED+def_path;
        }
	}
	
	/**
	 * bywoong
	 * @param keys
	 * @return
	 * 2013. 2. 23.
	 */
	public static String getTarget(String keys){
		return getPath() + keys;
	}
	
	public static String getKeys(String grpCd, String lecCd, String lecSeq){
		return grpCd + "_" + lecCd + "_" + lecSeq;
	}
	
	/**
	 * bywoong
	 * @param files
	 * @return
	 * 2013. 2. 26.
	 */
	public static String getDownParam(ArrayList<Download> files){
		String param = "";
		try{
			if(files!=null && files.size()>0){
				for(Download dn: files){
					String key = dn.getGrpCd()+"_"+dn.getLecCd()+"_"+dn.getLecSeq();
					if(param.indexOf(key)==-1)
						param += dn.getGrpCd()+"_"+dn.getLecCd()+"_"+dn.getLecSeq()+";";
				}
			}
		}catch(Exception e){
			Logger.e(e.getMessage());
		}
		return param;
	}
	
	/**
	 * bywoong
	 * @param path
	 * 2013. 2. 26.
	 */
	public static boolean deleteDownFile(String path){ 
		Logger.d("VieoPlayer >> delete file path="+path);
	    
		boolean result = false;
		try{
			File file = new File(path);
		    if(file.isDirectory()){
		    	 File[] childFileList = file.listFiles();
		 	    for(File childFile : childFileList)
		 	    {
		 	        if(childFile.isDirectory()) {
		 	        	deleteDownFile(childFile.getAbsolutePath());      
		 	        }
		 	        else {
		 	            childFile.delete();    
		 	        }
		 	    }  
		    }
		    file.delete(); 
		    result = true;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	public static boolean deleteDownFolder(String lec_key){ 
		Logger.d("VieoPlayer >> delete file path="+lec_key);
	    
		boolean result = false;
		try{
			File file = new File(getPath());
		    if(file.isDirectory()){
		    	 File[] childFileList = file.listFiles();
		 	    for(File childFile : childFileList)
		 	    {
		 	        if(childFile.getName().contains(lec_key)){
		 	        	if(childFile.isDirectory()) {
			 	        	deleteDownFile(childFile.getAbsolutePath());      
			 	        }
			 	        else {
			 	            childFile.delete();    
			 	        }
		 	        }
		 	    }  
		    }
		    result = true;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	/**
	 * 학습진도체크?? 출석시간체크 ??
	 * bywoong
	 * 
	 * @param context
	 * @param lesson
	 * 2013. 2. 27.
	 */
//	public static void updateAttendTime(){
//    	
//		AQuery aquery = new AQuery(MainActivity.mainActivity);
//		try{
//			Lesson lesson = ContentsHandler.getShareLesson();
//			Logger.d("VieoPlayer", "Lesson AttendTime : " + lesson.toString());
//			
//	    	aquery.ajax(Config.lesson_update, lesson.getParamMap(), String.class, new AjaxCallback<String>(){
//	     		public void callback(String url, String object, AjaxStatus status) {
//	     			Logger.d("VieoPlayer", "Status : " + status.getMessage());
//	     			Logger.d("VieoPlayer", "Json : " + object);
//	     			if (object != null) {
//	     				try{
//	     					JSONObject obj = new JSONObject(object);
//	     					Logger.d("VieoPlayer", "result " + obj.toString());
//	     				}catch(Exception e){
//	     					//e.printStackTrace();
//	     				}
//	     			}
//	     		}
//	    	});
//		}catch(Exception e){
//			//e.printStackTrace();
//		}
//	}
	
	/**
	 * 학습완료체크
	 * 학습완료 : mobilePlaySecond: '130'
	 */
//	public static void updateAttendComplete(int total){
//    	
//		AQuery aquery = new AQuery(MainActivity.mainActivity);
//		try{
//			Lesson lesson = ContentsHandler.getShareLesson();
//			lesson.setTotal(String.valueOf(total));
//			Logger.d("VieoPlayer", "Lesson Complete : " + lesson.toString());
//			
//	    	aquery.ajax(Config.lesson_complate, lesson.getParamMap(), String.class, new AjaxCallback<String>(){
//	     		public void callback(String url, String object, AjaxStatus status) {
//	     			Logger.d("VieoPlayer", "Status : " + status.getMessage());
//	     			Logger.d("VieoPlayer", "Json : " + object);
//	     			if (object != null) {
//	     				try{
//	     					JSONObject obj = new JSONObject(object);
//	     					Logger.d("VieoPlayer", "result " + obj.toString());
//	     				}catch(Exception e){
//	     					//e.printStackTrace();
//	     				}
//	     			}
//	     		}
//	    	});
//		}catch(Exception e){
//			//e.printStackTrace();
//		}
//	}
	
	/**
	 * 학습진도체크?? 출석시간체크 ??
	 * bywoong
	 * 
	 * @param context
	 * @param lesson
	 * 2013. 2. 27.
	 */
//	public static void updateAttendTime(String type, Context context, PlayInfo info){
////		Logger.d("VieoPlayer", "<< updateAttendTime context="+context);
//		
//		AQuery aquery = new AQuery(context.getApplicationContext());
//		try{
//			HashMap<String, String> params = info.getParamMap(type);
//			Logger.d("VieoPlayer", "<< updateAttendTime parmas="+params.toString());
//			
//	    	aquery.ajax(Config.lesson_update, params, String.class, new AjaxCallback<String>(){
//	     		public void callback(String url, String object, AjaxStatus status) {
//	     			Logger.d("VieoPlayer", "Status : " + status.getMessage());
//	     			Logger.d("VieoPlayer", "Json : " + object);
//	     			if (object != null) {
//	     				try{
//	     					JSONObject obj = new JSONObject(object);
//	     					Logger.d("VieoPlayer", "<< updateAttendTime result " + obj.toString());
//	     				}catch(Exception e){
//	     					//e.printStackTrace();
//	     				}
//	     			}
//	     		}
//	    	});
//		}catch(Exception e){
//			//e.printStackTrace();
//		}
//	}
	
	/**
	 * 학습완료체크
	 * 학습완료 : mobilePlaySecond: '130'
	 */
//	public static void updateAttendComplete(String type, Context context, PlayInfo info, String itemID, int pageStartTime, int startTime, int endTime){
////		Logger.d("VieoPlayer", "<< updateAttendComplete context="+context);
//    	
//		AQuery aquery = new AQuery(context.getApplicationContext());
//		try{			
//			HashMap<String, String> params = info.getParamMap(type, itemID, String.valueOf(pageStartTime), String.valueOf(startTime), String.valueOf(endTime));
//			Logger.d("VieoPlayer", "<< updateAttendComplete parmas="+params.toString());
//			
//	    	aquery.ajax(Config.lesson_complate_play, params, String.class, new AjaxCallback<String>(){
//	     		public void callback(String url, String object, AjaxStatus status) {
//	     			Logger.d("VieoPlayer", "Status : " + status.getMessage());
//	     			Logger.d("VieoPlayer", "Json : " + object);
//	     			if (object != null) {
//	     				try{
//	     					JSONObject obj = new JSONObject(object);
//	     					Logger.d("VieoPlayer", "<< updateAttendComplete result " + obj.toString());
//	     				}catch(Exception e){
//	     					//e.printStackTrace();
//	     				}
//	     			}
//	     		}
//	    	});
//		}catch(Exception e){
//			//e.printStackTrace();
//		}
//	}
	
//	/**
//	 * @return
//	 */
//	public static Lesson getShareLesson(){
//		Lesson lesson = new Lesson();
//		SharedPreferences pref =  MainActivity.mainActivity.getSharedPreferences("Lesson", 0);
//		lesson.setGrpCd(pref.getString("grpCd", ""));
//		lesson.setLecCd(pref.getString("lecCd", ""));
//		lesson.setLecSeq(pref.getString("lecSeq", ""));
//		lesson.setUserId(pref.getString("userId", ""));
//		lesson.setContentpoolNo(pref.getString("poolNo", ""));
//		lesson.setItemId(pref.getString("itemId", ""));
//		return lesson;
//	}
}
