# 腾讯云开发者社区文章发布 MCP 服务器

<div align="center">
    <img width="120" alt="logo" src="https://github.com/user-attachments/assets/c95bbbd2-d1da-4255-a2e8-a07d7b89cfd7" />
    <br/>
    <p>
        <a href="https://github.com/yangbuyiya/tencent-send-article-mcp-server/stargazers">
            <img src="https://img.shields.io/github/stars/yangbuyiya/tencent-send-article-mcp-server?style=flat-square" alt="stars"/>
        </a>
        <a href="https://github.com/yangbuyiya/tencent-send-article-mcp-server/network/members">
            <img src="https://img.shields.io/github/forks/yangbuyiya/tencent-send-article-mcp-server?style=flat-square" alt="forks"/>
        </a>
        <a href="https://github.com/yangbuyiya/tencent-send-article-mcp-server/issues">
            <img src="https://img.shields.io/github/issues/yangbuyiya/tencent-send-article-mcp-server?style=flat-square" alt="issues"/>
        </a>
        <a href="https://github.com/yangbuyiya/tencent-send-article-mcp-server/blob/main/LICENSE">
            <img src="https://img.shields.io/github/license/yangbuyiya/tencent-send-article-mcp-server?style=flat-square" alt="license"/>
        </a>
    </p>
    <p>
        <img src="https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen?style=flat-square" alt="Spring Boot"/>
        <img src="https://img.shields.io/badge/Spring%20AI-1.0.0-brightgreen?style=flat-square" alt="Spring AI"/>
        <img src="https://img.shields.io/badge/Java-17-orange?style=flat-square" alt="Java"/>
        <img src="https://img.shields.io/badge/Maven-3.6+-blue?style=flat-square" alt="Maven"/>
    </p>
</div>

## 📝 项目简介

这是一个基于 MCP (Model Context Protocol) 协议的服务器，专门用于自动化发布文章到腾讯云开发者社区。通过集成 Spring Boot 3.x
和 Spring AI，为 AI 助手提供了与腾讯云开发者社区交互的能力，实现文章的自动发布和管理。

<div align="center">
    <img src="https://profile-counter.glitch.me/tencent-send-article-mcp-server/count.svg" alt="访问计数" style="border-radius: 5px; padding: 5px; background: #f0f0f0;"/>
</div>

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

4. **修改代码定义文章专栏**

