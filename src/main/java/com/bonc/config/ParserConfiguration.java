package com.bonc.config;

import org.apache.tika.parser.AutoDetectParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserConfiguration {
	@Bean
	public AutoDetectParser autoDetectParser() {
		return new AutoDetectParser();
	}
}
