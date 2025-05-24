package com.yby6.mcp.server.tencent.api.dto;

import com.yby6.mcp.server.tencent.mcpService.utils.MarkdownToProseMirrorConverter;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 腾讯云开发者社区发布文章请求DTO
 * <p>
 * 该DTO类用于封装发布文章到腾讯云开发者社区时所需的所有参数。
 * 包含了文章的基本信息、分类信息、标签信息、展示设置等多个维度的数据。
 * 使用Lombok的@Data注解自动生成getter、setter等方法。
 * <p>
 * 主要功能：
 * 1. 封装文章发布所需的所有参数
 * 2. 支持Markdown到ProseMirror格式的自动转换
 * 3. 提供文章分类、标签、专栏等管理功能
 *
 * @author yby6
 * @version 1.0.0
 */
@Data
public class AddArticleRequest {
    /**
     * 文章标题
     * 必填字段，用于显示文章的主标题
     */
    private String title;

    /**
     * 文章内容（JSON格式）
     * 包含富文本编辑器的完整内容结构
     * 使用ProseMirror格式存储，支持复杂的富文本编辑功能
     */
    private String content;

    /**
     * 文章纯文本内容
     * 用于SEO和摘要展示
     * 当content为空时，会自动将plain转换为ProseMirror格式
     */
    private String plain;

    /**
     * 文章来源类型
     * 1: 原创
     * 用于标识文章的创作类型
     */
    private Integer sourceType;

    /**
     * 文章分类ID列表
     * 用于文章分类管理
     * 支持多分类，每个分类对应一个ID
     */
    private List<Integer> classifyIds;

    /**
     * 文章标签ID列表
     * 用于文章标签管理
     * 支持多标签，每个标签对应一个ID
     */
    private List<Integer> tagIds;

    /**
     * 长尾标签列表
     * 用于SEO优化
     * 支持自定义长尾关键词，提升文章搜索可见性
     */
    private List<String> longtailTag;

    /**
     * 专栏ID列表
     * 用于文章专栏管理
     * 支持将文章添加到多个专栏中
     */
    private List<Integer> columnIds;

    /**
     * 是否开启评论
     * 1: 开启
     * 0: 关闭
     * 控制文章是否允许读者评论
     */
    private Integer openComment;

    /**
     * 是否关闭文本链接
     * 1: 关闭
     * 0: 开启
     * 控制文章中的文本链接是否可点击
     */
    private Integer closeTextLink;

    /**
     * 用户摘要
     * 用于文章预览展示
     * 提供文章内容的简短描述
     */
    private String userSummary;

    /**
     * 文章封面图片URL
     * 用于文章列表和详情页的展示
     * 支持自定义封面图片
     */
    private String pic;

    /**
     * 来源详情
     * 用于记录文章来源的详细信息
     * 包含来源类型、URL等额外信息
     */
    private Map<String, Object> sourceDetail;

    /**
     * 专区名称
     * 用于指定文章所属的专区
     * 支持文章分类到特定专区
     */
    private String zoneName;

    /**
     * 草稿ID
     * 如果是编辑已有草稿，需要提供此ID
     * 用于支持文章的草稿编辑功能
     */
    private Long draftId;

    /**
     * 获取ProseMirror格式的内容
     * <p>
     * 该方法用于获取文章内容的ProseMirror格式。
     * 如果content为空，则自动将plain转换为ProseMirror格式。
     * 确保文章内容始终以正确的格式提供给编辑器。
     *
     * @return ProseMirror格式的内容
     */
    public String getContent() {
        return MarkdownToProseMirrorConverter.convert(plain);
    }

}
