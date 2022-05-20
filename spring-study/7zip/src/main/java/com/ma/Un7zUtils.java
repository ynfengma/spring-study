package com.ma;


import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;

import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;

public class Un7zUtils {


    /**
     *
     * @Description (解压7z)
     * @param file7zPath(7z文件路径)
     * @param outPutPath(解压路径)
     * @param passWord(文件密码.没有可随便写,或空)
     * @return
     * @throws Exception
     */
    public static int un7z(String file7zPath, final String outPutPath, String passWord) throws Exception {
        IInArchive archive;
        RandomAccessFile randomAccessFile;
        randomAccessFile = new RandomAccessFile(file7zPath, "r");
        archive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile), passWord);
        int numberOfItems = archive.getNumberOfItems();
        ISimpleInArchive simpleInArchive = archive.getSimpleInterface();
        for (final ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
            final int[] hash = new int[] { 0 };
            if (!item.isFolder()) {
                ExtractOperationResult result;
                final long[] sizeArray = new long[1];
                result = item.extractSlow(new ISequentialOutStream() {
                    @Override
                    public int write(byte[] data) throws SevenZipException {
                        try {
                            String str = item.getPath();
                            System.out.println(File.separator);
                            str = str.substring(0, str.lastIndexOf(File.separator));
                            File file = new File(outPutPath + File.separator + str + File.separator);
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            File file1 = new File(outPutPath + File.separator + item.getPath());
                            IOUtils.write(data, new FileOutputStream(file1, true));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        hash[0] ^= Arrays.hashCode(data); // Consume data
                        sizeArray[0] += data.length;
                        return data.length; // Return amount of consumed
                    }
                }, passWord);
                if (result == ExtractOperationResult.OK) {
                    File fi = new File("/root/test/"+passWord+".txt");
                    fi.createNewFile();
                    System.out.println("解压成功...." + String.format("%9X | %10s | %s ", hash[0], sizeArray[0], item.getPath()));
                } else {
                    System.out.println("解压失败：密码错误或者其他错误...." + result);
                }
            }
        }
        archive.close();
        randomAccessFile.close();

        return numberOfItems;
    }


    /**
     * 递归删除文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // 删除文件
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File[] files = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件用这个方法进行迭代
                }
                file.delete(); // 删除文件夹
            }
        }
    }

    public static void main(String[] args){
            getPassword();
    }

    private static void getPassword()  {

        String ALLCHAR =  "1234567890lwaimyfneg";
//        String ALLCHAR =  "1234567890";
        char[] chars = ALLCHAR.toCharArray();
        int length = chars.length;

        //nohup java -jar 7zip-full.jar >msg.log 2>&1 &;

        for(int a = 0;a<length;a++) {
            StringBuffer sb = new StringBuffer();
            sb.append(chars[a]);
            for( int b = 0;b<length;b++){
                for (int c = 0; c < length; c++) {
                    for (int d = 0; d < length; d++) {
                        for (int e = 0; e < length; e++) {
                            for (int f = 0; f < length; f++) {
                                for (int g = 0; g < length; g++) {
                                    for (int h = 0; h < length; h++) {
                                        for (int i = 0; i < length; i++) {
                                            for (int j = 0; j < length; j++) {
                                                for (int k = 0; k < length; k++) {
                                                    for (int l = 0; l < length; l++) {
                                                        for (int m = 0; m < length; m++) {
                                                            for (int n = 0; n < length; n++) {

                                                                try {
                                                                    String password = sb.toString() + chars[b] + chars[c] + chars[d]
                                                                            + chars[e] + chars[f]  + chars[g]+ chars[h]+ chars[i]
                                                                            + chars[j]+ chars[k]+ chars[l]+ chars[m]+ chars[n];
                                                                    int i1 = Un7zUtils.un7z("/root/zp.7z", "/root/", password);
                                                                } catch (Exception dfafa) {
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}