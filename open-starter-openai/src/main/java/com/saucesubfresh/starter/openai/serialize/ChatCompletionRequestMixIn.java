package com.saucesubfresh.starter.openai.serialize;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;

/**
 * @author lijunping
 */
public abstract class ChatCompletionRequestMixIn {

    @JsonSerialize(using = ChatCompletionRequestSerializer.Serializer.class)
    @JsonDeserialize(using = ChatCompletionRequestSerializer.Deserializer.class)
    abstract ChatCompletionRequest.ChatCompletionRequestFunctionCall getFunctionCall();

}
