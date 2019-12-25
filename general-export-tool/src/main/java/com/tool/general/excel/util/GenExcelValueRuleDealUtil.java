package com.tool.general.excel.util;

import cn.hutool.core.date.DateUtil;
import com.tool.general.excel.common.constants.GenExcelConstant;
import com.tool.general.excel.common.constants.GenExcelNumberConstant;
import com.tool.general.excel.common.constants.ParamRuleConstant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 参数规则处理工具类
 *
 * @author mengqiang
 */
public class GenExcelValueRuleDealUtil {


    /**
     * 根据模板设置的规则处理参数
     *
     * @param value            当前字段值
     * @param filedRuleItemMap 当前字段规则
     * @return
     */
    public static Object dealValue(Object value, Map<String, Object> filedRuleItemMap) {
        //文本替换
        if (filedRuleItemMap.containsKey(ParamRuleConstant.REPLACE)) {
            value = replaceValue(value, filedRuleItemMap);
        }
        //数字格式化
        if (filedRuleItemMap.containsKey(ParamRuleConstant.NUM_FORMAT)) {
            value = numFormatValue(value, filedRuleItemMap);
        }
        //时间格式化
        if (filedRuleItemMap.containsKey(ParamRuleConstant.DATE_FORMAT)) {
            value = dateFormatValue(value, filedRuleItemMap);
        }
        //类型转换
        if (filedRuleItemMap.containsKey(ParamRuleConstant.TYPE_PARSE)) {
            value = dataTypeParseValue(value, filedRuleItemMap);
        }
        //前缀拼接
        if (filedRuleItemMap.containsKey(ParamRuleConstant.PREFIX)) {
            value = (String) filedRuleItemMap.get(ParamRuleConstant.PREFIX) + value;
        }
        //后缀拼接
        if (filedRuleItemMap.containsKey(ParamRuleConstant.SUFFIX)) {
            value = value + (String) filedRuleItemMap.get(ParamRuleConstant.SUFFIX);
        }
        return value;
    }

    /**
     * 替换处理
     */
    private static Object replaceValue(Object value, Map<String, Object> filedRuleItemMap) {
        //文本替换
        Object replaceObj = filedRuleItemMap.get(ParamRuleConstant.REPLACE);
        if (replaceObj instanceof Map) {
            Map<String, String> replaceMap = (Map<String, String>) replaceObj;
            String replaceMapKey = String.valueOf(value);
            if (replaceMap.containsKey(replaceMapKey)) {
                value = replaceMap.get(replaceMapKey);
            }
        }
        return value;
    }

    /**
     * 数字格式化
     */
    private static Object numFormatValue(Object value, Map<String, Object> filedRuleItemMap) {
        Object numFormatObj = filedRuleItemMap.get(ParamRuleConstant.NUM_FORMAT);
        if (!(numFormatObj instanceof Map)) {
            return value;
        }
        Map<String, String> numFormatMap = (Map<String, String>) numFormatObj;
        //乘法 示例 : multiply_2
        if (numFormatMap.containsKey(ParamRuleConstant.MULTIPLY)) {
            value = GenExcelDecimalUtil.multiply(new BigDecimal(String.valueOf(value)), new BigDecimal(numFormatMap.get(ParamRuleConstant.MULTIPLY)));
        }
        //除法 示例 : divide_2
        if (numFormatMap.containsKey(ParamRuleConstant.DIVIDE)) {
            value = GenExcelDecimalUtil.divide(new BigDecimal(String.valueOf(value)), new BigDecimal(numFormatMap.get(ParamRuleConstant.DIVIDE)));
        }
        //小数格式化 示例 : format_2
        if (numFormatMap.containsKey(ParamRuleConstant.FORMAT)) {
            value = GenExcelDecimalUtil.setScale(new BigDecimal(String.valueOf(value)), Integer.parseInt(numFormatMap.get(ParamRuleConstant.FORMAT)));
        }
        return value;
    }


    /**
     * 日期格式化
     */
    private static Object dateFormatValue(Object value, Map<String, Object> filedRuleItemMap) {
        String formatValue = String.valueOf(filedRuleItemMap.get(ParamRuleConstant.DATE_FORMAT));
        if (value instanceof Date) {
            value = DateUtil.format((Date) value, formatValue);
        } else {
            //若非data类型
            String oldFormat = GenExcelConstant.DEFAULT_DATE_FORMAT;
            String newFormat = formatValue;
            //分隔符 "_"
            if (formatValue.contains(GenExcelStringPool.DASH2)) {
                String[] split = formatValue.split(GenExcelStringPool.DASH2);
                if (GenExcelNumberConstant.TWO == split.length) {
                    oldFormat = split[GenExcelNumberConstant.ZERO];
                    newFormat = split[GenExcelNumberConstant.ONE];
                }
            }
            if (GenExcelToolUtils.isNotBlank(oldFormat)) {
                value = DateUtil.format(DateUtil.parse(String.valueOf(value), oldFormat), newFormat);
            } else {
                value = DateUtil.format(DateUtil.parse(String.valueOf(value), oldFormat), (String) filedRuleItemMap.get(ParamRuleConstant.DATE_FORMAT));
            }
        }
        return value;
    }

    /**
     * 类型转换
     */
    private static Object dataTypeParseValue(Object value, Map<String, Object> filedRuleItemMap) {
        String typeValue = String.valueOf(filedRuleItemMap.get(ParamRuleConstant.TYPE_PARSE));

        //字符串 : string
        if (ParamRuleConstant.TYPE_PARSE_STRING.equalsIgnoreCase(typeValue)) {
            value = String.valueOf(value);
        }
        //字符串 : number(包括，Integer 与 Long )
        else if (ParamRuleConstant.TYPE_PARSE_NUMBER.equalsIgnoreCase(typeValue)) {
            value = Long.parseLong(String.valueOf(value));
        }

        return value;
    }

}