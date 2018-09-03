package com.ynhuang.dh.springboot.customer;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

/**
 * 1、手工签收 2、监听器
 * 
 * @author 018399
 *
 */
@Component
public class Customer {

	@RabbitListener(bindings = @QueueBinding
			(value = @Queue(value = "queue-1", durable = "true"), 
			exchange = @Exchange(value = "exchange-1", durable = "true", 
			type = "topic",ignoreDeclarationExceptions="true"),
			key = "springboot.*"
			)
	)
	@RabbitHandler
	public void onMessage(Message message, Channel channel) throws IOException {
		System.err.println("消费端收到的消息："+message.getPayload());
		Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
		channel.basicAck(deliveryTag, false);
	}

}
