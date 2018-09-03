package com.ynhuang.dh.quickstart;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Procuder {

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.74.128");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		// 通过连接工厂创建一个连接
		Connection connection = connectionFactory.newConnection();
		// 创建一个管道
		Channel channel = connection.createChannel();
		// send data
		for (int i = 0; i < 5; i++) {
			String msg = "hello";
			channel.basicPublish("", "test001", null, msg.getBytes());
		}

		channel.close();
		connection.close();
	}

}
