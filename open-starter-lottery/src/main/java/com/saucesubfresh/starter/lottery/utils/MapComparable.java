package com.saucesubfresh.starter.lottery.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author: 李俊平
 * @Date: 2020-12-03 12:23
 */
public class MapComparable {

  /**
   * <pr>
   * map 按照 key 排序
   * </pr>
   *
   * @param map
   * @param asc
   * @param <K>
   * @param <V>
   * @return
   */
  public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map, boolean asc) {
    Map<K, V> result = new LinkedHashMap<>();
    Stream<Map.Entry<K, V>> stream = map.entrySet().stream();
    if (asc) {
      stream.sorted(Map.Entry.<K, V>comparingByKey())
          .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
    } else {
      stream.sorted(Map.Entry.<K, V>comparingByKey().reversed())
          .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
    }
    return result;
  }

  /**
   * <pr>
   * map 按照 value 排序
   * </pr>
   *
   * @param map
   * @param asc
   * @param <K>
   * @param <V>
   * @return
   */
  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean asc) {
    Map<K, V> result = new LinkedHashMap<>();
    Stream<Map.Entry<K, V>> stream = map.entrySet().stream();
    if (asc) {
      //stream.sorted(Comparator.comparing(e -> e.getValue()))
      stream.sorted(Map.Entry.<K, V>comparingByValue())
          .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
    } else {
      //stream.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
      stream.sorted(Map.Entry.<K, V>comparingByValue().reversed())
          .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
    }
    return result;
  }
}
