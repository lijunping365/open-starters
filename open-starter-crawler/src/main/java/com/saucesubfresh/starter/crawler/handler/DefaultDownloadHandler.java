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
package com.saucesubfresh.starter.crawler.handler;

import com.saucesubfresh.starter.crawler.domain.SpiderRequest;
import com.saucesubfresh.starter.crawler.plugin.UsePlugin;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认 下载器
 *
 * @author lijunping
 */
@Slf4j
@UsePlugin(type = DownloadHandler.class)
public class DefaultDownloadHandler implements DownloadHandler {

    @Override
    public String download(SpiderRequest request) {
        return null;
    }
}
