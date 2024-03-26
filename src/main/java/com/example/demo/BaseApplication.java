package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.JavaVersion;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class BaseApplication {


	@Autowired
	BaseApplication( Environment environment){
		this.environment=environment;
	}


	private Environment environment;

	@RequestMapping("/")
	String home(){
		return "hola mundo 11";
	}

	public record Status(String status,String env,String springVersion,String springBootVersion,String jdk,String java){}
	@RequestMapping("/status")
	Status status(){
		var entornos = String.join(",",environment.getActiveProfiles());
		return new Status(
				"OK"
				,entornos
				, SpringVersion.getVersion()
				, SpringBootVersion.getVersion()
				, System.getProperty("java.version")
				, JavaVersion.getJavaVersion().toString()
		);

	}


	public static void main(String[] args) {
		SpringApplication.run(BaseApplication.class, args);
	}

}
