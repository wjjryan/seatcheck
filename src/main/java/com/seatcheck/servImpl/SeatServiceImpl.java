package com.seatcheck.servImpl;

import com.seatcheck.common.result.Result;
import com.seatcheck.common.result.ResultCode;
import com.seatcheck.entity.Node;
import com.seatcheck.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: wjj
 * @date:
 * @time:
 * @description:
 */

@Service
public class SeatServiceImpl implements SeatService {

    private static int maxRow = 0;
    private static int maxColumn = 0;
    private static final String ZERO = "0";
    private static final String OrderNum = "888";
    private static final int TIME = 40;

    @Autowired
    private SetOperations<String, Object> setOperations;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据座位表生成指令
     */
    @Override
    public Result buildAndSentInstruction(ArrayList<List<String>> list) {
        LinkedList<Node> nodes = ArrayTOString(list);
        LinkedList<LinkedList<Node>> forest = generateForest(nodes);
        Map<String,Object> map = generateOrders(forest);
        System.out.println(sentToRedis(map));
        return Result.success(map);
    }

    /**
     * 座位锁定
     */
    @Override
    public Result checkSeat(String orderNum,String scaner,String scanned){
        if (redisTemplate.opsForSet().isMember(orderNum,scaner) &&
                redisTemplate.opsForSet().isMember(orderNum,scanned)){
            String fatherInfo = redisTemplate.opsForHash().get(scanned,"parentInfo").toString();
            if(fatherInfo != null){
               if (fatherInfo.equals(scaner)){
                    redisTemplate.opsForHash().put(scaner,"snum",scaner + "x");
                    redisTemplate.opsForHash().put(scanned,"snum",scanned + "x");
                    return Result.success();
                }
            }
            else {
                //该处判断scanned是否为改orderNum对应导师的工号
                if (scanned.equals("TeachersNum")) {
                    redisTemplate.opsForHash().put(scaner,"snum",scaner + "x");
                    return Result.success();
                }
            }
            return Result.failure(ResultCode.PARAM_IS_INVALID,"扫描双方关系不适用");
        }
        return Result.failure(ResultCode.PARAM_IS_INVALID,"学生可能并未进行选座位");

    }

    /**
     * 获取单个指令
     */
    @Override
    public  Result getMyinstruction(String orderNum,String studentNum){
        if(redisTemplate.opsForSet().isMember(orderNum,studentNum)){
           String instruction =  redisTemplate.opsForHash().get(studentNum,"instruction").toString();
            return Result.success(instruction);
        }return Result.failure(ResultCode.PARAM_IS_INVALID,"该学生并未参与此次选座");
    }

    /**
     * 获取当前验证后的座位表
     */
    @Override
    public Result getSeatingChart(String orderNum){
        Iterator iterator =  redisTemplate.opsForSet().members(orderNum).iterator();
        int mRow = 0;
        int mColumn = 0;
        List<Node> nodes = new ArrayList<>();
        while(iterator.hasNext()){
            String studentNum = iterator.next().toString();
            int row = Integer.parseInt(redisTemplate.opsForHash().get(studentNum,"row").toString());
            int column = Integer.parseInt(redisTemplate.opsForHash().get(studentNum,"column").toString());
            if(row > mRow){mRow = row;}
            if(column > mColumn){mColumn = column;}
            Node node = new Node(row,column,redisTemplate.opsForHash().get(studentNum,"snum").toString());
            nodes.add(node);
       }
        String[][] seats = new String[mRow+1][mColumn+1];
        for (int i = 0;i<=mRow;i++){
            for (int j = 0;j<=mColumn;j++){
                seats[i][j] = "0";
            }
        }
        for(Node node : nodes){
            if(node.getSnum().endsWith("x")){
                seats[node.getRow()][node.getColumn()] = "2";
            }else {
                seats[node.getRow()][node.getColumn()] = "1";
            }
        }
        return Result.success(seats);
    }



    /**
     * ArrayList转成LinkedList<Node>对象组
     */
    private LinkedList<Node> ArrayTOString(ArrayList<List<String>> list) {
        maxRow = list.size() - 1;
        maxColumn = list.get(0).size() -1;

        LinkedList<Node> nodes = new LinkedList<Node>();
        //String[][] seats = new String[list.size()][maxRow];
        int x = 0;
        int y;
        for (List<String> i : list) {
            y = 0;
            for (String j : i) {
                Node node = new Node(x,y,j);
                nodes.add(node);
//                seats[x][y] = j;
//                System.out.println(seats[x][y]);
                y++;
            }
            x++;
        }
       // System.out.println(seats[1][1]);
        return nodes;

    }

    /**
     * 生成森林
     */
    private LinkedList<LinkedList<Node>> generateForest(LinkedList<Node> nodes) {
        // 森林
        LinkedList<LinkedList<Node>> forest = new LinkedList<LinkedList<Node>>();
        //树号
        int treeNum = 0;
        //遍历每一个节点对象
        for (Node node : nodes) {
            //树
            LinkedList<Node>  tree = new LinkedList<Node>();
            // 判断当前位置是否被选中且是否加入森林
            if (node.getSnum().equals(ZERO) || node.getTreeNum() != 0 ) {
                continue;
            }
            //树号+1
            treeNum += 1;
            // 当前结点为根的生成树（DFS遍历）
            DFS(node,null, tree,treeNum,forest,nodes);
            // 将生成树添加至森林
            if (!tree.isEmpty()) {
                forest.add(tree);
            }
        }
        return forest;
    }


