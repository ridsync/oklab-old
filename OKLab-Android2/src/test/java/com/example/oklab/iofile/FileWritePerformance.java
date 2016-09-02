package com.oklab.iofile;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import junit.framework.TestCase;

/** JAVA IO  & NIO
 * http://bcho.tistory.com/288
 * http://www.mobilejava.co.kr/bbs/temp/lecture/j2me/perf5.html
 **/

public class FileWritePerformance extends TestCase{
	final static int loop = 1000;
	final static String LOG_HOME = "d:/OKDownload/";
	static String LOG_STRING = "";
	static {
	}

	Timer timer = new Timer(Timer.Resolution.MILLISECOND);
	
	public void testSuite() throws Exception {
		
		int stringSize[] = { 100, 200, 500, 1000, 5000 };
		for (int i = 0; i < stringSize.length; i++) {
			LOG_STRING = "";
			for (int j = 0; j < stringSize[i]; j++)
				LOG_STRING += "1234567890";
			log(stringSize[i] + " bytes");
			testFileWriter();
			System.gc();
			testBufferedWriter();
			System.gc();
			testFileOutputStream();
			System.gc();
			testFileBufferedOutputStream();
			System.gc();
			testFileChannel();
			System.gc();
			testFileChannelOneBuffer();
			System.gc();
		}
	}

	public static void log(String str) {
		System.out.println(str);
	}

	// java.io.FileWriter
	private void testFileWriter() throws Exception {
		FileWriter fw = new FileWriter(LOG_HOME + "writer.log");
		timer.reset();
		for (int i = 0; i < loop; i++) {
			fw.write(LOG_STRING);
		}
		log("FileWriter :" + timer.getElapsedTime() + "ms");
		fw.close();
	}

	// java.io.BufferedWriter
	private void testBufferedWriter() throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_HOME
				+ "writer.log"));
		timer.reset();
		for (int i = 0; i < loop; i++) {
			bw.write(LOG_STRING);
		}
		log("BufferedWriter :" + timer.getElapsedTime() + "ms");
		bw.close();
	}

	// java.io.FileOutputStream
	private void testFileOutputStream() throws Exception {
		FileOutputStream fos = new FileOutputStream(LOG_HOME
				+ "outputstream.log");
		timer.reset();
		for (int i = 0; i < loop; i++) {
			byte[] buf = LOG_STRING.getBytes();
			fos.write(buf);
		}
		log("FileOutputStream :" + timer.getElapsedTime() + "ms");
		fos.close();
	}

	// java.io.FileOutputStream
	// + java.io.BufferedOutputStream
	private void testFileBufferedOutputStream() throws Exception {
		BufferedOutputStream fos = new BufferedOutputStream(
				new FileOutputStream(LOG_HOME + "bufferedoutputstream.log"));
		timer.reset();
		for (int i = 0; i < loop; i++) {
			byte[] buf = LOG_STRING.getBytes();
			fos.write(buf);
		}
		log("BufferedOutputStream :" + timer.getElapsedTime() + "ms");
		fos.close();
	}

	private void testFileChannel() throws Exception {
		FileChannel fc = (new FileOutputStream(new File(LOG_HOME
				+ "filechannel.log"))).getChannel();
		timer.reset();
		for (int i = 0; i < loop; i++) {
			byte[] buf = LOG_STRING.getBytes();
			ByteBuffer bytebuffer = ByteBuffer.allocateDirect(buf.length);
			bytebuffer.put(buf);
			bytebuffer.flip();
			fc.write(bytebuffer);
		}
		log("FileChannel  :" + timer.getElapsedTime() + "ms");
		fc.close();
	}

	private void testFileChannelOneBuffer() throws Exception {
		FileChannel fc = (new FileOutputStream(new File(LOG_HOME
				+ "filechannelonebuf.log"))).getChannel();
		int BUF_SIZE = 1024;
		timer.reset();
		ByteBuffer bytebuffer = ByteBuffer.allocateDirect(BUF_SIZE);
		for (int i = 0; i < loop; i++) {
			byte[] buf = LOG_STRING.getBytes();
			int offset = 0;
			int length = buf.length;
			while (offset < length) {
				int chunkSize = BUF_SIZE > length - offset ? length - offset
						- 1 : BUF_SIZE;
				bytebuffer.put(buf, offset, chunkSize);
				bytebuffer.flip();
				offset += BUF_SIZE;
				fc.write(bytebuffer);
				bytebuffer.clear();

			}
		}
		log("FileChannel with reusing buffer:" + timer.getElapsedTime()
				+ "ms");
		fc.close();
	}

}
