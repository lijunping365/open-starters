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
package com.saucesubfresh.starter.crawler.pipeline;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;

import java.util.NoSuchElementException;

/**
 * @author lijunping
 */
public class DefaultCrawlerPipeline implements CrawlerPipeline{

    private final DefaultCrawlerHandlerContext head;
    private final DefaultCrawlerHandlerContext tail;

    public DefaultCrawlerPipeline() {
        head = new DefaultCrawlerHandlerContext("HEAD");
        tail = new DefaultCrawlerHandlerContext("TAIL");
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public CrawlerPipeline addFirst(String name, CrawlerHandler handler) {
        final DefaultCrawlerHandlerContext newNode;
        synchronized (this) {
            newNode = newHandlerNode(name, handler);
            addFirst0(newNode);
        }
        return this;
    }

    @Override
    public CrawlerPipeline addLast(String name, CrawlerHandler handler) {
        final DefaultCrawlerHandlerContext newNode;
        synchronized (this) {
            newNode = newHandlerNode(name, handler);
            addLast0(newNode);
        }
        return this;
    }

    @Override
    public CrawlerPipeline addBefore(String baseName, String name, CrawlerHandler handler) {
        final DefaultCrawlerHandlerContext newNode;
        final DefaultCrawlerHandlerContext node;
        synchronized (this) {
            newNode = newHandlerNode(name, handler);
            node = getContextOrDie(baseName);
            addBefore0(node, newNode);
        }
        return this;
    }

    @Override
    public CrawlerPipeline addAfter(String baseName, String name, CrawlerHandler handler) {
        final DefaultCrawlerHandlerContext newNode;
        final DefaultCrawlerHandlerContext node;
        synchronized (this) {
            newNode = newHandlerNode(name, handler);
            node = getContextOrDie(baseName);
            addAfter0(node, newNode);
        }
        return this;
    }

    @Override
    public void fireCrawlerHandler(SpiderRequest request, Object msg) {
        DefaultCrawlerHandlerContext.invokeCrawlerHandler(head, request, msg);
    }

    private DefaultCrawlerHandlerContext newHandlerNode(String name, CrawlerHandler handler) {
        return new DefaultCrawlerHandlerContext(name, handler);
    }

    private DefaultCrawlerHandlerContext getContextOrDie(String name) {
        DefaultCrawlerHandlerContext ctx = context(name);
        if (ctx == null) {
            throw new NoSuchElementException(name);
        } else {
            return ctx;
        }
    }

    private DefaultCrawlerHandlerContext context(String name) {
        DefaultCrawlerHandlerContext context = head.next;
        while (context != tail) {
            if (context.name().equals(name)) {
                return context;
            }
            context = context.next;
        }
        return null;
    }

    private void addFirst0(DefaultCrawlerHandlerContext newNode) {
        DefaultCrawlerHandlerContext next = head.next;
        newNode.prev = head;
        newNode.next = next;
        head.next = newNode;
        next.prev = newNode;
    }

    private void addLast0(DefaultCrawlerHandlerContext newNode) {
        DefaultCrawlerHandlerContext prev = tail.prev;
        newNode.prev = prev;
        newNode.next = tail;
        prev.next = newNode;
        tail.prev = newNode;
    }

    private void addBefore0(DefaultCrawlerHandlerContext ctx, DefaultCrawlerHandlerContext newCtx) {
        newCtx.prev = ctx.prev;
        newCtx.next = ctx;
        ctx.prev.next = newCtx;
        ctx.prev = newCtx;
    }

    private static void addAfter0(DefaultCrawlerHandlerContext ctx, DefaultCrawlerHandlerContext newCtx) {
        newCtx.prev = ctx;
        newCtx.next = ctx.next;
        ctx.next.prev = newCtx;
        ctx.next = newCtx;
    }
}
