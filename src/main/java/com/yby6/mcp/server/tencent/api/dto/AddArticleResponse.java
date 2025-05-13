package com.yby6.mcp.server.tencent.api.dto;

import lombok.Data;

/**
 * 腾讯云开发者社区发布文章响应DTO
 *
 * 该DTO类用于封装发布文章到腾讯云开发者社区后的返回结果。
 * 包含了文章发布的状态信息和文章的唯一标识符。
 * 使用Lombok的@Data注解自动生成getter、setter等方法。
 *
 * 主要功能：
 * 1. 提供文章发布结果的反馈
 * 2. 返回新创建文章的ID
 * 3. 指示发布操作的成功或失败状态
 *
 * @author yby6
 * @version 1.0.0
 */
@Data
public class AddArticleResponse {
    /**
     * 文章ID
     * 发布成功后返回的文章唯一标识
     * 用于后续对文章进行编辑、删除等操作
     * 在发布失败时可能为null
     */
    private Long articleId;

    /**
     * 发布状态
     * 0: 成功
     * 非0: 失败
     * 用于快速判断文章发布是否成功
     * 具体的错误码需要参考腾讯云开发者社区的API文档
     */
    private Integer status;
}
