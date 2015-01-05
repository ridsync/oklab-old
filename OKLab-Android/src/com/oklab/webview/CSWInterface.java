package com.oklab.webview;

public interface CSWInterface {
	
	public static final String EMPTY_STRING = "";
	
	public static final int WV_CNT = 3;
	
	public static final int HEADER = 0;
	public static final int BODY = 1;
	public static final int FOOTER = 2;
	
	public static final int[] WV_IDS = {
			HEADER
		,	BODY
		,	FOOTER
	};
	
	public static final int BASE_WIDTH = 1280;
	public static final int BASE_HEADER_HEIGHT = 40;
	public static final int BASE_FOOTER_HEIGHT = 50;

	public static final String JSI_NAME = "CSaF";
	
	public static final String[] JSI_IDS = {
			"header"
		,	"body"
		,	"footer"
	};

	public static final String IDXS = "idxs";
	public static final String FNC = "fnc";
	public static final String ARGS = "args";
	
	public static final String SCRIPT_PREFIX = "javascript:";
	public static final String SCRIPT_SUFFIX = ";";
	
	/*
	 	개발 중에는 필요한 값을 박아서 사용
	 
	public static final String[] DEFAULT_URI = {
			"/csaf/titlebar.html"
		,	"/csaf/main.html"
		,	"/csaf/tabbar.html"
	};
	*/
}
