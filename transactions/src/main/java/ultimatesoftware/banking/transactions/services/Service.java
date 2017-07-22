package ultimatesoftware.banking.transactions.services;

import com.netflix.ribbon.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import ultimatesoftware.banking.transactions.discovery.DiscoveryClient;

import java.util.HashMap;

/**
 * Created by justinp on 7/21/17.
 */
public abstract class Service<T> {
    @Autowired
    private DiscoveryClient discoveryClient;

    protected <T> T request(String appName, String path, HashMap<String, String> params, Class<T> type) {
        RequestTemplate rt = new RequestTemplate();
        RequestTemplate req = rt.requestBuilder().build();
        return type.cast(result);
    }
}
