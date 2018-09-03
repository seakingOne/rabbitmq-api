package com.ynhuang.dh;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;


public class Sample {
	// 设置APPID/AK/SK
	public static final String APP_ID = "11724506";
	public static final String API_KEY = "KdC6H18Racbsepdna2sW0ndB";
	public static final String SECRET_KEY = "IciMBZTmhAupUOCCZ7KFPGehVixEmm81";

	public static void main(String[] args) throws JSONException {
		// 初始化一个AipOcr
		AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);

		// 可选：设置log4j日志输出格式，若不设置，则使用默认配置
		// 也可以直接通过jvm启动参数设置此环境变量
		System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

		// 调用接口
		String path = "C:\\Users\\018399.SSS\\Desktop\\upload.png";
		JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
		System.out.println(res.toString(2));

	}

}
