package ultimatesoftware.banking.transactions.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public abstract class RestService<T> {
    @Autowired
    private DiscoveryClient discoveryClient;

    protected <T> T get(String appName, String path, Class<T> type) throws IllegalAccessException, InstantiationException {
        String host = discoveryClient.getInstances(appName).get(0).getUri().toString();
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(host + path, type);
    }

    protected boolean put(String appName, String path, T objectToPut, Class<T> type) {
        String host = discoveryClient.getInstances(appName).get(0).getUri().toString();
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<T> requestUpdate = new HttpEntity<>(objectToPut);
        ResponseEntity<T> response = restTemplate.exchange(host + path , HttpMethod.PUT, requestUpdate, type);
        return response.getStatusCode().is2xxSuccessful();
    }
}
