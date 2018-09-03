package com.ynhuang.dh;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitmqApiApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(RabbitmqApiApplication.class, args);
//		Path path = Paths.get("C:\\Users\\018399.SSS\\Desktop", "upload.png");
//		String filename = UUID.randomUUID().toString();
//		String newPath = "E:\\" + filename + ".png";
//		File file = new File(newPath);
//		Files.copy (path, file.toPath());
//      ------------------------------------------------------------------------		
//		Path path = Paths.get("C:\\Users\\018399.SSS\\Desktop", "upload.png");
//		byte[] bytes = Files.readAllBytes(path);
//		String filename = UUID.randomUUID().toString();
//		String newPath = "E:\\" + filename + ".png";
//		File file = new File(newPath);
//		Files.copy (path, file.toPath());
//		Files.copy(new ByteArrayInputStream(bytes),file.toPath());
	}
}
