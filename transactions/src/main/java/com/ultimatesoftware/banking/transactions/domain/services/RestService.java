package com.ultimatesoftware.banking.transactions.domain.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public abstract class RestService<T> {
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected RestTemplate restTemplate;

    protected T get(String appName, String path, Class<T> type) {
        return restTemplate.getForObject("http://" + appName + path, type);
    }

    protected HttpStatus put(String appName, String path, T objectToPut, Class<T> type) {
        HttpEntity<T> requestUpdate = new HttpEntity<>(objectToPut);
        log.info(String.format("Sending put to : http://" + appName + path));
        ResponseEntity<String> response = restTemplate.exchange("http://" + appName + path , HttpMethod.PUT, requestUpdate, String.class);
        return response.getStatusCode();
    }
}
