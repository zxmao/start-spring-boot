/**
 * 
 */
package zxm.boot.cache;

import zxm.boot.handler.DateHandler;
import zxm.boot.handler.SecurityHandler;
import zxm.boot.handler.StringHandler;
import org.apache.commons.lang3.StringUtils;

public class CacheKey {

	public static String loginUserKey(Integer userId) {
		return "LOGIN_USER_SINGLE_" + userId;
	}
	/**
	 * 单点登录有效
	 * @param userId
	 * @return
	 */
	public static String loginUserCacheId(String userId){
		String userToken = "LOGIN_USER_" + StringHandler.randomUUID() + "_" + StringUtils.leftPad(userId, 11, "0").substring(5);
		return SecurityHandler.MD5(userToken);
	}

	public static String activeUserKey(Integer id) {
		return SecurityHandler.MD5("ACTIVE_USER_" + id);
	}
	
	public static String forgetPassword(String userId) {
		return "USER_FORGET_PASSWORD_" + DateHandler.getCurrentTime("yyyyMMddHHmmss") + "_" + StringUtils.leftPad(userId, 11, "0");
	}
	
	public static String tokenKey4WXService(){
		return SecurityHandler.MD5("NEOKLINIK_WX_TOKEN_PROD");
	}
	
	public static String jsTokenKey4WXService(){
		return SecurityHandler.MD5("NEOKLINIK_WX_JS_TOKEN_PROD");
	}
	
	public static String jsTicketKey4WX(){
		return SecurityHandler.MD5("NEOKLINIK_WX_JS_TICKET_PROD");
	}
	
	public static String wxMessageDealKey(String fromUserName, int createTime){
		return SecurityHandler.MD5("MESSAGE_DEAL_" + fromUserName + createTime);
	}
	
	public static String wxPageReqToken(String id){
		return SecurityHandler.MD5("PAGE_REQ_TOKEN_ACTIVE_" + id);
	}
	
	public static String wxPageRefreshToken(String id){
		return SecurityHandler.MD5("PAGE_REFRESH_TOKEN_ACTIVE_" + id);
	}
	
	public static String wxOpenId(String id){
		return SecurityHandler.MD5("WX_OPEN_ID_UNBIND_" + id);
	}
	
	public static String wxUnionId(String id){
		return SecurityHandler.MD5("WX_UNION_ID_UNBIND_" + id);
	}

	public static String smsCode(String telephone, String code) {
		return SecurityHandler.MD5(telephone + "_SMS_CODE_" + code);
	}

	public static String drLoToken(String loginCode) {
		return SecurityHandler.MD5(loginCode + "_DOCTOR_LOGIN_" + StringHandler.getCode(4));
	}

	public static String smsCount(String telephone) {
		return SecurityHandler.MD5(telephone + "_SMS_COUNT");
	}

	public static String smsCount(String telephone,String sendType) {
		return SecurityHandler.MD5(telephone + sendType + "_SMS_COUNT");
	}

	public static String wxPrepayId(Integer appointmentId) {
		return SecurityHandler.MD5("WX_PREPAYID_CACHE_" + appointmentId);
	}
}
