package com.seatcheck.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: wjj
 * @date:
 * @time:
 * @description:
 */

public class Node {
    //行
    private int row;
    //列
    private int column;
    //学号
    private String Snum;
    //双亲节点
    private Node parent;
    //还在节点
    private LinkedList<Node> child = new LinkedList<Node>();
    //父亲学号
    private String parentInfo;
    //还在学号
    private List<String> childInfo;

    //所在的树号
    private int TreeNum = 0;
    //指令
    private String instruction;

    public Node() {

    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int cloumn) {
        this.column = cloumn;
    }

    public String getSnum() {
        return Snum;
    }

    public void setSnum(String snum) {
        Snum = snum;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public LinkedList<Node> getChild() {
        return child;
    }

    public void setChild(LinkedList<Node> child) {
        this.child = child;
    }

    public int getTreeNum() {
        return TreeNum;
    }

    public void setTreeNum(int treeNum) {
        TreeNum = treeNum;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getParentInfo() {
        return parentInfo;
    }

    public void setParentInfo(String parentInfo) {
        this.parentInfo = parentInfo;
    }

    public List<String> getChildInfo() {
        return childInfo;
    }

    public void setChildInfo(List<String> childInfo) {
        this.childInfo = childInfo;
    }

    public Node(int row, int column, String snum) {
        this.row = row;
        this.column = column;
        Snum = snum;
    }

    public Node(int row, int column, String snum, String instruction) {
        this.row = row;
        this.column = column;
        Snum = snum;
        this.instruction = instruction;
    }

    public Node(int row, int column, String snum, String parentInfo, List<String> childInfo, String instruction) {
        this.row = row;
        this.column = column;
        Snum = snum;
        this.parentInfo = parentInfo;
        this.childInfo = childInfo;
        this.instruction = instruction;
    }
}
