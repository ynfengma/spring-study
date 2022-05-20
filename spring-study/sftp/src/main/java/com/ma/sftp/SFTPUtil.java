//package com.ma.sftp;
//
//import com.jcraft.jsch.Channel;
//import com.jcraft.jsch.ChannelSftp;
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.Session;
//import com.jcraft.jsch.SftpException;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Properties;
//import java.util.concurrent.TimeUnit;
//
//public class SFTPUtil {
//
//    /**
//     * 下载重试次数
//     */
//    private static final int DOWNLOAD_RETRY = 3;
//
//    /**
//     * 下载重试间隔时间 单位毫秒
//     */
//    private static final long DOWNLOAD_SLEEP = 3 * 1000;
//    private static final SFTPUtil SFTP = new SFTPUtil();
//    private static ChannelSftp client;
//    private static Session session;
//
//    /**
//     * @return
//     */
//    public static SFTPUtil getInstance() {
//        return SFTP;
//    }
//
//    /**
//     * 获取SFTP连接
//     *
//     * @param username
//     * @param password
//     * @param ip
//     * @param port
//     * @return
//     */
//    synchronized public ChannelSftp makeConnection(String username, String password, String ip, int port) {
//        if (client == null || session == null || !client.isConnected() || !session.isConnected()) {
//            try {
//                JSch jsch = new JSch();
//                session = jsch.getSession(username, ip, port);
//                if (password != null) {
//                    session.setPassword(password);
//                }
//                Properties config = new Properties();
//                // 设置第一次登陆的时候主机公钥确认提示，可选值：(ask | yes | no)
//                config.put("StrictHostKeyChecking", "no");
//                session.setConfig(config);
//                session.connect();
//                //sftp协议
//                Channel channel = session.openChannel("sftp");
//                channel.connect();
//                client = (ChannelSftp) channel;
//            } catch (JSchException e) {
//                e.printStackTrace();
//            }
//        }
//        return client;
//    }
//
//    /**
//     *
//     * 关闭连接 server
//     */
//    public static void close() {
//        if (client != null && client.isConnected()) {
//            client.disconnect();
//        }
//        if (session != null && session.isConnected()) {
//            session.disconnect();
//        }
//    }
//    /**
//     * 单次下载文件
//     *
//     * @param downloadFile 下载文件地址
//     * @param saveFile     保存文件地址
//     * @param ip           主机地址
//     * @param port         主机端口
//     * @param username     用户名
//     * @param password     密码
//     * @param rootPath     根目录
//     * @return
//     */
//    public synchronized static File download(String downloadFile, String saveFile, String ip, Integer port, String username, String password, String rootPath) {
//        boolean result = false;
//        File file = null;
//        Integer i = 0;
//        while (!result) {
//            //获取连接
//            ChannelSftp sftp = getInstance().makeConnection(username, password, ip, port);
//            FileOutputStream fileOutputStream = null;
//            try {
//                sftp.cd(rootPath);
//                file = new File(saveFile);
//                if (file.exists()) {
//                    file.delete();
//                } else {
//                    file.createNewFile();
//                }
//                fileOutputStream = new FileOutputStream(file);
//                sftp.get(downloadFile, fileOutputStream);
//                result = true;
//            } catch (FileNotFoundException e) {
//            } catch (IOException e) {
//            } catch (SftpException e) {
//                i++;
//                if (i > DOWNLOAD_RETRY) {
//                    return file;
//                }
//                try {
//                    TimeUnit.MILLISECONDS.sleep(DOWNLOAD_SLEEP);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//            } finally {
//                try {
//                    fileOutputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            SFTPUtil.close();
//
//        }
//        return file;
//    }
//    /**
//     * 下载文件
//     *
//     * @param downloadFile 下载文件的路径
//     * @param saveFile     保存的路径
//     * @param rootPath     根目录
//     * @return
//     */
//    public synchronized static File download(String downloadFile, String saveFile, String rootPath) {
//        boolean result = false;
//        File file = null;
//        Integer i = 0;
//        while (!result) {
//            FileOutputStream fileOutputStream = null;
//            try {
//                //获取连接、读取文件(ChannelSftp) session.openChannel("sftp")
//                client.cd(rootPath);
//                file = new File(saveFile);
//                if (file.exists()) {
//                    file.delete();
//                } else {
//                    file.createNewFile();
//                }
//                fileOutputStream = new FileOutputStream(file);
//                client.get(downloadFile, fileOutputStream);
//                result = true;
//            } catch (FileNotFoundException e) {
//            } catch (IOException e) {
//            } catch (SftpException e) {
//                i++;
//                if (i > DOWNLOAD_RETRY) {
//                    return file;
//                }
//                try {
//                    TimeUnit.MILLISECONDS.sleep(DOWNLOAD_SLEEP);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//            } finally {
//                try {
//                    if (fileOutputStream != null) {
//                        fileOutputStream.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return file;
//    }
//
//
//    public static void main(String[] args) {
//        SFTPUtil.getInstance().makeConnection("ftp", "ftp", "127.0.0.1", 8008);
//    }
//
//
//}
