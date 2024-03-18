package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class BaseApplication {

	@RequestMapping("/")
	String home(){
		return "hola mundo 3";
	}



	public static void main(String[] args) {
		SpringApplication.run(BaseApplication.class, args);
	}

}
