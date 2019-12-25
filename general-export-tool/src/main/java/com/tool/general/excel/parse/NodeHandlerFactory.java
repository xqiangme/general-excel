package com.tool.general.excel.parse;

import com.tool.general.excel.parse.tag.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * xml sql 标签节点解析工厂
 *
 * @author mengqiang
 */
public class NodeHandlerFactory {

    private static Map<String, BaseHandler> NODE_MAP = new ConcurrentHashMap<>();

    static {
        NODE_MAP.put("if", new IfHandler());
        NODE_MAP.put("where", new WhereHandler());
        NODE_MAP.put("foreach", new ForeachHandler());
        NODE_MAP.put("trim", new TrimHandler());
        NODE_MAP.put("choose", new ChooseHandler());
        NODE_MAP.put("when", new IfHandler());
        NODE_MAP.put("otherwise", new OtherWiseHandler());
        NODE_MAP.put("bind", new BindHandler());
    }

    public static void addNode(String nodeName, BaseHandler node) {
        NODE_MAP.put(nodeName, node);
    }

    public static BaseHandler parseNode(String nodeName) {
        return NODE_MAP.get(nodeName);
    }

}