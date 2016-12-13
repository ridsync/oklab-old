package com.example.test.oktest.oklab.concurrency;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableNFutureTest {


	public static class WordLengthCallable
			implements Callable {
		private String word;

		public WordLengthCallable(String word) {
			this.word = word;
		}

		public Integer call() {
			System.out.printf("\n Callable ,Integer call()  word.length() = " + word.length());
			return Integer.valueOf(word.length());
		}
	}

	public static void main(String args[]) throws Exception {
		String[] argss =  new String[100];
		for (int i = 0; i < 100; i++) {
			argss[i] = "word" + i;
		}
		ExecutorService pool = Executors.newCachedThreadPool();
		Set<Future<Integer>> set = new HashSet<>();
		for (String word : argss) {
			Callable<Integer> callable = new WordLengthCallable(word);
			Future<Integer> future = pool.submit(callable);
			set.add(future);
			System.out.printf("\n future, set.add(future) = " + word );
		}

		int sum = 0;
		for (Future<Integer> future : set) {
			sum += future.get();
			System.out.printf("\n future, future.get() = " + future.get());
		}

		System.out.printf("\n The sum of lengths is %s%n", sum);
		System.exit(sum);
	}
}
