package com.hzx;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class Test_SpringDataRedis {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisConnection() {
        String testKey = "testKey";
        String testValue = "testValue1234";
        // 写入 Redis
        redisTemplate.opsForValue().set(testKey, testValue);
        // 从 Redis 读取
        String valueFromRedis = (String) redisTemplate.opsForValue().get(testKey);
        System.out.println("Redis connection is working: " + valueFromRedis);
    }


    @Test
    public void testRedisTemplate() {
        System.out.println(redisTemplate);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
    }

    /**
     * 操作字符串类型的数据
     */
    @Test
    public void testString() {
        //set get
        redisTemplate.opsForValue().set("city", "杭州");
        String city = (String) redisTemplate.opsForValue().get("city");
        System.out.println(city);
        //seteex
        redisTemplate.opsForValue().set("code", "1735", 3, TimeUnit.MINUTES);
        //setnx
        Boolean getLock1 = redisTemplate.opsForValue().setIfAbsent("lock", "1");
        System.out.println("local1: " + getLock1);
        Boolean getLock2 = redisTemplate.opsForValue().setIfAbsent("lock", "2");
        System.out.println("local2: " + getLock2);
    }

    /**
     * 操作哈希表类型的数据
     */
    @Test
    public void testHash() {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        //hset
        hashOperations.put("person", "name", "hzx");
        hashOperations.put("person", "age", "20");

        //hget
        String name = (String) hashOperations.get("person", "name");
        String age = (String) hashOperations.get("person", "age");
        System.out.println("name: " + name + ", age: " + age);

        //hkeys
        Set<Object> keys = hashOperations.keys("person");
        System.out.println("keys: " + keys);

        //hvals
        List<Object> values = hashOperations.values("person");
        System.out.println("values: " + values);

        //hdel
        Long delete = hashOperations.delete("person", "name");
        System.out.println(delete);
    }

    /**
     * 操作列表类型的数据
     */
    @Test
    public void testList() {
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        //lpush  从左往右加入
        Long leftPushAll = listOperations.leftPushAll("mylist", new String[]{"a", "b", "c"});
        System.out.println("leftPushAll: " + leftPushAll);
        Long leftPush = listOperations.leftPush("mylist", "d");
        System.out.println("leftPush: " + leftPush);

        //lrange
        List<Object> mylist = listOperations.range("mylist", 0, -1);
        System.out.println("mylist: " + mylist);

        //rpop
        String str = (String) listOperations.rightPop("mylist");
        System.out.println("rpop: " + str);

        //llen
        Long len = listOperations.size("mylist");
        System.out.println("len: " + len);
    }

    /**
     * 操作集合类型的数据
     */
    @Test
    public void testSet() {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        //sadd
        Long set1_add = setOperations.add("set1", new String[]{"a", "b", "c", "d"});
        System.out.println("set1_add: " + set1_add);
        Long set2_add = setOperations.add("set2", new String[]{"a", "b", "x", "y"});
        System.out.println("set2_add: " + set2_add);

        //smember
        Set<Object> set1_member = setOperations.members("set1");
        System.out.println("set1_member: " + set1_member);

        //scard
        Long set2_len = setOperations.size("set2");
        System.out.println("set2_len: " + set2_len);

        //sinter
        Set<Object> intersect = setOperations.intersect("set1", "set2");
        System.out.println("intersect: " + intersect);

        //sunion
        Set<Object> union = setOperations.union("set1", "set2");
        System.out.println("union: " + union);

        //sdiff
        Set<Object> difference = setOperations.difference("set1", "set2");
        System.out.println("difference: " + difference);

        //srandmember
        List<Object> randomMembers = setOperations.randomMembers("set1", 5);
        System.out.println("randomMembers: " + randomMembers);

        //srem
        Long remove = setOperations.remove("set2", "y", "x");
        System.out.println("remove: " + remove);
    }

    /**
     * 操作有序集合类型的数据
     */
    @Test
    public void testZSet() {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        //zadd
        Boolean zset1_add = zSetOperations.add("zset1", "a", 10);
        System.out.println("zset1_add: " + zset1_add);
        Boolean zset2_add = zSetOperations.add("zset1", "b", 10.5);
        System.out.println("zset2_add: " + zset2_add);
        Boolean zset3_add = zSetOperations.add("zset1", "c", 10.6);
        System.out.println("zset3_add: " + zset3_add);

        //zrange
        Set<Object> zset1_range = zSetOperations.range("zset1", 0, -1);
        System.out.println("zset1_range: " + zset1_range);

        //zincrby
        Double zincrby = zSetOperations.incrementScore("zset1", "c", -0.2);
        System.out.println("zincrby: " + zincrby);
        Set<Object> zset1_range2 = zSetOperations.range("zset1", 0, -1);
        System.out.println("zset1_range2: " + zset1_range2);

        //zrem
        Long remove = zSetOperations.remove("zset1", "a", "b");
        System.out.println("remove: " + remove);
        Set<Object> zset1_range3 = zSetOperations.range("zset1", 0, -1);
        System.out.println("zset1_range3: " + zset1_range3);
    }

    /**
     * 通用命令的操作
     */
    @Test
    public void testCommon() {
        // keys
        Set<String> keys = redisTemplate.keys("*");
        System.out.println("keys: " + keys);
        // exist
        Boolean exist1 = redisTemplate.hasKey("set1");
        System.out.println("exist1: " + exist1);
        Boolean exist2 = redisTemplate.hasKey("set1111");
        System.out.println("exist2: " + exist2);
        // type
        for (String key : keys) {
            DataType type = redisTemplate.type(key);
            System.out.println("key: " + key + ", type.name(): " + type.name());
        }
        //del
        Boolean delete = redisTemplate.delete("mylist");
        System.out.println("delete: " + delete);
        for (Object key : keys) {
            System.out.println("key: " + key);
        }
    }


}
