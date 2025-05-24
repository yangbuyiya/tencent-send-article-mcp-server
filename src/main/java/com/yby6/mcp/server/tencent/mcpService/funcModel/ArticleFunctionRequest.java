package com.yby6.mcp.server.tencent.mcpService.funcModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 文章功能请求模型
 * <p>
 * 该模型类用于封装文章发布功能所需的请求参数。
 * 使用Jackson注解进行JSON序列化配置，确保必填字段的验证。
 * 使用Lombok的@Data注解自动生成getter、setter等方法。
 * <p>
 * 主要功能：
 * 1. 定义文章发布所需的基本信息
 * 2. 提供JSON序列化配置
 * 3. 支持必填字段验证
 *
 * @author yangbuyiya
 * @version 1.0.0
 * @since 2025/05/05
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleFunctionRequest {

    /**
     * 文章标题
     * <p>
     * 必填字段，用于显示文章的主标题。
     * 在JSON序列化时使用"title"作为字段名。
     */
    @JsonProperty(required = true, value = "title")
    @JsonPropertyDescription("文章标题")
    @ToolParam(description = "文章标题")
    private String title;

    /**
     * 文章内容
     * <p>
     * 必填字段，使用Markdown格式存储文章内容。
     * 在JSON序列化时使用"markdowncontent"作为字段名。
     * 支持富文本格式，包括标题、列表、代码块等。
     */
    @JsonProperty(required = true, value = "markdowncontent")
    @JsonPropertyDescription("文章内容")
    @ToolParam(description = "文章内容")
    private String markdowncontent;

    /**
     * 文章摘要
     * <p>
     * 必填字段，用于提供文章内容的简短描述。
     * 在JSON序列化时使用"userSummary"作为字段名。
     * 通常用于文章列表展示和SEO优化。
     */
    @JsonProperty(required = true, value = "userSummary")
    @JsonPropertyDescription("文章摘要")
    @ToolParam(description = "文章摘要")
    private String userSummary;
}
