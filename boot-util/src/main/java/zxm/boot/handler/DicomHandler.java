/**
 * 
 */
package zxm.boot.handler;

import zxm.boot.properties.UtilProperties;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author alex.hu
 * @date Jun 2, 2016
 */
public class DicomHandler {
	static Logger logger = LoggerFactory.getLogger(DicomHandler.class);
	
	private static String DICOM_SERVER = UtilProperties.getProperty("dicom.server.path");
	private static String UPLOAD_PATH = DICOM_SERVER + "/instances";
	private static String READ_PATH = DICOM_SERVER + "/%s/file";
	
	public static JSONObject upload(File file){
		if(file == null){
			return null;
		}
		
		logger.info("上传DICOM文件：{}", file.getAbsolutePath());
		return HttpHandler.httpPost(UPLOAD_PATH, file, false);
	}
	
	public static void readInstance(String path, String outPath){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(outPath));
			readInstance(path, fos);
		} catch (FileNotFoundException e) {
			logger.error("读取dicom文件异常！", e);
		}finally {
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					logger.error("关闭数据流异常！");
				}
			}
		}
	}
	
	public static void readInstance(String path, OutputStream os){
		if(StringUtils.isBlank(path)){
			return;
		}
		 
		HttpHandler.httpGet(String.format(READ_PATH, path), os);
	}
}
