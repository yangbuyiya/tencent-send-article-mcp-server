package com.yby6.mcp.server.tencent.mcpService.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;

import java.util.UUID;

/**
 * Markdown到ProseMirror格式转换器
 * <p>
 * 该工具类用于将Markdown格式的文本转换为ProseMirror编辑器可用的JSON格式。
 * ProseMirror是一个富文本编辑器框架，需要特定的JSON结构来表示文档内容。
 * <p>
 * 主要功能：
 * 1. 解析Markdown文本为AST（抽象语法树）
 * 2. 将AST转换为ProseMirror格式的JSON
 * 3. 支持段落、文本、标题等基本元素的转换
 * <p>
 * 技术特点：
 * - 使用commonmark-java库解析Markdown
 * - 使用Jackson处理JSON
 * - 采用递归方式处理文档树结构
 * <p>
 * 使用示例：
 * ```java
 * String markdown = "# 标题\n这是一段文本";
 * String proseMirrorJson = MarkdownToProseMirrorConverter.convert(markdown);
 * ```
 * <p>
 * 注意事项：
 * 1. 输入必须是有效的Markdown格式
 * 2. 转换过程可能抛出RuntimeException
 * 3. 输出是符合ProseMirror规范的JSON字符串
 *
 * @author yby6
 * @since 2025/05/25
 */
public class MarkdownToProseMirrorConverter {
    /**
     * JSON对象映射器
     * 用于创建和操作JSON节点，处理JSON序列化和反序列化
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Markdown解析器
     * 用于将Markdown文本解析为AST（抽象语法树）
     * 使用commonmark-java库的Parser实现
     */
    private static final Parser parser = Parser.builder().build();

    /**
     * 文本渲染器
     * 用于将AST节点渲染为纯文本
     * 主要用于调试和日志记录
     */
    private static final TextContentRenderer textRenderer = TextContentRenderer.builder().build();

    /**
     * 私有构造函数
     * <p>
     * 防止工具类被实例化，所有方法都是静态的。
     * 符合工具类的最佳实践。
     */
    private MarkdownToProseMirrorConverter() {
        // 私有构造函数，防止实例化
    }

    /**
     * 将Markdown文本转换为ProseMirror格式
     * <p>
     * 该方法执行以下步骤：
     * 1. 解析Markdown文本为AST
     * 2. 创建ProseMirror文档结构
     * 3. 递归处理AST节点
     * 4. 返回JSON字符串
     * <p>
     * 转换过程：
     * - 首先创建文档根节点
     * - 然后递归处理所有子节点
     * - 最后将结果序列化为JSON字符串
     * <p>
     * 错误处理：
     * - 捕获所有可能的异常
     * - 将异常包装为RuntimeException
     * - 提供详细的错误信息
     *
     * @param markdown 要转换的Markdown文本，不能为null
     * @return ProseMirror格式的JSON字符串
     * @throws RuntimeException 当转换过程中发生错误时抛出
     */
    public static String convert(String markdown) {
        try {
            // 格式化Markdown
            markdown = formatMarkdown(markdown);

            // 解析 Markdown
            Node document = parser.parse(markdown);

            // 创建 ProseMirror 文档结构
            ObjectNode doc = objectMapper.createObjectNode();
            doc.put("type", "doc");

            ArrayNode content = objectMapper.createArrayNode();
            doc.set("content", content);

            // 处理文档节点
            processNode(document, content);

            return objectMapper.writeValueAsString(doc);
        } catch (Exception e) {
            throw new RuntimeException("转换失败", e);
        }
    }

