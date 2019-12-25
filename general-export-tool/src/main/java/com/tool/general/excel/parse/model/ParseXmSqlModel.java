package com.tool.general.excel.parse.model;

import org.dom4j.Node;

import java.io.Serializable;

/**
 * 解析xml动态SQL参数
 *
 * @author mengqiang
 */
public class ParseXmSqlModel implements Serializable {

    private static final long serialVersionUID = -1L;

    private String taskId;

    private Node node;

    public ParseXmSqlModel() {
    }

    public ParseXmSqlModel(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}