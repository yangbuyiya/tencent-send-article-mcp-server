package com.yby6.mcp.server.tencent.mcpService.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Markdown到ProseMirror格式转换工具类
 * <p>
 * 该工具类提供将Markdown文本转换为ProseMirror JSON格式的功能。
 * ProseMirror是一个用于构建富文本编辑器的框架，使用特定的JSON结构表示文档。
 * 本转换器支持基本的Markdown语法，包括标题、段落、列表、代码块、链接、强调等。
 * <p>
 * 主要功能：
 * 1. 解析Markdown文本生成语法树
 * 2. 将语法树转换为ProseMirror节点结构
 * 3. 生成符合ProseMirror格式的JSON字符串
 *
 * @author yby6
 * @version 1.0.0
 */
@Component
public class MarkdownToProseMirrorConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Parser parser = Parser.builder().build();

    /**
     * 将Markdown文本转换为ProseMirror格式的JSON字符串
     *
     * @param markdown Markdown格式的文本
     * @return ProseMirror格式的JSON字符串
     */
    public String convert(String markdown) {
        if (markdown == null || markdown.trim().isEmpty()) {
            return createEmptyDocument();
        }

        // 解析Markdown生成语法树
        Node document = parser.parse(markdown);

        // 创建ProseMirror文档根节点
        ObjectNode proseMirrorDoc = objectMapper.createObjectNode();
        proseMirrorDoc.put("type", "doc");
        ArrayNode content = proseMirrorDoc.putArray("content");

        // 转换Markdown节点为ProseMirror节点
        convertNode(document, content);

        try {
            return objectMapper.writeValueAsString(proseMirrorDoc);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Markdown to ProseMirror format", e);
        }
    }

    /**
     * 创建一个空的ProseMirror文档
     *
     * @return 空文档的JSON字符串
     */
    private String createEmptyDocument() {
        ObjectNode proseMirrorDoc = objectMapper.createObjectNode();
        proseMirrorDoc.put("type", "doc");
        ArrayNode content = proseMirrorDoc.putArray("content");
        
        // 添加一个空段落
        ObjectNode paragraph = objectMapper.createObjectNode();
        paragraph.put("type", "paragraph");
        
        // 添加ID属性
        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", generateId());
        paragraph.set("attrs", attrs);
        
        // 添加空内容数组
        paragraph.putArray("content");
        
        content.add(paragraph);
        
        try {
            return objectMapper.writeValueAsString(proseMirrorDoc);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create empty ProseMirror document", e);
        }
    }

    /**
     * 递归转换Markdown节点为ProseMirror节点
     *
     * @param node    当前处理的Markdown节点
     * @param content 当前ProseMirror内容数组
     */
    private void convertNode(Node node, ArrayNode content) {
        // 处理不同类型的节点
        if (node instanceof Document) {
            // 文档节点，处理其子节点
            processChildren(node, content);
        } else if (node instanceof Heading) {
            // 标题节点
            processHeading((Heading) node, content);
        } else if (node instanceof Paragraph) {
            // 段落节点
            processParagraph((Paragraph) node, content);
        } else if (node instanceof BulletList) {
            // 无序列表
            processBulletList((BulletList) node, content);
        } else if (node instanceof OrderedList) {
            // 有序列表
            processOrderedList((OrderedList) node, content);
        } else if (node instanceof ListItem) {
            // 列表项
            processListItem((ListItem) node, content);
        } else if (node instanceof FencedCodeBlock) {
            // 代码块
            processCodeBlock((FencedCodeBlock) node, content);
        } else if (node instanceof BlockQuote) {
            // 引用块
            processBlockQuote((BlockQuote) node, content);
        } else if (node instanceof ThematicBreak) {
            // 分隔线
            processThematicBreak(content);
        } else {
            // 其他节点类型，处理其子节点
            processChildren(node, content);
        }
    }

    /**
     * 处理节点的所有子节点
     *
     * @param parent  父节点
     * @param content 当前ProseMirror内容数组
     */
    private void processChildren(Node parent, ArrayNode content) {
        Node child = parent.getFirstChild();
        while (child != null) {
            convertNode(child, content);
            child = child.getNext();
        }
    }

    /**
     * 处理标题节点
     *
     * @param heading 标题节点
     * @param content 当前ProseMirror内容数组
     */
    private void processHeading(Heading heading, ArrayNode content) {
        ObjectNode headingNode = objectMapper.createObjectNode();
        headingNode.put("type", "heading");
        
        // 设置标题级别
        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("level", heading.getLevel());
        attrs.put("id", generateId());
        headingNode.set("attrs", attrs);
        
        // 处理标题内容
        ArrayNode headingContent = headingNode.putArray("content");
        processInlineContent(heading.getFirstChild(), headingContent);
        
        content.add(headingNode);
    }

    /**
     * 处理段落节点
     *
     * @param paragraph 段落节点
     * @param content   当前ProseMirror内容数组
     */
    private void processParagraph(Paragraph paragraph, ArrayNode content) {
        ObjectNode paragraphNode = objectMapper.createObjectNode();
        paragraphNode.put("type", "paragraph");
        
        // 添加ID属性
        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", generateId());
        paragraphNode.set("attrs", attrs);
        
        // 处理段落内容
        ArrayNode paragraphContent = paragraphNode.putArray("content");
        processInlineContent(paragraph.getFirstChild(), paragraphContent);
        
        content.add(paragraphNode);
    }

    /**
     * 处理无序列表节点
     *
     * @param bulletList 无序列表节点
     * @param content    当前ProseMirror内容数组
     */
    private void processBulletList(BulletList bulletList, ArrayNode content) {
        ObjectNode listNode = objectMapper.createObjectNode();
        listNode.put("type", "bullet_list");
        
        // 添加属性
        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", generateId());
        listNode.set("attrs", attrs);
        
        // 处理列表项
        ArrayNode listContent = listNode.putArray("content");
        processChildren(bulletList, listContent);
        
        content.add(listNode);
    }

    /**
     * 处理有序列表节点
     *
     * @param orderedList 有序列表节点
     * @param content     当前ProseMirror内容数组
     */
    private void processOrderedList(OrderedList orderedList, ArrayNode content) {
        ObjectNode listNode = objectMapper.createObjectNode();
        listNode.put("type", "ordered_list");
        
        // 添加属性
        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", generateId());
        attrs.put("start", orderedList.getStartNumber());
        listNode.set("attrs", attrs);
        
        // 处理列表项
        ArrayNode listContent = listNode.putArray("content");
        processChildren(orderedList, listContent);
        
        content.add(listNode);
    }

    /**
     * 处理列表项节点
     *
     * @param listItem 列表项节点
     * @param content  当前ProseMirror内容数组
     */
    private void processListItem(ListItem listItem, ArrayNode content) {
        ObjectNode itemNode = objectMapper.createObjectNode();
        itemNode.put("type", "list_item");
        
        // 添加属性
        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", generateId());
        itemNode.set("attrs", attrs);
        
        // 处理列表项内容
        ArrayNode itemContent = itemNode.putArray("content");
        processChildren(listItem, itemContent);
        
        content.add(itemNode);
    }

    /**
     * 处理代码块节点
     *
     * @param codeBlock 代码块节点
     * @param content   当前ProseMirror内容数组
     */
    private void processCodeBlock(FencedCodeBlock codeBlock, ArrayNode content) {
        ObjectNode codeNode = objectMapper.createObjectNode();
        codeNode.put("type", "code_block");
        
        // 添加属性
        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", generateId());
        String language = codeBlock.getInfo();
        if (language != null && !language.isEmpty()) {
            attrs.put("language", language);
        }
        codeNode.set("attrs", attrs);
        
        // 处理代码内容
        ArrayNode codeContent = codeNode.putArray("content");
        ObjectNode textNode = objectMapper.createObjectNode();
        textNode.put("type", "text");
        textNode.put("text", codeBlock.getLiteral());
        codeContent.add(textNode);
        
        content.add(codeNode);
    }

    /**
     * 处理引用块节点
     *
     * @param blockQuote 引用块节点
     * @param content    当前ProseMirror内容数组
     */
    private void processBlockQuote(BlockQuote blockQuote, ArrayNode content) {
        ObjectNode quoteNode = objectMapper.createObjectNode();
        quoteNode.put("type", "blockquote");
        
        // 添加属性
        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", generateId());
        quoteNode.set("attrs", attrs);
        
        // 处理引用内容
        ArrayNode quoteContent = quoteNode.putArray("content");
        processChildren(blockQuote, quoteContent);
        
        content.add(quoteNode);
    }

    /**
     * 处理分隔线节点
     *
     * @param content 当前ProseMirror内容数组
     */
    private void processThematicBreak(ArrayNode content) {
        ObjectNode hrNode = objectMapper.createObjectNode();
        hrNode.put("type", "horizontal_rule");
        
        // 添加属性
        ObjectNode attrs = objectMapper.createObjectNode();
        attrs.put("id", generateId());
        hrNode.set("attrs", attrs);
        
        content.add(hrNode);
    }

    /**
     * 处理内联内容（文本、链接、强调等）
     *
     * @param node    当前处理的内联节点
     * @param content 当前ProseMirror内容数组
     */
    private void processInlineContent(Node node, ArrayNode content) {
        while (node != null) {
            if (node instanceof Text) {
                // 文本节点
                ObjectNode textNode = objectMapper.createObjectNode();
                textNode.put("type", "text");
                textNode.put("text", ((Text) node).getLiteral());
                content.add(textNode);
            } else if (node instanceof Emphasis) {
                // 斜体
                ObjectNode emNode = objectMapper.createObjectNode();
                emNode.put("type", "italic");
                ArrayNode emContent = emNode.putArray("content");
                processInlineContent(node.getFirstChild(), emContent);
                content.add(emNode);
            } else if (node instanceof StrongEmphasis) {
                // 粗体
                ObjectNode strongNode = objectMapper.createObjectNode();
                strongNode.put("type", "bold");
                ArrayNode strongContent = strongNode.putArray("content");
                processInlineContent(node.getFirstChild(), strongContent);
                content.add(strongNode);
            } else if (node instanceof Link) {
                // 链接
                Link link = (Link) node;
                ObjectNode linkNode = objectMapper.createObjectNode();
                linkNode.put("type", "link");
                
                // 添加链接属性
                ObjectNode attrs = objectMapper.createObjectNode();
                attrs.put("href", link.getDestination());
                if (link.getTitle() != null) {
                    attrs.put("title", link.getTitle());
                }
                linkNode.set("attrs", attrs);
                
                // 处理链接文本
                ArrayNode linkContent = linkNode.putArray("content");
                processInlineContent(link.getFirstChild(), linkContent);
                
                content.add(linkNode);
            } else if (node instanceof Code) {
                // 行内代码
                ObjectNode codeNode = objectMapper.createObjectNode();
                codeNode.put("type", "code");
                ArrayNode codeContent = codeNode.putArray("content");
                
                ObjectNode textNode = objectMapper.createObjectNode();
                textNode.put("type", "text");
                textNode.put("text", ((Code) node).getLiteral());
                codeContent.add(textNode);
                
                content.add(codeNode);
            } else if (node instanceof Image) {
                // 图片
                Image image = (Image) node;
                ObjectNode imageNode = objectMapper.createObjectNode();
                imageNode.put("type", "image");
                
                // 添加图片属性
                ObjectNode attrs = objectMapper.createObjectNode();
                attrs.put("src", image.getDestination());
                attrs.put("alt", image.getTitle() != null ? image.getTitle() : "");
                attrs.put("id", generateId());
                imageNode.set("attrs", attrs);
                
                content.add(imageNode);
            } else {
                // 其他内联节点，递归处理
                processInlineContent(node.getFirstChild(), content);
            }
            
            node = node.getNext();
        }
    }

    /**
     * 生成唯一ID
     *
     * @return 唯一ID字符串
     */
    private String generateId() {
        return "id-" + UUID.randomUUID().toString().substring(0, 8);
    }
}