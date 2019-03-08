package com.wewins.custom.forutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 类说明：<br />
 * 
 * @author binley
 * <br />
 * E-mail: wangbl@we-wins.com
 * @version 1.0
 * <br />
 * Time create:2018年1月2日 下午7:15:24
 *
 */
public class BUSaveLocalize {

	//
	// 保存数据到本地
	//

    /**
     * 根据key获取value。当查询不到key对应的数据时，返回值为null
     * @param ctx
     * @param key
     * @return
     */
    public static String getString(Context ctx, String key) {
        SharedPreferences sp = getSharedPreferences(ctx);
        return sp.getString(key, null);
    }

	
	/**
	 * 保存数据到本地
	 * @param context
	 * @param strs key,value,key,value,key...（为Key 和 Value形式，必须配对填入，且Key必须为字符串类型，Value必须为字符串、整形或者布尔类型）
	 */
	public static void save(Context context, Object...strs) {
		save(context, getSavingMap(strs));
	}

    /**
     * 保存数据到本地
     * @param context
     * @param map 且Key必须为字符串类型
     */
    public static void save(Context context, Map<String, Object> map) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        int count = 0;
        Iterator<Entry<String, Object>> iterator =  map.entrySet().iterator();
        while(iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof String) {
                editor.putString(key, (String)value);
            } else if(value instanceof Integer) {
                editor.putInt(key, (Integer)value);
            } else if(value instanceof Boolean) {
                editor.putBoolean(key, (Boolean)value);
            }
            count++;
        }
        if(count == 0) {
            return;
        }
        editor.commit();
    }
	
	/** 获取本地存储编辑器*/
	public static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
		SharedPreferences sp = getSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		return editor;
	}

	/** 获取数据存储类*/
	public static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

    /** 获取用于方法save(Context context, Map<String, Object> map)方法的map*/
    public static Map<String, Object> getSavingMap(Object...obj) {
        int length = obj.length;
        if(length < 1) return null;
        try {
            if(length % 2 != 0) {
                throw new Exception("传入的内容必须为双数");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        for(int i=0;i<length;i+=2) {
            Object o1 = obj[i];
            if(o1 instanceof String) {
                Object o2 = obj[i+1];
                if(o2 instanceof String || o2 instanceof Integer || o2 instanceof Boolean) {
                    map.put((String)o1, o2);
                } else {
                    try {
                        throw new Exception("保存的数据仅可以为：字符型、整形、布尔型");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    throw new Exception("传入的Key必须为字符串");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }
}
