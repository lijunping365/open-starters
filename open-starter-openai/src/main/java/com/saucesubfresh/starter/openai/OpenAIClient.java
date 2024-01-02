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
package com.saucesubfresh.starter.openai;

import com.saucesubfresh.starter.openai.exception.OpenAIException;
import com.saucesubfresh.starter.openai.properties.OpenAIProperties;
import com.saucesubfresh.starter.openai.utils.JSON;
import com.theokanning.openai.DeleteResult;
import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.audio.CreateTranslationRequest;
import com.theokanning.openai.audio.TranscriptionResult;
import com.theokanning.openai.audio.TranslationResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.file.File;
import com.theokanning.openai.image.CreateImageEditRequest;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.CreateImageVariationRequest;
import com.theokanning.openai.image.ImageResult;
import com.theokanning.openai.model.Model;
import io.reactivex.Single;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;

/**
 * @author lijunping
 */
public class OpenAIClient {

    private final OpenAIApi openAIApi;
    private final OkHttpClient okHttpClient;
    private final OpenAIProperties properties;

    public OpenAIClient(OkHttpClient okHttpClient, OpenAIProperties properties) {
        this.okHttpClient = okHttpClient;
        this.properties = properties;
        this.openAIApi = new Retrofit.Builder()
            .baseUrl(properties.getBaseUrl())
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create())
            .build().create(OpenAIApi.class);
    }

    /**
     * openAi模型列表
     *
     * @return Model  list
     */
    public List<Model> models() {
        Single<OpenAiResponse<Model>> models = this.openAIApi.models();
        return models.blockingGet().getData();
    }


    /**
     * openAi模型详细信息
     *
     * @param id 模型主键
     * @return Model    模型类
     */
    public Model model(String id) {
        Single<Model> model = this.openAIApi.getModel(id);
        return model.blockingGet();
    }

    /**
     * 最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
     *
     * @param request 问答参数
     * @return 答案
     */
    public ChatCompletionResult chatCompletion(ChatCompletionRequest request) {
        Single<ChatCompletionResult> chatCompletionResponse = this.openAIApi.createChatCompletion(request);
        return chatCompletionResponse.blockingGet();
    }

    /**
     * 最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型 - 流式
     *
     * @param completion 问答参数
     * @return 答案
     */
    public void chatCompletionStream(ChatCompletionRequest completion, EventSourceListener eventSourceListener) {
        try {
            EventSource.Factory factory = EventSources.createFactory(this.okHttpClient);
            String requestBody = JSON.toJSON(completion);
            Request request = new Request.Builder()
                    .url(properties.getBaseUrl() + "v1/chat/completions")
                    .post(RequestBody.create(MediaType.parse("application/json"), requestBody))
                    .build();
            factory.newEventSource(request, eventSourceListener);
        } catch (Exception e) {
            throw new OpenAIException(e.getMessage());
        }
    }

    /**
     * 文本转换向量
     *
     * @param request 入参
     * @return EmbeddingResponse
     */
    public EmbeddingResult embeddings(EmbeddingRequest request) {
        Single<EmbeddingResult> embeddings = this.openAIApi.createEmbeddings(request);
        return embeddings.blockingGet();
    }

    /**
     * 获取文件列表
     *
     * @return File  list
     */
    public List<File> files() {
        Single<OpenAiResponse<File>> files = this.openAIApi.listFiles();
        return files.blockingGet().getData();
    }

    /**
     * 上传文件
     *
     * @param purpose purpose
     * @param file    文件对象
     * @return UploadFileResponse
     */
    public File uploadFile(String purpose, java.io.File file) {
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), fileBody);

        RequestBody purposeBody = RequestBody.create(MediaType.parse("multipart/form-data"), purpose);
        Single<File> uploadFileResponse = this.openAIApi.uploadFile(purposeBody, multipartBody);
        return uploadFileResponse.blockingGet();
    }

    /**
     * 删除文件
     *
     * @param fileId 文件id
     * @return DeleteResponse
     */
    public DeleteResult deleteFile(String fileId) {
        Single<DeleteResult> deleteFile = this.openAIApi.deleteFile(fileId);
        return deleteFile.blockingGet();
    }

    /**
     * 检索文件
     *
     * @param fileId 文件id
     * @return File
     */
    public File retrieveFile(String fileId) {
        Single<File> fileContent = this.openAIApi.retrieveFile(fileId);
        return fileContent.blockingGet();
    }

    /**
     * 根据描述生成图片
     *
     * @param request 图片参数
     * @return ImageResponse
     */
    public ImageResult genImages(CreateImageRequest request) {
        Single<ImageResult> edits = this.openAIApi.createImage(request);
        return edits.blockingGet();
    }

    /**
     * Creates an edited or extended image given an original image and a prompt.
     * 根据描述修改图片
     *
     * @param image     png格式的图片，最大4MB
     * @param mask      png格式的图片，最大4MB
     * @param request 图片参数
     * @return Item list
     */
    public ImageResult createImageEdit(CreateImageEditRequest request, java.io.File image, java.io.File mask) {
        RequestBody imageBody = RequestBody.create(MediaType.parse("image"), image);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MediaType.get("multipart/form-data"))
                .addFormDataPart("prompt", request.getPrompt())
                .addFormDataPart("size", request.getSize())
                .addFormDataPart("response_format", request.getResponseFormat())
                .addFormDataPart("image", "image", imageBody);

        if (request.getN() != null) {
            builder.addFormDataPart("n", request.getN().toString());
        }

        if (mask != null) {
            RequestBody maskBody = RequestBody.create(MediaType.parse("image"), mask);
            builder.addFormDataPart("mask", "mask", maskBody);
        }
        Single<ImageResult> imageEdit = this.openAIApi.createImageEdit(builder.build());

        return imageEdit.blockingGet();
    }

    /**
     * Creates a variation of a given image.
     * <p>
     * 变化图片，类似ai重做图片
     *
     * @param image           图片对象
     * @param request 图片参数
     * @return ImageResponse
     */
    public ImageResult createImageVariation(CreateImageVariationRequest request, java.io.File image) {
        RequestBody imageBody = RequestBody.create(MediaType.parse("image"), image);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MediaType.get("multipart/form-data"))
                .addFormDataPart("size", request.getSize())
                .addFormDataPart("response_format", request.getResponseFormat())
                .addFormDataPart("image", "image", imageBody);

        if (request.getN() != null) {
            builder.addFormDataPart("n", request.getN().toString());
        }

        Single<ImageResult> imageVariation = this.openAIApi.createImageVariation(builder.build());
        return imageVariation.blockingGet();
    }

    /**
     * 语音转文字
     *
     * @param request 参数
     * @param audio           语音文件 最大支持25MB mp3, mp4, mpeg, mpga, m4a, wav, webm
     * @return 语音文本
     */
    public TranscriptionResult createTranscription(CreateTranscriptionRequest request, java.io.File audio) {
        RequestBody audioBody = RequestBody.create(MediaType.parse("audio"), audio);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MediaType.get("multipart/form-data"))
                .addFormDataPart("model", request.getModel())
                .addFormDataPart("file", audio.getName(), audioBody);

        if (request.getPrompt() != null) {
            builder.addFormDataPart("prompt", request.getPrompt());
        }
        if (request.getResponseFormat() != null) {
            builder.addFormDataPart("response_format", request.getResponseFormat());
        }
        if (request.getTemperature() != null) {
            builder.addFormDataPart("temperature", request.getTemperature().toString());
        }
        if (request.getLanguage() != null) {
            builder.addFormDataPart("language", request.getLanguage());
        }

        Single<TranscriptionResult> transcriptionResultSingle = this.openAIApi.speechToTextTranscriptions(builder.build());
        return transcriptionResultSingle.blockingGet();
    }

    /**
     * 语音翻译：目前仅支持翻译为英文
     *
     * @param request 参数
     * @param audio         语音文件 最大支持25MB mp3, mp4, mpeg, mpga, m4a, wav, webm
     * @return 翻译后文本
     */
    public TranslationResult createTranslation(CreateTranslationRequest request, java.io.File audio) {
        RequestBody audioBody = RequestBody.create(MediaType.parse("audio"), audio);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MediaType.get("multipart/form-data"))
                .addFormDataPart("model", request.getModel())
                .addFormDataPart("file", audio.getName(), audioBody);

        if (request.getResponseFormat() != null) {
            builder.addFormDataPart("response_format", request.getResponseFormat());
        }
        if (request.getPrompt() != null) {
            builder.addFormDataPart("prompt", request.getPrompt());
        }
        if (request.getTemperature() != null) {
            builder.addFormDataPart("temperature", request.getTemperature().toString());
        }

        Single<TranslationResult> translationResultSingle = this.openAIApi.speechToTextTranslations(builder.build());
        return translationResultSingle.blockingGet();
    }

}
