package com.yby6.mcp.server.tencent.springAi;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;

public class SampleClient {

	private final McpClientTransport transport;

	public SampleClient(McpClientTransport transport) {
		this.transport = transport;
	}

	public void run() {

		var client = McpClient.sync(this.transport).build();

		client.initialize();

		client.ping();

		// List and demonstrate tools
		McpSchema.ListToolsResult toolsList = client.listTools();
		System.out.println("Available Tools = " + toolsList);


		client.closeGracefully();

	}

}