package com.codetiger.we.utils;


import com.codetiger.we.WeTalkApp;

/**
 * 描述：获取文件资源工具类
 *
 */

public class ResUtils {
    /* 获取文件资源 */
    public static String getString(int strId) {
        return WeTalkApp.getContext().getResources().getString(strId);
    }
}
