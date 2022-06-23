package com.saucesubfresh.starter.cache.factory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Map;

/**
 * copy from redisson
 * @author lijunping on 2022/6/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class CacheConfig {

    /**
     * 键值条目的存活时间，以毫秒为单位。
     */
    private long ttl;

    /**
     * 缓存容量
     */
    private int maxSize;

    /**
     * 键值输入的最大空闲时间(毫秒)。
     */
    private long maxIdleTime;

    /**
     * Read config objects stored in JSON format from <code>String</code>
     *
     * @param content of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromJSON(String content) throws IOException {
        return new CacheConfigSupport().fromJSON(content);
    }

    /**
     * Read config objects stored in JSON format from <code>InputStream</code>
     *
     * @param inputStream of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromJSON(InputStream inputStream) throws IOException {
        return new CacheConfigSupport().fromJSON(inputStream);
    }

    /**
     * Read config objects stored in JSON format from <code>File</code>
     *
     * @param file of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromJSON(File file) throws IOException {
        return new CacheConfigSupport().fromJSON(file);
    }

    /**
     * Read config objects stored in JSON format from <code>URL</code>
     *
     * @param url of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromJSON(URL url) throws IOException {
        return new CacheConfigSupport().fromJSON(url);
    }

    /**
     * Read config objects stored in JSON format from <code>Reader</code>
     *
     * @param reader of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromJSON(Reader reader) throws IOException {
        return new CacheConfigSupport().fromJSON(reader);
    }

    /**
     * Convert current configuration to JSON format
     *
     * @param config object
     * @return json string
     * @throws IOException error
     */
    public static String toJSON(Map<String, ? extends CacheConfig> config) throws IOException {
        return new CacheConfigSupport().toJSON(config);
    }

    /**
     * Read config objects stored in YAML format from <code>String</code>
     *
     * @param content of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromYAML(String content) throws IOException {
        return new CacheConfigSupport().fromYAML(content);
    }

    /**
     * Read config objects stored in YAML format from <code>InputStream</code>
     *
     * @param inputStream of config
     * @return config
     * @throws IOException  error
     */
    public static Map<String, ? extends CacheConfig> fromYAML(InputStream inputStream) throws IOException {
        return new CacheConfigSupport().fromYAML(inputStream);
    }

    /**
     * Read config objects stored in YAML format from <code>File</code>
     *
     * @param file of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromYAML(File file) throws IOException {
        return new CacheConfigSupport().fromYAML(file);
    }

    /**
     * Read config objects stored in YAML format from <code>URL</code>
     *
     * @param url of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromYAML(URL url) throws IOException {
        return new CacheConfigSupport().fromYAML(url);
    }

    /**
     * Read config objects stored in YAML format from <code>Reader</code>
     *
     * @param reader of config
     * @return config
     * @throws IOException error
     */
    public static Map<String, ? extends CacheConfig> fromYAML(Reader reader) throws IOException {
        return new CacheConfigSupport().fromYAML(reader);
    }

    /**
     * Convert current configuration to YAML format
     *
     * @param config map
     * @return yaml string
     * @throws IOException error
     */
    public static String toYAML(Map<String, ? extends CacheConfig> config) throws IOException {
        return new CacheConfigSupport().toYAML(config);
    }

}
