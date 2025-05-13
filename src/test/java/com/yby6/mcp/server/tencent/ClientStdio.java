/*
* Copyright 2024 - 2024 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.yby6.mcp.server.tencent;

import java.util.Map;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ListToolsResult;

/**
 * With stdio transport, the MCP server is automatically started by the client. But you
 * have to build the server jar first:
 *
 * <pre>
 * ./mvnw clean install -DskipTests
 * </pre>
 */
public class ClientStdio {

	public static void main(String[] args) {

		var stdioParams = ServerParameters.builder("/Users/yangbuyi/Library/Java/JavaVirtualMachines/graalvm-jdk-17.0.11/Contents/Home/bin/java")
			.args("-jar",
					"/Users/yangbuyi/.m2/repository/com/yby6/mcp/server/tencent/tencent-send-article-mcp-server/1.0.0/tencent-send-article-mcp-server-1.0.0.jar")
			.build();

		var transport = new StdioClientTransport(stdioParams);
		var client = McpClient.sync(transport).build();

		client.initialize();

		// List and demonstrate tools
		ListToolsResult toolsList = client.listTools();
		System.out.println("Available Tools = " + toolsList);


//		final CallToolResult callToolResult = client.callTool(new CallToolRequest(
//				"saveArticle",
//				Map.of(
//						"articleId", "123456789"
//				)));

		client.closeGracefully();
	}

}