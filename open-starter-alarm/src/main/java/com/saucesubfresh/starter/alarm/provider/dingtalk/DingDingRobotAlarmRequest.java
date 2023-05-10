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
package com.saucesubfresh.starter.alarm.provider.dingtalk;

import com.saucesubfresh.starter.alarm.request.BaseAlarmRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 钉钉自定义机器人报警入参
 * @author lijunping
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingDingRobotAlarmRequest extends BaseAlarmRequest {
    /**
     * at
     */
    private AtVO at;
    /**
     * text
     */
    private TextVO text;
    /**
     * link
     */
    private LinkVO link;
    /**
     * markdown
     */
    private MarkdownVO markdown;
    /**
     * actionCard
     */
    private ActionCardVO actionCard;
    /**
     * feedCard
     */
    private FeedCardVO feedCard;
    /**
     * msgtype
     */
    private String msgtype;
    /**
     * 配置
     */
    private ConfigVO config;

    /**
     * AtVO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class AtVO {
        /**
         * atMobiles
         */
        private List<String> atMobiles;
        /**
         * atUserIds
         */
        private List<String> atUserIds;
        /**
         * isAtAll
         */
        private Boolean isAtAll;
    }

    /**
     * TextVO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class TextVO {
        /**
         * content
         */
        private String content;
    }

    /**
     * LinkVO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class LinkVO {
        /**
         * text
         */
        private String text;
        /**
         * title
         */
        private String title;
        /**
         * picUrl
         */
        private String picUrl;
        /**
         * messageUrl
         */
        private String messageUrl;
    }

    /**
     * MarkdownVO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class MarkdownVO {
        /**
         * title
         */
        private String title;
        /**
         * text
         */
        private String text;
    }

    /**
     * ActionCardVO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class ActionCardVO {
        /**
         * title
         */
        private String title;
        /**
         * text
         */
        private String text;
        /**
         * btnOrientation
         */
        private String btnOrientation;
        /**
         * btns
         */
        private List<BtnsVO> btns;

        /**
         * BtnsVO
         */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        public static class BtnsVO {
            /**
             * title
             */
            private String title;
            /**
             * actionURL
             */
            private String actionURL;
        }
    }

    /**
     * FeedCardVO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class FeedCardVO {
        /**
         * links
         */
        private List<LinksVO> links;

        /**
         * LinksVO
         */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        public static class LinksVO {
            /**
             * title
             */
            private String title;
            /**
             * messageURL
             */
            private String messageURL;
            /**
             * picURL
             */
            private String picURL;
        }
    }

    /**
     * ConfigVO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfigVO {
        /**
         * 钉钉机器人密钥
         */
        private String secret;

        /**
         * 自定义群机器人中的 webhook
         */
        private String webhook;
    }
}
