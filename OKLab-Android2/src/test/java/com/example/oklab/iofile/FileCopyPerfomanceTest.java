package com.oklab.iofile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * JAVA NIO TEST
 * 참고 사이트 : 
 * http://eincs.com/2009/08/java-nio-bytebuffer-channel-file/
 * http://eincs.com/2009/08/java-nio-bytebuffer-performance/
 * @author ojungwon
 *
 */
public class FileCopyPerfomanceTest {

	private static final String SRC_FILE_NAME = "filechannel.log";
	private static final String DST_FILE_NAME = "copy_database.db";

	private static String FILE_PATH = null;

	public static void log(String str) {
		System.out.println(str);
	}

	Timer timer = new Timer(Timer.Resolution.MILLISECOND);
	
	public static void main(String[] args) {

		/**
		 * 머신 루트 디렉토리 경로의 파일 복사
		 */
		File[] roots = File.listRoots();
		FILE_PATH = roots[1] + "OKDownload\\"; // roots[1] 두번째 드라이브

		File src = new File(FILE_PATH, SRC_FILE_NAME);
		File dst = new File(FILE_PATH, DST_FILE_NAME);

		Timer timer = new Timer(Timer.Resolution.MILLISECOND);

		// 1) IO 
//		 FileCopyNCryptoTest fc = new FileCopyNCryptoTest();
//		 try {
//		 timer.reset();
//		 fc.copy(src, dst);
//		 log("copy elapsed Time :" + timer.getElapsedTime() + "ms");
//		 } catch (IOException e) {
//		 e.printStackTrace();
//		 }
		// 2) NIO
//		timer.reset();
//		FileCopyNCryptoTest.copyFileFast(src, dst);
//		log("copyFileFast elapsed Time :" + timer.getElapsedTime() + "ms");
		
		/**
		 * NIO 사용한 파일 복사 Example  버퍼에따라 속도차이가 난다... 
		 * 빠른순서...
		 */
		try {
        	timer.reset();
        	copyTransfer(src, dst);
	        log("copyTransfer Elapsed Time :" + timer.getElapsedTime() + "ms");
	        timer.reset();
            copyMap(src, dst);
            log("copyMap Elapsed Time :" + timer.getElapsedTime() + "ms");
            timer.reset();
            copyNIO(src, dst);
            log("copyNIO Elapsed Time :" + timer.getElapsedTime() + "ms");
            timer.reset();
            copyIO(src, dst);
            log("copyIO Elapsed Time :" + timer.getElapsedTime() + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Test
		String path = System.getProperty("user.dir");
		System.out.println("user.dir Path = " + path);
		System.out.println(new File(".").getAbsolutePath());
		System.out.println(roots[0].getAbsolutePath());
		System.out.println(roots[0].getName());

	}

	/**
	 * 파일카피 방법 FileChannel 사용으로 OS네이티브 사용으로 빠르다 ????? !!
	 * 
	 * @param fOrg
	 * @param fTarget
	 * @throws IOException
	 */
	public static void copyFileFast(File src, File dst) {
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			inChannel = new FileInputStream(src).getChannel();
			outChannel = new FileOutputStream(dst).getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (Exception e) {

		} finally {
			if (inChannel != null)
				try {
					inChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (outChannel != null)
				try {
					outChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private void copy(File fOrg, File fTarget) throws IOException {
		FileInputStream io = new FileInputStream(fOrg);

		if (!fTarget.isFile()) {
			File fParent = new File(fTarget.getParent());
			if (!fParent.exists()) {
				fParent.mkdir();
			}
			fTarget.createNewFile();
		}

		FileOutputStream out = new FileOutputStream(fTarget);

		byte[] bBuffer = new byte[1024 * 8];

		int nRead;
		while ((nRead = io.read(bBuffer)) != -1) {
			out.write(bBuffer, 0, nRead);
		}

		io.close();
		out.close();
	}
	
	
	 // copyIO() -- >1. io패키지를 이용한 파일 복사
    public static void copyIO(File src, File dst)
            throws Exception {
 
    	if (!dst.isFile()) {
			File fParent = new File(dst.getParent());
			if (!fParent.exists()) {
				fParent.mkdir();
			}
			dst.createNewFile();
		}

    	FileInputStream f_in = new FileInputStream(src);
        FileOutputStream f_out = new FileOutputStream(dst);
        
        byte[] buf = new byte[1024];
        for (int i; (i = f_in.read(buf)) != -1;) {
            f_out.write(buf, 0, i);
        }
        f_in.close();
        f_out.close();
        System.out.println("1. 파일간 복사 --> io패키지 사용 성공!!");
 
    }
 
    // copyMap() -->2. MappedByteBuffer를 이용한 파일간 복사
    public static void copyMap(File src, File dst)
            throws Exception {
    	FileInputStream f_in = new FileInputStream(src);
        FileOutputStream f_out = new FileOutputStream(dst);
        
    	FileChannel in = f_in.getChannel();
        FileChannel out = f_out.getChannel();
        // 입력파일을 매핑한다.
        MappedByteBuffer m = in
                .map(FileChannel.MapMode.READ_ONLY, 0, in.size());
        // 파일을 복사한다.
        out.write(m);
        in.close();
        out.close();
        System.out.println("2. 파일간 복사 --> 매핑 사용 성공!!");
 
    }
 
    // copyNIO() --> 3. read()와 write()사용한 파일 복사
    public static void copyNIO(File src, File dst)
            throws Exception {
    	
    	FileInputStream f_in = new FileInputStream(src);
        FileOutputStream f_out = new FileOutputStream(dst);
    	FileChannel in = f_in.getChannel();
        FileChannel out = f_out.getChannel();
        
        ByteBuffer buf = ByteBuffer.allocateDirect((int) in.size());// 버퍼를 읽기를 할 파일의
        
        in.read(buf);// 읽기
        buf.flip();// 버퍼의 position을 0으로 만든다.
        out.write(buf);// 쓰기
        in.close();
        out.close();
        System.out.println("3. 파일간 복사 --> read()와 write()사용 성공!!");
 
    }
 
    // copyTransfer()-->4. transferTo() 사용한 파일 복사
    public static void copyTransfer(File src, File dst)
            throws Exception {
    	if (!dst.isFile()) {
			File fParent = new File(dst.getParent());
			if (!fParent.exists()) {
				fParent.mkdir();
			}
			dst.createNewFile();
		}
    	
    	FileInputStream f_in = new FileInputStream(src);
        FileOutputStream f_out = new FileOutputStream(dst);
        
    	FileChannel in = f_in.getChannel();
        FileChannel out = f_out.getChannel();
        
        in.transferTo(0, in.size(), out);// 0부터 읽기용 채널 크기까지 쓰기채널에 출력한다.
        in.close();
        out.close();
        System.out.println("1. 파일간 복사 --> transferTo() 사용 성공!!");
 
    }

}
