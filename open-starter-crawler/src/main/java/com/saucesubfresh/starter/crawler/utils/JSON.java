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
package com.saucesubfresh.starter.crawler.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.saucesubfresh.starter.crawler.exception.JsonException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * JSON 工具类
 *
 * @author lijunping
 */
public abstract class JSON {

  private JSON() {
  }

  private static final ObjectMapper INSTANCE = new ObjectMapper();

  static {
    //序列化的时候序列对象的所有属性
    INSTANCE.setSerializationInclusion(JsonInclude.Include.ALWAYS);
    //反序列化的时候如果多了其他属性,不抛出异常
    INSTANCE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //如果是空对象的时候,不抛异常
    INSTANCE.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
    INSTANCE.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    INSTANCE.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    // 解决反序列化 LocalDateTime的问题
    INSTANCE.registerModule(new JavaTimeModule());
    // 全局开启JSON美化输出
    INSTANCE.enable(SerializationFeature.INDENT_OUTPUT);
  }

  /**
   * 获取已经配置好的 {@link ObjectMapper}实例
   *
   * @return {@link ObjectMapper}
   */
  public static ObjectMapper mapper() {
    return INSTANCE;
  }

  /**
   * 将对象那转化为 JSON
   *
   * @param object 任意对象
   * @return {@link String}
   */
  public static String toJSON(Object object) {
    try {
      return INSTANCE.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * 将一个泛型的类型转化为另一个类型
   *
   * @param t   T类型的泛型
   * @param clz 需要将字符串转化为的 Class 类型
   * @param <T> 被转换的类型
   * @param <R> 转换完成的类型
   * @return R 如果出现异常会返回null
   */
  public static <T, R> R parse(T t, Class<R> clz) {
    try {
      byte[] bytes = INSTANCE.writerWithDefaultPrettyPrinter().writeValueAsBytes(t);
      return INSTANCE.readValue(bytes, clz);
    } catch (IOException e) {
      throw new JsonException(e);
    }
  }

  /**
   * 将 JSON 格式的字符串转化为 对应 Class 类型的对象
   *
   * @param s   JSON格式的字符串
   * @param clz 需要将字符串转化为的 Class 类型
   * @return {@link R} 如果出现异常会返回null
   */
  public static <R> R parse(String s, Class<R> clz) {
    try {
      return INSTANCE.readValue(s, clz);
    } catch (IOException e) {
      throw new JsonException(e);
    }
  }

  /**
   * 将 byte[]数组转换为对象的class类型的对象
   *
   * @param bytes byte【】
   * @param clz   需要将字符串转化为的 Class 类型
   * @return {@link R} 如果出现异常会返回null
   */
  public static <R> R parse(byte[] bytes, Class<R> clz) {
    try {
      return INSTANCE.readValue(bytes, clz);
    } catch (IOException e) {
      throw new JsonException(e);
    }
  }

  /**
   * 将json反序列化为Java集合对象
   *
   * @param json json字符串
   * @param clz  集合的泛型类型
   * @return {@link List<T>}
   */
  public static <T> List<T> parseList(String json, Class<T> clz) {
    try {
      final JavaType javaType = INSTANCE.getTypeFactory().constructCollectionType(List.class, clz);
      return INSTANCE.readValue(json, javaType);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * 将json反序列化为 Map
   *
   * @param json 字符串
   * @param k    key的Class
   * @param v    v的class
   * @return {@link Map<K,V>}
   */
  public static <K, V> Map<K, V> parseMap(String json, Class<K> k, Class<V> v) {
    try {
      return INSTANCE.readValue(json, INSTANCE.getTypeFactory().constructMapType(Map.class, k, v));
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }
}
