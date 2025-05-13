package com.yby6.mcp.server.tencent.mcpService.tools;

/**
 * 所有Tool Service需实现的标记接口，用于自动批量注册。
 */
public interface ToolServiceMarker {

    /**
     * 获取版权信息
     */
    default String getCopyright() {
        return "Copyright (c) 2025 yangbuyiya. All rights reserved.";
    }

}