package com.ynhuang.dh;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.ynhuang.dh.adapter.MyAdapter;

//@Configuration
//@ComponentScan({ "com.ynhuang.dh.*" })
public class RabbitMQConfig {

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setAddresses("192.168.74.130:5672");
		cachingConnectionFactory.setUsername("guest");
		cachingConnectionFactory.setPassword("guest");
		cachingConnectionFactory.setVirtualHost("/");
		return cachingConnectionFactory;
	}

	/**
	 * 绑定queue和exchange
	 * 
	 * @param connectionFactory
	 * @return
	 */
	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
		rabbitAdmin.setAutoStartup(true);
		return rabbitAdmin;
	}

	/**
	 * 声明队列，交换机，绑定关系
	 */
	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange("admin.topic.exchange", true, false);
	}

	@Bean
	public Queue queue() {
		return new Queue("admin.topic.queue", true);
	}

	@Bean
	public Binding binding() {
		return BindingBuilder.bind(queue()).to(topicExchange()).with("admin.#");
	}

	/**
	 * 消息发送模板
	 * 
	 * @param connectionFactory
	 * @return
	 */
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		return rabbitTemplate;

	}

	/**
	 * 配置消费者 SimpleMessageListenerContainer
	 * 
	 * @param connectionFactory
	 * @return
	 */
	@Bean
	public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
		container.setQueues(queue());// 此处可以添加多个
		container.setConcurrentConsumers(1);
		container.setMaxConcurrentConsumers(6);
		container.setDefaultRequeueRejected(false);
		container.setAcknowledgeMode(AcknowledgeMode.AUTO); // 自动签收
		
		// 设置消费者标签
		container.setConsumerTagStrategy(new ConsumerTagStrategy() {

			@Override
			public String createConsumerTag(String queue) {
				return queue + "_" + UUID.randomUUID();
			}
		});
		// 监听
//		container.setMessageListener(new ChannelAwareMessageListener() {
//
//			@Override
//			public void onMessage(Message message, Channel channel) throws Exception {
//				String msg = new String(message.getBody());
//				System.err.println("msg:" + msg + "，消息的header：" + message.getMessageProperties().getHeaders());
//			}
//		});
	
		System.out.println("--------------------------------");
		
		//使用适配器代替Listener
		MessageListenerAdapter adapter = new MessageListenerAdapter(new MyAdapter());
//		container.setMessageListener(adapter);
//		//设置适配器兼容String类型
//		container.setMessageConverter(new MessageConverter() {
//			
//			@Override
//			public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
//				return new Message(object.toString().getBytes(), messageProperties);
//			}
//			
//			@Override
//			public Object fromMessage(Message message) throws MessageConversionException {
//				String contentType = message.getMessageProperties().getContentType();
//				if(null != contentType && contentType.contains("text")) {
//					return new String(message.getBody());
//				}
//				return message.getBody();
//			}
//		});
		
		//MessageConverter做对象转换
		//1、支持json数据支持
//		adapter.setDefaultListenerMethod("consumeMessage");
//		Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//		adapter.setMessageConverter(jackson2JsonMessageConverter);


		
		//2、支持java对象转换
		adapter.setDefaultListenerMethod("consumeMessage");
		Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
		DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
		//加入信任所有的包
//		javaTypeMapper.setTrustedPackages("*");
//		jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
//		adapter.setMessageConverter(jackson2JsonMessageConverter);
		
		
		//3、加入支持java多對象映射
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("order", com.ynhuang.dh.adapter.Order.class);
		map.put("order1", com.ynhuang.dh.adapter.Order.class);
		javaTypeMapper.setTrustedPackages("*");
		javaTypeMapper.setIdClassMapping(map);
		jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
		adapter.setMessageConverter(jackson2JsonMessageConverter);
		
		
		container.setMessageListener(adapter);
		return container;
		
	}

}
