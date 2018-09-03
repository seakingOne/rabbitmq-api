package com.ynhuang.dh.adapter;

import java.util.Map;

public class MyAdapter {

	// public void handleMessage(byte[] messageBody) {
	// System.err.println("默认消息为:" + new String(messageBody));
	// }

	public void handleMessage(String messageBody) {
		System.err.println("字符串消息为:" + new String(messageBody));
	}

	public void consumeMessage(Map messageBody) {
		System.err.println("json对象为:" + messageBody);
	}

	public void consumeMessage(Order order) {
		System.err.println("order对象为:" + order.getId() + "," + order.getName() + "," + order.getDesc());
	}

}
