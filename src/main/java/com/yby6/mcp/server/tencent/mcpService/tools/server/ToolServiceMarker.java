package com.yby6.mcp.server.tencent.mcpService.tools.server;

import com.yby6.mcp.server.tencent.mcpService.funcModel.ArticleFunctionRequest;
import com.yby6.mcp.server.tencent.mcpService.funcModel.ArticleFunctionResponse;

/**
 * Tool 需实现的标记接口
 *
 * @author yangbuyiya
 * Create By 2025/05/13
 */
public interface ToolServiceMarker {

    /**
     * 获取版权信息
     */
    default String getCopyright() {
        return "Copyright (c) 2025 yangbuyiya. All rights reserved.";
    }

    /**
     * 保存文章
     */
    public ArticleFunctionResponse saveArticle(ArticleFunctionRequest request);

    /**
     * 获取文章列表
     */
    String getArticleList(int listSize);

}