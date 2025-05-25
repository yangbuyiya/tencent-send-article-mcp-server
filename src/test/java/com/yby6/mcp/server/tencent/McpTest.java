package com.yby6.mcp.server.tencent;

import com.alibaba.fastjson.JSON;
import com.yby6.mcp.server.tencent.mcpService.funcModel.ArticleFunctionRequest;
import com.yby6.mcp.server.tencent.mcpService.funcModel.ArticleFunctionResponse;
import com.yby6.mcp.server.tencent.mcpService.tools.McpToolServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * MCP 服务测试
 *
 * @author yangbuyiya
 * Create By 2025/05/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class McpTest {

    @Autowired
    private McpToolServer mcpToolServer;

    @Test
    public void testWriteArticle() {
        ArticleFunctionRequest request = new ArticleFunctionRequest();
        request.setTitle("测试文章");
        String markdown = """
                # 主标题
                
                这是一段介绍文本。
                
                ## 二级标题
                
                > 这是一段引用
                > 包含多行内容
                
                ### 三级标题
                这里是一些代码示例
                
                ```java
                public class Example {
                    public void method() {
                        // 代码示例
                    }
                }
                ```
                
                ```vue
                public class Example {
                    public void method() {
                        // 代码示例
                    }
                }
                ```
                
                
                我是一段普通文本, 可以包含换行
                
                
                文本中可以包含图片
                
                ![示例图片](https://developer.qcloudimg.com/http-save/1774592/a2641eb818b346b569c752dfffcbe505.png)
                
                这里是一段链接文本
                
                [访问链接](https://copilot.tencent.com/chat/?from_column=20421&from=20421)
                
                这里是高亮块
                
                ```高亮
                这里是高亮代码 哈哈哈
                ```
                
                """;

        request.setMarkdowncontent(markdown);
        request.setUserSummary("这是测试文章的摘要");
        ArticleFunctionResponse articleFunctionResponse = mcpToolServer.saveArticle(request);
        System.out.println(JSON.toJSONString(articleFunctionResponse, true));
    }

}
