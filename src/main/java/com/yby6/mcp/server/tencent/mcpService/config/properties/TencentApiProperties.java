package com.yby6.mcp.server.tencent.mcpService.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 腾讯云API配置属性类
 * <p>
 * 该配置类用于管理腾讯云开发者社区API的配置属性。
 * 使用Spring Boot的配置属性机制，从配置文件中读取相关配置。
 * 配置前缀为"tencent.api"。
 * <p>
 * 主要功能：
 * 1. 管理API认证信息（Cookie）
 * 2. 管理文章分类信息
 * 3. 提供配置属性的访问方法
 *
 * @author yby6
 * @version 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "tencent.api")
public class TencentApiProperties {

    /**
     * 认证Cookie
     * <p>
     * 用于腾讯云开发者社区API的身份验证。
     * 在配置文件中通过tencent.api.cookie属性设置。
     * 该值在应用启动时会被验证，不能为空。
     */
    private String cookie;

}
