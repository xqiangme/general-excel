package com.tool.general.excel.util;

import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;

/**
 * 拷贝工具
 */
public class GenExcelBeanCopierUtil {

    /**
     * 对象属性拷贝
     */
    public static <T> T copy(Object source, Class<T> targetClass) {
        T t = null;
        try {
            t = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("GenExcelBeanCopier Exception Create new instance of %s failed: %s");
        }
        copyProperty(source, t);
        return t;
    }

    /**
     * 集合拷贝
     */
    public static <T> List<T> copyList(List orig, Class<T> dest) {
        try {

            if (orig == null || orig.size() <= 0) {
                return new ArrayList<>(0);
            }
            List<T> resultList = new ArrayList<>(orig.size());
            for (Object o : orig) {
                T destObject = dest.newInstance();
                if (o == null) {
                    continue;
                }
                copyProperty(o, destObject);
                resultList.add(destObject);
            }
            return resultList;
        } catch (Exception e) {
            return null;
        }
    }

    private static void copyProperty(Object source, Object target) {
        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }

}
