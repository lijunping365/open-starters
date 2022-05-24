package com.saucesubfresh.starter.cache.listener;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lijunping on 2022/5/24
 */
@Data
public class Message implements Serializable {

    /**
     * 消息唯一 id
     */
    private String msgId;

    /**
     * 消息体
     */
    private byte[] body;
}
