# 腾讯云开发者社区文章发布 MCP 服务器

## 项目简介

这是一个基于 MCP (Model Context Protocol) 协议的服务器，专门用于自动化发布文章到腾讯云开发者社区。通过集成 Spring Boot 3.x 和 Spring AI，为 AI 助手提供了与腾讯云开发者社区交互的能力，实现文章的自动发布和管理。

## 主要功能

- 🚀 **自动发布文章**：支持将文章内容自动发布到腾讯云开发者社区
- 🔧 **MCP 协议支持**：完全兼容 MCP 协议，可与各种 AI 助手集成
- 🔐 **安全认证**：使用腾讯云 Cookie 进行身份验证
- 📝 **内容管理**：支持文章标题、内容、标签等完整信息的处理
- 🌐 **RESTful API**：提供标准的 REST 接口用于文章操作

## 技术栈

- **框架**: Spring Boot 3.4.5
- **AI 集成**: Spring AI 1.0.0
- **运行环境**: Java 17
- **构建工具**: Maven
- **协议支持**: MCP (Model Context Protocol)
- **HTTP 客户端**: Retrofit2
- **JSON 处理**: FastJSON, Jackson

## 快速开始

### 环境要求

- JDK 17 或更高版本
- Maven 3.6 或更高版本
- 有效的腾讯云开发者社区账号

### 安装步骤

1. **克隆项目**
```bash
git clone https://github.com/yangbuyiya/tencent-send-article-mcp-server.git
cd tencent-send-article-mcp-server
```

2. **获取腾讯云 Cookie**
   - 登录 [腾讯云开发者社区](https://cloud.tencent.com/developer)
   - 打开浏览器开发者工具 (F12)
   - 切换到 Network 标签页
   - 刷新页面，在任意请求的 Headers 中找到 Cookie
   - 复制完整的 Cookie 字符串

3. **配置认证信息**
```bash
# 方式一：环境变量
export TENCENT_API_COOKIE="your_cookie_here"

# 方式二：命令行参数
java -jar target/tencent-send-article-mcp-server-1.0.0.jar --tencent.api.cookie="your_cookie_here"
```

4. **编译和运行**
```bash
# 编译项目
mvn clean package

# 启动服务
java -jar target/tencent-send-article-mcp-server-1.0.0.jar
```

## 配置说明

### MCP 客户端配置

在您的 MCP 客户端配置文件中添加以下配置：

```json
{
  "mcpServers": {
    "tencent-article-publisher": {
      "command": "java",
      "args": [
        "-Dspring.ai.mcp.server.stdio=true",
        "-Dfile.encoding=utf-8",
        "-jar",
        "/path/to/tencent-send-article-mcp-server-1.0.0.jar",
        "--tencent.api.cookie=your_cookie_here"
      ]
    }
  }
}
```

### 应用配置

在 `application.yml` 中可以配置以下选项：

```yaml
spring:
  application:
    name: tencent-send-article-mcp-server
  ai:
    mcp:
      server:
        version: "1.0.0"
        stdio: true

tencent:
  api:
    cookie: ${TENCENT_API_COOKIE:}
    base-url: https://cloud.tencent.com/developer
    
logging:
  file:
    path: data/log
    name: ${logging.file.path}/tencent-send-article-mcp-server.log
```

## API 使用示例

### 发布文章

通过 MCP 协议调用文章发布功能：

```json
{
  "method": "tools/call",
  "params": {
    "name": "publish_article",
    "arguments": {
      "title": "文章标题",
      "content": "文章内容（支持 Markdown）",
      "tags": ["技术", "云计算", "开发"],
      "category": "技术分享"
    }
  }
}
```

### 响应格式

```json
{
  "content": [
    {
      "type": "text",
      "text": "文章发布成功！\n文章ID: 12345\n文章链接: https://cloud.tencent.com/developer/article/12345"
    }
  ]
}
```

## 支持的工具 (Tools)

| 工具名称 | 描述 | 参数 |
|---------|------|------|
| `publish_article` | 发布文章到腾讯云开发者社区 | title, content, tags, category |
| `get_article_status` | 查询文章发布状态 | article_id |
| `update_article` | 更新已发布的文章 | article_id, title, content, tags |

## 安全注意事项

⚠️ **重要提醒**：

- Cookie 包含敏感的身份认证信息，请妥善保管
- 不要将 Cookie 提交到代码仓库或公开分享
- 建议使用环境变量方式配置 Cookie
- Cookie 可能会定期失效，需要及时更新
- 生产环境建议使用更安全的认证方式

## 故障排除

### 常见问题

1. **Cookie 失效**
   - 重新登录腾讯云开发者社区获取新的 Cookie
   - 检查 Cookie 格式是否完整

2. **文章发布失败**
   - 检查网络连接
   - 验证文章内容是否符合社区规范
   - 确认账号权限

3. **MCP 连接问题**
   - 检查 Java 路径配置
   - 验证 JAR 文件路径
   - 查看应用日志文件

### 日志查看

```bash
# 查看实时日志
tail -f data/log/tencent-send-article-mcp-server.log

# 查看错误日志
grep "ERROR" data/log/tencent-send-article-mcp-server.log
```

## 开发指南

### 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/tencent/mcp/
│   │       ├── config/          # 配置类
│   │       ├── controller/      # REST 控制器
│   │       ├── service/         # 业务逻辑
│   │       ├── model/           # 数据模型
│   │       └── client/          # 腾讯云 API 客户端
│   └── resources/
│       ├── application.yml      # 应用配置
│       └── logback-spring.xml   # 日志配置
```

### 扩展开发

如需添加新功能，可以：

1. 在 `service` 包中添加新的服务类
2. 在 `controller` 包中添加对应的 MCP 工具
3. 在 `model` 包中定义数据模型
4. 更新配置文件

## 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 [Issue](../../issues)
- 发送邮件至：[your-email@example.com]

---

**注意**：本项目仅用于学习和研究目的，请遵守腾讯云开发者社区的使用条款和相关法律法规。
