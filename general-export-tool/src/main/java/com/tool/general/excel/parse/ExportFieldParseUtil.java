package com.tool.general.excel.parse;

import com.tool.general.excel.common.constants.ParamRuleConstant;
import com.tool.general.excel.common.exception.GenExcelBusinessException;
import com.tool.general.excel.parse.model.ExportFieldParseModel;
import com.tool.general.excel.util.GenExcelToolUtils;
import com.tool.general.excel.util.GenExcelStringPool;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 导出模板表达式解析工具类
 *
 * @author mengqiang
 */
public class ExportFieldParseUtil {

    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;

    /**
     * 解析模板配置的导出字段表达式- 获得导出标题与字段规则
     * 示例：user_id?title=用户ID|status?title=状态&replace=1_启用,2_停用|username?title=用户名称
     *
     * @param fieldsExp 字段表达式集
     * @author mengqiang
     * @date 2019-11-24
     */
    public static ExportFieldParseModel getHeadAndFiledRule(String fieldsExp) {
        if (GenExcelToolUtils.isBlank(fieldsExp)) {
            return ExportFieldParseModel.initDefault();
        }
        //分隔符 |
        return getHeadAndFiledRule(fieldsExp.split(GenExcelStringPool.PIPE_ESCAPE));
    }

    /**
     * 解析模板配置的导出字段表达式- 获得导出标题与字段规则
     * 示例：user_id?title=用户ID|status?title=状态&replace=1_启用,2_停用|username?title=用户名称
     *
     * @param fieldsArray 字段表达式集
     * @author mengqiang
     * @date 2019-11-24
     */
    public static ExportFieldParseModel getHeadAndFiledRule(String[] fieldsArray) {
        Map<String, String> excelHeadMap = new LinkedHashMap<>(fieldsArray.length);
        Map<String, Map<String, Object>> fieldRuleMap = new HashMap<>(fieldsArray.length);
        for (String field : fieldsArray) {
            FiledParseModel filedParseModel = ExportFieldParseUtil.parseFiled(field);
            if (GenExcelToolUtils.isBlank(filedParseModel.getFiledName())) {
                continue;
            }
            //标题
            if (filedParseModel.getFileRuleMap().containsKey(ParamRuleConstant.TITLE)) {
                excelHeadMap.put(filedParseModel.getFiledName(), String.valueOf(filedParseModel.getFileRuleMap().get(ParamRuleConstant.TITLE)));
            } else {
                excelHeadMap.put(filedParseModel.getFiledName(), filedParseModel.getFiledName());
            }
            //当前参数字段规则
            fieldRuleMap.put(filedParseModel.getFiledName(), filedParseModel.getFileRuleMap());
        }
        return new ExportFieldParseModel(excelHeadMap, fieldRuleMap);
    }

    /**
     * 解析列属性
     *
     * @param columnExp 列内容,支持规则与URL一致
     * @return
     */
    private static FiledParseModel parseFiled(String columnExp) {
        if (null == columnExp || GenExcelStringPool.EMPTY.equals(columnExp)) {
            return FiledParseModel.initDefault();
        }
        //去空格处理
        columnExp = columnExp.trim();
        String[] urlParts = columnExp.split(GenExcelStringPool.QUESTION_MARK_ESCAPE);
        //没有参数
        if (urlParts.length == ONE) {
            return FiledParseModel.initDefault(urlParts[ZERO]);
        }
        FiledParseModel parseModel = new FiledParseModel(urlParts[ZERO]);
        //有参数 分隔符 &
        String[] params = urlParts[ONE].split(GenExcelStringPool.AMPERSAND);
        Map<String, Object> temMap = null;
        Map<String, Object> paramRule = new HashMap<>(params.length);
        for (String param : params) {
            //分隔符 "="
            String[] keyValue = param.split(GenExcelStringPool.EQUAL_SIGN);
            //必须同时拥有key与value
            if (keyValue.length != TWO) {
                continue;
            }
            //替换值处理
            if (ParamRuleConstant.REPLACE.equals(keyValue[ZERO])) {
                temMap = getManyRuleMap(keyValue, temMap);
                paramRule.put(keyValue[ZERO], temMap);
            }
            //数字格式处理
            else if (ParamRuleConstant.NUM_FORMAT.equals(keyValue[ZERO])) {
                temMap = getManyRuleMap(keyValue, temMap);
                paramRule.put(keyValue[ZERO], temMap);
            } else {
                paramRule.put(keyValue[ZERO], keyValue[ONE]);
            }
        }
        parseModel.setFileRuleMap(paramRule);
        return parseModel;
    }

    /**
     * 复杂多规则处理通用Map转换方法
     */
    private static Map<String, Object> getManyRuleMap(String[] keyValue, Map<String, Object> temMap) {
        String replaceRules = keyValue[ONE];
        //分隔符 ","
        String[] replaceRulesArray = replaceRules.split(GenExcelStringPool.COMMA);
        temMap = new HashMap<>(replaceRulesArray.length);
        for (String replaceRule : replaceRulesArray) {
            //分隔符"_"
            String[] split1 = replaceRule.split(GenExcelStringPool.DASH2);
            if (split1.length != TWO) {
                throw new GenExcelBusinessException("表达式格式错误,示例 : 固定规则常量_新值处理");
            }
            temMap.put(split1[ZERO], split1[ONE]);
        }

        return temMap;
    }

    private static class FiledParseModel {

        /**
         * 字段名称
         */
        private String filedName;

        /**
         * 参数规则Map
         */
        private Map<String, Object> fileRuleMap;

        private FiledParseModel() {
        }

        private FiledParseModel(String columnName) {
            this.filedName = columnName;
        }

        private static FiledParseModel initDefault() {
            FiledParseModel parseModel = new FiledParseModel();
            parseModel.setFiledName(GenExcelStringPool.EMPTY);
            parseModel.setFileRuleMap(new HashMap<>(ZERO));
            return parseModel;
        }

        private static FiledParseModel initDefault(String columnName) {
            FiledParseModel parseModel = new FiledParseModel();
            parseModel.setFiledName(columnName);
            parseModel.setFileRuleMap(new HashMap<>(ZERO));
            return parseModel;
        }

        public String getFiledName() {
            return filedName;
        }

        public void setFiledName(String filedName) {
            this.filedName = filedName;
        }

        public Map<String, Object> getFileRuleMap() {
            return fileRuleMap;
        }

        public void setFileRuleMap(Map<String, Object> fileRuleMap) {
            this.fileRuleMap = fileRuleMap;
        }
    }
}