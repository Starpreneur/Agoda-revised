package com.example.Klouud.AgodaJson;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.util.Arrays;

@SpringBootApplication
@PropertySources({
		@PropertySource(value = "classpath:application.properties"),
		@PropertySource(value = "file:${SPRING_MONGO_CONFIG_LOCATION_AGODAJSON}/mongo.config.properties", ignoreResourceNotFound = false)
})
public class AgodaJsonApplication {

	@Value("${spring.data.mongodb.host}")
	private String host;

	@Value("${spring.data.mongodb.port}")
	private Integer port;

	@Value("${spring.data.mongodb.username}")
	private String userName;

	@Value("${spring.data.mongodb.database}")
	private String database;

	@Value("${spring.data.mongodb.password}")
	private String password;

	public static void main(String[] args) {
		SpringApplication.run(AgodaJsonApplication.class, args);
	}

	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {
		// Set credentials
		MongoCredential credential = MongoCredential.createCredential(userName, database, password.toCharArray());
		ServerAddress serverAddress = new ServerAddress(host, port);

		// Mongo Client
		com.mongodb.MongoClient mongoClient = new MongoClient(serverAddress, Arrays.asList(credential));
//         MongoClient mongoClient = new MongoClient(serverAddress);

		// Mongo DB Factory
		SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(mongoClient, database);

		return simpleMongoDbFactory;
	}
}
