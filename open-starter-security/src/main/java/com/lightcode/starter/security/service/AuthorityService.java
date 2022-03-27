package com.lightcode.starter.security.service;

import java.util.List;
import java.util.Map;

/**
 * @author: 李俊平
 * @Date: 2021-12-23 23:48
 */
public interface AuthorityService {

    /**
     * 获取菜单权限
     * return 例如
     * {
     *     "/path/**": ["admin","super_admin"],
     *     "/test/**": ["admin"]
     *     ...
     * }
     */
    Map<String, List<String>> getAuthorities();

}
