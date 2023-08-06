package com.s2i.sabong.web.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.s2i.sabong.service.config.AppConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
public class Previewer implements ApplicationListener<ContextRefreshedEvent> {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private CacheConfiguration cacheConfiguration;
	private ObjectMapper objectMapper;

	private AppConfigProperties appConfig;

	public Previewer(CacheConfiguration cacheConfiguration, ObjectMapper objectMapper, AppConfigProperties appConfig){
		this.cacheConfiguration = cacheConfiguration;
		this.objectMapper = objectMapper;
		this.appConfig = appConfig;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.debug("Previewer.onApplicationEvent - START");
		log.debug("Cache Configuration Git={}", toJson(cacheConfiguration.getGitProperties()));
		log.debug("Cache Configuration Build={}", toJson(cacheConfiguration.getBuildProperties()));
		log.debug("AppConfigProperties={}", toJson(appConfig));

		log.debug("Previewer.onApplicationEvent - END");
	}

	private String toJson(Object o) {
		String json = "{}";
		try {
			ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
			json = ow.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			json = "{}";
			log.error("JsonProcessingException: {}", e.getMessage());
		}
		return json;
	}
}