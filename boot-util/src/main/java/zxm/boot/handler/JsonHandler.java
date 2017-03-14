/**
 * 
 */
package zxm.boot.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class JsonHandler {
	
	public static JSONObject toJsonObject(Object obj){
		if(obj instanceof String){
			return JSON.parseObject(obj.toString());
		}
		return JSON.parseObject(JSON.toJSONString(obj));
	}
	
	public static <T> T fromJsonObject(Object jsonObject, Class<T> clazz){
		return JSON.parseObject(JSON.toJSONString(jsonObject), clazz);
	}
	
	public static JSONArray toJsonArray(List<?> list){
		return JSON.parseArray(JSON.toJSONString(list));
	}
	
	public static <T> List<T> fromJsonArray(JSONArray array, Class<T> clazz){
		return JSON.parseArray(array.toJSONString(), clazz);
	}
	
	public static <T> List<T> fromJsonArray(String str, Class<T> clazz) {
		return fromJsonArray(JSON.parseArray(str), clazz);
	}
	
	public static List<Map<String, Object>> fromJsonArray(String array){
		return fromJson(array, new TypeReference<List<Map<String, Object>>>(){});
	}
	
	public static List<Map<String, Object>> fromJsonArray(JSONArray array){
		return fromJson(array.toJSONString(), new TypeReference<List<Map<String, Object>>>(){});
	}
	
	public static <T> T fromJson(String array, TypeReference<T> reference){
		return JSON.parseObject(array, reference);
	}
	
	public static JSONObject map2Json(Map<String, Object> map){
		return JSON.parseObject(JSON.toJSONString(map));
	}
	
	public static Map<String, Object> json2Map(JSONObject jsonObject){
		return JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, Object>>(){});
	}

	public static JSONObject forToJsonObject(String obj){
		return JSONObject.parseObject(obj);
	}

	
}
