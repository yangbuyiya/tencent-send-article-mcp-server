package com.yby6.mcp.server.tencent.mcpService.config;

import com.yby6.mcp.server.tencent.api.ITencentService;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Retrofit配置类
 * <p>
 * 该配置类用于配置和注册Retrofit相关的Bean。
 * 主要功能：
 * 1. 配置Retrofit实例
 * 2. 注册ITencentService接口的实现
 *
 * @author yby6
 * @version 1.0.0
 */
@Configuration
public class RetrofitConfig {

    /**
     * 腾讯云API基础URL
     */
    private static final String BASE_URL = "https://cloud.tencent.com/";

    /**
     * 配置并创建腾讯API服务实例
     * <p>
     * 该方法创建了一个配置了超时设置的OkHttpClient，并使用Retrofit构建API服务接口。
     * 主要配置包括：
     * - 连接超时：30秒
     * - 读取超时：30秒
     * - 写入超时：30秒
     *
     * @return 配置好的腾讯API服务接口实例
     */
    @Bean
    public ITencentService tencentService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return retrofit.create(ITencentService.class);
    }

} 