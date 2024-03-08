/*
@Author
Abhishek - M1050754
abhishek@mindtree.com
 */
package com.kelloggs.promotions.winnerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan(basePackages = {"com.kelloggs.promotions.lib,com.kelloggs.promotions.winnerservice"})
@EntityScan(basePackages = {"com.kelloggs.promotions.lib.entity"})
@EnableJpaRepositories(basePackages = {"com.kelloggs.promotions.lib.repository"})
//@EnableEncryptableProperties
public class WinnerApp extends SpringBootServletInitializer  {
	
	private static Class<WinnerApp> winnerApp = WinnerApp.class;

    public static void main(String[] args) {
        SpringApplication.run(winnerApp,args);
		System.out.println("*****spring.profiles.active*********"+System.getenv("spring.profiles.active"));
		System.out.println("*****spring.datasource.username*********"+System.getenv("spring.datasource.username"));
		System.out.println("*****spring.datasource.password*********"+System.getenv("spring.datasource.password"));		
		System.out.println("*****SPRING_PROFILES_ACTIVE*********"+System.getenv("SPRING_PROFILES_ACTIVE"));
		System.out.println("*****SPRING_DATASOURCE_URL*********"+System.getenv("SPRING_DATASOURCE_URL"));
		System.out.println("*****SPRING_DATASOURCE_USERNAME*********"+System.getenv("SPRING_DATASOURCE_USERNAME"));
		System.out.println("*****SPRING_DATASOURCE_PASSWORD*********"+System.getenv("SPRING_DATASOURCE_PASSWORD"));
		System.out.println("*****jasypt.encryptor.password*********"+System.getenv("jasypt.encryptor.password"));
    }
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		//return super.configure(builder);
		System.out.println("*****spring.profiles.active*********"+System.getenv("spring.profiles.active"));
		System.out.println("*****spring.datasource.username*********"+System.getenv("spring.datasource.username"));
		System.out.println("*****spring.datasource.password*********"+System.getenv("spring.datasource.password"));		
		System.out.println("*****SPRING_PROFILES_ACTIVE*********"+System.getenv("SPRING_PROFILES_ACTIVE"));
		System.out.println("*****SPRING_DATASOURCE_URL*********"+System.getenv("SPRING_DATASOURCE_URL"));
		System.out.println("*****SPRING_DATASOURCE_USERNAME*********"+System.getenv("SPRING_DATASOURCE_USERNAME"));
		System.out.println("*****SPRING_DATASOURCE_PASSWORD*********"+System.getenv("SPRING_DATASOURCE_PASSWORD"));
		System.out.println("*****jasypt.encryptor.password*********"+System.getenv("jasypt.encryptor.password"));
		return builder.sources(winnerApp);
	}
}
