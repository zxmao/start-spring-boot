//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPFile;
//import org.apache.commons.net.ftp.FTPReply;
//
//import java.io.*;
//
///**
// * @author JunXiong
// * @Date 2017/3/8
// */
//public class FtpClientConnect {
//
//    /**
//     * ftp下载数据
//     */
//    public void downFile() {
//        // ftp登录用户名
//        String userName = "it_one";
//        // ftp登录密码
//        String userPassword = "199306abc";
//        // ftp地址:直接IP地址
//        String server = "192.168.2.89";
//        // 创建的文件
//        String fileName = "8c69965630aa48f499d6e5b8e311ce48.jpg";
//        // 指定写入的目录
//        String path = "neoklinik-static//upload//upload//20170110";
//        // 指定本地写入文件
//        String localPath="D:\\";
//
//        FTPClient ftp = new FTPClient();
//        try {
//            int reply;
//            //1.连接服务器
//            ftp.connect(server);
//            //2.登录服务器 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
//            ftp.login(userName, userPassword);
//            //3.判断登陆是否成功
//            reply = ftp.getReplyCode();
//            if (!FTPReply.isPositiveCompletion(reply)) {
//                ftp.disconnect();
//            }
//            //4.指定要下载的目录
//            ftp.changeWorkingDirectory(path);// 转移到FTP服务器目录
//            //5.遍历下载的目录
//            FTPFile[] fs = ftp.listFiles();
//            for (FTPFile ff : fs) {
//                if (ff.isFile()) {
//                    byte[] bytes=ff.getName().getBytes("iso-8859-1");
//                    String fn=new String(bytes,"utf8");
//                    System.out.println(ff);
//                //解决中文乱码问题，两次解码
//
//                if (fn.equals(fileName)) {
//                    //6.写操作，将其写入到本地文件中
//                    File localFile = new File(localPath + ff.getName());
//                    OutputStream is = new FileOutputStream(localFile);
//                    ftp.retrieveFile(ff.getName(), is);
//                    is.close();
//                }
//                }
//
//            }
//            ftp.logout();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (ftp.isConnected()) {
//                try {
//                    ftp.disconnect();
//                } catch (IOException ioe) {
//                }
//            }
//        }
//    }
//
//}