    /**
     * 格式化Markdown文本
     * 处理换行符和特殊字符，确保Markdown格式正确
     *
     * @param markdown 原始Markdown文本
     * @return 格式化后的Markdown文本
     */
    private static String formatMarkdown(String markdown) {
        if (markdown == null || markdown.trim().isEmpty()) {
            return "";
        }

        // 1. 统一换行符为\n
        String formatted = markdown.replace("\r\n", "\n").replace("\r", "\n");

        // 2. 处理连续的空行，最多保留两个
        formatted = formatted.replaceAll("\n{3,}", "\n\n");

        // 3. 确保标题前后有空行
        formatted = formatted.replaceAll("(?m)^(#{1,6}\\s.*?)$", "\n$1\n");

        // 4. 确保列表项前后有空行
        formatted = formatted.replaceAll("(?m)^([*+-]\\s.*?)$", "\n$1\n");

        // 5. 确保代码块前后有空行
        formatted = formatted.replaceAll("(?m)^(```.*?)$", "\n$1\n");

        // 6. 移除开头的空行
        formatted = formatted.replaceAll("^\n+", "");

        // 7. 移除结尾的空行
        formatted = formatted.replaceAll("\n+$", "\n");

        return formatted;
    }

    /**
     * 处理AST节点并转换为ProseMirror格式
     * <p>
     * 该方法递归处理不同类型的节点：
     * - 段落节点：创建段落结构
     * - 文本节点：创建文本内容
     * - 标题节点：创建标题结构
     * - 其他节点：递归处理子节点
     * <p>
     * 节点处理流程：
     * 1. 判断节点类型
     * 2. 创建对应的ProseMirror节点
     * 3. 设置节点属性
     * 4. 递归处理子节点
     *
     * @param node    要处理的AST节点，不能为null
     * @param content 用于存储转换结果的JSON数组节点，不能为null
     */
    private static void processNode(Node node, ArrayNode content) {
        if (node instanceof Paragraph) {
            // 处理段落
            ObjectNode paragraph = createParagraphNode();
            ArrayNode paragraphContent = objectMapper.createArrayNode();
            paragraph.set("content", paragraphContent);

            // 处理段落内的子节点
            Node child = node.getFirstChild();
            while (child != null) {
                processNode(child, paragraphContent);
                child = child.getNext();
            }

            content.add(paragraph);
        } else if (node instanceof Text) {
            // 处理文本节点
            ObjectNode text = objectMapper.createObjectNode();
            text.put("type", "text");
            text.put("text", ((Text) node).getLiteral());
            
            // 检查父节点是否为链接
            Node parent = node.getParent();
            if (parent instanceof Link) {
                ArrayNode marks = objectMapper.createArrayNode();
                ObjectNode linkMark = objectMapper.createObjectNode();
                linkMark.put("type", "link");
                
                ObjectNode linkAttrs = objectMapper.createObjectNode();
                linkAttrs.put("href", ((Link) parent).getDestination());
                linkAttrs.put("target", "_blank");
                linkAttrs.put("rel", "noopener noreferrer nofollow");
                linkAttrs.put("class", (String) null);
                
                linkMark.set("attrs", linkAttrs);
                marks.add(linkMark);
                text.set("marks", marks);
            }
            
            content.add(text);
        } else if (node instanceof Heading) {
            // 处理标题
            ObjectNode heading = createHeadingNode((Heading) node);
            ArrayNode headingContent = objectMapper.createArrayNode();
            heading.set("content", headingContent);

            // 处理标题内的子节点
            Node child = node.getFirstChild();
            while (child != null) {
                processNode(child, headingContent);
                child = child.getNext();
            }

            content.add(heading);
        } else if (node instanceof FencedCodeBlock) {
            // 处理代码块和高亮块
            FencedCodeBlock codeBlock = (FencedCodeBlock) node;
            String info = codeBlock.getInfo();
            
            if ("高亮".equals(info)) {
                // 处理高亮块
                ObjectNode highlightBlock = createHighlightBlockNode();
                ArrayNode highlightContent = objectMapper.createArrayNode();
                highlightBlock.set("content", highlightContent);

                ObjectNode text = objectMapper.createObjectNode();
                text.put("type", "text");
                text.put("text", codeBlock.getLiteral().trim());
                highlightContent.add(text);

                content.add(highlightBlock);
            } else {
                // 处理普通代码块
                ObjectNode codeBlockNode = createCodeBlockNode(codeBlock);
                ArrayNode codeContent = objectMapper.createArrayNode();
                codeBlockNode.set("content", codeContent);

                ObjectNode text = objectMapper.createObjectNode();
                text.put("type", "text");
                text.put("text", codeBlock.getLiteral());
                codeContent.add(text);

                content.add(codeBlockNode);
            }
        } else if (node instanceof BlockQuote) {
            // 处理引用块
            ObjectNode blockQuote = createBlockQuoteNode();
            ArrayNode quoteContent = objectMapper.createArrayNode();
            blockQuote.set("content", quoteContent);

            Node child = node.getFirstChild();
            while (child != null) {
                processNode(child, quoteContent);
                child = child.getNext();
            }

            content.add(blockQuote);
        } else if (node instanceof Image) {
            // 处理图片
            ObjectNode image = createImageNode((Image) node);
            content.add(image);
        } else if (node instanceof Link) {
            // 处理链接 - 现在链接作为文本节点的mark处理
            Node child = node.getFirstChild();
            while (child != null) {
                processNode(child, content);
                child = child.getNext();
            }
        } else {
            // 处理其他类型的节点
            Node child = node.getFirstChild();
            while (child != null) {
                processNode(child, content);
                child = child.getNext();
            }
        }
    }

