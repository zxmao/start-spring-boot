package zxm.boot.handler;

import zxm.boot.exc.HandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SecurityHandler {
	private static Logger logger = LoggerFactory.getLogger(SecurityHandler.class);

	public static final String DEFAULT_KEY = "default_key";

	private static final String ENCODING = "UTF-8";
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	public static String DES = "AES"; // optional value AES/DES/DESede

	public static String CIPHER_ALGORITHM = "AES"; // optional value AES/DES/DESede

	//解決 linux系统解密异常
	private static Key initKeyForAES(String key) throws NoSuchAlgorithmException {
		if (null == key || key.length() == 0) {
			throw new NullPointerException("key not is null");
		}
		SecretKeySpec key2 = null;
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(key.getBytes());
		try {
			KeyGenerator kgen = KeyGenerator.getInstance(CIPHER_ALGORITHM);
			kgen.init(128, random);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			key2 = new SecretKeySpec(enCodeFormat, CIPHER_ALGORITHM);
		} catch (NoSuchAlgorithmException ex) {
			throw new NoSuchAlgorithmException();
		}
		return key2;

	}

	public static String encrypt(String data,String key) throws Exception {
		SecureRandom sr = new SecureRandom();
		Key securekey = initKeyForAES(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		byte[] bt = cipher.doFinal(data.getBytes());
		String strs = Base64.encodeToString(bt, false);
		return strs;
	}

	public static String detrypt(String message,String key) throws Exception{
		SecureRandom sr = new SecureRandom();
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		Key securekey = initKeyForAES(key);
		cipher.init(Cipher.DECRYPT_MODE, securekey,sr);
		byte[] res = Base64.decode(message);
		res = cipher.doFinal(res);
		return new String(res);
	}

	/**
	 * SHA1加密
	 * 
	 * @param inStr
	 * @return
	 */
	public static String SHA1(String inStr) {
		MessageDigest md = null;
		String outStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(inStr.getBytes());
			outStr = byteToString(digest);
		} catch (NoSuchAlgorithmException e) {
			logger.error("执行SHA1加密失败！", e);
		}
		return outStr;
	}

	/**
	 * MD5加密
	 * 
	 * @param inStr
	 * @return
	 */
	public static String MD5(String inStr) {
		MessageDigest md = null;
		String outStr = null;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(inStr.getBytes());
			outStr = byteToString(digest);
		} catch (NoSuchAlgorithmException e) {
			logger.error("执行MD5加密失败！", e);
			throw new HandlerException(e.getMessage());
		}
		return outStr;
	}

	/**
	 * 16位MD5，取中间的16位
	 * 
	 * @param inStr
	 * @return
	 */
	public static String MD5_16(String inStr) {
		MessageDigest md = null;
		String outStr = null;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(inStr.getBytes());
			outStr = byteToString(digest).substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			logger.error("执行MD5_16加密失败！", e);
		}
		return outStr;
	}

	/**
	 * 签名处理
	 */
	public static String RSAEncrypt(String prikeyvalue, String sign_str) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getBytesBASE64(prikeyvalue));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
			// 用私钥对信息生成数字签名
			Signature signet = Signature.getInstance("MD5withRSA");
			signet.initSign(myprikey);
			signet.update(sign_str.getBytes(ENCODING));
			byte[] signed = signet.sign(); // 对信息的数字签名
			return new String(org.apache.commons.codec.binary.Base64.encodeBase64(signed));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 签名验证
	 *
	 */
	public static boolean RSADecrypt(String pubkeyvalue, String oid_str, String signed_str) {
		try {
			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(Base64.getBytesBASE64(pubkeyvalue));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
			byte[] signed = Base64.getBytesBASE64(signed_str);// 这是SignatureData输出的数字签�?
			Signature signetcheck = Signature.getInstance("MD5withRSA");
			signetcheck.initVerify(pubKey);
			signetcheck.update(oid_str.getBytes(ENCODING));
			return signetcheck.verify(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String encryptHMAC(String data, String secret) throws HandlerException {
	    try {
	        SecretKey secretKey = new SecretKeySpec(secret.getBytes(ENCODING), "HmacMD5");
	        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
	        mac.init(secretKey);
	        byte[] bytes = mac.doFinal(data.getBytes(ENCODING));
	        return byteToString(bytes);
	    } catch (GeneralSecurityException e) {
	        throw new HandlerException(e.toString());
	    } catch (UnsupportedEncodingException e) {
	    	throw new HandlerException(e.toString());
		}
	}
	
	/**
	 * 转换字符串
	 * @param digest
	 * @return
	 */
	public static String byteToString(byte[] digest) {
		int len = digest.length;
		StringBuilder buf = new StringBuilder(len * 2);
		// 把密文转换成十六进制的字符串形式
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(digest[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[digest[j] & 0x0f]);
		}
		return buf.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(MD5(MD5("report2016-2017")));
	}
}
