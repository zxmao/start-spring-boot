/**
 * 
 */
package zxm.boot.contant;


import zxm.boot.properties.UtilProperties;

public class SystemContant {

	public static final String STATIC_FOLDER_PATH = UtilProperties.getProperty("static.folder.url", "");
	public static final String STATIC_SERVER_PATH = UtilProperties.getProperty("static.url", "");
	public static final String SYSTEM_WEBSITE = UtilProperties.getProperty("sys.website", "");
	public static final String UPLOAD_STATIC_PATH = "/upload/";
	
	//应用市场应用名称
	public static final String APP_NAME = "新医堂预约";

	public static final String DEFAULT_RATING = "4,4,4,4";
	public static final Integer DEFAULT_PAYMENT_CODE = 3001;
	//预约过期时效（分钟）
	public static final int DEFAULT_APPOINTMENT_EXPIRED_TIME = 15;
	//退款时效时间
	public static final int REFUND_LIMIT_TIME = 72;
	
	public static final int DEFAULT_PAGE_SIZE = 7;
	public static final int DEFAULT_PAGE_NUM = 1;

	//预约类型标识符
	public static final String NEW_PATIENT = "new_patient";
	public static final String FIRST_ASKING = "first_asking";
	public static final String SECOND_ASKING = "second_asking";
	public static final String MEDICAL_EXAMINATION = "medical_examination";
	
	//系统变量
	public static int USER_EXPIRED_TIME = 15 * 60;
	public static final int ACTIVE_CODE_EXPIRED_TIME = 24 * 60 * 60;
	public static final int FORGET_PASSWORD_EXPIRED_TIME = 15 * 60;
	//短信验证成功token有效时间
	public static final int DEFAULT_SMS_TOKEN_EXPIRED_TIME = 60 * 60;
	//每一循环时间内最大短信发送条数
	public static final int DEFAULT_SMS_MAX_COUNT = 3;
	//短信有效时间
	public static final int DEFAULT_SMS_EXPIRED_TIME = 60;
	//短信验证循环周期
	public static final int DEFAULT_SMS_PRE_CYCLE_TIME = 60 * 60 * 24;
	//医生查询日程标识
	public static final String DOCTOR_FLAG_KEY = "doctor_flag_key";

	public static final String WEB_SEND_CODE_PREFIX = "WEB_";

}
