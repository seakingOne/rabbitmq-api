package com.ynhuang.dh;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ynhuang.dh.adapter.Order;
import com.ynhuang.dh.springboot.sender.Sender;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqApiApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private ConnectionFactory connectionFactory;

	@Autowired
	private RabbitAdmin rabbitAdmin;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private Sender sender;

	@Test
	public void testAdmin() {
		System.out.println(connectionFactory.getHost());
		rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("test.topic.queue", false)) // 直接创建队列
				.to(new TopicExchange("test.topic", false, false)) // 直接创建交换机 建立关联关系
				.with("user.#"));
	}

	@Test
	public void testSend() {
		// 创建消息
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.getHeaders().put("id", "1");
		Message message = new Message("test!!!!".getBytes(), messageProperties);
		rabbitTemplate.convertAndSend("admin.topic.exchange", "admin.1", message, new MessagePostProcessor() {

			// 发送额外的消息
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				message.getMessageProperties().getHeaders().put("id", "666");
				return message;
			}
		});
	}

	@Test
	public void testSend1() {
		// 创建消息
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType("text/plain");
		Message message = new Message("发送String的消息，非字节数组！".getBytes(), messageProperties);
		rabbitTemplate.convertAndSend("admin.topic.exchange", "admin.2", message);
	}

	@Test
	public void testSendJSON() throws JsonProcessingException {
		Order order = new Order();
		order.setId(1);
		order.setName("ynhuang");
		order.setDesc("666");
		String json = JSON.toJSONString(order);
		// 创建消息
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType("application/json");
		Message message = new Message(json.getBytes(), messageProperties);
		rabbitTemplate.send("admin.topic.exchange", "admin.order", message);
	}

	@Test
	public void testSendJavaJSON() throws JsonProcessingException {
		Order order = new Order();
		order.setId(1);
		order.setName("ynhuang");
		order.setDesc("666");
		String json = JSON.toJSONString(order);
		// 创建消息
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType("application/json");
		messageProperties.getHeaders().put("__TypeId__", "com.ynhuang.dh.adapter.Order");
		Message message = new Message(json.getBytes(), messageProperties);
		rabbitTemplate.send("admin.topic.exchange", "admin.order.java", message);
	}

	@Test
	public void testSendClassJSON() throws Exception {
		Order order = new Order();
		order.setId(1);
		order.setName("ynhuang");
		order.setDesc("666");
		String json = JSON.toJSONString(order);
		// 创建消息
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType("application/json");
		messageProperties.getHeaders().put("__TypeId__", "order");
		Message message = new Message(json.getBytes(), messageProperties);
		rabbitTemplate.send("admin.topic.exchange", "admin.order.java", message);
	}

	@Test
	public void testImg() throws Exception {
		Path path = Paths.get("C:\\Users\\018399.SSS\\Desktop", "upload.png");
		System.out.println(path);
		byte[] bytes = Files.readAllBytes(path);
		Path writePath = Paths.get("F:\\", "test.png");
		String content = new String(bytes, "UTF-8");
		Files.write(writePath, content.getBytes("UTF-8")); // 传入byte[]
		System.out.println(bytes);
	}

	/**
	 * springboot发送消息
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSendSpringboot() throws Exception {
		Map<String, Object> properties = new HashMap<>();
		properties.put("header", "666");
		sender.send("springboot消息体", properties);
	}

}