前往 getAddArticleRequest 方法中修改文章部分请求参数
具体参数前往 登录 [腾讯云开发者社区](https://cloud.tencent.com/developer) 
打开开发者工具, 发布一个测试文章选好你要发布的专栏等数据
找到 `addArticle` 请求复制出来, 将请求参数下面当中的参数替换即可

```java
// 只允许修改下面的参数
addArticleRequest.setSourceType(1);  // 设置为原创
addArticleRequest.setClassifyIds(List.of(3,4));  // 设置文章分类
addArticleRequest.setTagIds(List.of(17375));  // 设置文章标签
addArticleRequest.setLongtailTag(List.of("面试","面试题","趣味面试"));  // 设置长尾标签
addArticleRequest.setColumnIds(List.of(105380));  // 设置专栏ID
addArticleRequest.setOpenComment(1);  // 开启评论
addArticleRequest.setCloseTextLink(1);  // 允许文本链接
addArticleRequest.setPic("https://foruda.gitee.com/images/1748188287230778527/9289646d_5151444.png");  // 设置封面图片
addArticleRequest.setSourceDetail(new HashMap<>());  // 设置来源详情
addArticleRequest.setZoneName("");  // 设置专区名称
```

5. **编译和运行**

```bash
# 编译项目
mvn clean package
```

## 配置说明

### MCP 客户端配置 STDIO 模式

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

也可以调用 [ClientStdio.java](src/test/java/com/yby6/mcp/server/tencent/springAi/ClientStdio.java) 类进行测试是否通信

### MCP 客户端配置 SSE 模式

```yaml
{
  "mcpServers": {
    "tencent-send-article-mcp-server": {
      "baseUrl": "http://127.0.0.1:8633/sse"
    }
  }
}

```

项目默认使用 STDIO 模式，如果需要使用 SSE 模式，请按照以下配置进行修改：(项目已经区分配置, 可通过 profiles.active=sse/stdio
切换模式)

#### 修改配置文件 - 新增端口

```yaml

server:
  port: 8633 # 端口号

```

#### 修改配置文件 - 切换模式为SSE

```yaml
  ai:
    mcp:
      server:
        stdio: true # 设置为false 为sse模式
        name: ${spring.application.name}
        version: 1.0.0
        type: sync # 同步/异步
        instructions: "
        这是一个基于 MCP (Model Context Protocol) 协议的服务器，
        专门用于自动化发布文章到腾讯云开发者社区。通过集成 Spring Boot 3.x 和 Spring AI，为 AI 助手提供了与腾讯云开发者社区交互的能力，
        实现文章的自动发布和管理。
        "
        sse-endpoint: /sse # sse端点
        sse-message-endpoint: /mcp/messages # 客户端 sse消息端点
        capabilities:
          tool: true
          resource: true
          prompt: true
          completion: true
```

#### 修改配置文件 - 日志配置修改

```yaml

# 注意：您必须禁用web横幅和控制台日志记录，以允许STDIO传输工作！！！
main:
  banner-mode: off
  # SSE打开, STDIO注释
  web-application-type: none

logging:
  # SSE打开, STDIO注释
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: data/log/${spring.application.name}.log

```

#### 修改 pom.xml - 新增SSE依赖支持

```xml

<!-- 标准 SSE/STDIO 服务器配置 通过配置切换 -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webflux</artifactId>
</dependency>

```

然后就可以调用 [ClientSse.java](src/test/java/com/yby6/mcp/server/tencent/springAi/ClientSse.java) 类进行测试是否通信

## Docker 部署使用

### 快速部署

```shell

docker run -d --name tencent-send-article-mcp-server \
-p 8635:8633 \
-e TENCENT_API_COOKIE="测试Cookie" \
registry.cn-hangzhou.aliyuncs.com/yangbuyiya/tencent-send-article-mcp-server-app:1.0.0

```

### 构建镜像

> ⚠️先修改部署配置文件, 我这里以shell文件为例

```shell
# Docker镜像相关配置
DOCKER_IMAGE_NAME="改成你的DockerHub仓库名称/tencent-send-article-mcp-server-app"
DOCKER_IMAGE_TAG="1.0.0"
DOCKER_PLATFORM="linux/amd64,linux/arm64"
DOCKERFILE_PATH="Dockerfile"

# 阿里云配置
ALIYUN_REGISTRY="registry.cn-hangzhou.aliyuncs.com"
NAMESPACE="你的阿里云命名空间仓库名称"
IMAGE_NAME="tencent-send-article-mcp-server-app"
IMAGE_TAG="1.0.0"

```

修改完毕后, 运行 [deploy](script/deploy) 脚本进行构建, 构建完成后会生成镜像

### 运行

```shell

docker run -d --name tencent-send-article-mcp-server \
-p 8635:8633 \
-e TENCENT_API_COOKIE="qcommunity_identify_id=V6pvjxmW1JPuCEMovMUTz; qcloud_uid=VaIEoNdiKsUA; web_uid=ae70b69c-f87e-41c8-adfc-06efafb5678d; loginType=wx; lastLoginIdentity=51605f0971933755e7f8a53c030a98bc; qcommunity_session=8f9a94888dd80c7cba9f04103107b4821b78caacdbdc8a2ef693787a26905024; language=zh; _ga=GA1.2.273799001.1746384291; _gcl_au=1.1.1073535231.1746384292; qcstats_seo_keywords=%E5%93%81%E7%89%8C%E8%AF%8D-%E5%93%81%E7%89%8C%E8%AF%8D-%E8%85%BE%E8%AE%AF%E4%BA%91; ewpUid=a3134f73-11f8-48ef-a865-7a91c897b948; opc_xsrf=ad04464bd315d51a9252cd0e5e37b930%7C1747930601; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22100005325524%22%2C%22first_id%22%3A%221969c9d82841f57-06185ef48d4f27-1a525636-2104200-1969c9d82852679%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTk2OWM5ZDgyODQxZjU3LTA2MTg1ZWY0OGQ0ZjI3LTFhNTI1NjM2LTIxMDQyMDAtMTk2OWM5ZDgyODUyNjc5IiwiJGlkZW50aXR5X2xvZ2luX2lkIjoiMTAwMDA1MzI1NTI0In0%3D%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%22100005325524%22%7D%2C%22%24device_id%22%3A%221969c9d82841f57-06185ef48d4f27-1a525636-2104200-1969c9d82852679%22%7D; trafficParams=***%24%3Btimestamp%3D1747932275435%3Bfrom_type%3Dserver%3Btrack%3Dd6e10566-f9d9-4045-a6ce-45ce105aec4c%3B%24***; qcloud_from=qcloud.directEnter.developer-1748014503549; qcmainCSRFToken=4xnRFK_vITQD; qcloud_visitId=a034592f38863868d900b64935b11757; _gat=1; uin=o100005325524; nick=1692700664; intl=1" \
registry.cn-hangzhou.aliyuncs.com/yangbuyiya/tencent-send-article-mcp-server-app:1.0.0

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
