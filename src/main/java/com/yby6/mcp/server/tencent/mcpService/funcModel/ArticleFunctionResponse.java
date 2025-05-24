package com.yby6.mcp.server.tencent.mcpService.funcModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

/**
 * 文章功能响应模型
 * <p>
 * 该模型类用于封装文章发布功能的结果响应。
 * 使用Jackson注解进行JSON序列化配置，确保必填字段的验证。
 * 使用Lombok的@Data注解自动生成getter、setter等方法。
 * <p>
 * 主要功能：
 * 1. 提供文章发布结果的反馈信息
 * 2. 包含文章的唯一标识和访问链接
 * 3. 指示发布操作的成功或失败状态
 *
 * @author yangbuyiya
 * @version 1.0.0
 * @since 2025/05/05
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleFunctionResponse {
    
    /**
     * 文章ID
     * <p>
     * 必填字段，发布成功后返回的文章唯一标识。
     * 在JSON序列化时使用"articleId"作为字段名。
     * 用于后续对文章进行编辑、删除等操作。
     * 在发布失败时可能为null。
     */
    @JsonProperty(required = true, value = "articleId")
    @JsonPropertyDescription("articleId")
    private Long articleId;
    
    /**
     * 发布状态
     * <p>
     * 必填字段，用于指示文章发布的结果。
     * 在JSON序列化时使用"status"作为字段名。
     * 状态码说明：
     * - 0: 发布成功
     * - 非0: 发布失败，具体错误码需要参考API文档
     */
    @JsonProperty(required = true, value = "status")
    @JsonPropertyDescription("status")
    private Integer status;
    
    /**
     * 文章链接
     * <p>
     * 必填字段，发布成功后返回的文章访问URL。
     * 在JSON序列化时使用"url"作为字段名。
     * 用于直接访问已发布的文章。
     * 在发布失败时可能为null。
     */
    @JsonProperty(required = true, value = "url")
    @JsonPropertyDescription("url")
    private String url;
    
    /**
     * 工具版权信息
     */
    @JsonProperty(required = true, value = "copyright")
    @JsonPropertyDescription("copyright")
    private String copyright;
}
