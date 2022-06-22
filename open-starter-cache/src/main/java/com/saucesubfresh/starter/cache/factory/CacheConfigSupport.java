package com.saucesubfresh.starter.cache.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Map;

/**
 * @author lijunping on 2022/6/22
 */
public class CacheConfigSupport {

    ObjectMapper jsonMapper = new ObjectMapper();
    ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    public Map<String, CacheConfig> fromJSON(String content) throws IOException {
        return jsonMapper.readValue(content, new TypeReference<Map<String, CacheConfig>>() {});
    }

    public Map<String, CacheConfig> fromJSON(File file) throws IOException {
        return jsonMapper.readValue(file, new TypeReference<Map<String, CacheConfig>>() {});
    }

    public Map<String, CacheConfig> fromJSON(URL url) throws IOException {
        return jsonMapper.readValue(url, new TypeReference<Map<String, CacheConfig>>() {});
    }

    public Map<String, CacheConfig> fromJSON(Reader reader) throws IOException {
        return jsonMapper.readValue(reader, new TypeReference<Map<String, CacheConfig>>() {});
    }

    public Map<String, CacheConfig> fromJSON(InputStream inputStream) throws IOException {
        return jsonMapper.readValue(inputStream, new TypeReference<Map<String, CacheConfig>>() {});
    }

    public String toJSON(Map<String, ? extends CacheConfig> configs) throws IOException {
        return jsonMapper.writeValueAsString(configs);
    }

    public Map<String, CacheConfig> fromYAML(String content) throws IOException {
        return yamlMapper.readValue(content, new TypeReference<Map<String, CacheConfig>>() {});
    }

    public Map<String, CacheConfig> fromYAML(File file) throws IOException {
        return yamlMapper.readValue(file, new TypeReference<Map<String, CacheConfig>>() {});
    }

    public Map<String, CacheConfig> fromYAML(URL url) throws IOException {
        return yamlMapper.readValue(url, new TypeReference<Map<String, CacheConfig>>() {});
    }

    public Map<String, CacheConfig> fromYAML(Reader reader) throws IOException {
        return yamlMapper.readValue(reader, new TypeReference<Map<String, CacheConfig>>() {});
    }

    public Map<String, CacheConfig> fromYAML(InputStream inputStream) throws IOException {
        return yamlMapper.readValue(inputStream, new TypeReference<Map<String, CacheConfig>>() {});
    }

    public String toYAML(Map<String, ? extends CacheConfig> configs) throws IOException {
        return yamlMapper.writeValueAsString(configs);
    }
}
