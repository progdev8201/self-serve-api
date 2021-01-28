package com.configuration;

import com.service.feign.OmnivoreClient;
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
}
