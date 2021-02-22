package com.configuration;

import com.service.feign.OmnivoreClient;
import com.service.feign.OmnivoreItemClient;
import com.service.feign.OmnivoreMenuClient;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OmnivoreClientConfig {
    @Value("${omnivore.url}")
    String omnivoreUrl;

    @Bean
    public OmnivoreClient omnivoreClientImpl (){
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(OmnivoreClient.class))
                .logLevel(Logger.Level.FULL)
                .target(OmnivoreClient.class, omnivoreUrl);
    }

    @Bean
    public OmnivoreItemClient omnivoreItemClientImpl(){
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(OmnivoreItemClient.class))
                .logLevel(Logger.Level.FULL)
                .target(OmnivoreItemClient.class, omnivoreUrl);
    }

    @Bean
    public OmnivoreMenuClient omnivoreMenuClientImpl(){
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(OmnivoreMenuClient.class))
                .logLevel(Logger.Level.FULL)
                .target(OmnivoreMenuClient.class, omnivoreUrl);
    }
}
