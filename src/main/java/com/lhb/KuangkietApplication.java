package com.lhb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


/**
 *  参加阿里项目
 * @author lhb
 */
@SpringBootApplication(exclude = { RedisAutoConfiguration.class,
        MongoAutoConfiguration.class,
        DataSourceAutoConfiguration.class}  )
@ComponentScan("com.kuangkie")
public class KuangkietApplication {

	public static void main(String[] args) {
		SpringApplication.run(KuangkietApplication.class, args);
	}

}

