package com.yby6.mcp.server.tencent.api;

import com.yby6.mcp.server.tencent.api.dto.AddArticleRequest;
import com.yby6.mcp.server.tencent.api.dto.AddArticleResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * 腾讯云开发者社区服务接口
 * <p>
 * 该接口定义了与腾讯云开发者社区API交互的方法。
 * 使用Retrofit框架实现HTTP请求，支持文章的发布等操作。
 * 所有请求都需要包含必要的认证信息和请求头。
 *
 * @author yby6
 * @version 1.0.0
 */
public interface ITencentService {

    /**
     * 发布文章到腾讯云开发者社区
     * <p>
     * 该方法通过HTTP POST请求将文章发布到腾讯云开发者社区。
     * 请求包含完整的HTTP头信息，模拟浏览器行为，确保请求能够被正确处理。
     * <p>
     * 请求头说明：
     * - accept: 指定接受的响应类型
     * - content-type: 指定请求体类型为JSON
     * - origin/referer: 指定请求来源
     * - user-agent: 指定客户端信息
     * - 其他安全相关头部
     *
     * @param cookie  用户认证Cookie，用于身份验证
     * @param request 文章发布请求，包含文章内容、标题等信息
     * @return 包含发布结果的响应对象
     */
    @POST("https://cloud.tencent.com/developer/api/article/addArticle")
    @Headers({
            "accept: application/json, text/plain, */*",
            "accept-language: zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7",
            "content-type: application/json",
            "origin: https://cloud.tencent.com",
            "priority: u=1, i",
            "referer: https://cloud.tencent.com/developer/article/write-new",
            "sec-ch-ua: \"Google Chrome\";v=\"135\", \"Not-A.Brand\";v=\"8\", \"Chromium\";v=\"135\"",
            "sec-ch-ua-mobile: ?0",
            "sec-ch-ua-platform: \"Windows\"",
            "sec-fetch-dest: empty",
            "sec-fetch-mode: cors",
            "sec-fetch-site: same-origin",
            "sec-gpc: 1",
            "user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36"
    })
    Call<AddArticleResponse> addArticle(
            @Header("Cookie") String cookie,
            @Body AddArticleRequest request
    );
}
