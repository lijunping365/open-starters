/*
 * Copyright © 2022 organization SauceSubFresh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saucesubfresh.starter.crawler.generator;


import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * 默认使用 google 的 HashFunction 生成 id
 *
 * @author lijunping
 */
public class DefaultKeyGenerator implements KeyGenerator{

    @Override
    public String generate(String params) {
        HashFunction hashFunction = Hashing.murmur3_32_fixed();
        return hashFunction.hashString(params, StandardCharsets.UTF_8).toString();
    }
}
