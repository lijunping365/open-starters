package com.saucesubfresh.starter.crawler.generator;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * 默认使用 google 的 HashFunction 生成 id
 *
 * @author lijunping on 2022/7/13
 */
public class DefaultKeyGenerator implements KeyGenerator{

    @Override
    public String generate(String params) {
        HashFunction hashFunction = Hashing.murmur3_32_fixed();
        return hashFunction.hashString(params, StandardCharsets.UTF_8).toString();
    }
}
