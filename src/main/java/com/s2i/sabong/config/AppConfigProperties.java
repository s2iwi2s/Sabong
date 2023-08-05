package com.s2i.sabong.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "appconfig", ignoreUnknownFields = false)
public class AppConfigProperties implements Serializable {

    private final Cache cache = new Cache();
    private Jwt jwt = new Jwt();
    private CorsConfiguration cors = new CorsConfiguration();
    private List<String> authRequest = new ArrayList<>();
    private List<String> permitAll = new ArrayList<>();
    private List<String> corsFilter = new ArrayList<>();
    private List<String> ignoredRequest = new ArrayList<>();

    @Getter
    @Setter
    public static class Cache implements Serializable {
        private final Ehcache ehcache = new Ehcache();

        public Cache() {
        }

        @Getter
        @Setter
        public static class Ehcache implements Serializable {
            private int timeToLiveSeconds = 3600;
            private long maxEntries = 100L;

            public Ehcache() {
            }

            public void setTimeToLiveSeconds(int timeToLiveSeconds) {
                this.timeToLiveSeconds = timeToLiveSeconds;
            }

            public void setMaxEntries(long maxEntries) {
                this.maxEntries = maxEntries;
            }
        }
    }
    @Getter
    @Setter
    public static class Jwt {
        private String secret;
        private String base64Secret;
        private long tokenValidityInSeconds;
        private long tokenValidityInSecondsForRememberMe;

        public Jwt() {
            this.secret = null;
            this.base64Secret = null;
            this.tokenValidityInSeconds = 86400L;
            this.tokenValidityInSecondsForRememberMe = 2592000L;
        }
    }
}
