package com.fishkj.starter.term.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {
    private ObjectMapper mapper;
    public ObjectMapper getMapper() {
        return mapper;
    }
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    public JsonUtils(Include inclusion){
    	 mapper =new ObjectMapper();
         mapper.setSerializationInclusion(inclusion);
         mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES , false);
         mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
         mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
         mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
         mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
         setDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 创建输出全部属性
     * @return
     */
    public static JsonUtils buildNormalBinder(){
        return new JsonUtils(Include.ALWAYS);
    }
    /**
     * 创建只输出非空属性的
     * @return
     */
    public static JsonUtils buildNonNullBinder(){
        return new JsonUtils(Include.NON_NULL);
    }
    /**
     * 创建只输出初始值被改变的属性
     * @return
     */
    public static JsonUtils buildNonDefaultBinder(){
        return new JsonUtils(Include.NON_DEFAULT);
    }
    /**
     * 把json字符串转成对象
     * @param json
     * @param clazz
     * @return
     */
    public <T> T getJsonToObject(String json,Class<T> clazz){
        T object=null;
        if(!StringUtils.isEmpty(json)) {
            try {
                object=getMapper().readValue(json, clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object;
    }
    
    /**
     * 把json字符串转成对象
     * @param json
     * @param clazz
     * @return
     */
    public <T> T getMapToObject(Map<String, Object> map,Class<T> clazz){
        T object=null;
        if(map != null && !map.isEmpty()) {
            try {
                object = getMapper().readValue(this.toJson(map), clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object;
    }
    /**
     * 把JSON转成list
     * @param json
     * @param clazz
     * @return
     */
	public <T> List<T> getJsonToList(String json,Class<T> clazz){
    	List<T> list = null;
        if(!StringUtils.isEmpty(json)) {
           try {
        	   JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
        	   list = getMapper().readValue(json,javaType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    
    /**
     * 把map转成combo数据格式的json格式
     * @return String (json)
     */
    public String getMapToJson(Map<String,String> map) {
        List<String[]> list =new ArrayList<String[]>();
        if (null != map && !map.isEmpty()) {
            for (String key : map.keySet()) {
                String[] strS = new String[2];
                strS[0] = key;
                strS[1] = map.get(key);
                list.add(strS);
            }
        }
        return toJson(list);
    }
    
    /**
     * 把JSON转成Map
     * @param json
     * @param keyclazz
     * @param valueclazz
     * @return
     */
    public <K,V> Map<K,V> getJsonToMap(String json,Class<K> keyclazz,Class<V> valueclazz){
        Map<K,V> object=null;
        if(!StringUtils.isEmpty(json)) {
            try {
                JavaType javaType = mapper.getTypeFactory().constructParametricType(Map.class, keyclazz,valueclazz);
                object=getMapper().readValue(json,javaType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    /**
     * 把JSON转成Object
     * @param json
     * @param keyclazz
     * @param valueclazz
     * @return
     */
	public Object getJsonToObject(String json,Class<?> objclazz,Class<?> ...pclazz){
        Object object=null;
        if(!StringUtils.isEmpty(json)) {
            try {
            	JavaType javaType = mapper.getTypeFactory().constructParametricType(objclazz, pclazz);
                object=getMapper().readValue(json,javaType);
            } catch (Exception e) {
            }
        }
        return object;
    }
    /**
     * 把对象转成字符串
     * @param object
     * @return
     */
    public String toJson(Object object){
        String json=null;
        try {
            json=getMapper().writeValueAsString(object);
        }  catch (Exception e) {
        	e.printStackTrace();
        }
        return json;
    }
    /**
     * 设置日期格式
     * @param pattern
     */
    public void setDateFormat(String pattern){
        if(!StringUtils.isEmpty(pattern)){
            DateFormat df=new SimpleDateFormat(pattern);
            getMapper().setDateFormat(df);
        }
    }
    
}

