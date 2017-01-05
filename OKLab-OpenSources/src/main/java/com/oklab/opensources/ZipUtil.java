package com.oklab.opensources;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by andman on 2016-12-12.
 */
public class ZipUtil {

    private static final int COMPRESSION_LEVEL = 8;

    private static final int BUFFER_SIZE = 1024 * 2;


    /**
     * 지정된 폴더를 Zip 파일로 압축한다.
     * @param sourcePath - 압축 대상 디렉토리 또는 파일  ('경로/파일')
     * @param output - 저장 zip파일명 ('경로/파일명.zip')
     * @throws Exception
     */
    public static void zip(String sourcePath, String output) throws Exception {

        // 압축 대상(sourcePath)이 디렉토리나 파일이 아니면 리턴한다.
        File sourceFiles = new File(sourcePath);
        if (!sourceFiles.isFile() && !sourceFiles.isDirectory()) {
            throw new FileNotFoundException("압축 대상의 파일을 찾을 수가 없습니다.");
        }

        // output 의 확장자가 zip이 아니면 리턴한다.
        //if (!(StringUtils.substringAfterLast(output, ".")).equalsIgnoreCase("zip")) {
        //    throw new Exception("압축 후 저장 파일명의 확장자를 확인하세요");
        //}

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipOutputStream zos = null;

        try {
            fos = new FileOutputStream(output); // FileOutputStream
            bos = new BufferedOutputStream(fos); // BufferedStream
            zos = new ZipOutputStream(bos); // ZipOutputStream
            zos.setLevel(COMPRESSION_LEVEL); // 압축 레벨 - 최대 압축률은 9, 디폴트 8

            zipEntry(sourceFiles, sourcePath, zos); // Zip 파일 생성
            zos.finish(); // ZipOutputStream finish
            Log.i("ZipUtil", "Zip Finished [ " + output + " ]");
        } finally {
            if (zos != null) {
                zos.close();
            }
            if (bos != null) {
                bos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 압축
     * @param sourceFile
     * @param sourcePath
     * @param zos
     * @throws Exception
     */
    private static void zipEntry(File sourceFile, String sourcePath, ZipOutputStream zos) throws Exception {
        // sourceFile 이 디렉토리인 경우 하위 파일 리스트 가져와 재귀호출
        if (sourceFile.isDirectory()) {
            if (sourceFile.getName().equalsIgnoreCase(".metadata")) { // .metadata 디렉토리 return
                return;
            }
            File[] fileArray = sourceFile.listFiles(); // sourceFile 의 하위 파일 리스트
            for (int i = 0; i < fileArray.length; i++) {
                zipEntry(fileArray[i], sourcePath, zos); // 재귀 호출
            }
        } else { // sourcehFile 이 디렉토리가 아닌 경우
            BufferedInputStream bis = null;

            try {
                String sFilePath = sourceFile.getPath();
                //String zipEntryName = sFilePath.substring(sourcePath.length() + 1, sFilePath.length());
                StringTokenizer tok = new StringTokenizer(sFilePath,"/");

                int tok_len = tok.countTokens();
                String zipEntryName=tok.toString();
                while(tok_len != 0){
                    tok_len--;
                    zipEntryName = tok.nextToken();
                }
                bis = new BufferedInputStream(new FileInputStream(sourceFile));

                ZipEntry zentry = new ZipEntry(zipEntryName);
                zentry.setTime(sourceFile.lastModified());
                zos.putNextEntry(zentry);

                byte[] buffer = new byte[BUFFER_SIZE];
                int cnt = 0;

                while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    zos.write(buffer, 0, cnt);
                }
                zos.closeEntry();
                // Log.i("ZipUtil", "Compressing File ( "+ sFilePath + " )" );
            } finally {
                if (bis != null) {
                    bis.close();
                }
            }
        }
    }

    /**
     * Zip 파일의 압축을 푼다.
     *
     * @param zipFile - 압축 풀 소스 Zip파일 ('경로/파일명.zip')
     * @param targetDir - 압축 푼 파일들이 저장될 타겟 디렉토리 ('경로명')
     * @param fileNameToLowerCase - 파일명을 소문자로 바꿀지 여부
     * @throws Exception
     */
    public static void unzip(String zipFile, String targetDir, boolean fileNameToLowerCase) throws Exception {
        FileInputStream fis = null;
        ZipInputStream zis = null;
        ZipEntry zentry = null;

        try {
            fis = new FileInputStream(zipFile); // FileInputStream
            zis = new ZipInputStream(fis); // ZipInputStream

            while ((zentry = zis.getNextEntry()) != null) {
                String fileNameToUnzip = zentry.getName();
                if (fileNameToLowerCase) { // fileName toLowerCase
                    fileNameToUnzip = fileNameToUnzip.toLowerCase();
                }

                File targetFile = new File(targetDir, fileNameToUnzip);

                if (zentry.isDirectory()) {// Directory 인 경우
                    //FileUtils.makeDir(targetFile.getAbsolutePath()); // 디렉토리 생성
                    File path = new File(targetFile.getAbsolutePath());
                    path.mkdirs();
                } else { // File 인 경우
                    // parent Directory 생성
                    //FileUtils.makeDir(targetFile.getParent());
                    File path = new File(targetFile.getParent());
                    path.mkdirs();
                    unzipEntry(zis, targetFile);
                }
            }
            Log.i("ZipUtil", "UnZip Finished [ " + zipFile + " ]");
        } finally {
            if (zis != null) {
                zis.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * Zip 파일의 한 개 엔트리의 압축을 푼다.
     *
     * @param zis - Zip Input Stream
     * @param targetFile - 압축 풀린 파일의 경로
     * @return
     * @throws Exception
     */
    private static File unzipEntry(ZipInputStream zis, File targetFile) throws Exception {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);

            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = zis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
//            Log.i("ZipUtil", "Decompressing file (" + targetFile.getName() + " )");
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return targetFile;
    }
}
