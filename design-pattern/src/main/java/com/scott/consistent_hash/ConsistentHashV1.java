package com.scott.consistent_hash;

import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author scott 2022/3/6
 *
 * 不带虚拟节点的一致性Hash算法
 * 1.如何造一个hash环，2.如何在哈希环上映射服务器节点，3.如何找到对应的节点
 */

public class ConsistentHashV1 {

    //待添加入Hash环的服务器列表
    private static String[] servers = { "192.168.0.0:111", "192.168.0.1:111",
            "192.168.0.2:111", "192.168.0.3:111", "192.168.0.4:111" };

    private static SortedMap<Integer,String> hashRing = new TreeMap<>();

    static {
        for (String server : servers) {
            int hashValue = doHash(server);
            hashRing.put(hashValue, server);
        }
    }

    public static String getServer(String key){
        int hashValue = doHash(key);
        SortedMap<Integer, String> theGreater = hashRing.tailMap(hashValue);

        if (theGreater.isEmpty()){
            Integer firstKey = hashRing.firstKey();
            return hashRing.get(firstKey);
        } else {
            Integer firstGreater = theGreater.firstKey();
            return theGreater.get(firstGreater);
        }

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
