package com.wcl.toutiao.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ViewObject 
 * @Description: 视图展示对象
 * @author Lulu
 * @date 2017年11月23日 上午9:12:42 
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }
    
    public Object get(String key) {
        return objs.get(key);
    }
}
