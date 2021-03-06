/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Application
 * 
 * @author ThinkGem
 * @version 2018-10-13
 */
@SpringBootApplication
@EnableSwagger2
public class Application extends SpringBootServletInitializer {
	public static boolean devMode = true;
	public static void main(String[] args) {
		ApplicationHome h = new ApplicationHome(Application.class);
		String home = h.getSource().getParentFile().toString();
		System.out.println(home);
		ArrayList<String> la = new ArrayList<String>(Arrays.asList(args));
		la.forEach(s -> {
			if ("--devMode=false".equalsIgnoreCase(s)) {
				devMode = false;
			}
		});
		if (!devMode) {
			la.add("--spring.datasource.url=jdbc:h2:tcp://localhost:3306/"+home+"/ssdb;MODE=MYSQL;DATABASE_TO_UPPER=false;INIT=SET SCHEMA ssdb");
		}
		args = la.toArray(new String[la.size()]);
		la.forEach(s -> System.out.println(s));
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		this.setRegisterErrorPageFilter(false); // 错误页面有容器来处理，而不是SpringBoot
		return builder.sources(Application.class);
	}

	@SuppressWarnings("rawtypes")
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.jeesite.modules.restful")).paths(PathSelectors.any())
				.build()
				.apiInfo(new ApiInfo("Video Service Api Documentation", "Documentation automatically generated", "V1",
						null, new Contact("Video System", "www.july7.top", "wuqinghe2005@126.com"), null, null,
						new ArrayList<VendorExtension>()))
				.genericModelSubstitutes(Optional.class);
	}

	@Bean
	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public Gson createGson() {
		return new Gson();
	}
}