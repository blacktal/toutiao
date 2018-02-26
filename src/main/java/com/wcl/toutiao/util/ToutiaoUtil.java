package com.wcl.toutiao.util;

import java.security.MessageDigest;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: ToutiaoUtil 
 * @Description: 工具类
 * @author Lulu
 * @date 2017年11月24日 下午5:44:25 
 */
@Component
public class ToutiaoUtil {

    private static final Logger logger = LoggerFactory.getLogger(ToutiaoUtil.class);
    
    /**
     * @Fields IMAGE_EXT : 支持的图片扩展名
     */
    public static final String[] IMAGE_EXT = new String[]{"jpg","jpeg","png","bmp"};
    
    /**
     * @Fields IMAGE_DIR : 上传图片保存位置  
     */
    public static final String IMAGE_DIR = "C:/Users/Lulu/Pictures/Saved Pictures/";
    
    /**
     * @Fields TOUTIAO_DOMAIN : toutiao程序的域名
     */
    public static final String TOUTIAO_DOMAIN = "http://127.0.0.1:8080/";
    
    public static String md5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }
    
    //这里用了阿里的fastJSON包，因为解析速度更快一些，官方也有org.json.JSONObject.jar包可用
    //code:错误代码。0代表正常，1或其他数字代表不同的异常
    public static String getJSONString(String code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }
    
    public static String getJSONString(int code, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }
    
    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }
    
    public static boolean isImageAllowed(String fileExt) {
        for (String ext : IMAGE_EXT) {
            if (fileExt.equals(ext)) {
                return true;
            }
        }
        return false;
    }
}
