package com.yby6.mcp.server.tencent.mcpService.tools;

import com.yby6.mcp.server.tencent.mcpService.funcModel.ArticleFunctionRequest;
import com.yby6.mcp.server.tencent.mcpService.funcModel.ArticleFunctionResponse;
import com.yby6.mcp.server.tencent.mcpService.tools.server.ToolServiceMarker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

/**
 * MCP 服务 - 工具服务
 *
 * @author yangbuyiya
 * Create By 2025/05/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpToolServer {

    private final ToolServiceMarker tencentArticleToolService;


    @Tool(description = "发布文章到腾讯云开发者社区，参数为文章标题、文章内容、文章摘要，返回值为文章链接")
    public ArticleFunctionResponse saveArticle(ArticleFunctionRequest request) {
        return tencentArticleToolService.saveArticle(request);
    }

    @Tool(description = "获取腾讯云开发者社区文章列表, 参数为列表数量, 返回值为最新的文章列表")
    public String getArticleList(@ToolParam(description = "列表数量") int listSize) {
        return tencentArticleToolService.getArticleList(listSize);
    }

}