    /**
     * DFS遍历
     */
    private Node DFS(Node node, Node parent, LinkedList<Node> tree,int treeNum, LinkedList<LinkedList<Node>> forest, LinkedList<Node> nodes) {
        LinkedList<Node> childArr = new LinkedList<Node>();

        //设置树号
        node.setTreeNum(treeNum);

        // 添加结点
        tree.add(node);

        //获得当前节点的坐标
        int column = node.getColumn();
        int row = node.getRow();
        //添加父母节点
        if (parent != null){
            node.setParent(parent);}

        //获取上方节点对象
        if (row > 0){
            Node up = nodes.get((((row - 1) * (maxColumn + 1)) + (column + 1)) - 1);
            // 判断上方结点是否被选中且还没加入树中
            if ( !up.getSnum().equals(ZERO) && up.getTreeNum() == 0) {
                System.out.println("IN");
            childArr.add(DFS(up,node,tree,treeNum,forest,nodes));}
        }

        //获取左方节点对象
        if (column > 0) {
            Node left = nodes.get(((row * (maxColumn + 1)) + column) - 1);
            // 判断左边结点是否被选中且还没加入树中
            if (!left.getSnum().equals(ZERO) && left.getTreeNum() == 0) {
                childArr.add(DFS(left, node, tree, treeNum, forest, nodes));
            }
        }

        //获取下方节点对象
        if(maxRow  > row ){
            Node down = nodes.get((((row + 1) * (maxColumn + 1)) + (column + 1)) - 1);
            // 判断下方结点是否被选中且还没加入树中
            if (!down.getSnum().equals(ZERO) && down.getTreeNum() == 0){
            childArr.add(DFS(down,node,tree,treeNum,forest,nodes));}
        }

        //获取右方节点对象
        if (maxColumn > column){
            Node right = nodes.get(((row * (maxColumn + 1)) + (column + 2)) - 1);
            // 判断右边结点是否被选中且还没加入树中
            if (!right.getSnum().equals(ZERO) && right.getTreeNum() == 0){
                childArr.add(DFS(right,node,tree,treeNum,forest,nodes));}
        }
        if (!childArr.isEmpty()){
            node.setChild(childArr);}
        return node;
    }

    /**
     * 生成发送给每个结点的指令(双亲扫孩子)
     */
    private Map<String,Object> generateOrders(LinkedList<LinkedList<Node>> forest) {
        Map<String,Object> returnMap = new HashMap();
        List<Node> nodeList = new ArrayList<>();
        for (LinkedList<Node> tree : forest) {
            for (Node node : tree) {
                Node replaceNode = new Node(node.getRow(),node.getColumn(),node.getSnum());
                //判断是否为孤点
                if (tree.size() == 1) {
                    String str = "该结点为孤点————请找导师进行考勤";
                    replaceNode.setInstruction(str);
                    nodeList.add(replaceNode);
                    continue;
                }

                String strDirection;
                String str = "";
                if (node.getParent() != null) {
                    // 被扫的一方
                    replaceNode.setParentInfo(node.getParent().getSnum());
                    str += "请将你的二维码提供给你" + judgeIndex(node,node.getParent()) + "方的同学扫描以完成签到";
                    }

                if (node.getChild() != null) {
                    // 扫码的一方
                    if (str != "") {
                        str += "或";
                    }
                    str += "扫描";
                    List<String> childInfo = new ArrayList<>();
                    for (Node nodeChild : node.getChild()) {
                        childInfo.add(nodeChild.getSnum());
                        strDirection = judgeIndex(node,nodeChild);
                        if (!strDirection.equals("")){
                            str += strDirection.trim();}
                    }
                    replaceNode.setChildInfo(childInfo);
                    str += "方同学的信息";
                }
                replaceNode.setInstruction(str);
                nodeList.add(replaceNode);
            }
        }
        for(Node i : nodeList){
            System.out.println(i.getSnum());
            System.out.println(i.getInstruction());
        }
        returnMap.put("datas",nodeList);
        return returnMap;
    }

    /**
     * 根据坐标比较相对位置
     */
    private String judgeIndex(Node center,Node target) {
        String str = "";
        int centerRow = center.getRow();
        int centerColumn = center.getColumn();
        int targetRow = target.getRow();
        int targetColumn = target.getColumn();
        if ( centerRow+ 1 == targetRow && centerColumn == targetColumn){
            str = "下";}
        if (centerRow - 1 == targetRow && centerColumn == targetColumn){
            str = "上";}
        if (centerRow == targetRow && centerColumn + 1 == targetColumn){
            str = "右";}
        if (centerRow == targetRow && centerColumn - 1 == targetColumn){
            str = "左";}
        return str;
    }

    /**
     * 把信息存入redis中
     */
    private int sentToRedis(Map<String,Object> map){

        List<Node> nodeList =(ArrayList)map.get("datas");
        List<String> list = new ArrayList<String>();
        for(Node node : nodeList){
            Map<String,String> nodeInfo = new HashMap<>();
            nodeInfo.put("snum",node.getSnum());
            nodeInfo.put("row",String.valueOf(node.getRow()));
            nodeInfo.put("column",String.valueOf(node.getColumn()));
            if (node.getParentInfo() != null){
                nodeInfo.put("parentInfo",String.valueOf(node.getRow()));}
            else {
                nodeInfo.put("parentInfo","none");}
            if(node.getChildInfo() != null){
            nodeInfo.put("childInfo",node.getChildInfo().toString());}
            else {
                nodeInfo.put("parentInfo","none");}
            nodeInfo.put("instruction",node.getInstruction());
            setOperations.add(OrderNum,node.getSnum());
            hashOperations.putAll(node.getSnum(),(nodeInfo));
            redisTemplate.expire(node.getSnum(),TIME, TimeUnit.MINUTES);
        }
        redisTemplate.expire(OrderNum,TIME,TimeUnit.MINUTES);
        return 10086;
    }

}
