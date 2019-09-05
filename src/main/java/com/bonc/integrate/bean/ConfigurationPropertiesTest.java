package com.bonc.integrate.bean;

import javax.annotation.PostConstruct;
import javax.validation.constraints.AssertTrue;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("h2")
@Component
@Validated
public class ConfigurationPropertiesTest {
	@AssertTrue
	private boolean enabled;

	
	public ConfigurationPropertiesTest() {
		super();
		System.out.println("ConfigurationPropertiesTest");
	}
	@PostConstruct
	public void postConstruct() {
		System.out.println("ConfigurationPropertiesTest postConstruct");
	}
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
