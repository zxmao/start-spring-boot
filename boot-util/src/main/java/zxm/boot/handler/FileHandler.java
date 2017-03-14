/**
 * 
 */
package zxm.boot.handler;

import zxm.boot.contant.SystemContant;
import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.UUID;

public class FileHandler {
	private static Logger logger = LoggerFactory.getLogger(FileHandler.class);

	private static final int BUFFEREDSIZE = 1024;
	
	/**
	 * 解压zip格式的压缩文件到当前文件夹
	 * 
	 * @param zipFileName
	 * @throws Exception
	 */
	public static boolean unzip(String zipFileName) {
		try {
			File f = new File(zipFileName);
			if ((!f.exists()) && (f.length() <= 0)) {
				logger.warn("需要解压的文件不存在，忽略！");
			}

			String strPath = zipFileName.replace(".zip", "") ;
			return unzip(zipFileName, strPath);
		} catch (Exception e) {
			logger.error("解压缩文件失败！", e);
		}
		return false;
	}

	/**
	 * 解压zip格式的压缩文件到指定位置
	 * 
	 * @param zipFileName
	 *            压缩文件
	 * @param extPlace
	 *            解压目录
	 * @throws Exception
	 */
	public static boolean unzip(String zipFileName, String extPlace){
		try {
			File extPlaceFile = new File(extPlace);
			if(!extPlaceFile.exists()){
				extPlaceFile.mkdirs();
			}
			
			File f = new File(zipFileName);
			if ((!f.exists()) && (f.length() <= 0)) {
				logger.warn("需要解压的文件不存在，忽略！");
				return true;
			}
			
			ZipFile zipFile = new ZipFile(zipFileName);
			String strPath, gbkPath, strtemp;
			strPath = extPlaceFile.getAbsolutePath();
			Enumeration<ZipEntry> e = zipFile.getEntries();
			while (e.hasMoreElements()) {
				ZipEntry zipEnt = e.nextElement();
				gbkPath = zipEnt.getName();
				if (zipEnt.isDirectory()) {
					strtemp = strPath + File.separator + gbkPath;
					File dir = new File(strtemp);
					dir.mkdirs();
					continue;
				} else {
					// 读写文件
					InputStream is = zipFile.getInputStream(zipEnt);
					BufferedInputStream bis = new BufferedInputStream(is);
					gbkPath = zipEnt.getName();
					strtemp = strPath + File.separator + gbkPath;

					// 建目录
					String strsubdir = gbkPath;
					for (int i = 0; i < strsubdir.length(); i++) {
						if (strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
							String temp = strPath + File.separator + strsubdir.substring(0, i);
							File subdir = new File(temp);
							if (!subdir.exists())
								subdir.mkdir();
						}
					}
					FileOutputStream fos = new FileOutputStream(strtemp);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					int c;
					while ((c = bis.read()) != -1) {
						bos.write((byte) c);
					}
					bos.close();
					fos.close();
				}
			}
			return true;
		} catch (Exception e) {
			logger.error("解压缩文件失败！", e);
		}
		return false;
	}

	/**
	 * 压缩zip格式的压缩文件
	 * 
	 * @param inputFilename
	 *            压缩的文件或文件夹及详细路径
	 * @param zipFilename
	 *            输出文件名称及详细路径
	 * @throws IOException
	 */
	public static boolean zip(String inputFilename, String zipFilename) {
		return zip(new File(inputFilename), zipFilename);
	}

