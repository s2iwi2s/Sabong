package com.s2i.sabong.web.config;

import com.s2i.sabong.service.config.AppConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfigProperties.class)
public class ApplicationConfiguration {
}
