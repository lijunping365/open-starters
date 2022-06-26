package com.saucesubfresh.starter.cache.message;

import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lijunping on 2022/6/24
 */
@Data
public class CacheMessage implements Serializable {

    private String msgId;

    private byte[] key;

    private byte[] value;

    private String topic;

    private String cacheName;

    private CacheMessageCommand command;

//    public CacheMessage(ByteBuf keyBuf, ByteBuf valueBuf) {
//        key = new byte[keyBuf.readableBytes()];
//        keyBuf.getBytes(keyBuf.readerIndex(), key);
//
//        value = new byte[valueBuf.readableBytes()];
//        valueBuf.getBytes(valueBuf.readerIndex(), value);
//    }
}
