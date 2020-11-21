package com.pay.spread;

import java.util.Iterator;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import lombok.extern.slf4j.Slf4j;

/**
 * @SpringBootApplication 는 아래의 세가지 기능을 제공한다.
 * @EnableAutoConfiguration Spring Boot의 자동화 기능(Spring 설정)을 활성화시켜줍니다.
 * @ComponentScan 패키지내 application 컴포넌트가 어디에 위치해있는지 검사합니다. (빈 검색)
 * @Configuration 빈에 대해서 Context에 추가하거나 특정 클래스를 참조해올 수 있습니다.
 * @author rio.choi
 *
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "com.pay.spread")
public class ParsApiApplication extends SpringBootServletInitializer implements ApplicationRunner {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ParsApiApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ParsApiApplication.class);
		app.addListeners(new ApplicationPidFileWriter());
		app.run(args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("Start REST API server!!!");

		Iterator<String> iter = args.getOptionNames().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Object value = args.getOptionValues(key);
			log.info("{}={}", key, value);
		}
	}

}
