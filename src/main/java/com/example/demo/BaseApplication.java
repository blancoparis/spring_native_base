package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class BaseApplication {

	@Autowired
	private Environment environment;

	@RequestMapping("/")
	String home(){
		return "hola mundo 10";
	}

	public record Status(String status,String env){}
	@RequestMapping("/status")
	Status status(){
		var entornos = String.join(",",environment.getActiveProfiles());
		return new Status("OK",entornos);
	}


	public static void main(String[] args) {
		SpringApplication.run(BaseApplication.class, args);
	}

}
