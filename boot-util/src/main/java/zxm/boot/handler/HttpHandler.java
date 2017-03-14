/**
 * 
 */
package zxm.boot.handler;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpHandler {
	private static Logger logger = LoggerFactory.getLogger(HttpHandler.class); // 日志记录
	
	private static int SOCKET_TIMEOUT = 10000;
	private static int CONNECT_TIMEOUT = 30000;

	public static String getRemoteHost(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}

	/**
	 * httpPost
	 * 
	 * @param url
	 *            路径
	 * @param jsonParam
	 *            参数
	 * @return
	 */
	public static JSONObject httpPost(String url, JSONObject jsonParam) {
		return httpPost(url, jsonParam, null, false);
	}
	
	/**
	 * httpPost
	 * 
	 * @param url
	 *            路径
	 *            参数
	 * @return
	 */
	public static JSONObject httpPost(String url, Map<String, String> paramMap) {
		return httpPost(url, null, paramMap, false);
	}
	
	public static JSONObject httpPost(String url, Map<String, String> paramMap, boolean noNeedResponse) {
		return httpPost(url, null, paramMap, noNeedResponse);
	}

	/**
	 * post请求
	 * 
	 * @param url
	 *            url地址
	 * @param jsonParam
	 *            参数
	 * @param noNeedResponse
	 *            不需要返回结果
	 * @return
	 */
	public static JSONObject httpPost(String url, JSONObject jsonParam, Map<String, String> paramMap,
			boolean noNeedResponse) {
		JSONObject jsonResult = null;
		
		CloseableHttpResponse result = null;
		try {
			// post请求返回结果
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			logger.info("发起post请求，地址为{}", url);
			HttpPost method = new HttpPost(url);
			if (null != jsonParam) {
				// 解决中文乱码问题
				StringEntity entity = new StringEntity(
						jsonParam.toJSONString(), "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				method.setEntity(entity);
			}else if(paramMap != null){
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				for (String key : paramMap.keySet()) {
					formparams.add(new BasicNameValuePair(key, paramMap.get(key)));
				}
				HttpEntity entity = new UrlEncodedFormEntity(formparams, "utf-8");
				method.setEntity(entity);
			}
			
			url = URLDecoder.decode(url, "UTF-8");
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(3000).setConnectTimeout(2000).build();// 设置请求和传输超时时间
			method.setConfig(requestConfig);
			result = httpClient.execute(method);
			/** 请求发送成功，并得到响应 **/
			logger.info("请求结果状态码为{}", result.getStatusLine().getStatusCode());
			if (result.getStatusLine().getStatusCode() == 200) {
				/** 读取服务器返回过来的json字符串数据 **/
				String str = EntityUtils.toString(result.getEntity());
				if (noNeedResponse) {
					return null;
				}
				/** 把json字符串转换成json对象 **/
				jsonResult = JSONObject.parseObject(str);
				logger.info("post请求成功返回,{}", jsonResult.toJSONString());
			}
		} catch (IOException e) {
			logger.error("post请求提交失败:{}", url, e);
		}finally{
			if(result != null){
				try {
					result.close();
				} catch (IOException e) {
				}
			}
		}
		return jsonResult;
	}
	
	/**
	 * 发送get请求
	 * 
	 *            路径
	 * @return
	 */
	public static JSONObject httpGet(String strUrl) {
		// get请求返回结果
		JSONObject jsonResult = null;
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			
			URL url = new URL(strUrl);
			URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
			// 发送get请求
			HttpGet request = new HttpGet(uri);
			HttpResponse response = httpClient.execute(request);
			
			/** 请求发送成功，并得到响应 **/
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				/** 读取服务器返回过来的json字符串数据 **/
				String strResult = EntityUtils.toString(response.getEntity());
				/** 把json字符串转换成json对象 **/
				if(StringUtils.isNotBlank(strResult)){
					jsonResult = JSONObject.parseObject(strResult);
				}
				//url = URLDecoder.decode(url, "UTF-8");
			} else {
				logger.error("get请求提交失败:{}", url);
			}
		} catch (IOException e) {
			logger.error("get请求提交失败:{}", strUrl, e);
		} catch (Throwable t){
			logger.error("get请求处理失败!", t);
		}
		return jsonResult;
	}

	/**
     * post方式请求服务器(https协议)
     * 
     * @param url 请求地址
     * @param content 参数
     * @param charset 编码
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws IOException
     */
    public static byte[] httpsPost(String url, String content, String charset) {
        try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			URL console = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setDoOutput(true);
			conn.connect();
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.write(content.getBytes(charset));
			// 刷新、关闭
			out.flush();
			out.close();
			InputStream is = conn.getInputStream();
			if (is != null) {
			    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			    byte[] buffer = new byte[1024];
			    int len = 0;
			    while ((len = is.read(buffer)) != -1) {
			        outStream.write(buffer, 0, len);
			    }
			    is.close();
			    return outStream.toByteArray();
			}
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
			logger.error("发起SSL请求异常", e);
		}
        return null;
    }
    
    /**
     * https请求
     * @param url	请求地址
     * @param charset	连接编码
     * @param sslFactory	安全机制
     * @return
     */
    public static String httpsPost(String url, String xmlContent, String charset, SSLConnectionSocketFactory sslFactory) {
    	charset = StringUtils.isBlank(charset) ? "UTF-8" : charset;
    	HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslFactory).build();

		// 根据默认超时限制初始化requestConfig
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT)
				.setConnectTimeout(CONNECT_TIMEOUT).build();
		
		HttpPost httpPost = new HttpPost(url);

		logger.info("API，POST过去的数据是： {}", xmlContent);

		// 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
		StringEntity postEntity = new StringEntity(xmlContent, charset);
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.setEntity(postEntity);

		// 设置请求器的配置
		httpPost.setConfig(requestConfig);

		logger.info("executing request" + httpPost.getRequestLine());
		String result = "";
		try {
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, charset);
		} catch (ParseException | IOException e) {
			logger.error("发起https post请求异常！", e);
		} finally {
			httpPost.abort();
		}
		logger.info("post返回数据为:{}", result);
		return result;
    }
    
    /**
     * post方式请求服务器(https协议)
     * 
     * @param url
     *            请求地址
     *            参数
     *            编码
     */
    public static JSONObject httpPost(String url, File file, boolean noNeedResponse) {
		JSONObject jsonResult = null;
		
		CloseableHttpResponse result = null;
		try {
			// post请求返回结果
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			logger.info("发起post请求，地址为{}", url);
			HttpPost method = new HttpPost(url);
			
			url = URLDecoder.decode(url, "UTF-8");
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(3000).setConnectTimeout(2000).build();// 设置请求和传输超时时间
			method.setConfig(requestConfig);
			
			FileEntity entity = new FileEntity(file, ContentType.MULTIPART_FORM_DATA);
            method.setEntity(entity);  
			result = httpClient.execute(method);
			/** 请求发送成功，并得到响应 **/
			logger.info("请求结果状态码为{}", result.getStatusLine().getStatusCode());
			if (result.getStatusLine().getStatusCode() == 200) {
				/** 读取服务器返回过来的json字符串数据 **/
				String str = EntityUtils.toString(result.getEntity());
				if (noNeedResponse) {
					return null;
				}
				/** 把json字符串转换成json对象 **/
				jsonResult = JSONObject.parseObject(str);
				logger.info("post请求成功返回,{}", jsonResult.toJSONString());
			}
		} catch (IOException e) {
			logger.error("post请求提交失败:{}", url, e);
		}finally{
			if(result != null){
				try {
					result.close();
				} catch (IOException e) {
				}
			}
		}
		return jsonResult;
	}
    
    private static class TrustAnyTrustManager implements X509TrustManager {
    	 
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
 
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
 
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }
 
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

	public static void httpGet(String url, OutputStream os) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			// 发送get请求
			HttpGet request = new HttpGet(url);
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				response.getEntity().writeTo(os);
			} else {
				logger.error("get请求提交失败:{}", url);
			}
		} catch (IOException e) {
			logger.error("get请求提交失败:{}", url, e);
		}
	}
}
