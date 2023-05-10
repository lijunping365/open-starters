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
package com.saucesubfresh.starter.alarm.provider.feishu;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.saucesubfresh.starter.alarm.request.BaseAlarmRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 企业微信自定义机器人报警入参
 * @author lijunping
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeiShuRobotAlarmRequest extends BaseAlarmRequest {
    /**
     * msgType
     */
    @JsonProperty("msg_type")
    private String msgType;
    /**
     * content
     */
    private ContentDTO content;
    /**
     * card
     */
    private CardDTO card;
    /**
     * 配置
     */
    private ConfigVO config;

    /**
     * ContentDTO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class ContentDTO {
        /**
         * text
         */
        private String text;
        /**
         * post
         */
        private PostDTO post;
        /**
         * shareChatId
         */
        @JsonProperty("share_chat_id")
        private String shareChatId;
        /**
         * imageKey
         */
        @JsonProperty("image_key")
        private String imageKey;

        /**
         * PostDTO
         */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        @Builder
        public static class PostDTO {
            /**
             * zhCn
             */
            @JsonProperty("zh_cn")
            private ZhCnDTO zhCn;

            /**
             * ZhCnDTO
             */
            @NoArgsConstructor
            @Data
            @AllArgsConstructor
            @Builder
            public static class ZhCnDTO {
                /**
                 * title
                 */
                private String title;
                /**
                 * content
                 */
                private List<List<ContentDTO.PostDTO.ZhCnDTO.PostContentDTO>> content;

                /**
                 * ContentDTO
                 */
                @NoArgsConstructor
                @Data
                @AllArgsConstructor
                @Builder
                public static class PostContentDTO {
                    /**
                     * tag
                     */
                    private String tag;
                    /**
                     * text
                     */
                    private String text;
                    /**
                     * href
                     */
                    private String href;
                    /**
                     * userId
                     */
                    @JsonProperty("user_id")
                    private String userId;
                }
            }
        }
    }

    /**
     * CardDTO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class CardDTO {
        /**
         * config
         */
        private ConfigDTO config;
        /**
         * elements
         */
        private List<ElementsDTO> elements;
        /**
         * header
         */
        private HeaderDTO header;

        /**
         * ConfigDTO
         */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        @Builder
        public static class ConfigDTO {
            /**
             * wideScreenMode
             */
            @JsonProperty("wide_screen_mode")
            private Boolean wideScreenMode;
            /**
             * enableForward
             */
            @JsonProperty("enable_forward")
            private Boolean enableForward;
        }

        /**
         * HeaderDTO
         */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        @Builder
        public static class HeaderDTO {
            /**
             * title
             */
            private TitleDTO title;

            /**
             * TitleDTO
             */
            @NoArgsConstructor
            @Data
            @AllArgsConstructor
            @Builder
            public static class TitleDTO {
                /**
                 * content
                 */
                private String content;
                /**
                 * tag
                 */
                private String tag;
            }
        }

        /**
         * ElementsDTO
         */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        @Builder
        public static class ElementsDTO {
            /**
             * tag
             */
            private String tag;
            /**
             * text
             */
            private TextDTO text;
            /**
             * actions
             */
            private List<ActionsDTO> actions;

            /**
             * TextDTO
             */
            @NoArgsConstructor
            @Data
            @AllArgsConstructor
            @Builder
            public static class TextDTO {
                /**
                 * content
                 */
                private String content;
                /**
                 * tag
                 */
                private String tag;
            }

            /**
             * ActionsDTO
             */
            @NoArgsConstructor
            @Data
            @AllArgsConstructor
            @Builder
            public static class ActionsDTO {
                /**
                 * tag
                 */
                private String tag;
                /**
                 * text
                 */
                private TextDTO text;
                /**
                 * url
                 */
                private String url;
                /**
                 * type
                 */
                private String type;


                /**
                 * TextDTO
                 */
                @NoArgsConstructor
                @Data
                public static class TextDTO {
                }

                /**
                 * ValueDTO
                 */
                @Data
                @NoArgsConstructor
                public static class ValueDTO {
                }
            }
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
         * 自定义群机器人中的 webhook
         */
        private String webhook;
    }
}
