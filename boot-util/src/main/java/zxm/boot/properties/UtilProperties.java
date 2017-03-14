/**
 * 
 */
package zxm.boot.properties;

import org.apache.commons.lang3.StringUtils;

import java.util.ResourceBundle;

public class UtilProperties {

	private static String SYSPARAM_FILE = "util";
	private static ResourceBundle BUNDLE = ResourceBundle.getBundle(SYSPARAM_FILE);
	
	/**
	 * 获得系统参数
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return BUNDLE.getString(key);
	}
	
	/**
	 * 获得系统参数
	 * @param key
	 * @return
	 */
	public static String getProperty(String key, String defaultValue) {
		String s = BUNDLE.getString(key);
		if(StringUtils.isBlank(s)){
			return defaultValue;
		}
		return s;
	}
	
	public static boolean getBooleanProperty(String key){
		return Boolean.parseBoolean(getProperty(key));
	}
}
