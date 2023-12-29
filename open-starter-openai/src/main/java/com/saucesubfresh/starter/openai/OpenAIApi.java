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

import com.theokanning.openai.DeleteResult;
import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.audio.TranscriptionResult;
import com.theokanning.openai.audio.TranslationResult;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.edit.EditRequest;
import com.theokanning.openai.edit.EditResult;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.file.File;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.ImageResult;
import com.theokanning.openai.model.Model;
import com.theokanning.openai.moderation.ModerationRequest;
import com.theokanning.openai.moderation.ModerationResult;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

/**
 * @author lijunping
 */
public interface OpenAIApi {

    /**
     * 模型列表
     *
     * @return Single ModelResponse
     */
    @GET("v1/models")
    Single<OpenAiResponse<Model>> listModels();

    /**
     * models 返回的数据id
     *
     * @param modelId 模型主键
     * @return Single Model
     */
    @GET("v1/models/{model_id}")
    Single<Model> getModel(@Path("model_id") String modelId);

    /**
     * 文本问答
     * Given a prompt, the model will return one or more predicted completions, and can also return the probabilities of alternative tokens at each position.
     *
     * @param request 问答参数
     * @return Single CompletionResponse
     */
    @POST("v1/completions")
    Single<CompletionResult> createCompletion(@Body CompletionRequest request);

    /**
     * 文本问答-流式
     * Given a prompt, the model will return one or more predicted completions, and can also return the probabilities of alternative tokens at each position.
     *
     * @param request 问答参数
     * @return Single CompletionResponse
     */
    @Streaming
    @POST("v1/completions")
    Call<ResponseBody> createCompletionStream(@Body CompletionRequest request);

    /**
     * 最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
     *
     * @param request chat completion
     * @return 返回答案
     */
    @POST("v1/chat/completions")
    Single<ChatCompletionResult> createChatCompletion(@Body ChatCompletionRequest request);

    /**
     * 最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型 - 流式
     *
     * @param request chat completion
     * @return 返回答案
     */
    @Streaming
    @POST("v1/chat/completions")
    Call<ResponseBody> createChatCompletionStream(@Body ChatCompletionRequest request);

    /**
     * Creates a new edit for the provided input, instruction, and parameters.
     * 文本修复
     *
     * @param request 编辑参数
     * @return Single EditResponse
     */
    @POST("v1/edits")
    Single<EditResult> createEdit(@Body EditRequest request);

    /**
     * 文本向量计算
     *
     * @param request 向量参数
     * @return Single EmbeddingResponse
     */
    @POST("v1/embeddings")
    Single<EmbeddingResult> createEmbeddings(@Body EmbeddingRequest request);

    /**
     * Returns a list of files that belong to the user's organization.
     *
     * @return Single OpenAiResponse File
     */
    @GET("v1/files")
    Single<OpenAiResponse<File>> listFiles();

    /**
     * 上传文件
     *
     * @param purpose purpose
     * @param file    文件对象
     * @return Single UploadFileResponse
     */
    @Multipart
    @POST("v1/files")
    Single<File> uploadFile(@Part("purpose") RequestBody purpose, @Part MultipartBody.Part file);

    /**
     * 删除文件
     *
     * @param fileId 文件id
     * @return Single DeleteResponse
     */
    @DELETE("v1/files/{file_id}")
    Single<DeleteResult> deleteFile(@Path("file_id") String fileId);

    /**
     * 检索文件
     *
     * @param fileId 文件id
     * @return Single File
     */
    @GET("v1/files/{file_id}")
    Single<File> retrieveFile(@Path("file_id") String fileId);

    /**
     * Creates an image given a prompt.
     * 根据描述生成图片
     *
     * @param request 图片对象
     * @return Single ImageResponse
     */
    @POST("v1/images/generations")
    Single<ImageResult> createImage(@Body CreateImageRequest request);

    /**
     * Creates an edited or extended image given an original image and a prompt.
     * 根据描述修改图片
     *
     * @param image          图片对象
     * @param mask           图片对象
     * @param requestBodyMap 请求参数
     * @return Single ImageResponse
     */
    @Multipart
    @POST("v1/images/edits")
    Single<ImageResult> createImageEdit(@Part() MultipartBody.Part image, @Part() MultipartBody.Part mask, @PartMap() Map<String, RequestBody> requestBodyMap);

    /**
     * Creates a variation of a given image.
     *
     * @param image          图片对象
     * @param requestBodyMap 请求参数
     * @return Single ImageResponse
     */
    @Multipart
    @POST("v1/images/variations")
    Single<ImageResult> createImageVariation(@Part() MultipartBody.Part image, @PartMap() Map<String, RequestBody> requestBodyMap);

    /**
     * 语音转文字
     *
     * @param file           语音文件
     * @param requestBodyMap 参数
     * @return 文本
     */
    @Multipart
    @POST("v1/audio/transcriptions")
    Single<TranscriptionResult> speechToTextTranscriptions(@Part MultipartBody.Part file, @PartMap() Map<String, RequestBody> requestBodyMap);

    /**
     * 语音翻译：目前仅支持翻译为英文
     *
     * @param file           语音文件
     * @param requestBodyMap 参数
     * @return 文本
     */
    @Multipart
    @POST("v1/audio/translations")
    Single<TranslationResult> speechToTextTranslations(@Part MultipartBody.Part file, @PartMap() Map<String, RequestBody> requestBodyMap);

    /**
     * 文本审核
     *
     * @param request 文本审核参数
     * @return Single ModerationResponse
     */
    @POST("v1/moderations")
    Single<ModerationResult> createModeration(@Body ModerationRequest request);
}
