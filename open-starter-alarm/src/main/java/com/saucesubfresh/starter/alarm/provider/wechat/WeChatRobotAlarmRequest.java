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
package com.saucesubfresh.starter.alarm.provider.wechat;

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
public class WeChatRobotAlarmRequest extends BaseAlarmRequest {
    /**
     * msgtype
     */
    @JsonProperty("msgtype")
    private String msgType;
    /**
     * text
     */
    private TextDTO text;
    /**
     * markdown
     */
    private MarkdownDTO markdown;

    /**
     * image
     */
    private ImageDTO image;
    /**
     * news
     */
    private NewsDTO news;
    /**
     * file
     */
    private FileDTO file;
    /**
     * templateCard
     */
    @JsonProperty("template_card")
    private TemplateCardDTO templateCard;

    /**
     * 配置
     */
    private ConfigVO config;

    /**
     * TextDTO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class TextDTO {
        /**
         * content
         */
        private String content;
        /**
         * mentionedList
         */
        @JsonProperty("mentioned_list")
        private List<String> mentionedList;
        /**
         * mentionedMobileList
         */
        @JsonProperty("mentioned_mobile_list")
        private List<String> mentionedMobileList;
    }

    /**
     * MarkdownDTO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class MarkdownDTO {
        /**
         * content
         */
        private String content;
    }

    /**
     * ImageDTO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class ImageDTO {
        /**
         * base64
         */
        private String base64;
        /**
         * md5
         */
        private String md5;
    }

    /**
     * NewsDTO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class NewsDTO {
        /**
         * articles
         */
        private List<ArticlesDTO> articles;

        /**
         * ArticlesDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class ArticlesDTO {
            /**
             * title
             */
            private String title;
            /**
             * description
             */
            private String description;
            /**
             * url
             */
            private String url;
            /**
             * picurl
             */
            private String picurl;
        }
    }

    /**
     * FileDTO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class FileDTO {
        /**
         * mediaId
         */
        @JsonProperty("media_id")
        private String mediaId;
    }

    /**
     * TemplateCardDTO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class TemplateCardDTO {
        /**
         * cardType
         */
        @JsonProperty("card_type")
        private String cardType;
        /**
         * source
         */
        private SourceDTO source;
        /**
         * mainTitle
         */
        @JsonProperty("main_title")
        private MainTitleDTO mainTitle;
        /**
         * emphasisContent
         */
        @JsonProperty("emphasis_content")
        private EmphasisContentDTO emphasisContent;
        /**
         * quoteArea
         */
        @JsonProperty("quote_area")
        private QuoteAreaDTO quoteArea;
        /**
         * subTitleText
         */
        @JsonProperty("sub_title_text")
        private String subTitleText;
        /**
         * horizontalContentList
         */
        @JsonProperty("horizontal_content_list")
        private List<HorizontalContentListDTO> horizontalContentList;
        /**
         * jumpList
         */
        @JsonProperty("jump_list")
        private List<JumpListDTO> jumpList;
        /**
         * cardAction
         */
        @JsonProperty("card_action")
        private CardActionDTO cardAction;

        /**
         * SourceDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class SourceDTO {
            /**
             * iconUrl
             */
            @JsonProperty("icon_url")
            private String iconUrl;
            /**
             * desc
             */
            private String desc;
            /**
             * descColor
             */
            @JsonProperty("desc_color")
            private Integer descColor;
        }

        /**
         * MainTitleDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class MainTitleDTO {
            /**
             * title
             */
            private String title;
            /**
             * desc
             */
            private String desc;
        }

        /**
         * EmphasisContentDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class EmphasisContentDTO {
            /**
             * title
             */
            private String title;
            /**
             * desc
             */
            private String desc;
        }

        /**
         * QuoteAreaDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class QuoteAreaDTO {
            /**
             * type
             */
            private Integer type;
            /**
             * url
             */
            private String url;
            /**
             * appid
             */
            @JsonProperty("appid")
            private String appId;
            /**
             * pagepath
             */
            @JsonProperty("pagepath")
            private String pagePath;
            /**
             * title
             */
            private String title;
            /**
             * quoteText
             */
            @JsonProperty("quote_text")
            private String quoteText;
        }

        /**
         * CardActionDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class CardActionDTO {
            /**
             * type
             */
            private Integer type;
            /**
             * url
             */
            private String url;
            /**
             * appid
             */
            @JsonProperty("appid")
            private String appId;
            /**
             * pagepath
             */
            @JsonProperty("pagepath")
            private String pagePath;
        }

        /**
         * HorizontalContentListDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class HorizontalContentListDTO {
            /**
             * keyname
             */
            @JsonProperty("keyname")
            private String keyName;
            /**
             * value
             */
            private String value;
            /**
             * type
             */
            private Integer type;
            /**
             * url
             */
            private String url;
            /**
             * mediaId
             */
            @JsonProperty("media_id")
            private String mediaId;
        }

        /**
         * JumpListDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class JumpListDTO {
            /**
             * type
             */
            private Integer type;
            /**
             * url
             */
            private String url;
            /**
             * title
             */
            private String title;
            /**
             * appid
             */
            @JsonProperty("appid")
            private String appId;
            /**
             * pagepath
             */
            @JsonProperty("pagepath")
            private String pagePath;
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
