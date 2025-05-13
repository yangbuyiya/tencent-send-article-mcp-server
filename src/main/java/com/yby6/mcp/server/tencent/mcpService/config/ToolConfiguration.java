package com.yby6.mcp.server.tencent.mcpService.config;

import com.yby6.mcp.server.tencent.mcpService.tools.TencentArticleToolService;
import com.yby6.mcp.server.tencent.mcpService.tools.ToolServiceMarker;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 工具类配置
 * 用于批量注册和管理所有工具类
 *
 * @author Yang Shuai
 * Create By 2025/05/13
 */
@Configuration
public class ToolConfiguration {
    /**
     * 工具回调提供程序
     *
     * @param tencentArticleToolService 腾讯文章工具服务
     * @return {@code ToolCallbackProvider }
     */
    @Bean
    public ToolCallbackProvider toolCallbackProvider(
            TencentArticleToolService tencentArticleToolService
            // ....这里继续添加其他工具类
    ) {
        MethodToolCallbackProvider.Builder builder = MethodToolCallbackProvider.builder()
                .toolObjects(tencentArticleToolService);
        return builder.build();
    }
} 