package ultimatesoftware.banking.discoveryserviceclient.client;

import com.netflix.appinfo.CloudInstanceConfig;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.EurekaClient;

import java.util.List;

/**
 * Created by justinp on 7/21/17.
 */
public class EurekaDiscoveryService implements DiscoveryService {
    private EurekaClient eurekaClient;

    public EurekaDiscoveryService() {
        eurekaClient = DiscoveryManager.getInstance().initComponent(
            new CloudInstanceConfig(), new DefaultEurekaClientConfig());
    }

    @Override
    public <T> T queryOne() {
        return null;
    }

    @Override
    public List<?> queryMany() {
        return null;
    }

    @Override
    public <T> T command() {
        return null;
    }
}
