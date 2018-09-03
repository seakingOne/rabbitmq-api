package com.ynhuang.dh.springboot.sender;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 生产端消息确认模式
 * 
 * @author 018399
 *
 */
@Component
public class Sender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	final ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {

		@Override
		public void confirm(CorrelationData correlationData, boolean ack, String cause) {
			System.err.println("correlationData:" + correlationData);
			System.err.println("ack:" + ack);
			if (!ack) {
				System.err.println("异常处理!");
			}
		}
	};

	final ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {

		@Override
		public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText,
				String exchange, String routingKey) {
			System.err.println("返回：" + exchange + "," + exchange);
		}
	};

	public void send(Object message, Map<String, Object> properties) throws Exception {
		MessageHeaders headers = new MessageHeaders(properties);
		Message<Object> msg = MessageBuilder.createMessage(message, headers);
		rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		CorrelationData correlationData = new CorrelationData();
		correlationData.setId("1");//设置全局唯一id+时间戳，实际消息的id
		rabbitTemplate.convertAndSend("exchange-1", "springboot.hello", msg, correlationData);
	}

}
