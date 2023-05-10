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

import com.saucesubfresh.starter.alarm.request.BaseAlarmMessage;
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
public class WeChatMessageRequest extends BaseAlarmMessage {
    /**
     * msgtype
     */
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
    private TemplateCardDTO templateCard;

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
        private List<String> mentionedList;
        /**
         * mentionedMobileList
         */
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
        private String cardType;
        /**
         * source
         */
        private SourceDTO source;
        /**
         * mainTitle
         */
        private MainTitleDTO mainTitle;
        /**
         * emphasisContent
         */
        private EmphasisContentDTO emphasisContent;
        /**
         * quoteArea
         */
        private QuoteAreaDTO quoteArea;
        /**
         * subTitleText
         */
        private String subTitleText;
        /**
         * horizontalContentList
         */
        private List<HorizontalContentListDTO> horizontalContentList;
        /**
         * jumpList
         */
        private List<JumpListDTO> jumpList;
        /**
         * cardAction
         */
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
            private String iconUrl;
            /**
             * desc
             */
            private String desc;
            /**
             * descColor
             */
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
            private String appid;
            /**
             * pagepath
             */
            private String pagepath;
            /**
             * title
             */
            private String title;
            /**
             * quoteText
             */
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
            private String appid;
            /**
             * pagepath
             */
            private String pagepath;
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
            private String keyname;
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
            private String appid;
            /**
             * pagepath
             */
            private String pagepath;
        }
    }
}
