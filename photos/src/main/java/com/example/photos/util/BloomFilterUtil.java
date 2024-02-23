package com.example.photos.util;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: raolongxiang
 * @Date: 2024/2/21
 * @Description: com.example.photos.util
 */
@Component
public class BloomFilterUtil {

    @Autowired
    private RedissonClient redissonClient;

    public <T> RBloomFilter<T> create(String filterName, long expectedInsertions, double falseProbability) {
        RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(filterName);
        bloomFilter.tryInit(expectedInsertions, falseProbability);
        return bloomFilter;
    }

    public <T> RBloomFilter<T> get(String filterName) {
        return redissonClient.getBloomFilter(filterName);
    }

}
