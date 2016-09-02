package com.example.test.oktest.oklab.thread;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CallableNFutureTest {

	
	public static void main(String[] args){
		
		test();
	}
	
	public static void test() {
//	  long today = System.currentTimeMillis(); // long 현재시간
//	  System.out.println(today);
//	  
//	  DateFormat df = new SimpleDateFormat("HH:mm:ss E"); // HH=24h, hh=12h
//	  String str = df.format(today);
//	  System.out.println(str);
//	  
//	  Date date = new Date(today);
//	  System.out.println(date);
		
	}
	
//	public class CallableNFutureTest {
//
//		  public static class WordLengthCallable
//		        implements Callable {
//		    private String word;
//		    public WordLengthCallable(String word) {
//		      this.word = word;
//		    }
//		    public Integer call() {
//		    	System.out.printf("\n Callable ,Integer call()  word.length() = " + word.length());
//		      return Integer.valueOf(word.length());
//		    }
//		  }
//
//		  public static void main(String args[]) throws Exception {
//		    ExecutorService pool = Executors.newFixedThreadPool(3);
//		    Set<Future<Integer>> set = new HashSet<Future<Integer>>();
//		    for (String word: args) {
//		      Callable<Integer> callable = new WordLengthCallable(word);
//		      Future<Integer> future = pool.submit(callable);
//		      set.add(future);
//		      System.out.printf("\n future, set.add(future) = " + future.get());
//		    }
//		    
//		    int sum = 0;
//		    for (Future<Integer> future : set) {
//		      sum += future.get();
//		      System.out.printf("\n future, future.get() = " + future.get());
//		    }
//		    
//		    System.out.printf("\n The sum of lengths is %s%n", sum);
//		    System.exit(sum);
//		  }
}
