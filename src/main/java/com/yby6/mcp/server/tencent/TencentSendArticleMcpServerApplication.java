package com.yby6.mcp.server.tencent;

import com.yby6.mcp.server.tencent.mcpService.config.properties.TencentApiProperties;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TencentSendArticleMcpServerApplication implements CommandLineRunner {

    /**
     * 日志记录器
     */
    private final Logger log = LoggerFactory.getLogger(TencentSendArticleMcpServerApplication.class);

    /**
     * 腾讯云API配置属性
     */
    @Resource
    private TencentApiProperties tencentApiProperties;

    public static void main(String[] args) {
        SpringApplication.run(TencentSendArticleMcpServerApplication.class, args);
    }

    /**
     * 应用程序启动时的初始化操作
     * <p>
     * 该方法在应用启动时执行，主要用于：
     * 1. 检查腾讯云Cookie配置
     * 2. 验证认证信息的有效性
     *
     * @param args 命令行参数
     * @throws Exception 如果初始化过程中发生错误
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("检查是否成功配置 cookie ... {}", tencentApiProperties.getCookie());
        String cookie = tencentApiProperties.getCookie();
        if (cookie == null || cookie.isEmpty()) {
            log.error("没有配置 cookie 请检查配置文件");
        }
    }
}
