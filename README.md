# Tencent-send-article-mcp-server

## 项目简介
这是一个基于 Spring Boot 3.x 和 Spring AI 的腾讯云 MCP 服务端项目。该项目主要用于与腾讯云 AI 服务进行集成，提供统一的接口调用能力。

## 技术栈
- Spring Boot 3.4.5
- Spring AI 1.0.0-M6
- Java 17
- Maven

## 快速开始

### 环境要求
- JDK 17 或更高版本
- Maven 3.6 或更高版本

### 配置说明

#### 1. MCP 服务配置
项目支持通过命令行参数或配置文件进行配置。以下是完整的配置示例：

```json
{
  "tencent-send-article-mcp-server": {
    "command": "/path/to/java",
    "args": [
      "-Dspring.ai.mcp.server.stdio=true",
      "-Dfile.encoding=utf-8",
      "-jar",
      "/path/to/tencent-send-article-mcp-server-1.0.0.jar",
      "--tencent.api.cookie=your_cookie_here"
    ]
  }
}
```

配置参数说明：
- `command`: Java 可执行文件路径
- `args`: 启动参数列表
    - `-Dspring.ai.mcp.server.stdio=true`: 启用标准输入输出
    - `-Dfile.encoding=utf-8`: 设置文件编码
    - `-jar`: 指定运行 JAR 文件
    - `--tencent.api.cookie`: 设置认证 Cookie

#### 2. Cookie 配置
项目使用腾讯云的 Cookie 进行身份验证，有两种配置方式：

1. 通过命令行参数配置：
```bash
java -jar tencent-send-article-mcp-server-1.0.0.jar --tencent.api.cookie="your_cookie_here"
```

2. 通过环境变量配置：
```bash
export TENCENT_API_COOKIE="your_cookie_here"
```

3. 通过配置文件配置：
   在 `application.yml` 中设置：
```yaml
tencent:
  api:
    cookie: your_cookie_here
```

#### 3. 其他配置项
- `spring.application.name`: 应用名称，默认为 "tencent-send-article-mcp-server"
- `spring.ai.mcp.server.version`: 服务版本号

### 运行项目
1. 克隆项目
```bash
git clone [项目地址]
```

2. 配置 Cookie
```bash
export TENCENT_API_COOKIE="your_cookie_here"
```

3. 编译运行
```bash
mvn clean package
java -jar target/tencent-send-article-mcp-server-1.0.0.jar
```

## 获取 Cookie 的方法

1. 登录腾讯云控制台
2. 打开浏览器开发者工具（F12）
3. 切换到 Network 标签页
4. 刷新页面
5. 在请求中找到任意一个请求，查看其 Cookie 信息
6. 复制完整的 Cookie 字符串

## 注意事项
- Cookie 包含敏感信息，请勿将其提交到代码仓库
- 建议使用环境变量方式配置 Cookie
- Cookie 可能会定期失效，需要及时更新
- 确保 Java 路径配置正确
- 确保 JAR 文件路径配置正确

## 日志配置
- 日志文件位置：`data/log/tencent-send-article-mcp-server.log`
- 日志格式：标准控制台输出格式

## 依赖说明
主要依赖包括：
- spring-ai-mcp-server-spring-boot-starter
- spring-web
- fastjson
- retrofit2
- lombok
- jackson-databind

## 许可证
[添加许可证信息] 
