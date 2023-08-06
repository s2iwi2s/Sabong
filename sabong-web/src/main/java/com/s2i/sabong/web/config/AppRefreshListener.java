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
public class AppRefreshListener implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private CacheConfiguration cacheConfiguration;
    private ObjectMapper objectMapper;

    private AppConfigProperties appConfig;

    public AppRefreshListener(CacheConfiguration cacheConfiguration, ObjectMapper objectMapper, AppConfigProperties appConfig) {
        this.cacheConfiguration = cacheConfiguration;
        this.objectMapper = objectMapper;
        this.appConfig = appConfig;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        log.debug("\nPreviewer.onApplicationEvent - START\n" +
                "Build={}\n" +
                "Git={}\n" +
                "cors={}\n" +
                "authRequest={}\n" +
                "permitAll={}\n" +
                "corsFilter={}\n" +
                "ignoredRequest={}",
				toJson(cacheConfiguration.getBuildProperties()),
				toJson(cacheConfiguration.getGitProperties()),
				toJson(appConfig.getCors()),
				toJson(appConfig.getAuthRequest()),
				toJson(appConfig.getPermitAll()),
				toJson(appConfig.getCorsFilter()),
				toJson(appConfig.getIgnoredRequest())
        );

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