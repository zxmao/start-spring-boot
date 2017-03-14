/**
 * 
 */
package zxm.boot.handler;

import com.google.common.collect.Lists;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class StringHandler {
	private static final Logger logger = LoggerFactory.getLogger(StringHandler.class);
	
	/**
	 * 扰身份证信息
	 * 
	 * @param s
	 * @return
	 */
	public static String fuzzyIdCard(String s){
		StringBuffer buffer = new StringBuffer();
		int length = s.length();
		buffer.append(s.substring(0, 4));
		for (int i = 0; i < length - 8; i++) {
			buffer.append("*");
		}
		
		buffer.append(s.substring(length-4));
		return buffer.toString();
	}
	
	/**
	 * 扰手机号码
	 * @param s
	 * @return
	 */
	public static String fuzzyPhoneNum(String s){
		StringBuffer buffer = new StringBuffer();
		int length = s.length();
		buffer.append(s.substring(0, 2));
		for (int i = 0; i < length - 5; i++) {
			buffer.append("*");
		}
		
		buffer.append(s.substring(length-3));
		return buffer.toString();
	}
	
	/**
	 * 按位生成code
	 * @param count int 
	 * @return 验证码/邀请码
	 */
	public static String getCode(int count){
		StringBuffer buf = new StringBuffer();
		Random ran = new Random();
		for(int i=0;i<count;i++){
			buf.append(ran.nextInt(10));
		}
		
		return buf.toString();
	}
	
	/**
	 * 按位生成code
	 * @param count int 
	 * @return 验证码/邀请码
	 */
	static final String[] codeStr = {"2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","J","K","L","M",
			"N","P","Q","R","S","T","U","V","W","X","Y","Z"};
	public static String getComplexCode(int count){
		StringBuffer buf = new StringBuffer();
		Random ran = new Random();
		for(int i=0;i<count;i++){
			buf.append(codeStr[ran.nextInt(codeStr.length)]);
		}
		
		return buf.toString();
	}
	
	public static String randomUUID(){
		UUID id = UUID.randomUUID();
		String idStr = id.toString().replaceAll("-", "");
		return idStr;
	}

	public static String list2Str(List<?> list, String separator) {
		if(list == null || list.size() <= 0){
			return null;
		}
		
		StringBuffer buffer = new StringBuffer();
		int length = list.size() - 1;
		for (int i = 0; i < length; i++) {
			if(StringUtils.isNotBlank(list.get(i).toString())){
				buffer.append(list.get(i).toString());
				buffer.append(separator);
			}
		}
		buffer.append(list.get(length));
		return buffer.toString();
	}
	
	public static String array2Str(String separator, Object...args) {
		if(args == null || args.length <= 0){
			return "";
		}
		
		StringBuffer buffer = new StringBuffer();
		int length = args.length - 1;
		for (int i = 0; i < length; i++) {
			buffer.append(args[i].toString());
			buffer.append(separator);
		}
		buffer.append(args[length]);
		return buffer.toString();
	}
	
	public static String collection2Str(String separator, Collection<String> c) {
		if(c == null || c.size() <= 0){
			return "";
		}
		
		StringBuffer buffer = new StringBuffer();
		
		Object[] args = c.toArray();
		int length = args.length - 1;
		for (int i = 0; i < length; i++) {
			buffer.append(args[i].toString());
			buffer.append(separator);
		}
		buffer.append(args[length]);
		return buffer.toString();
	}
	  
	public static boolean isTelNum(String phoneNum){
        String pattern = "^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$";
        return patternCheck(phoneNum, pattern);      
    }
	
	public static boolean isPhoneNum(String phoneNum){
        String pattern = "^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$";
        return patternCheck(phoneNum, pattern);
    }

	public static boolean isNumber(String number) {
		Pattern pattern = Pattern.compile("[0-9]*");   
	    return pattern.matcher(number).matches();
	}
     
    public static boolean isEmail(String email){       
    	String pattern = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";  
        return patternCheck(email, pattern);  
    }
    
    public static boolean isChinese(String str){
    	if(StringUtils.isBlank(str)){
    		return false;
    	}
    	
    	String pattern = "^[\u4e00-\u9fa5],{0,}$";
        return patternCheck(str, pattern);  
    }
    
    public static boolean isURL(String urlStr){
    	String pattern = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
        return patternCheck(urlStr, pattern);  
    }
    
    public static boolean isIp(String urlStr){
    	String pattern = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
        return patternCheck(urlStr, pattern);  
    }
    
    private static boolean patternCheck(String sourceStr, String pattern){       
        Pattern p = Pattern.compile(pattern);       
        Matcher m = p.matcher(sourceStr);       
        return m.matches();       
    }

	public static boolean isContaints(String[] array, String value) {
		List<String> list = Arrays.asList(array);
		return list.contains(value);
	}

	public static String filterNull(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static String getTimestampCode() {
		return DateHandler.getCurrentTime("yyyyMMddHHmmssSSS" + getCode(3));
	}

	public static String addItem(String list, String item) {
		if(StringUtils.isBlank(list)){
			list = "";
		}
		
		String[] array = list.split(",");
		List<String> itemList = array2List(array);
		
		if(StringUtils.isBlank(item)){
			return list;
		}

		if(!itemList.contains(item)){
			itemList.add(item);
		}
		return list2Str(itemList, ",");
	} 
	
	private static List<String> array2List(String[] array) {
		List<String> list = new ArrayList<>();
		if(array == null || array.length == 0){
			return list;
		}
		
		for (String string : array) {
			list.add(string);
		}
		return list;
	}

	public static String removeItem(String list, String item){
		if(StringUtils.isBlank(item) || StringUtils.isBlank(list)){
			return list;
		}
		
		String[] array = list.split(",");
		List<String> itemList = array2List(array);
		itemList.remove(item);
		return list2Str(itemList, ",");
	}
	
	public static String getLocaleLanguage(Locale defaultLanguage) {
		String country = defaultLanguage.getCountry();
		String language = defaultLanguage.getLanguage();
		switch (country) {
		case "HANS":
			country = "CN";
			break;
		case "HANT":
			country = "TW";
			break;
		default:
			break;
		}
		
		if(!StringUtils.isBlank(country)){
			language = language + "_" + country;
		}
		return language;
	}

	/**
	 * 解密
	 * @param token
	 * @return
     */
	public static String tokenDecode(String token){
		String forBase64 = Base64.getFromBASE64(token);
		try {
			return SecurityHandler.detrypt(forBase64,SecurityHandler.DEFAULT_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密
	 * @param token
	 * @return
     */
	public static String tokenEncry(String token){
		String encryptPasswordToken = null;
		try {
			encryptPasswordToken = SecurityHandler.encrypt(token,SecurityHandler.DEFAULT_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (StringUtils.isBlank(encryptPasswordToken)){
			return null;
		}
		return Base64.getBASE64(encryptPasswordToken);
	}

	public static List<Integer> str2ForList(String idsStr){
		List<Integer> ids = new ArrayList<>();
		String[] strs = null;
		if (idsStr.contains(",")) {
			strs = idsStr.split(",");
		}
		if (strs == null) {
			ids.add(Integer.valueOf(idsStr));
		} else {
			for (int i = 0; i < strs.length; i++) {
				ids.add(Integer.valueOf(strs[i]));
			}
		}
		return ids;
	}
	
	public static String getFullName(String firstName, String lastName) {
		boolean isChinese = false;
		if(isChinese(firstName)){
			isChinese = true;
		}
		
		if(isChinese(lastName)){
			isChinese = true;
		}
		
		if(StringUtils.isNotBlank(lastName) && StringUtils.isNotBlank(firstName)){
			if(isChinese){
				return lastName + firstName;
			}else{
				return lastName + " " + firstName;
			}
		}
		
		if(StringUtils.isBlank(firstName)){
			return lastName;
		}
		
		if(StringUtils.isBlank(lastName)){
			return firstName;
		}
		return "";
	}

	public static boolean isBloodType(String blood) {
		ArrayList<String> bloodType = Lists.newArrayList("A", "B", "O", "AB");
		return bloodType.contains(blood);
	}

	public static boolean isPain(String pain) {
		ArrayList<String> painType = Lists.newArrayList("0度","Ⅰ度","Ⅱ度","Ⅲ度","Ⅳ度");
		return painType.contains(pain);
	}

	public static String replaceStr(String serviceTags) {
		if (StringUtils.isEmpty(serviceTags)) {
			return "";
		}
		if (serviceTags.contains("，")) {
			serviceTags = serviceTags.replace("，", ",");
		}
		return serviceTags;
	}

	public static List<String> strForList(String serviceTags){
		List<String> sts = new ArrayList<>();
		String[] strs = null;
		if (serviceTags.contains(",")) {
			strs = serviceTags.split(",");
		}
		if (strs == null) {
			sts.add(serviceTags);
		} else {
			for (int i = 0; i < strs.length; i++) {
				sts.add(strs[i]);
			}
		}
		return sts;
	}

	public static boolean isZip(String filePath) {
		if (StringUtils.isBlank(filePath)){
			return false;
		}else if (StringUtils.isNotBlank(filePath) && filePath.indexOf(".zip") != -1) {
			return true;
		}
		return false;
	}

	public static boolean isRar(String filePath) {

		if (StringUtils.isBlank(filePath)){
			return false;
		}else if (StringUtils.isNotBlank(filePath) && filePath.indexOf(".rar") != -1) {
			return true;
		}
		return false;
	}	
	
	/**
	 * 取第一个汉字的首字母
	 * @Title: getFirstLetter
	 * String @throws
	 */
	public static String getFirstLetter(String ChineseLanguage, HanyuPinyinCaseType caseType) {
		caseType = caseType == null ? HanyuPinyinCaseType.UPPERCASE : caseType;
		
		char[] cl_chars = ChineseLanguage.trim().toCharArray();
		String hanyupinyin = "";
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(caseType);// 输出拼音全部大写
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
		try {
			String str = String.valueOf(cl_chars[0]);
			if (str.matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
				hanyupinyin = PinyinHelper.toHanyuPinyinStringArray(cl_chars[0], defaultFormat)[0].substring(0, 1);
				;
			} else if (str.matches("[0-9]+")) {// 如果字符是数字,取数字
				hanyupinyin += cl_chars[0];
			} else if (str.matches("[a-zA-Z]+")) {// 如果字符是字母,取字母
				hanyupinyin += cl_chars[0];
			} else {// 否则不转换

			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			System.out.println("字符不能转成汉语拼音");
		}
		return hanyupinyin;
	}
	
	/**
	 * 获取汉字的首字母字符串
	 * @param chineselanguage
	 * @param caseType
	 * @return
	 */
	public static String getFirstLetters(String chineselanguage, HanyuPinyinCaseType caseType) {
		caseType = caseType == null ? HanyuPinyinCaseType.UPPERCASE : caseType;
		
		char[] cl_chars = chineselanguage.trim().toCharArray();
		String hanyupinyin = "";
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(caseType);// 输出拼音全部大写
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
		try {
			for (int i = 0; i < cl_chars.length; i++) {
				String str = String.valueOf(cl_chars[i]);
				if (str.matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
					hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0].substring(0, 1);
				} else if (str.matches("[0-9]+")) {// 如果字符是数字,取数字
					hanyupinyin += cl_chars[i];
				} else if (str.matches("[a-zA-Z]+")) {// 如果字符是字母,取字母
					hanyupinyin += cl_chars[i];
				} else {// 否则不转换
					hanyupinyin += cl_chars[i];// 如果是标点符号的话，带着
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			logger.error("字符不能转成汉语拼音");
		}
		if (caseType == HanyuPinyinCaseType.UPPERCASE && hanyupinyin.matches(".*?[a-z]+.*?")){
			hanyupinyin = hanyupinyin.toUpperCase();
		}else if (caseType == HanyuPinyinCaseType.LOWERCASE && hanyupinyin.matches(".*?[A-Z]+.*?")){
			hanyupinyin = hanyupinyin.toLowerCase();
		}
		return hanyupinyin;
	}

	/**
	 * 将文字转为汉语拼音
	 * @param chineselanguage 要转成拼音的中文
	 */
	public static String getPinyinString(String chineselanguage, HanyuPinyinCaseType caseType) {
		caseType = caseType == null ? HanyuPinyinCaseType.UPPERCASE : caseType;
		
		char[] cl_chars = chineselanguage.trim().toCharArray();
		String hanyupinyin = "";
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(caseType);// 输出拼音全部大写
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V) ;
		try {
			for (int i = 0; i < cl_chars.length; i++) {
				String str = String.valueOf(cl_chars[i]);
				if (str.matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
					hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
				} else if (str.matches("[0-9]+")) {// 如果字符是数字,取数字
					hanyupinyin += cl_chars[i];
				} else if (str.matches("[a-zA-Z]+")) {// 如果字符是字母,取字母
					hanyupinyin += cl_chars[i];
				} else {// 否则不转换
					hanyupinyin += cl_chars[i];
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			logger.error("字符不能转成汉语拼音");
		}
		return hanyupinyin;
	}

	public static boolean isFemale(String idCardNumber) {
		String num = idCardNumber.substring(idCardNumber.length() - 2, idCardNumber.length() - 1);
		int i = Integer.valueOf(num);
		if(i%2 == 0){
			return true;
		}
		return false;
	}

	public static String getBirthday(String idCardNumber) {
		String num = idCardNumber.substring(6, 14);
		Date date = DateHandler.formatDate(num, "yyyyMMdd");
		return DateHandler.getTime(date, "yyyy-MM-dd");
	}

	/**
	 * 拆分检索关键字
	 * @param keyword
	 * @return
	 */
	public static Map<String, List<String>> splitSearchKey(String keyword) {
		if(StringUtils.isBlank(keyword)){
			return new HashMap<>();
		}
		Map<String, List<String>> resultMap = new HashMap<>();
		
		List<String> chinese = new ArrayList<>();
		List<String> pyList = new ArrayList<>();
		
		int length = keyword.length();
		
		int index = 0;
		for (int i = 0; i < length; i++) {
			String s = keyword.substring(i, i+1);
			if(isChinese(s)){
				chinese.add(s);
				if(index < i){
					pyList.add(keyword.substring(index, i).toUpperCase());
				}
				index = i + 1;
			}
			
			if(i == length -1 && index <= i){
				pyList.add(keyword.substring(index).toUpperCase());
			}
		}
		
		resultMap.put("chinese", chinese);
		resultMap.put("pyList", pyList);
		return resultMap;
	}
}