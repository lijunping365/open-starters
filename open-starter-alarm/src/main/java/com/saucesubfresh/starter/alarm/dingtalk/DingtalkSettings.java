package com.saucesubfresh.starter.alarm.dingtalk;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class DingtalkSettings {

    private String textTemplate;
    @Builder.Default
    private List<WebHookUrl> webhooks = new ArrayList<>();

    @AllArgsConstructor
    @Setter
    @Getter
    @ToString
    public static class WebHookUrl {
        private final String secret;
        private final String url;
    }
}
