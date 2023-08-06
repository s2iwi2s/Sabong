package com.s2i.sabong.web.config;

import com.s2i.sabong.service.config.AppConfigProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class StaticResourcesWebConfiguration implements WebMvcConfigurer {

    protected static final String[] RESOURCE_LOCATIONS = new String[] {
        "classpath:/static/app/",
        "classpath:/static/content/",
        "classpath:/static/i18n/",
    };
    protected static final String[] RESOURCE_PATHS = new String[] { "/app/*", "/content/*", "/i18n/*" };

    private final AppConfigProperties appConfig;

    public StaticResourcesWebConfiguration(AppConfigProperties appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ResourceHandlerRegistration resourceHandlerRegistration = appendResourceHandler(registry);
        initializeResourceHandler(resourceHandlerRegistration);
    }

    protected ResourceHandlerRegistration appendResourceHandler(ResourceHandlerRegistry registry) {
        return registry.addResourceHandler(RESOURCE_PATHS);
    }

    protected void initializeResourceHandler(ResourceHandlerRegistration resourceHandlerRegistration) {
        resourceHandlerRegistration.addResourceLocations(RESOURCE_LOCATIONS).setCacheControl(getCacheControl());
    }

    protected CacheControl getCacheControl() {
        return CacheControl.maxAge(getJHipsterHttpCacheProperty(), TimeUnit.DAYS).cachePublic();
    }

    private int getJHipsterHttpCacheProperty() {
        return appConfig.getHttp().getCache().getTimeToLiveInDays();
    }
}
