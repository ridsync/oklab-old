package com.example.test.oktest;



public class EnumTest {

//	private static HashMap<String,> hashMap = new HashMap<String, > ();
	
	static enum MENU_MAG{
		EMPLOYEE_CLOCK_INOUT(0);
		
		MENU_MAG(int value) { this.value = value; }
		private final int value;
		public int value() { return value; }
	};
	
	static enum MENU_SALES{
		SALES_TOTAL(0);
		
		MENU_SALES(int value) { this.value = value; }
		private final int value;
		public int value() { return value; }
	};
	
	static enum MENU_CUSTOMER {
		NONE(0);
		
		MENU_CUSTOMER(int value) { this.value = value; }
		private final int value;
		public int value() { return value; }
	};
	
	MENU_MAG magMenu[] = {MENU_MAG.EMPLOYEE_CLOCK_INOUT};
	MENU_SALES salesMenu[] = {MENU_SALES.SALES_TOTAL};
	MENU_CUSTOMER customerMenu[] = {MENU_CUSTOMER.NONE};
	
	int menuIconsManagement[] = {0};
	int menuIconsSalesReport[] = {0, 1};
	int menuIconsCustomer[] = {};
	
	public static void main(String[]  args){
		
//		hashMap.put("1", MENU_MAG);
//		hashMap.put("2", MENU_SALES);
//		hashMap.put("3", MENU_MAG);
		
	}
}
