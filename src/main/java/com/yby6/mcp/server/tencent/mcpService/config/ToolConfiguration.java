package com.yby6.mcp.server.tencent.mcpService.config;

import com.yby6.mcp.server.tencent.mcpService.tools.McpToolServer;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工具类配置
 * 用于批量注册和管理所有工具类
 *
 * @author yangbuyiya
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
    public ToolCallbackProvider toolCallbackProvider(McpToolServer mcpToolServer) {
        MethodToolCallbackProvider.Builder builder = MethodToolCallbackProvider
                .builder()
                .toolObjects(mcpToolServer);
        return builder.build();
    }

} 