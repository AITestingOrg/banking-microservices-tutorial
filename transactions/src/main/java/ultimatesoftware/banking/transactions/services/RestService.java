package ultimatesoftware.banking.transactions.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public abstract class RestService<T> {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    protected RestTemplate restTemplate;

    protected T get(String appName, String path, Class<T> type) throws IllegalAccessException, InstantiationException {
        return restTemplate.getForObject("http://" + appName + path, type);
    }

    protected boolean put(String appName, String path, T objectToPut, Class<T> type) {
        HttpEntity<T> requestUpdate = new HttpEntity<>(objectToPut);
        ResponseEntity<T> response = restTemplate.exchange("http://" + appName + path , HttpMethod.PUT, requestUpdate, type);
        return response.getStatusCode().is2xxSuccessful();
    }
}