    /**
     * 创建段落节点
     * <p>
     * 创建具有以下属性的段落节点：
     * - 唯一ID：使用UUID生成
     * - 文本对齐方式：默认为inherit
     * - 缩进级别：默认为0
     * - 文本颜色：默认为null
     * - 背景颜色：默认为null
     * - 拖拽句柄状态：默认为false
     * <p>
     * 节点结构：
     * ```json
     * {
     * "type": "paragraph",
     * "attrs": {
     * "id": "uuid",
     * "textAlign": "inherit",
     * "indent": 0,
     * "color": null,
     * "background": null,
     * "isHoverDragHandle": false
     * }
     * }
     * ```
     *
     * @return 配置好的段落节点
     */
    private static ObjectNode createParagraphNode() {
        ObjectNode paragraph = objectMapper.createObjectNode();
        paragraph.put("type", "paragraph");

        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", UUID.randomUUID().toString());
        attrs.put("textAlign", "inherit");
        attrs.put("indent", 0);
        attrs.put("color", (String) null);
        attrs.put("background", (String) null);
        attrs.put("isHoverDragHandle", false);

        paragraph.set("attrs", attrs);
        return paragraph;
    }

    private static ObjectNode createHeadingNode(Heading heading) {
        ObjectNode headingNode = objectMapper.createObjectNode();
        headingNode.put("type", "heading");

        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", UUID.randomUUID().toString());
        attrs.put("textAlign", "inherit");
        attrs.put("indent", 0);
        attrs.put("level", heading.getLevel());
        attrs.put("isHoverDragHandle", false);

        headingNode.set("attrs", attrs);
        return headingNode;
    }

    private static ObjectNode createCodeBlockNode(FencedCodeBlock codeBlock) {
        ObjectNode codeBlockNode = objectMapper.createObjectNode();
        codeBlockNode.put("type", "codeBlock");

        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", UUID.randomUUID().toString());
        attrs.put("language", codeBlock.getInfo());
        attrs.put("theme", "atom-one-dark");
        attrs.put("runtimes", 0);
        attrs.put("isHoverDragHandle", false);
        attrs.put("key", UUID.randomUUID().toString().substring(0, 5));
        attrs.put("languageByAi", codeBlock.getInfo());

        codeBlockNode.set("attrs", attrs);
        return codeBlockNode;
    }

    private static ObjectNode createBlockQuoteNode() {
        ObjectNode blockQuote = objectMapper.createObjectNode();
        blockQuote.put("type", "blockquote");

        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", UUID.randomUUID().toString());
        attrs.put("textAlign", "inherit");
        attrs.put("isHoverDragHandle", false);

        blockQuote.set("attrs", attrs);
        return blockQuote;
    }

