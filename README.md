
<h1 align="center">
    <img width="100" alt="image" src="https://github.com/user-attachments/assets/c95bbbd2-d1da-4255-a2e8-a07d7b89cfd7" />
	<strong>腾讯云开发者社区文章发布 MCP 服务器</strong>
</h1>
<h3 align="center">
    这是一个基于 MCP (Model Context Protocol) 协议的服务器，专门用于自动化发布文章到腾讯云开发者社区。通过集成 Spring Boot 3.x
和 Spring AI，为 AI 助手提供了与腾讯云开发者社区交互的能力，实现文章的自动发布和管理。
</h3>

<div align="center"><b>您是第<img src="https://profile-counter.glitch.me/tencent-send-article-mcp-server/count.svg"></img>个访问tencent-send-article-mcp-server~</b></div>


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
java -jar target/tencent-send-article-mcp-server-app.jar --tencent.api.cookie="your_cookie_here"
```

4. **编译和运行**

```bash
# 编译项目
mvn clean package

# 启动服务
java -jar target/tencent-send-article-mcp-server-app.jar
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
        "/path/to/tencent-send-article-mcp-server-app.jar",
        "--tencent.api.cookie=your_cookie_here"
      ]
    }
  }
}
```

### 应用配置

在 `application.yml` 中可以配置以下选项：

```yaml

tencent:
  api:
    cookie: ${TENCENT_API_COOKIE:这里是你的Cookie}
```

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

## 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本项目
2. 创建特性分支 dev
3. 提交更改
4. 推送到分支
5. 开启 Pull Request

---

**注意**：本项目仅用于学习和研究目的，请遵守腾讯云开发者社区的使用条款和相关法律法规。
