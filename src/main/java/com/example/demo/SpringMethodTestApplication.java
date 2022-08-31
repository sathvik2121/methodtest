package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SpringMethodTestApplication {

	public static void main(String[] args) {
		//SpringMethodTestApplication ob1=new SpringMethodTestApplication();
		//String message2=ob1.method1();
		//System.out.println(message2);
		SpringApplication.run(SpringMethodTestApplication.class, args);
	}

	@GetMapping("/")
	public String method1()
	{
		SpringMethodTestApplication ob=new SpringMethodTestApplication();
		String message=ob.method2();
		return message;
	}
	public String method2()
	{
		return "hi";
	}
	
}
