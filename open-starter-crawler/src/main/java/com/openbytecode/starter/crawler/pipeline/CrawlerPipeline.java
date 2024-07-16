/*
 * Copyright © 2022 organization openbytecode
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
package com.openbytecode.starter.crawler.pipeline;

import com.openbytecode.starter.crawler.domain.SpiderRequest;
import com.openbytecode.starter.crawler.domain.SpiderResponse;

import java.util.NoSuchElementException;

/**
 * @author lijunping
 */
public interface CrawlerPipeline {

    /**
     * Inserts a {@link CrawlerHandler} at the first position of this pipeline.
     *
     * @param name     the name of the handler to insert first
     * @param handler  the handler to insert first
     *
     * @throws IllegalArgumentException
     *         if there's an entry with the same name already in the pipeline
     * @throws NullPointerException
     *         if the specified handler is {@code null}
     */
    CrawlerPipeline addFirst(String name, CrawlerHandler handler);

    /**
     * Appends a {@link CrawlerHandler} at the last position of this pipeline.
     *
     * @param name     the name of the handler to append
     * @param handler  the handler to append
     *
     * @throws IllegalArgumentException
     *         if there's an entry with the same name already in the pipeline
     * @throws NullPointerException
     *         if the specified handler is {@code null}
     */
    CrawlerPipeline addLast(String name, CrawlerHandler handler);

    /**
     * Inserts a {@link CrawlerHandler} before an existing handler of this
     * pipeline.
     *
     * @param baseName  the name of the existing handler
     * @param name      the name of the handler to insert before
     * @param handler   the handler to insert before
     *
     * @throws NoSuchElementException
     *         if there's no such entry with the specified {@code baseName}
     * @throws IllegalArgumentException
     *         if there's an entry with the same name already in the pipeline
     * @throws NullPointerException
     *         if the specified baseName or handler is {@code null}
     */
    CrawlerPipeline addBefore(String baseName, String name, CrawlerHandler handler);

    /**
     * Inserts a {@link CrawlerHandler} after an existing handler of this
     * pipeline.
     *
     * @param baseName  the name of the existing handler
     * @param name      the name of the handler to insert after
     * @param handler   the handler to insert after
     *
     * @throws NoSuchElementException
     *         if there's no such entry with the specified {@code baseName}
     * @throws IllegalArgumentException
     *         if there's an entry with the same name already in the pipeline
     * @throws NullPointerException
     *         if the specified baseName or handler is {@code null}
     */
    CrawlerPipeline addAfter(String baseName, String name, CrawlerHandler handler);

    /**
     * A {@link CrawlerHandler} received a message.
     *
     * This will result in having the {@link CrawlerHandler#handler(CrawlerHandlerContext, SpiderRequest, SpiderResponse)}
     * method  called of the next {@link CrawlerHandler} contained in the {@link CrawlerPipeline}.
     */
    void fireCrawlerHandler(SpiderRequest request, SpiderResponse response);
}
