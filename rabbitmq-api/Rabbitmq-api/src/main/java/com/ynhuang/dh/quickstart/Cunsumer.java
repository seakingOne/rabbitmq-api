package com.ynhuang.dh.quickstart;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

public class Cunsumer {

	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.74.128");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		// 通过连接工厂创建一个连接
		Connection connection = connectionFactory.newConnection();
		// 创建一个管道
		Channel channel = connection.createChannel();
		// 创建一个队列
		String queueName = "test001";
		channel.queueDeclare("test001", true, false, false, null);
		// 创建消费者
		QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
		// 设置channel
		channel.basicConsume(queueName, true, queueingConsumer);
		//获取消息
		while(true) {
			Delivery delivery = queueingConsumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println("msg:"+msg);
			//Envelope envelope = delivery.getEnvelope();
		}
	}

}
