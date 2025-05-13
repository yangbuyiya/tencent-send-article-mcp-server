package com.yby6.mcp.server.tencent.mcpService.tools;

import com.alibaba.fastjson.JSON;
import com.yby6.mcp.server.tencent.api.ITencentService;
import com.yby6.mcp.server.tencent.api.dto.AddArticleRequest;
import com.yby6.mcp.server.tencent.api.dto.AddArticleResponse;
import com.yby6.mcp.server.tencent.mcpService.config.properties.TencentApiProperties;
import com.yby6.mcp.server.tencent.mcpService.funcModel.ArticleFunctionRequest;
import com.yby6.mcp.server.tencent.mcpService.funcModel.ArticleFunctionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;

/**
 * 腾讯云开发者社区文章服务
 * <p>
 * 该服务类负责处理与腾讯云开发者社区文章相关的业务逻辑，
 * 包括文章的发布、更新等操作。作为领域服务层的一部分，
 * 它通过端口适配器模式与基础设施层进行交互。
 *
 * @author yby6
 * @version 1.0.0
 */
@Slf4j
@Service
public class TencentArticleToolService implements ToolServiceMarker {

    @Autowired
    private ITencentService tencentService;

    @Autowired
    private TencentApiProperties tencentApiProperties;

    /**
     * 发布文章到腾讯云开发者社区
     * <p>
     * 该方法是一个MCP工具方法，用于将文章发布到腾讯云开发者社区。
     * 主要功能：
     * 1. 接收文章发布请求
     * 2. 记录请求参数日志
     * 3. 通过端口适配器调用实际的文章发布服务
     * 4. 处理异常情况并返回响应
     *
     * @param request 文章发布请求，包含文章标题、内容等信息
     * @return 文章发布响应，包含发布结果信息
     */
    @Tool(description = "发布文章到腾讯云开发者社区")
    public ArticleFunctionResponse saveArticle(ArticleFunctionRequest request) {
        try {
            log.info("腾讯云开发者社区发帖参数：{}", JSON.toJSONString(request));
            log.info("接收到的参数: {}", request.toString());

            AddArticleRequest addArticleRequest = new AddArticleRequest();
            addArticleRequest.setTitle(request.getTitle());
            addArticleRequest.setPlain(request.getMarkdowncontent());
            addArticleRequest.setContent(addArticleRequest.getContent());
            addArticleRequest.setSourceType(1);  // 设置为原创
            addArticleRequest.setClassifyIds(List.of(2));  // 设置文章分类
            addArticleRequest.setTagIds(List.of(18126));  // 设置文章标签
            addArticleRequest.setLongtailTag(List.of("mcp"));  // 设置长尾标签
            addArticleRequest.setColumnIds(List.of(101806));  // 设置专栏ID
            addArticleRequest.setOpenComment(1);  // 开启评论
            addArticleRequest.setCloseTextLink(0);  // 允许文本链接
            addArticleRequest.setUserSummary(request.getUserSummary());
            addArticleRequest.setPic("");  // 设置封面图片
            addArticleRequest.setSourceDetail(new HashMap<>());  // 设置来源详情
            addArticleRequest.setZoneName("");  // 设置专区名称

            // 执行API调用
            Call<AddArticleResponse> call = tencentService.addArticle(tencentApiProperties.getCookie(), addArticleRequest);
            Response<AddArticleResponse> response = call.execute();

            // 记录请求和响应日志
            log.info("\n\n请求腾讯云开发者社区发布文章\n req:{} \nres:{}", JSON.toJSONString(addArticleRequest), JSON.toJSONString(response));

            // 构建返回对象
            ArticleFunctionResponse articleFunctionResponse = new ArticleFunctionResponse();
            articleFunctionResponse.setStatus(-1);
            articleFunctionResponse.setArticleId(null);
            articleFunctionResponse.setUrl(null);
            articleFunctionResponse.setCopyright(getCopyright());

            if (response.isSuccessful()) {
                log.info("腾讯云开发者社区发布文章成功: {}", JSON.toJSONString(response.body()));

                // 处理成功响应
                AddArticleResponse articleResponseDTO = response.body();
                if (null == articleResponseDTO) return null;

                articleFunctionResponse.setStatus(articleResponseDTO.getStatus());
                articleFunctionResponse.setArticleId(articleResponseDTO.getArticleId());
                articleFunctionResponse.setUrl("https://cloud.tencent.com/developer/article/" + articleResponseDTO.getArticleId());

                return articleFunctionResponse;
            }
            log.error("腾讯云开发者社区发布文章失败: {}", JSON.toJSONString(response));
            return articleFunctionResponse;
        } catch (Exception e) {
            log.error("腾讯云开发者社区发帖失败 ", e);
        }
        return null;
    }
}
