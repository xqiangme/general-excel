package com.tool.general.excel.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 字符串工具类
 *
 * @author mengqiang
 */
public class GenExcelToolUtils {

    private GenExcelToolUtils() {
    }

    /**
     * 字符是否为空
     *
     * @return 是否为空
     * @author mengqiang
     */
    public static boolean isBlank(CharSequence content) {
        int strLen;
        if (content != null && (strLen = content.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(content.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 字符串是否不为空
     *
     * @param content 字符
     * @return 是否不为空
     * @author mengqiang
     */
    public static boolean isNotBlank(CharSequence content) {
        return !GenExcelToolUtils.isBlank(content);
    }

    /**
     * 替换换行
     *
     * @param content 字符
     * @param replace 替换值
     * @author mengqiang
     */
    public static String replaceNewline(String content, String replace) {
        if (isBlank(content)) {
            return "";
        }
        //替换换行
        return content.replaceAll("(\\r\\n|\\n|\\n\\r)", replace);
    }


    /**
     * 异常转字符串
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

}