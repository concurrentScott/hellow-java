package com.scott.consistent_hash;

import java.util.*;

/**
 * @author scott 2022/3/6
 *
 * 带虚拟节点的一致性Hash算法
 * 1.如何造一个hash环，2.如何在哈希环上映射服务器节点，3.如何找到对应的节点
 */

public class ConsistentHashV2 {

    //待添加入Hash环的服务器列表
    private static String[] servers = { "192.168.0.0:111", "192.168.0.1:111",
            "192.168.0.2:111", "192.168.0.3:111", "192.168.0.4:111" };

    private static List<String> realNodes = new LinkedList<>();

    private static SortedMap<Integer,String> vNodes = new TreeMap<>();

    private static final int VIRTUAL_NODES_COUNT = 5;

    static {
        realNodes.addAll(Arrays.asList(servers));

        for (String realNode : realNodes) {

            for (int i = 0; i < VIRTUAL_NODES_COUNT; i++) {
                String vNodeName = realNode + "&&VN" + String.valueOf(i);
                int vHash = doHash(vNodeName);
                System.out.println("虚拟节点" + vNodeName + "被添加");
                vNodes.put(vHash,vNodeName);
            }
        }

    }


    public static String getServer(String key){
        int hash = doHash(key);
        SortedMap<Integer, String> greaterMap = vNodes.tailMap(hash);

        String vNode;
        if (greaterMap.isEmpty()){
            Integer firstKey = vNodes.firstKey();
            vNode =  vNodes.get(firstKey);
        } else {
            Integer firstKey = greaterMap.firstKey();
            vNode = vNodes.get(firstKey);

        }

        return vNode.split("&&VN")[0];
    }


    private static int doHash(String value){
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < value.length(); i++)
            hash = (hash ^ value.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }



}
