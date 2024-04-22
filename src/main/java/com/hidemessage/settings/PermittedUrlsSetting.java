package com.hidemessage.settings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PermittedUrlsSetting {

    @Bean(name = "permittedUrls")
    String[] permittedUrls() {
        return new String[]{
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/user/signup",
                "/user/signin",
                "/img/**",
                "/favicon.ico",
                "/posts",
                "/h2/**",
                "/data/posts/**"
        };
    }
}
