package com.oklab.iofile;

import java.io.File;


public class JavaStreamTest {
	
	private static final String SRC_FILE_NAME = "uprlite.db"; 
	private static final String DST_FILE_NAME = "copy_database.db";
	
	private static String FILE_PATH = null; 
	
	public static void main(String[] args){

		/**
		 * 머신 루트 디렉토리 경로의 파일 복사
		 */
		File[] roots = File.listRoots();
		FILE_PATH = roots[1]+"OKDownload\\"; // roots[1] 두번째 드라이브
		
		File src = new File (FILE_PATH, SRC_FILE_NAME );
		
	}
	
	 
}
