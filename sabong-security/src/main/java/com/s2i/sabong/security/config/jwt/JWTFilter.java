package com.s2i.sabong.security.config.jwt;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
public class JWTFilter extends GenericFilterBean {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {

        logRequest((HttpServletRequest) servletRequest);

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);

        logResponse((HttpServletResponse) servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    private void logRequest(HttpServletRequest request) throws IOException {
        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
        byte[] requestBody = req.getContentAsByteArray();
        if (log.isDebugEnabled()) {
            log.debug("===========================request begin================================================");
            log.debug("URI         : {}", request.getRequestURI());
            log.debug("Method: {}, Request: {}", request.getMethod(), new String(requestBody, StandardCharsets.UTF_8));
            req.getParameterMap().entrySet().forEach(e -> {
                log.debug("{}={}", e.getKey(), e.getValue());
            });
            log.debug("===========================request end================================================");
        }
    }

    private void logResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper resp = new ContentCachingResponseWrapper(response);
        byte[] responseBody = resp.getContentAsByteArray();
        if (log.isDebugEnabled()) {
            log.debug("=======================response start=================================================");
            log.debug("Status code  : {}", response.getStatus());
            log.debug("Response body: {}", new String(responseBody, StandardCharsets.UTF_8));
            log.debug("=======================response end=================================================");
        }
    }
}