	/**
	 * 压缩zip格式的压缩文件
	 * 
	 * @param inputFile
	 *            需压缩文件
	 * @param zipFilename
	 *            输出文件及详细路径
	 * @throws IOException
	 */
	public static boolean zip(File inputFile, String zipFilename){
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(zipFilename));
			return zip(inputFile, out, "");
		} catch (IOException e) {
			logger.error("压缩zip文件异常！", e);
		} finally {
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					logger.error("关闭zip输出流异常！", e);
				}
			}
		}
		return false;
	}

	/**
	 * 压缩zip格式的压缩文件
	 * 
	 * @param inputFile
	 *            需压缩文件
	 * @param out
	 *            输出压缩文件
	 * @param base
	 *            结束标识
	 * @throws IOException
	 */
	private static boolean zip(File inputFile, ZipOutputStream out, String base) {
		FileInputStream in = null;
		try {
			if (inputFile.isDirectory()) {
				File[] inputFiles = inputFile.listFiles();
				if(inputFiles == null){
					return true;
				}
				
				out.putNextEntry(new ZipEntry(base + "/"));
				base = base.length() == 0 ? "" : base + "/";
				for (int i = 0; i < inputFiles.length; i++) {
					zip(inputFiles[i], out, base + inputFiles[i].getName());
				}
			} else {
				if (base.length() > 0) {
					out.putNextEntry(new ZipEntry(base));
				} else {
					out.putNextEntry(new ZipEntry(inputFile.getName()));
				}
				in = new FileInputStream(inputFile);
				int c;
				byte[] by = new byte[BUFFEREDSIZE];
				while ((c = in.read(by)) != -1) {
					out.write(by, 0, c);
				}
			}
			return true;
		} catch (IOException e) {
			logger.error("压缩文件异常！", e);
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					logger.error("关闭文件输入流失败！");
				}
			}
		}
		return false;
	}

	public static void unrar(String srcPath){  
        File srcFile = new File(srcPath);
        String unrarPath = srcPath.replace(".rar", "") ;
  
        unrar(srcFile, unrarPath);  
    }  
  
    public static void unrar(File srcFile,String unrarPath){  
        FileOutputStream fileOut = null;  
        Archive rarfile = null;  
  
        try{  
        	File unrarPathFile = new File(unrarPath);
        	if(!unrarPathFile.exists()){
        		unrarPathFile.mkdirs();
        	}
        	unrarPath = unrarPathFile.getAbsolutePath();
        	
            rarfile = new Archive(srcFile);  
            FileHeader fh = rarfile.nextFileHeader();  
            while(fh != null){  
  
                String entrypath = "";  
                if(fh.isUnicode()){//解決中文乱码  
                    entrypath = fh.getFileNameW().trim();  
                }else{  
                    entrypath = fh.getFileNameString().trim();  
                }  
                entrypath = entrypath.replaceAll("\\\\", "/");  
  
                File file = new File(unrarPath + File.separator + entrypath);
  
                if(fh.isDirectory()){  
                    file.mkdirs();  
                }else{  
                    File parent = file.getParentFile();  
                    if(parent!=null && !parent.exists()){  
                        parent.mkdirs();  
                    }  
                    fileOut = new FileOutputStream(file);  
                    rarfile.extractFile(fh, fileOut);  
                    fileOut.close();  
                }  
                fh = rarfile.nextFileHeader();  
            }  
            rarfile.close();  
  
        } catch (Exception e) {  
        	logger.error("解压缩文件异常！", e);
        } finally {  
            if (fileOut != null) {  
                try {  
                    fileOut.close();  
                    fileOut = null;  
                } catch (Exception e) {  
                    logger.error("关闭数据流异常！");
                }  
            }  
            if (rarfile != null) {  
                try {  
                    rarfile.close();  
                    rarfile = null;  
                } catch (Exception e) {  
                	logger.error("关闭数据流异常！");
                }  
            }  
        }  
    }
    
    public static String staticFileUpload(File file){
    	if(file == null){
    		return null;
    	}
    	
    	String myFileName = file.getName();
    	String fileName = UUID.randomUUID().toString() + myFileName.substring(myFileName.lastIndexOf('.'));
    	
    	String pathPrefix = SystemContant.STATIC_FOLDER_PATH;
    	String path = SystemContant.UPLOAD_STATIC_PATH + DateHandler.getCurrentTime("yyyyMMdd") + "/";
    	File localFile = new File(pathPrefix + path);
    	if(!localFile.exists()){
    		logger.debug("文件目录不存在，即时创建！");
    		localFile.mkdirs();
    	}
    	localFile = new File(pathPrefix + path + fileName);
    	
    	FileOutputStream fos = null;
    	BufferedInputStream bis = null;
    	try {
    		logger.info("上传静态文件操作：{} --> {}", file.getAbsolutePath(), localFile.getAbsolutePath());
			fos = new FileOutputStream(localFile);
			bis = new BufferedInputStream(new FileInputStream(file));
			
			byte[] b = new byte[1024];
			while(bis.read(b) != -1){
				fos.write(b);
			}
			return path + fileName;
		} catch (IOException e) {
			logger.error("文件上传静态服务器失败", e);
		}finally {
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					logger.error("关闭数据流失败！");
				}
			}
			if(bis != null){
				try {
					bis.close();
				} catch (IOException e) {
					logger.error("关闭数据流失败！");
				}
			}
		}
    	return null;
    }
    

	public static void readStaticFile(String reportUrl, OutputStream os) {
		if(reportUrl == null || os == null){
			return;
		}
		
		String path = SystemContant.STATIC_FOLDER_PATH + reportUrl;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(path));
			byte[] b = new byte[1024];
			while(fis.read(b) != -1){
				os.write(b);
			}
		} catch (IOException e) {
			logger.error("读取文件异常！", e);
		}finally {
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					logger.error("关闭数据流失败！");
				}
			}
		}
		
	}
    
    public static void main(String[] args) {
    	/*unrar("d:\\work_detail.rar");
    	zip("d:\\work_detail", "d:\\work_detail.zip");*/
    	unzip("d:\\work_detail.zip");
	}

	/**
	 * 以指定的编码读一个文本文件到String中
	 *
	 * @param filename
	 *            待读取的文件名，必须是绝对路径
	 * @param charset 编码
	 *            文件编码
	 * @return 如果文件成功读取，就返回文件内容
	 */
	public static String readFile(final String filename, final String charset) {
		return readFileToStringBuffer(filename, charset).toString();
	}

	/**
	 * 以指定的编码读一个文本文件到StringBuffer中
	 *
	 * @param filename
	 *            待读取的文件名，必须是绝对路径
	 * @param charset 编码
	 *            文件编码
	 * @return 如果文件成功读取，就返回文件内容
	 */
	public static StringBuffer readFileToStringBuffer(final String filename,
																										final String charset) {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
							filename), charset));
			;
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return sb;
	}
}
