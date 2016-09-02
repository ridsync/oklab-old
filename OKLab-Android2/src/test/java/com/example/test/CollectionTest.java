package com.example.test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class CollectionTest {

	public static final int SORT_TYPE_ASC 		= 0;
	public static final int SORT_TYPE_DESC 		= 1;
	
	private static Vector<CmpData> m_data 		 	= null;
	
	
	public static void main(String[] args) {
		
		List<CmpData> arList 		= new ArrayList<CmpData>();
		// 임시 코드
//		for (int i = 0 ; i < 4 ;i++){
//			CmpData emp = new CmpData(); 
//			emp.intData = i+100;
//			emp.intData2 = i+200;
//			emp.strData = "s10"+i;
//			emp.strData2 = "s20"+i;
//			arList.add(emp);
//		}
//		createDataList(arList); 
		
		// Compare Test 메소드
		crreateDatas();
		sort(SORT_TYPE_ASC);
		printLists();
		
	}

	private static void createDataList(List<CmpData> data) {
		List<CmpData> arList = data;
		
//		arList.removeAll(arList);
		
		for (int i = 0 ; i < arList.size() ;i++  ){
			System.out.println( "list (" + i + ") / " + arList.get(i).intData + " / " +
					+ arList.get(i).intData2 + " / " + arList.get(i).strData + " / " + arList.get(i).strData2);
		}
	}

	private static void crreateDatas() {
		final int  listSize = 55;
		m_data = new Vector<CmpData>();
		for (int i = 1 ; i < listSize ;i++){
			CmpData emp = new CmpData(); 
			emp.intData = i;
			emp.intData2 = i;
			emp.strData = "s10"+i;
			emp.strData2 = "s20"+i;
			m_data.add(emp);
		}
		
		CmpData emp = new CmpData(); 
		emp.intData = 106;
		emp.intData2 = 206;
		emp.strData = "s103";
		emp.strData2 = "s203";
		m_data.add(emp);
		
		CmpData emp2 = new CmpData();
		
		emp2.intData = 99;
		emp2.intData2 = 199;
		emp2.strData = "s107";
		emp2.strData2 = "s207";
		m_data.add(emp2);
	}

	private static void sort(int sortType) {
		if(sortType == SORT_TYPE_ASC) {
	   		Collections.sort(m_data, new IntDataASCCompare());
		} else {
	   		Collections.sort(m_data, new IntDataDESCCompare());
		}
	}
	
	private static void printLists(){
		int size = m_data.size();
		for (int i = 0 ; i < size ;i++  ){
			System.out.println( "list (" + i + ") / " + m_data.get(i).intData + " / " +
					+ m_data.get(i).intData2 + " / " + m_data.get(i).strData + " / " + m_data.get(i).strData2);
		}
	}
	/**
	 * Comparator implements Class
	 * @author ok
	 *
	 */
	static class StrDataASCCompare implements Comparator<CmpData> {
		@Override
		public int compare(CmpData data1, CmpData data2) {
			int nRet = data1.strData.compareTo(data2.strData); 
			
			if(nRet == 0) {
				nRet = data1.strData2.compareTo(data2.strData2); 
//				nRet = data1.intData > data2.intData ? 1 : -1;
			}
			
			
			return nRet;
		}
	}
	
	static class StrDataDESCCompare implements Comparator<CmpData> {
		@Override
		public int compare(CmpData data1, CmpData data2) {
			int nRet = data1.strData.compareTo(data2.strData); 
			if(nRet != 0) nRet *= -1;
			
			if(nRet == 0) {
				nRet = data1.strData2.compareTo(data2.strData2); 
				nRet *= -1; 
//				nRet = data1.intData < data2.intData ? 1 : -1;
			}
			
			return nRet;
		}
	}
	
	static class IntDataASCCompare implements Comparator<CmpData> {
		@Override
		public int compare(CmpData data1, CmpData data2) {
			int nRet = data1.intData > data2.intData ? 1 : -1;
			
			return nRet;
		}
	}
	
	static class IntDataDESCCompare implements Comparator<CmpData> {
		@Override
		public int compare(CmpData data1, CmpData data2) {
			int nRet = data1.intData < data2.intData ? 1 : -1;
			
			return nRet;
		}
	}
	
	static class CmpData {
		
		public CmpData() {
		}
		
		private String strData = null;
		private String strData2 = null;
		private int intData = -1;
		private int intData2 = -1;
	}
}