    /**
     * 创建高亮块节点
     * <p>
     * 创建具有以下属性的高亮块节点：
     * - 唯一ID：使用UUID生成
     * - 颜色：默认为空
     * - 背景色：默认为rgba(194, 239, 214, 1)
     * - 边框颜色：默认为rgba(41, 199, 112, 1)
     * <p>
     * 节点结构：
     * ```json
     * {
     * "type": "highlightBlock",
     * "attrs": {
     * "id": "uuid",
     * "color": "",
     * "background": "rgba(194, 239, 214, 1)",
     * "border": "rgba(41, 199, 112, 1)",
     * "isHoverDragHandle": false
     * }
     * }
     * ```
     *
     * @return 配置好的高亮块节点
     */
    private static ObjectNode createHighlightBlockNode() {
        ObjectNode highlightBlock = objectMapper.createObjectNode();
        highlightBlock.put("type", "highlightBlock");

        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", UUID.randomUUID().toString());
        attrs.put("color", "");
        attrs.put("background", "rgba(194, 239, 214, 1)");
        attrs.put("border", "rgba(41, 199, 112, 1)");
        attrs.put("isHoverDragHandle", false);

        highlightBlock.set("attrs", attrs);
        return highlightBlock;
    }

    /**
     * 创建图片节点
     * <p>
     * 创建具有以下属性的图片节点：
     * - 唯一ID：使用UUID生成
     * - 图片URL：从Image节点获取
     * - 文件扩展名：从URL中提取
     * - 对齐方式：默认为center
     * - 其他属性：按照平台要求设置
     * <p>
     * 节点结构：
     * ```json
     * {
     * "type": "image",
     * "attrs": {
     * "id": "uuid",
     * "src": "图片URL",
     * "extension": "文件扩展名",
     * "align": "center",
     * "alt": "",
     * "showAlt": false,
     * "href": "",
     * "boxShadow": "",
     * "width": 504,
     * "aspectRatio": "1.300676",
     * "status": "success",
     * "showText": true,
     * "isPercentage": false,
     * "percentage": 0,
     * "isHoverDragHandle": false
     * }
     * }
     * ```
     *
     * @param image 要处理的图片节点
     * @return 配置好的图片节点
     */
    private static ObjectNode createImageNode(Image image) {
        ObjectNode imageNode = objectMapper.createObjectNode();
        imageNode.put("type", "image");

        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", UUID.randomUUID().toString());
        
        // 处理图片URL
        String imageUrl = image.getDestination();
        attrs.put("src", imageUrl);
        
        // 处理文件扩展名
        String extension = getFileExtension(imageUrl);
        attrs.put("extension", extension);
        
        // 设置其他属性
        attrs.put("align", "center");
        attrs.put("alt", image.getTitle() != null ? image.getTitle() : "");
        attrs.put("showAlt", false);
        attrs.put("href", "");
        attrs.put("boxShadow", "");
        attrs.put("width", 504);
        attrs.put("aspectRatio", "1.300676");
        attrs.put("status", "success");
        attrs.put("showText", true);
        attrs.put("isPercentage", false);
        attrs.put("percentage", 0);
        attrs.put("isHoverDragHandle", false);

        imageNode.set("attrs", attrs);
        return imageNode;
    }

    /**
     * 获取文件扩展名
     * <p>
     * 从URL中提取文件扩展名，处理以下情况：
     * 1. 普通URL：从最后一个点号后提取
     * 2. 带查询参数的URL：先去除查询参数再提取
     * 3. 空URL：返回空字符串
     *
     * @param url 文件URL
     * @return 文件扩展名
     */
    private static String getFileExtension(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }
        
        // 移除查询参数
        String cleanUrl = url.split("\\?")[0];
        
        // 获取扩展名
        int lastDotIndex = cleanUrl.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return cleanUrl.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }
}
